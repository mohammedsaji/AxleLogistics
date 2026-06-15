document.addEventListener('DOMContentLoaded', function (event) {
    async function PayloadExtractor() {

        const viewState = localStorage.getItem("viewState");
        const statusStates = statusViewState().SHIPMENT_STATUS;

         previousPageNavigation(viewState);

        if (viewState === statusStates.READ) {
            const statusId = localStorage.getItem("statusId");
            const operatorId = localStorage.getItem("operatorId");
            const driverId = localStorage.getItem("driverId");
            const vehicleId = localStorage.getItem("vehicleId");

            if (statusId) {
                const url = `/logistic/status/fetch?shippingStatusId=${statusId}`;
                const methodType = 'GET';

                const responseStatus = await ajaxCall(url, methodType, null);
                const responseRole = await ajaxCall(`/logistic/auth/fetch/role`, 'GET', null);

                LayoutRenderer(statusId, operatorId, driverId, vehicleId, responseStatus, responseRole, viewState);
            }
        } else {
            window.location.href = "../views/dashboard.html";
        }
    }

    PayloadExtractor();
});

function LayoutRenderer(statusId, operatorId, driverId, vehicleId, responseStatus, responseRole, viewState) {

    const roleArray = responseRole.valueMap.Role;

    const statusShellWrapperBody = document.querySelector('.status-shell-wrapper-body');

    const shipmentStatusOption = ["SHIPPED", "ARRIVED", "IN-TRANSIT", "DELAYED", "OUT-FOR-DELIVERY", "DELIVERED"];

    Object.entries(responseStatus).forEach(([keyName, keyValue]) => {

        if (keyName === "shippingStatusId") {
            const updateStatusBtn = document.querySelector('.update-status-btn');

            if (updateStatusBtn) updateStatusBtn.setAttribute(`data-${camelToKebabCase(keyName)}`, keyValue);
        }

        const elementDiv = document.createElement('div');
        elementDiv.className = 'status-shell-inner-body';

        if (keyName === "shippingStatus") {
            const elementSelect = document.createElement('select');
            elementSelect.id = camelToKebabCase(keyName);
            elementSelect.required = true;

            const elementDefaultOption = document.createElement('option');
            elementDefaultOption.value = "";
            elementDefaultOption.textContent = "Select Status";
            elementDefaultOption.disabled = true;
            elementSelect.append(elementDefaultOption);

            shipmentStatusOption.forEach(statusOption => {
                const elementDBStatusOption = document.createElement('option');
                elementDBStatusOption.value = statusOption;
                elementDBStatusOption.textContent = statusOption;
                if (statusOption === keyValue) elementDBStatusOption.selected = true;
                elementSelect.append(elementDBStatusOption);
            });
            elementDiv.append(elementSelect);
            statusShellWrapperBody.append(elementDiv);
        } else if (keyName === "shippingStatusId" || keyName === "currentLocation") {
            const elementLabel = document.createElement('label');
            elementLabel.htmlFor = camelToKebabCase(keyName);
            elementLabel.textContent = `${keyName.charAt(0).toUpperCase() + whiteSpacedCamelCase(keyName).slice(1)}`;
            elementDiv.append(elementLabel);

            const elementInput = document.createElement('input');
            elementInput.id = camelToKebabCase(keyName);
            elementInput.value = keyValue || "";

            if (keyName === "currentLocation") {
                elementInput.readOnly = false;
            }else{
                elementInput.readOnly = true;
            }
            elementDiv.append(elementInput);
            statusShellWrapperBody.append(elementDiv);
        } else if (keyName === "operatorId" || keyName === "driverId" || keyName === "vehicleId") {
            const elementDiv = document.createElement('div');
            elementDiv.className = 'status-shell-inner-body';

            const elementLabel = document.createElement('label');
            elementLabel.htmlFor = camelToKebabCase(keyName);
            elementLabel.textContent = `${keyName.charAt(0).toUpperCase() + whiteSpacedCamelCase(keyName).slice(1)}`;
            elementDiv.append(elementLabel);

            const elementInput = document.createElement('input');
            elementInput.id = camelToKebabCase(keyName);
            elementInput.value = keyValue != null ? keyValue : null;
            elementInput.readOnly = true;


            elementDiv.append(elementInput);
            statusShellWrapperBody.append(elementDiv);
        }
    });

    if (responseStatus.operatorId || responseStatus.driverId || responseStatus.vehicleId) {
        const elementDiv = document.createElement('div');
        elementDiv.className = 'status-shell-inner-body';

        if (roleArray.includes("ADMIN")) {
            const operatorBtn = document.createElement('button');
            operatorBtn.className = 'reassign-operator-btn';
            operatorBtn.setAttribute("data-status-id", statusId);
            operatorBtn.textContent = "Reassign operator";
            elementDiv.append(operatorBtn);

        } else if (roleArray.includes("MANAGER")) {
            const driverBtn = document.createElement('button');
            driverBtn.className = 'reassign-driver-btn';
            driverBtn.setAttribute("data-operator-id", responseStatus.operatorId);
            driverBtn.textContent = "Reassign driver";
            elementDiv.append(driverBtn);

            const vehicleBtn = document.createElement('button');
            vehicleBtn.className = 'reassign-vehicle-btn';
            vehicleBtn.setAttribute("data-operator-id", responseStatus.operatorId);
            vehicleBtn.textContent = "Reassign vehicle";
            elementDiv.append(vehicleBtn);
        }
        statusShellWrapperBody.append(elementDiv);
    }

    ClickEventBinder(viewState, statusId, responseStatus);
}

function ClickEventBinder(viewState, statusId, response) {
    const statusAssignmentStates = statusViewState().SHIPMENT_ASSIGNMENT;

    const dashboardBtn = document.querySelector('.dashboard-btn');
    if (dashboardBtn) {
        dashboardBtn.addEventListener('click', function () {
            window.location.href = "/views/dashboard.html";
        },{once : true});
    }

    const operatorBtn = document.querySelector('.reassign-operator-btn');
    if (operatorBtn) {
        operatorBtn.addEventListener('click', function () {
            localStorage.setItem("shippingStatusId", this.dataset.statusId);
            localStorage.setItem("viewState", statusAssignmentStates.OPERATOR_REASSIGN);
            window.location.href = "../../views/operator/transport-type.html";
        },{once : true});
    }

    const driverBtn = document.querySelector('.reassign-driver-btn');
    if (driverBtn) {
        driverBtn.addEventListener('click', function () {
            localStorage.setItem("operatorId", this.dataset.operatorId);
            localStorage.setItem("viewState", statusAssignmentStates.DRIVER_REASSIGN);
            window.location.href = "../../views/driver/driver-list.html";
        },{once : true});
    }

    const vehicleBtn = document.querySelector('.reassign-vehicle-btn');
    if (vehicleBtn) {
        vehicleBtn.addEventListener('click', function () {
            localStorage.setItem("operatorId", this.dataset.operatorId);
            localStorage.setItem("viewState", statusAssignmentStates.VEHICLE_REASSIGN);
            window.location.href = "../../views/vehicle/vehicle-list.html";
        },{once : true});
    }

    const updateStatusBtn = document.querySelector('.update-status-btn');
    if(updateStatusBtn){
        updateStatusBtn.addEventListener('click', async function () {
            const payload = {};
            Object.keys(response).forEach((key) => {
                const kebabCaseKey = camelToKebabCase(key);
                const domElementInput = document.getElementById(`${kebabCaseKey}`);
                if (domElementInput) {
                    const elementInputValue = domElementInput.value.trim();

                    if (key === "shippingStatusId" || key === "cargoId" || key === "operatorId" || key === "driverId" || key === "vehicleId" || key === "updatedBy") {
                        payload[key] = elementInputValue ? parseInt(elementInputValue, 10) : null;
                    } else {
                        payload[key] = elementInputValue || null;
                    }
                }
            });
            await ajaxCall(`/logistic/status/save`, 'POST', payload);
        });
    }
}



function previousPageNavigation(viewState) {

    const previousFormBtn = document.querySelector('.previous-form-btn');

    if (!previousFormBtn) return;

    if (window.history.length <= 1) {
        previousFormBtn.disabled = true;
    } else {
        previousFormBtn.disabled = false;

        previousFormBtn.addEventListener('click', function () {
            localStorage.setItem("viewState", viewState);
            window.history.back();
        }, { once: true });
    }
}