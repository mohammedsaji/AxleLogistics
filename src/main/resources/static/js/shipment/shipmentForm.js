document.addEventListener('DOMContentLoaded', function () {

    function displayCustomerCargoShipmentForm() {

        const viewState = localStorage.getItem("viewState");
        const shipmentState = shipmentViewState().SHIPMENT;

        previousPageNavigation(viewState);

        if (viewState === shipmentState.CREATE) {

            const shipmentShellWrapperBody = document.querySelector('.shipment-shell-wrapper-body');
            const objects = formFrame();

            const elementDivParent1 = document.createElement('div');
            elementDivParent1.className = 'shipment-shell-inner-body';

            Object.entries(objects).forEach(([rootKey, rootvalue]) => {

                const elementDivChild1 = document.createElement('div');
                elementDivChild1.className = 'shipment-shell-group-body';

                const elementP = document.createElement('p');
                elementP.className = `${rootKey}-details`;
                elementP.textContent = rootKey.charAt(0).toUpperCase() + rootKey.slice(1) + " details";

                elementDivChild1.append(elementP);

                Object.entries(rootvalue).forEach(([childKey, childValue]) => {

                    const elementDivChild2 = document.createElement('div');
                    elementDivChild2.className = 'shipment-shell-section-body';

                    const elementLabel = document.createElement('label');
                    elementLabel.htmlFor = childKey;
                    elementLabel.textContent = childValue;
                    elementDivChild2.append(elementLabel);

                    const elementInput = document.createElement('input');
                    elementInput.className = childKey;
                    elementInput.required = true;
                    elementInput.type = 'text';
                    elementInput.value = childValue;
                    elementDivChild2.append(elementInput);
                    elementDivChild1.append(elementDivChild2);
                });
                elementDivParent1.append(elementDivChild1);
                shipmentShellWrapperBody.append(elementDivParent1);
            });

            const element = carrierFormFrame();

            const elementDivParent2 = document.createElement('div');
            elementDivParent2.className = 'shipment-shell-inner-body';

            const elementDivChild3 = document.createElement('div');
            elementDivChild3.className = 'shipment-shell-group-body';

            const elementP = document.createElement('p');
            elementP.className = 'Shipment-details';
            elementP.textContent = 'Shipment Details';
            elementDivChild3.append(elementP);

            Object.entries(element).forEach(([key, value]) => {

                const elementDivChild4 = document.createElement('div');
                elementDivChild4.className = 'shipment-shell-section-body';

                const elementLabel = document.createElement('label');
                elementLabel.textContent = camelToLabelStyleCase(key);
                elementLabel.htmlFor = key;
                elementDivChild4.append(elementLabel)

                const elementP = document.createElement('p');
                elementP.className = key;
                elementP.textContent = value;
                elementDivChild4.append(elementP);
                elementDivChild3.append(elementDivChild4);
            });
            shipmentShellWrapperBody.append(elementDivChild3);

            const elementDiv = document.createElement('div');
            elementDiv.className = 'shipment-shell-event';

            const createBtn = document.createElement('button');
            createBtn.className = 'create-btn';
            createBtn.textContent = 'Create';

            elementDiv.append(createBtn);
            shipmentShellWrapperBody.append(elementDiv);
        }
        loadOperatorBasedInfo();
    }
    displayCustomerCargoShipmentForm();

});

function carrierFormFrame() {
    const carrierPlannedValues = {
        "operator-id": "Operator Id",
        "driver-id": "Driver Id",
        "vehicle-id": "Vehicle Id"
    };

    return carrierPlannedValues;
}

function formFrame() {

    const customer =
        {
            "customer-name": "Customer Name",
            "customer-email": "Customer Email",
            "customer-phoneno": "Customer PhoneNo"
        };

    const cargo =
        {
            "cargo-name": "Cargo Name",
            "cargo-weight": "Cargo Weight",
            "cargo-quantity": "Cargo Quantity",
            "cargo-type": "Cargo Type",
            "cargo-description": "Cargo Description"
        };

    const shipment =
        {
            "shipping-from": "Shipping From",
            "shipping-to": "Shipping To",
            "delivery-date": "Delivery Date"
        };

    const shipmentStatus =
        {
            "current-location": "Current Location",
            "shipment-status": "Shipment Status"
        };

    const response =
        {
            "customer": customer,
            "cargo": cargo,
            "shipment": shipment,
            "shipment Status": shipmentStatus
        };

    return response;
}

function loadOperatorBasedInfo() {
    const operatorId = localStorage.getItem("shippingOperatorId");
    const driverId = localStorage.getItem("shippingDriverId");
    const vehicleId = localStorage.getItem("shippingVehicleId");

    const getDOMOperatorElement = document.querySelector('.operator-id');
    const getDOMDriverElement = document.querySelector('.driver-id');
    const getDOMVehicleElement = document.querySelector('.vehicle-id');

    getDOMOperatorElement.textContent = operatorId;
    getDOMDriverElement.textContent = driverId;
    getDOMVehicleElement.textContent = vehicleId;

}

function smallToCamelCase(str) {
    return str.replace(/-([a-z])/g, (g) => g[1].toUpperCase());
}

function ClickEventBinder() {

    const dashboardBtn = document.querySelector('.dashboard-btn');
    if (dashboardBtn) {
        dashboardBtn.addEventListener('click', function () {
            localStorage.setItem("viewStatus",dashboardViewState().DASHBOARD);
            window.location.href = "/views/dashboard.html";
        });
    }

    const createBtn = document.querySelector('.create-btn');
    if(createBtn){
        createBtn.addEventListener('click', async function () {

            const getShipmentBasedForm = formFrame();

            const payload = {};

            Object.entries(getShipmentBasedForm).forEach(([key, value]) => {
                const spaceRemovedKey = key.replace(/\s+/g, '');
                const dtoKey = `rqt${spaceRemovedKey.charAt(0).toUpperCase() + spaceRemovedKey.slice(1)}DTO`;

                payload[dtoKey] = {};

                Object.entries(value).forEach(([key, value]) => {
                    const fieldName = smallToCamelCase(key);
                    const fieldValue = document.querySelector(`.${key}`);

                    payload[dtoKey][fieldName] = fieldValue ? fieldValue.value : null;
                });
            });

            const getCarrierBasedForm = carrierFormFrame();

            Object.entries(getCarrierBasedForm).forEach(([key, value]) => {
                const entityName = value.replace(" Id", "");
                const dtoKey = `rqt${entityName}DTO`;

                const fieldName = smallToCamelCase(key);
                const fieldValue = document.querySelector(`.${key}`).textContent || null;

                if(!payload[dtoKey]){
                    payload[dtoKey]={};
                }

                payload[dtoKey][fieldName] = fieldValue ? parseInt(fieldValue, 10) : null

            });

            const url = `/logistic/shipment/save`;
            const methodType = 'POST';
            const saveResponse = await ajaxCall(url, methodType, payload);
            alert(saveResponse.text);
        });
    }
}
ClickEventBinder();


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