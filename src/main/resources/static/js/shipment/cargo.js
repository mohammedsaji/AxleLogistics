const params = new URLSearchParams(window.location.search);

async function payloadExtractor() {
    const cargoId = params.get("cargoId");
    const url = `/logistic/cargo/fetch?cargoId=${cargoId}`;
    const methodType = 'GET';
    const response = await ajaxCall(url, methodType, null);
    valueInitializer(response);
    dynamicLayoutRender(params.get("shippingId"));
}
payloadExtractor();

function valueInitializer(response) {
    const cargoId = document.getElementById('cargo-id');
    const cargoName = document.getElementById('cargo-name');
    const cargoWeight = document.getElementById('cargo-weight');
    const cargoQuantity = document.getElementById('cargo-quantity');
    const cargoType = document.getElementById('cargo-type');
    const cargoDescription = document.getElementById('cargo-description');
    const createdAt = document.getElementById('created-at');
    const updatedAt = document.getElementById('updated-at');

    if (!response.cargoId) {
        alert('Cargo not available.');
        return;
    }

    cargoId.value = response.cargoId;
    cargoName.value = response.cargoName;
    cargoWeight.value = response.cargoWeight;
    cargoQuantity.value = response.cargoQuantity;
    cargoType.value = response.cargoType;
    cargoDescription.value = response.cargoDescription;
    createdAt.value = response.createdAt;
    updatedAt.value = response.updatedAt;
}

function dynamicLayoutRender(shippingId) {
    const returnToShipmentBtn = document.getElementById('return-to-shipment-btn');
    if (returnToShipmentBtn) {
        returnToShipmentBtn.setAttribute('data-shipping-id', shippingId);
    }
}

function clickEventBinder() {
    const userAction = params.get("userAction");

    const dashboardBtn = document.getElementById('dashboard-btn');
    if (dashboardBtn) {
        dashboardBtn.addEventListener('click', function () {
            window.location.href = "/views/dashboard.html";
        }, {once: true});
    }

    const returnToShipmentBtn = document.getElementById('return-to-shipment-btn');
    if (returnToShipmentBtn) {
        returnToShipmentBtn.addEventListener('click', function () {
            const shippingId = this.dataset.shippingId;
            window.location.href = `../../views/shipment/shipment.html?userAction=${userAction}&shippingId=${shippingId}`;
        }, {once: true});
    }
}
clickEventBinder();