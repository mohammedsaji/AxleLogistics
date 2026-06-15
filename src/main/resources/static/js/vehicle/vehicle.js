document.addEventListener('DOMContentLoaded', function (event) {
    async function PayloadExtractor() {

        const viewState = localStorage.getItem("viewState");

        const operatorStates = operatorViewState().OPERATOR;
        const shipmentStates = shipmentViewState().SHIPMENT;
        const statusStates = statusViewState().SHIPMENT_ASSIGNMENT;

        if (viewState !== operatorStates.READ || viewState !== operatorStates.CREATE) {
            const createVehicleBtn = document.querySelector('.create-vehicle-btn');
            const deleteVehicleBtn = document.querySelector('.delete-vehicle-btn');
            if(createVehicleBtn){
                createVehicleBtn.remove();
            }
            if(deleteVehicleBtn){
                deleteVehicleBtn.remove();
            }
        }

        if (
            viewState === operatorStates.READ ||
            viewState === operatorStates.CREATE ||
            viewState === shipmentStates.CREATE ||
            viewState === statusStates.VEHICLE_REASSIGN
        ) {
            const vehicleId = localStorage.getItem("vehicleId");
            const operatorId = localStorage.getItem("operatorId");
            const driverId = localStorage.getItem("driverId");

            if (vehicleId) {
                const url = `/logistic/vehicle/fetch?vehicleId=${vehicleId}`;
                const methodType = 'GET';
                const response = await ajaxCall(url, methodType, null);
                LayoutRenderer(vehicleId, operatorId, driverId, response, viewState);
            }
        }
    }

    PayloadExtractor();
});

function LayoutRenderer(vehicleId, operatorId, driverId, response, viewState) {

    const vehicleShellWrapperBody = document.querySelector('.vehicle-shell-wrapper-body');

    Object.entries(response).forEach(([keyName, keyValue]) => {

        if (keyName === "vehicleId") {
            const updateVehicleBtn = document.querySelector('.update-vehicle-btn');
            const deleteVehicleBtn = document.querySelector('.delete-vehicle-btn');

            if (updateVehicleBtn) updateVehicleBtn.setAttribute(`data-${camelToKebabCase(keyName)}`, keyValue);
            if (deleteVehicleBtn) deleteVehicleBtn.setAttribute(`data-${camelToKebabCase(keyName)}`, keyValue);
        }

        const elementDiv = document.createElement('div');
        elementDiv.className = 'vehicle-shell-inner-body';

        const elementLabel = document.createElement('label');
        elementLabel.htmlFor = camelToKebabCase(keyName);
        elementLabel.textContent = `${keyName.charAt(0).toUpperCase() + whiteSpacedCamelCase(keyName).slice(1)}`;
        elementDiv.append(elementLabel);

        const elementInput = document.createElement('input');
        elementInput.id = camelToKebabCase(keyName);
        elementInput.value = keyValue;
        elementInput.readOnly = true;

        elementDiv.append(elementInput);
        vehicleShellWrapperBody.append(elementDiv);
    });

    if (
        viewState === shipmentViewState().SHIPMENT.CREATE ||
        viewState === statusViewState().SHIPMENT_ASSIGNMENT.VEHICLE_REASSIGN ||
        viewState === operatorViewState().OPERATOR.READ ||
        viewState === operatorViewState().OPERATOR.CREATE
    ) {
        const elementDiv = document.createElement('div');
        elementDiv.className = 'vehicle-shell-inner-body';

        if (viewState === shipmentViewState().SHIPMENT.CREATE) {
            const shipmentBtn = document.createElement('button');
            shipmentBtn.className = 'proceed-shipment-btn';
            shipmentBtn.setAttribute('data-operator-id', operatorId);
            shipmentBtn.setAttribute('data-driver-id', driverId);
            shipmentBtn.setAttribute('data-vehicle-id', vehicleId);
            shipmentBtn.textContent = "Proceed Shipment";
            elementDiv.append(shipmentBtn);
            vehicleShellWrapperBody.append(elementDiv);
        } else if (viewState === statusViewState().SHIPMENT_ASSIGNMENT.VEHICLE_REASSIGN) {
            const reassignVehicleBtn = document.createElement('button');
            reassignVehicleBtn.className = 'reassign-vehicle-btn';
            reassignVehicleBtn.setAttribute('data-vehicle-id', vehicleId);
            reassignVehicleBtn.textContent = "Reassign vehicle";
            elementDiv.append(reassignVehicleBtn);
            vehicleShellWrapperBody.append(elementDiv);
        } else if (viewState === operatorViewState().OPERATOR.READ ||
            viewState === operatorViewState().OPERATOR.CREATE) {
            const backToOperatorBtn = document.createElement('button');
            backToOperatorBtn.className = 'back-to-operator-btn';
            backToOperatorBtn.setAttribute('data-operator-id', operatorId);
            backToOperatorBtn.textContent = "Back to operator";
            elementDiv.append(backToOperatorBtn);
            vehicleShellWrapperBody.append(elementDiv);
        }
    }

    ClickEventBinder(viewState, response);
}

function ClickEventBinder(viewState, response) {

    const shipmentStates = shipmentViewState().SHIPMENT;
    const statusAssignmentStates = statusViewState().SHIPMENT_ASSIGNMENT;
    const statusReadStates = statusViewState().SHIPMENT_STATUS;

    const dashboardBtn = document.querySelector('.dashboard-btn');
    if (dashboardBtn) {
        dashboardBtn.addEventListener('click', function () {
            window.location.href = "/views/dashboard.html";
        },{once : true});
    }

    if(viewState === operatorViewState().OPERATOR.READ || viewState === operatorViewState().OPERATOR.CREATE){

        const createVehicleBtn = document.querySelector('.create-vehicle-btn');
        if (createVehicleBtn) {
            createVehicleBtn.addEventListener('click', function () {
                let operatorIdFallback = localStorage.getItem("vehicleOperatorId");
                localStorage.setItem("viewState", viewState);
                localStorage.setItem("operatorId", operatorIdFallback);
                window.location.href = "../../views/vehicle/vehicle-creation-form.html";
            },{once : true});
        }

        const deleteVehicleBtn = document.querySelector('.delete-vehicle-btn');
        if (deleteVehicleBtn) {
            deleteVehicleBtn.addEventListener('click', async function () {
                const url = `/logistic/vehicle/delete?vehicleId=${this.dataset.vehicleId}`;
                const methodType = 'DELETE';
                await ajaxCall(url, methodType, null);
            },{once : true});
        }

        const backToOperatorBtn = document.querySelector('.back-to-operator-btn');
        if(backToOperatorBtn){
            backToOperatorBtn.addEventListener('click', async function () {
                const url = `/logistic/operator/fetch?operatorId=${this.dataset.operatorId}`;
                const methodType = 'GET';
                const response = await ajaxCall(url, methodType, null);

                localStorage.setItem("operatorId", response.operatorId);
                localStorage.setItem("viewState",operatorViewState().OPERATOR.READ);

                window.location.href = "../../views/operator/operator.html";
            },{once : true});
        }
    }

    if (viewState === shipmentStates.CREATE) {
        const shipmentBtn = document.querySelector('.proceed-shipment-btn');
        if(shipmentBtn){
            shipmentBtn.addEventListener('click', function () {
                localStorage.setItem("shippingOperatorId", this.dataset.operatorId);
                localStorage.setItem("shippingDriverId", this.dataset.driverId);
                localStorage.setItem("shippingVehicleId", this.dataset.vehicleId);
                window.location.href = "../../views/shipment/shipment-form.html";
            },{once : true});
        }
    }

    if (viewState === statusAssignmentStates.VEHICLE_REASSIGN) {
        const reassignVehicle = document.querySelector('.reassign-vehicle-btn');
        if(reassignVehicle){
            reassignVehicle.addEventListener('click', async function () {
                const url = `/logistic/status/update`;
                const methodType = 'POST';
                const payload = {
                    "shippingStatusId": localStorage.getItem("shippingStatusId"),
                    "vehicleId": parseInt(this.dataset.vehicleId, 10)
                };
                const statusUpdateResponse = await ajaxCall(url, methodType, payload);
                localStorage.setItem("statusId", statusUpdateResponse.shippingStatusId);
                localStorage.setItem("viewState", statusReadStates.READ);
                window.location.href = "../../views/status/status.html";
            },{once : true});
        }
    }
}