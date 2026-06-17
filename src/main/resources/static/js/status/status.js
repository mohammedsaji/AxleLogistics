const params = new URLSearchParams(window.location.search);

async function payloadExtractor() {
    const shippingStatusId = params.get("shippingStatusId");
    const url = `/logistic/status/fetch?shippingStatusId=${shippingStatusId}`;
    const methodType = 'GET';
    const responseStatus = await ajaxCall(url, methodType, null);
    const responseRole = await ajaxCall(`/logistic/auth/fetch/role`, 'GET', null);
    valueInitializer(responseStatus);
    dynamicLayoutRender(responseStatus.shippingStatusId, responseStatus.operatorId, responseRole);
}
payloadExtractor();

function valueInitializer(response) {
    const shippingStatusId = document.getElementById('shipping-status-id');
    const currentLocation = document.getElementById('current-location');
    const shippingStatus = document.getElementById('shipping-status');
    const updatedAt = document.getElementById('updated-at');
    const updatedby = document.getElementById('updatedby');
    const cargoId = document.getElementById('cargo-id');
    const operatorId = document.getElementById('operator-id');
    const driverId = document.getElementById('driver-id');
    const vehicleId = document.getElementById('vehicle-id');

    if (!response.shippingStatusId) {
        alert('Status not available.');
        return;
    }

    shippingStatusId.value = response.shippingStatusId;
    currentLocation.value = response.currentLocation;
    updatedAt.value = response.updatedAt;
    updatedby.value = response.updatedby;
    cargoId.value = response.cargoId;
    operatorId.value = response.operatorId;
    driverId.value = response.driverId;
    vehicleId.value = response.vehicleId;

    const shippingStatusOptions = shippingStatus.options;
    for (let i = 0; i < shippingStatusOptions.length; i++) {
        if (shippingStatusOptions[i].value === response.shippingStatus) {
            shippingStatusOptions[i].selected = true;
            break;
        }
    }
}

function dynamicLayoutRender(shippingStatusId, operatorId, responseRole) {
    const userAction = params.get("userAction");
    const roleArray = responseRole.valueMap.Role;

    if (roleArray.includes("ADMIN")) {
        const reassignDriverBtn = document.getElementById('reassign-driver-btn');
        if (reassignDriverBtn) reassignDriverBtn.remove();

        const reassignVehicleBtn = document.getElementById('reassign-vehicle-btn');
        if (reassignVehicleBtn) reassignVehicleBtn.remove();

        const reassignOperatorBtn = document.getElementById('reassign-operator-btn');
        if (reassignOperatorBtn) {
            reassignOperatorBtn.setAttribute('data-shipping-status-id', shippingStatusId);
        }

    } else if (roleArray.includes("MANAGER")) {
        const reassignOperatorBtn = document.getElementById('reassign-operator-btn');
        if (reassignOperatorBtn) reassignOperatorBtn.remove();

        const reassignDriverBtn = document.getElementById('reassign-driver-btn');
        if (reassignDriverBtn) {
            reassignDriverBtn.setAttribute('data-operator-id', operatorId);
            reassignDriverBtn.setAttribute('data-shipping-status-id', shippingStatusId);
        }

        const reassignVehicleBtn = document.getElementById('reassign-vehicle-btn');
        if (reassignVehicleBtn) {
            reassignVehicleBtn.setAttribute('data-operator-id', operatorId);
            reassignVehicleBtn.setAttribute('data-shipping-status-id', shippingStatusId);
        }

    } else {
        const reassignOperatorBtn = document.getElementById('reassign-operator-btn');
        if (reassignOperatorBtn) reassignOperatorBtn.remove();

        const reassignDriverBtn = document.getElementById('reassign-driver-btn');
        if (reassignDriverBtn) reassignDriverBtn.remove();

        const reassignVehicleBtn = document.getElementById('reassign-vehicle-btn');
        if (reassignVehicleBtn) reassignVehicleBtn.remove();
    }

    const updateBtn = document.getElementById('update-btn');
    if (updateBtn) {
        updateBtn.setAttribute('data-shipping-status-id', shippingStatusId);
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

    const reassignOperatorBtn = document.getElementById('reassign-operator-btn');
    if (reassignOperatorBtn) {
        reassignOperatorBtn.addEventListener('click', function () {
            const shippingStatusId = this.dataset.shippingStatusId;
            const userAction = 'Reassign operator';
            window.location.href = `../../views/operator/transport-types.html?userAction=${userAction}&shippingStatusId=${shippingStatusId}`;
        }, {once: true});
    }

    const reassignDriverBtn = document.getElementById('reassign-driver-btn');
    if (reassignDriverBtn) {
        reassignDriverBtn.addEventListener('click', function () {
            const operatorId = this.dataset.operatorId;
            const shippingStatusId = this.dataset.shippingStatusId;
            const userAction  = 'Reassign driver';
            window.location.href = `../../views/driver/driver-list.html?userAction=${userAction}&operatorId=${operatorId}&shippingStatusId=${shippingStatusId}`;
        }, {once: true});
    }

    const reassignVehicleBtn = document.getElementById('reassign-vehicle-btn');
    if (reassignVehicleBtn) {
        reassignVehicleBtn.addEventListener('click', function () {
            const operatorId = this.dataset.operatorId;
            const shippingStatusId = this.dataset.shippingStatusId;
            const userAction  = 'Reassign vehicle';
            window.location.href = `../../views/vehicle/vehicle-list.html?userAction=${userAction}&operatorId=${operatorId}&shippingStatusId=${shippingStatusId}`;
        }, {once: true});
    }

    const updateBtn = document.getElementById('update-btn');
    if (updateBtn) {
        updateBtn.addEventListener('click', async function () {
            const shippingStatusId = document.getElementById('shipping-status-id').value.trim();
            const currentLocation = document.getElementById('current-location').value.trim();
            const shippingStatus = document.getElementById('shipping-status').value.trim();
            const updatedAt = document.getElementById('updated-at').value.trim();
            const updatedby = document.getElementById('updatedby').value.trim();
            const cargoId = document.getElementById('cargo-id').value.trim();
            const operatorId = document.getElementById('operator-id').value.trim();
            const driverId = document.getElementById('driver-id').value.trim();
            const vehicleId = document.getElementById('vehicle-id').value.trim();

            if (shippingStatusId === '') {
                alert('Shipping status ID not available.');
            } else if (currentLocation === '') {
                alert('Current location not entered.');
            } else if (shippingStatus === '') {
                alert('Shipping status not selected.');
            }

            const payload = {
                "shippingStatusId": parseInt(shippingStatusId, 10),
                "currentLocation": currentLocation,
                "shippingStatus": shippingStatus,
                "updatedAt": updatedAt,
                "updatedby": parseInt(updatedby, 10),
                "cargoId": parseInt(cargoId, 10),
                "operatorId": parseInt(operatorId, 10),
                "driverId": parseInt(driverId, 10),
                "vehicleId": parseInt(vehicleId, 10)
            };

            await ajaxCall(`/logistic/status/update`, 'PUT', payload);
        });
    }
}
clickEventBinder();