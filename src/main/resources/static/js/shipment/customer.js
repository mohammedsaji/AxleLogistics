const params = new URLSearchParams(window.location.search);

async function payloadExtractor() {
    const customerId = params.get("customerId");
    const url = `/logistic/customer/fetch?customerId=${customerId}`;
    const methodType = 'GET';
    const response = await ajaxCall(url, methodType, null);
    valueInitializer(response);
    dynamicLayoutRender(params.get("shippingId"));
}
payloadExtractor();

function valueInitializer(response) {
    const customerId = document.getElementById('customer-id');
    const customerName = document.getElementById('customer-name');
    const customerEmail = document.getElementById('customer-email');
    const customerPhoneno = document.getElementById('customer-phoneno');
    const createdAt = document.getElementById('created-at');
    const createdBy = document.getElementById('created-by');

    if (!response.customerId) {
        alert('Customer not available.');
        return;
    }

    customerId.value = response.customerId;
    customerName.value = response.customerName;
    customerEmail.value = response.customerEmail;
    customerPhoneno.value = response.customerPhoneno;
    createdAt.value = response.createdAt;
    createdBy.value = response.createdBy;
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