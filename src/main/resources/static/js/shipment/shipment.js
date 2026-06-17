const params = new URLSearchParams(window.location.search);

async function payloadExtractor() {
    const shippingId = params.get("shippingId");
    const url = `/logistic/shipment/fetch?shippingId=${shippingId}`;
    const methodType = 'GET';
    const response = await ajaxCall(url, methodType, null);
    valueInitializer(response);
    dynamicLayoutRender(response.shippingId);
}
payloadExtractor();

function valueInitializer(response) {
    const shippingId = document.getElementById('shipping-id');
    const shippingFrom = document.getElementById('shipping-from');
    const shippingTo = document.getElementById('shipping-to');
    const deliveryDate = document.getElementById('delivery-date');
    const createdAt = document.getElementById('created-at');
    const updatedAt = document.getElementById('updated-at');
    const updatedBy = document.getElementById('updated-by');
    const customerId = document.getElementById('customer-id');
    const cargoId = document.getElementById('cargo-id');
    const statusId = document.getElementById('status-id');

    if (!response.shippingId) {
        alert('Shipment not available.');
        return;
    }

    shippingId.value = response.shippingId;
    shippingFrom.value = response.shippingFrom;
    shippingTo.value = response.shippingTo;
    deliveryDate.value = response.deliveryDate;
    createdAt.value = response.createdAt;
    updatedAt.value = response.updatedAt;
    updatedBy.value = response.updatedBy;
    customerId.value = response.customerId;
    cargoId.value = response.cargoId;
    statusId.value = response.statusId;

    const viewCustomerBtn = document.getElementById('view-customer-btn');
    if (viewCustomerBtn) {
        viewCustomerBtn.setAttribute('data-customer-id', response.customerId);
        viewCustomerBtn.setAttribute('data-shipping-id', response.shippingId);
    }
    const viewCargoBtn = document.getElementById('view-cargo-btn');
    if (viewCargoBtn) {
        viewCargoBtn.setAttribute('data-cargo-id', response.cargoId);
        viewCargoBtn.setAttribute('data-shipping-id', response.shippingId);
    }
    const viewStatusBtn = document.getElementById('view-status-btn');
    if (viewStatusBtn) {
        viewStatusBtn.setAttribute('data-status-id', response.statusId);
        viewStatusBtn.setAttribute('data-shipping-id', response.shippingId);
    }
}

function dynamicLayoutRender(shippingId) {
    const userAction = params.get("userAction");

    if (userAction === 'Read shipment') {
        const shipmentHeaderActionsDivB = document.getElementById('shipment-header-actions-b');
        if (shipmentHeaderActionsDivB) {
            shipmentHeaderActionsDivB.remove();
        }
        const shipmentBodyCommonActionsDiv = document.getElementById('shipment-body-common-actions');
        if (shipmentBodyCommonActionsDiv) {
            shipmentBodyCommonActionsDiv.remove();
        }

        const updateBtn = document.getElementById('update-btn');
        if (updateBtn) {
            updateBtn.setAttribute('data-shipping-id', shippingId);
        }
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

    const shipmentListBtn = document.getElementById('shipment-list-btn');
    if (shipmentListBtn) {
        shipmentListBtn.addEventListener('click', function () {
            window.location.href = `../../views/shipment/shipment-list.html?userAction=${userAction}`;
        }, {once: true});
    }

    const viewCustomerBtn = document.getElementById('view-customer-btn');
    if (viewCustomerBtn) {
        viewCustomerBtn.addEventListener('click', function () {
            const customerId = this.dataset.customerId;
            const shippingId = this.dataset.shippingId;
            window.location.href = `../../views/shipment/customer.html?userAction=${userAction}&customerId=${customerId}&shippingId=${shippingId}`;
        }, {once: true});
    }

    const viewCargoBtn = document.getElementById('view-cargo-btn');
    if (viewCargoBtn) {
        viewCargoBtn.addEventListener('click', function () {
            const cargoId = this.dataset.cargoId;
            const shippingId = this.dataset.shippingId;
            window.location.href = `../../views/shipment/cargo.html?userAction=${userAction}&cargoId=${cargoId}&shippingId=${shippingId}`;
        }, {once: true});
    }

    const viewStatusBtn = document.getElementById('view-status-btn');
    if (viewStatusBtn) {
        viewStatusBtn.addEventListener('click', function () {
            const statusId = this.dataset.statusId;
            const shippingId = this.dataset.shippingId;
            window.location.href = `../../views/status/status.html?userAction=${userAction}&statusId=${statusId}&shippingId=${shippingId}`;
        }, {once: true});
    }

    const updateBtn = document.getElementById('update-btn');
    if (updateBtn) {
        updateBtn.addEventListener('click', async function () {
            const shippingId = document.getElementById('shipping-id').value.trim();
            const shippingFrom = document.getElementById('shipping-from').value.trim();
            const shippingTo = document.getElementById('shipping-to').value.trim();
            const deliveryDate = document.getElementById('delivery-date').value.trim();
            const createdAt = document.getElementById('created-at').value.trim();
            const updatedAt = document.getElementById('updated-at').value.trim();
            const updatedBy = document.getElementById('updated-by').value.trim();
            const customerId = document.getElementById('customer-id').value.trim();
            const cargoId = document.getElementById('cargo-id').value.trim();
            const statusId = document.getElementById('status-id').value.trim();

            if (shippingId === '') {
                alert('Shipping ID not available.');
            } else if (shippingFrom === '') {
                alert('Shipping from not available.');
            } else if (shippingTo === '') {
                alert('Shipping to not available.');
            } else if (deliveryDate === '') {
                alert('Delivery date not entered.');
            } else if (createdAt === '') {
                alert('Created date not available.');
            } else if (updatedAt === '') {
                alert('Updated date not entered.');
            } else if (updatedBy === '') {
                alert('Updated by not entered.');
            }

            const payload = {
                "shippingId": parseInt(shippingId, 10),
                "shippingFrom": shippingFrom,
                "shippingTo": shippingTo,
                "deliveryDate": deliveryDate,
                "customerId": parseInt(customerId, 10),
                "cargoId": parseInt(cargoId, 10),
                "statusId": parseInt(statusId, 10),
                "createdAt": createdAt,
                "updatedAt": updatedAt,
                "updatedBy": parseInt(updatedBy, 10)
            };

            const response = await ajaxCall(`/logistic/shipment/update`, 'PUT', payload);
            if (response) {
                alert(response);
            }
        });
    }
}
clickEventBinder();