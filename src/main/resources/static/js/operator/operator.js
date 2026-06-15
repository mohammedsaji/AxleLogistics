document.addEventListener('DOMContentLoaded', function (event) {

    async function PayloadExtractor() {

        const viewState = localStorage.getItem("viewState");
        const operatorStates = operatorViewState().OPERATOR;
        const managerStates = managerViewState().MANAGER_PROFILE;
        const driverStates = driverViewState().DRIVER_PROFILE;
        const shipmentStates = shipmentViewState().SHIPMENT;
        const statusStates = statusViewState().SHIPMENT_ASSIGNMENT;

        previousPageNavigation(viewState);

        if (viewState !== operatorStates.READ) {
            const createOperatorBtn = document.querySelector('.create-operator-btn');
            const updateOperatorBtn = document.querySelector('.update-operator-btn');
            const deleteOperatorBtn = document.querySelector('.delete-operator-btn');

            if (createOperatorBtn) {
                createOperatorBtn.remove();
            }
            if (updateOperatorBtn) {
                updateOperatorBtn.remove();
            }
            if (deleteOperatorBtn) {
                deleteOperatorBtn.remove();
            }
        }

        if (
            viewState === operatorStates.CREATE ||
            viewState === operatorStates.READ ||
            viewState === managerStates.CREATE ||
            viewState === driverStates.CREATE ||
            viewState === shipmentStates.CREATE ||
            viewState === statusStates.OPERATOR_REASSIGN
        ) {
            const operatorId = localStorage.getItem("operatorId");

            if (operatorId) {
                const url = `/logistic/operator/fetch?operatorId=${operatorId}`;
                const methodType = 'GET';
                const response = await ajaxCall(url, methodType, null);
                LayoutRenderer(viewState, operatorId, response);
            }
        }
    }
    PayloadExtractor();
});

function LayoutRenderer(viewState, operatorId, response) {

    const managerStates = managerViewState().MANAGER_PROFILE;
    const driverStates = driverViewState().DRIVER_PROFILE;
    const operatorStates = operatorViewState().OPERATOR;
    const shipmentStates = shipmentViewState().SHIPMENT;
    const statusStates = statusViewState().SHIPMENT_ASSIGNMENT;

    const operatorShellWrapperBody = document.querySelector('.operator-shell-wrapper-body');

    if (operatorShellWrapperBody) {
        operatorShellWrapperBody.innerHTML = '';
    }

    Object.entries(response).forEach(([keyName, keyValue]) => {

        if (keyName === "operatorId") {
            const updateOperatorBtn = document.querySelector('.update-operator-btn');
            const deleteOperatorBtn = document.querySelector('.delete-operator-btn');

            if (updateOperatorBtn) updateOperatorBtn.setAttribute(`data-${camelToKebabCase(keyName)}`, keyValue);
            if (deleteOperatorBtn) deleteOperatorBtn.setAttribute(`data-${camelToKebabCase(keyName)}`, keyValue);
        }

        const elementDiv = document.createElement('div');
        elementDiv.className = 'operator-shell-inner-body';

        const elementLabel = document.createElement('label');
        elementLabel.htmlFor = camelToKebabCase(keyName);
        elementLabel.textContent = `${keyName.charAt(0).toUpperCase() + whiteSpacedCamelCase(keyName).slice(1)}`;
        elementDiv.append(elementLabel);

        if (keyName === "managerId") {
            if (viewState !== managerStates.CREATE && viewState !== driverStates.CREATE) {
                const elementBtn = document.createElement("button");
                elementBtn.className = 'view-manager-btn';
                elementBtn.textContent = "Manager";
                elementBtn.setAttribute('data-operator-id', operatorId);
                elementDiv.append(elementBtn);
            } else {
                const elementP = document.createElement('p');
                elementP.className = camelToKebabCase(keyName);
                elementP.textContent = `Manager : ${keyValue || "N/A"}`;
                elementDiv.append(elementP);
            }
        } else {
            const elementInput = document.createElement('input');
            elementInput.id = camelToKebabCase(keyName);
            elementInput.value = keyValue !== null ? keyValue : "";

            if (
                keyName === "operatorId" ||
                keyName === "createdAt" ||
                keyName === "updatedAt" ||
                keyName === "updatedBy"
            ) {
                elementInput.readOnly = true;
            } else {
                elementInput.readOnly = false;
            }
            elementDiv.append(elementInput);
        }

        operatorShellWrapperBody.append(elementDiv);
    });

    viewStateFormBinding(operatorId, viewState, operatorStates, managerStates, driverStates, shipmentStates, statusStates, operatorShellWrapperBody);
    ClickEventBinder(viewState, response, operatorId);
}


function viewStateFormBinding(operatorId, viewState, operatorStates, managerStates, driverStates, shipmentStates, statusStates, operatorShellWrapperBody) {

    const operatorShellGroupHeader = document.querySelector('.dynamic-btn-container');

    if (!operatorShellGroupHeader) return;

    operatorShellGroupHeader.innerHTML = '';

    if (viewState === managerStates.CREATE) {
        const createManagerBtn = document.createElement('button');
        createManagerBtn.className = "create-manager-btn";
        createManagerBtn.textContent = "Create Manager";
        createManagerBtn.setAttribute('data-operator-id',operatorId);
        operatorShellGroupHeader.append(createManagerBtn);
    }

    if (viewState === driverStates.CREATE) {
        const createDriverBtn = document.createElement('button');
        createDriverBtn.className = "create-driver-btn";
        createDriverBtn.textContent = "Create Driver";
        createDriverBtn.setAttribute('data-operator-id',operatorId);
        operatorShellGroupHeader.append(createDriverBtn);
    }

    if (viewState === operatorStates.CREATE || viewState === operatorStates.READ) {
        const createVehicleBtn = document.createElement('button');
        createVehicleBtn.className = "create-vehicle-btn";
        createVehicleBtn.setAttribute('data-operator-id', operatorId);
        createVehicleBtn.textContent = "Create vehicle";
        operatorShellGroupHeader.append(createVehicleBtn);
    }

    if (viewState === operatorStates.READ || viewState === shipmentStates.CREATE) {
        const viewDriverBtn = document.createElement('button');
        viewDriverBtn.className = 'view-driver-btn';
        viewDriverBtn.setAttribute('data-operator-id', operatorId);
        if (viewState === operatorStates.READ) {
            viewDriverBtn.textContent = "View Drivers";
        } else {
            viewDriverBtn.textContent = "Proceed to driver";
        }
        operatorShellWrapperBody.append(viewDriverBtn);
    }

    if (viewState === operatorStates.READ) {
        const viewVehicleBtn = document.createElement('button');
        viewVehicleBtn.className = "view-vehicle-btn";
        viewVehicleBtn.setAttribute('data-operator-id', operatorId);
        viewVehicleBtn.textContent = "View Vehicles";
        operatorShellWrapperBody.append(viewVehicleBtn);
    }

    if (viewState === statusStates.OPERATOR_REASSIGN) {
        const reassignOperatorBtn = document.createElement('button');
        reassignOperatorBtn.className = "reassign-operator-btn";
        reassignOperatorBtn.setAttribute('data-operator-id', operatorId);
        reassignOperatorBtn.textContent = "Reassign operator";
        operatorShellWrapperBody.append(reassignOperatorBtn);
    }
}

function ClickEventBinder(viewState, response, operatorId) {

    const operatorStates = operatorViewState().OPERATOR;
    const shipmentStates = shipmentViewState().SHIPMENT;
    const managerStates = managerViewState().MANAGER_PROFILE;
    const statusAssignmentStates = statusViewState().SHIPMENT_ASSIGNMENT;
    const statusReadStates = statusViewState().SHIPMENT_STATUS;
    const driverStates = driverViewState().DRIVER_PROFILE;

    const dashboardBtn = document.querySelector('.dashboard-btn');
    if (dashboardBtn) {
        dashboardBtn.addEventListener('click',function () {
            localStorage.setItem("viewState",dashboardViewState().DASHBOARD);
            window.location.href = "/views/dashboard.html";
        },{once : true});
    }

    const createOperatorBtn = document.querySelector('.create-operator-btn');
    if(createOperatorBtn){
        createOperatorBtn.addEventListener('click', function () {
            localStorage.setItem("viewState", operatorViewState().OPERATOR.CREATE);
            window.location.href = "../../views/operator/operator-creation-form.html";
        },{once : true});
    }

    if (viewState === operatorStates.READ || viewState === shipmentStates.CREATE) {
        const viewDriverBtn = document.querySelector('.view-driver-btn');
        if(viewDriverBtn){
            viewDriverBtn.addEventListener('click', function () {
                localStorage.setItem("operatorId", this.dataset.operatorId);
                localStorage.setItem("viewState", viewState);
                window.location.href = "../../views/driver/driver-list.html";
            },{once : true});
        }
    }

    if (viewState === operatorStates.READ) {
        const viewManagerBtn = document.querySelector('.view-manager-btn');
        if(viewManagerBtn){
            viewManagerBtn.addEventListener('click', function () {
                localStorage.setItem("operatorId", this.dataset.operatorId);
                localStorage.setItem("viewState", viewState);
                window.location.href = "../../views/manager/manager-list.html";
            },{once : true});
        }

        const viewVehicleBtn = document.querySelector('.view-vehicle-btn');
        if(viewVehicleBtn){
            viewVehicleBtn.addEventListener('click', function () {
                localStorage.setItem("operatorId", this.dataset.operatorId);
                localStorage.setItem("viewState", viewState);
                window.location.href = "../../views/vehicle/vehicle-list.html";
            },{once : true});
        }
    }

    if (viewState === managerStates.CREATE) {
        const createManagerBtn = document.querySelector('.create-manager-btn');
        if(createManagerBtn){
            createManagerBtn.addEventListener('click', function () {
                localStorage.setItem("operatorId", this.dataset.operatorId);
                localStorage.setItem("viewState", viewState);
                if (viewState === managerStates.CREATE) {
                    window.location.href = "../../views/manager/manager-creation-form.html";
                } else if (viewState === operatorStates.READ) {
                    window.location.href = "../../views/signUp/sign-up.html";
                }
            },{once : true});
        }
    }

    if (viewState === driverStates.CREATE) {
        const newDriverBtn = document.querySelector('.create-driver-btn');
        if(newDriverBtn){
            newDriverBtn.addEventListener('click', function () {
                localStorage.setItem("operatorId", this.dataset.operatorId);
                localStorage.setItem("viewState", viewState);
                window.location.href = "../../views/signUp/sign-up.html";
            },{once : true});
        }
    }

    if (viewState === operatorStates.CREATE || viewState === operatorStates.READ) {
        const newVehicleBtn = document.querySelector('.create-vehicle-btn');
        if(newVehicleBtn){
            newVehicleBtn.addEventListener('click', function () {
                localStorage.setItem("viewState", viewState);
                localStorage.setItem("operatorId", this.dataset.operatorId);
                window.location.href = "../../views/vehicle/vehicle-creation-form.html";
            },{once : true});
        }
    }

    if (viewState === statusAssignmentStates.OPERATOR_REASSIGN) {
        const reassignOperatorBtn = document.querySelector('.reassign-operator-btn');
        if (reassignOperatorBtn) {
            reassignOperatorBtn.addEventListener('click', async function () {
                const url = `/logistic/status/update`;
                const methodType = 'POST';
                const payload = {
                    "shippingStatusId": localStorage.getItem("shippingStatusId"),
                    "operatorId": parseInt(this.dataset.operatorId, 10)
                };

                const response = await ajaxCall(url, methodType, payload);

                const statusId = response.shippingStatusId;
                localStorage.setItem("statusId", statusId);
                localStorage.setItem("viewState", statusReadStates.READ);
                window.location.href = "../../views/status/status.html";
            },{once : true});
        }
    }

    if(viewState === operatorStates.READ){
        const updateOperatorBtn = document.querySelector('.update-operator-btn');
        if(updateOperatorBtn){
            updateOperatorBtn.addEventListener('click', async function () {
                const payload = {};
                Object.keys(response).forEach((key) => {

                    const kebabCaseKey = camelToKebabCase(key);
                    const domElementInput = document.getElementById(`${kebabCaseKey}`);
                    if (domElementInput) {
                        const elementInputValue = domElementInput.value.trim();

                        if (key === "managerId" || key === "updatedBy") {
                            payload[key] = elementInputValue ? parseInt(elementInputValue, 10) : null;
                        } else {
                            payload[key] = elementInputValue || null;
                        }
                    }

                });
                const fallbackResponse = await ajaxCall(`/logistic/operator/save`, 'POST', payload);
                if(fallbackResponse){
                    alert(fallbackResponse);
                }
            });
        }

        const deleteOperatorBtn = document.querySelector('.delete-operator-btn');
        if(deleteOperatorBtn){
            deleteOperatorBtn.addEventListener('click', async function () {
                const url = `/logistic/operator/delete?operatorId=${this.dataset.operatorId}`;
                const methodType = 'DELETE';
                await ajaxCall(url, methodType, null);
            },{once : true});
        }
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