document.addEventListener('DOMContentLoaded', function (event) {
    async function PayloadExtractor() {

        const viewState = localStorage.getItem("viewState");

        const shipmentStates = shipmentViewState().SHIPMENT;

        previousPageNavigation(viewState);

        if (viewState !== shipmentStates.READ) {
            const createShipmentBtn = document.querySelector('.create-shipment-btn');
            if (createShipmentBtn) {
                createShipmentBtn.remove();
            }
        }

        if (viewState === shipmentStates.READ) {
            const shipmentId = localStorage.getItem("shipmentId");

            if (shipmentId) {
                const url = `/logistic/shipment/fetch?shippingId=${shipmentId}`;
                const methodType = 'GET';
                const response = await ajaxCall(url, methodType, null);
                LayoutRenderer(shipmentId, response);
            }
        }
    }
    PayloadExtractor();
});

function LayoutRenderer(shipmentId, response) {
    const viewState = localStorage.getItem("viewState");
    const shipmentShellWrapperBody = document.querySelector('.shipment-shell-wrapper-body');

    Object.entries(response).forEach(([keyName, keyValue]) => {

        if (keyName === "shipmentId") {
            const updateShipmentBtn = document.querySelector('.update-shipment-btn');

            if (updateShipmentBtn) updateShipmentBtn.setAttribute(`data-${camelToKebabCase(keyName)}`, keyValue);
        }

        if (keyName !== "customerId" && keyName !== "cargoId" && keyName !== "statusId") {
            const elementDiv = document.createElement('div');
            elementDiv.className = 'shipment-shell-inner-body';

            const elementLabel = document.createElement('label');
            elementLabel.htmlFor = camelToKebabCase(keyName);
            elementLabel.textContent = `${keyName.charAt(0).toUpperCase() + whiteSpacedCamelCase(keyName).slice(1)}`;
            elementDiv.append(elementLabel);

            const elementInput = document.createElement('input');
            elementInput.id = camelToKebabCase(keyName);
            elementInput.value = keyValue;

            if (keyName === "deliveryDate") {
                elementInput.readOnly = false;
            }else{
                elementInput.readOnly = true;
            }
            elementDiv.append(elementInput);
            shipmentShellWrapperBody.append(elementDiv);

        } else if (keyName === "customerId" && keyName === "cargoId" && keyName === "statusId") {
            const elementDiv = document.createElement('div');
            elementDiv.className = 'shipment-shell-inner-body';

            const elementBtn = document.createElement('button');
            elementBtn.setAttribute('data-shipment-id', shipmentId);
            elementBtn.setAttribute(`data-${camelToKebabCase(keyName)}`, keyValue);

            if (keyName === "customerId") {
                elementBtn.className = 'view-customer-btn';
                elementBtn.setAttribute(`data-${camelToKebabCase(keyName)}`,keyValue);
                elementBtn.textContent = "View customer";
            } else if (keyName === "cargoId") {
                elementBtn.className = 'view-cargo-btn';
                elementBtn.setAttribute(`data-${camelToKebabCase(keyName)}`,keyValue);
                elementBtn.textContent = "View cargo";
            } else if (keyName === "statusId") {
                elementBtn.className = 'view-status-btn';
                elementBtn.setAttribute(`data-${camelToKebabCase(keyName)}`,keyValue);
                elementBtn.textContent = "View status";
            }
            elementDiv.append(elementBtn);
            shipmentShellWrapperBody.append(elementDiv);
        }
    });

    ClickEventBinder(viewState, response);
}

function ClickEventBinder(viewState, response) {
    const shipmentStates = shipmentViewState().SHIPMENT;
    const statusStates = statusViewState().SHIPMENT_STATUS;

    const dashboardBtn = document.querySelector('.dashboard-btn');
    dashboardBtn.addEventListener('click', function () {
        window.location.href = "/views/dashboard.html";
    },{once : true});

    if(viewState === shipmentStates.READ){
        const customerBtn = document.querySelector('.view-customer-btn');
        if(customerBtn) {
            customerBtn.addEventListener('click', function () {
                localStorage.setItem("customerId", this.dataset.customerId);
                localStorage.setItem("shipmentId", this.dataset.shipmentId);
                localStorage.setItem("viewState", shipmentStates.CUSTOMER.READ);
                window.location.href = "../../views/shipment/customer.html";
            },{once : true});
        }

        const cargoBtn = document.querySelector('.view-cargo-btn');
        if(cargoBtn){
            cargoBtn.addEventListener('click', function () {
                localStorage.setItem("cargoId", this.dataset.cargoId);
                localStorage.setItem("shipmentId", this.dataset.shipmentId);
                localStorage.setItem("viewState", shipmentStates.CARGO.READ);
                window.location.href = "../../views/shipment/cargo.html";
            },{once : true});
        }


        const statusBtn = document.querySelector('.view-status-btn');
        if(statusBtn){
            statusBtn.addEventListener('click', function () {
                localStorage.setItem("statusId", this.dataset.statusId);
                localStorage.setItem("shipmentId", this.dataset.shipmentId);
                localStorage.setItem("viewState", statusStates.READ);
                window.location.href = "../../views/status/status.html";
            },{once : true});
        }

        const createShipmentBtn = document.querySelector('.create-shipment-btn');
        if(createShipmentBtn){
            createShipmentBtn.addEventListener('click', function () {
                localStorage.setItem("viewState", shipmentStates.CREATE);
                window.location.href = "../../views/operator/transport-type.html";
            },{once : true});
        }

        const updateShipmentBtn = document.querySelector('.update-shipment-btn');
        if(updateShipmentBtn){
            updateShipmentBtn.addEventListener('click', async function () {
                const payload = {};
                Object.keys(response).forEach((key) => {
                    const kebabCaseKey = camelToKebabCase(key);
                    const domElementInput = document.getElementById(`${kebabCaseKey}`);
                    if (domElementInput) {
                        const elementInputValue = domElementInput.value.trim();

                        if (key === "shipmentId" || key === "customerId" || key === "cargoId" || key === "statusId" || key === "updatedBy") {
                            payload[key] = elementInputValue ? parseInt(elementInputValue, 10) : null;
                        } else {
                            payload[key] = elementInputValue || null;
                        }
                    }
                });
                const fallbackResponse = await ajaxCall(`/logistic/shipment/save`, 'POST', payload);
                if(fallbackResponse){
                    alert(fallbackResponse);
                }
            });
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