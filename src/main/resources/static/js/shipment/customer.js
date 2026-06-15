document.addEventListener('DOMContentLoaded', function (event) {
    async function PayloadExtractor() {

        const viewState = localStorage.getItem("viewState");
        const shipmentStates = shipmentViewState().SHIPMENT;

         previousPageNavigation(viewState);

        if (viewState === shipmentStates.CUSTOMER.READ) {
            const customerId = localStorage.getItem("customerId");
            const shipmentId = localStorage.getItem("shipmentId");

            if (customerId) {
                const url = `/logistic/customer/fetch?customerId=${customerId}`;
                const methodType = 'GET';
                const response = await ajaxCall(url, methodType, null);
                LayoutRenderer(customerId, shipmentId, response);
            }
        }
    }

    PayloadExtractor();
});

function LayoutRenderer(customerId, shipmentId, response) {

    const customerShellWrapperBody = document.querySelector('.customer-shell-wrapper-body');

    if (customerShellWrapperBody) {
        customerShellWrapperBody.innerHTML = '';
    }

    Object.entries(response).forEach(([keyName, keyValue]) => {

        const elementDiv = document.createElement('div');
        elementDiv.className = 'customer-shell-inner-body';

        const elementLabel = document.createElement('label');
        elementLabel.htmlFor = camelToKebabCase(keyName);
        elementLabel.textContent = `${keyName.charAt(0).toUpperCase() + whiteSpacedCamelCase(keyName).slice(1)}`;
        elementDiv.append(elementLabel);

        const elementInput = document.createElement('input');
        elementInput.id = camelToKebabCase(keyName);
        elementInput.value = keyValue != null ? keyValue : "";
        elementInput.readOnly = true;
        elementDiv.append(elementInput);

        customerShellWrapperBody.append(elementDiv);
    });

    const backToShipmentBtn = document.createElement('button');
    backToShipmentBtn.className = 'back-to-shipment-btn';
    backToShipmentBtn.setAttribute('data-shipment-id', shipmentId);
    backToShipmentBtn.textContent = "Back to shipment";
    customerShellWrapperBody.append(backToShipmentBtn);

    ClickEventBinder();
}

function ClickEventBinder() {
    const shipmentStates = shipmentViewState().SHIPMENT;

    const dashboardBtn = document.querySelector('.dashboard-btn');
    if (dashboardBtn) {
        dashboardBtn.addEventListener('click', function () {
            localStorage.setItem("viewState", dashboardViewState().DASHBOARD);
            window.location.href = "/views/dashboard.html";
        },{once : true});
    }

    const backToListBtn = document.querySelector('.back-to-shipment-btn');
    if (backToListBtn) {
        backToListBtn.addEventListener('click', function () {
            localStorage.setItem("viewState", shipmentStates.READ);
            localStorage.setItem("shipmentId", this.dataset.shipmentId);
            window.location.href = "../../views/shipment/shipment.html";
        },{once : true});
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