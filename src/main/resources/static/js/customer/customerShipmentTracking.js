const shipmentParams = new URLSearchParams(window.location.search);

async function CustomerBasedPayloadExtractor() {
    const userAction = shipmentParams.get("userAction");
    if(userAction === 'Customer shipment tracking'){
        const trackingId = shipmentParams.get("trackingId");
        const shipmentUrl = `/logistic/shipment/tracking?decodedTrackingId=${trackingId}`;
        const shipmentMethodType = 'GET';
        const customerShipmentTrackingResponse = await ajaxCall(shipmentUrl, shipmentMethodType, null);

        customerTrackingValueInitializer(customerShipmentTrackingResponse.data);
    }
}

CustomerBasedPayloadExtractor();

function customerTrackingValueInitializer(customerShipmentTrackingResponse) {

    const shippingFrom = document.getElementById('shipping-from');
    const shippingTo = document.getElementById('shipping-to');
    const deliveryDate = document.getElementById('delivery-date');
    const cargoName = document.getElementById('cargo-name');
    const cargoQuantity = document.getElementById('cargo-quantity');
    const cargoType = document.getElementById('cargo-type');
    const customerName = document.getElementById('customer-name');
    const currentLocation = document.getElementById('current-location');
    const shippingStatus = document.getElementById('shipping-status');
    const operatorName = document.getElementById('operator-name');

    if (!customerShipmentTrackingResponse.shippingId) {
        alert('Shipment not available.');
        return;
    }

    shippingFrom.value = customerShipmentTrackingResponse.shippingFrom;
    shippingTo.value = customerShipmentTrackingResponse.shippingTo;
    deliveryDate.value = customerShipmentTrackingResponse.deliveryDate;
    customerName.value = customerShipmentTrackingResponse.customerName;
    cargoName.value = customerShipmentTrackingResponse.cargoName;
    cargoQuantity.value = customerShipmentTrackingResponse.cargoQuantity;
    cargoType.value = customerShipmentTrackingResponse.cargoType;
    currentLocation.value = customerShipmentTrackingResponse.currentLocation;
    operatorName.value = customerShipmentTrackingResponse.operatorName;

    const shippingStatusOptions = shippingStatus.options;
    for (let i = 0; i < shippingStatusOptions.length; i++) {
        if (shippingStatusOptions[i].value === customerShipmentTrackingResponse.shippingStatus) {
            shippingStatusOptions[i].selected = true;
            break;
        }
    }

    customerBasedLayoutRender()
}

function customerBasedLayoutRender() {
    const elementsToRemove = [
        'dashboard-btn',

        'shipping-id',
        'origin-latitude',
        'origin-longitude',
        'current-latitude',
        'current-longitude',
        'destination-latitude',
        'destination-longitude',
        'created-at',
        'shipment-updated-at',
        'shipment-updated-by',
        'customer-id',
        'cargo-id',

        'shipping-status-log-id',
        'current-location-status',
        'current-latitude-status',
        'current-longitude-status',
        'assigned-by-name',
        'assigned-by-manager-name',
        'status-updated-at',
        'status-updated-by',
        'operator-id',
        'driver-id',
        'vehicle-id',
        'assigned-by',
        'driver-name',
        'vehicle-number',

        'update-operator-btn',
        'update-dependent-btn',
        'update-status-btn',

        'reassign-operator-btn',
        'reassign-driver-btn',

        'view-customer-btn',
        'view-cargo-btn',

        'location-suggestions'
    ];

    // Whole navigation dropdown ("Shipment ▾" -> Manage Shipments) — a
    // customer has no shipments to manage, so the entire nav group goes,
    // not just the button inside it.
    const shipmentNavigationGroup = document.querySelector('.shipment-navigation-group');
    if (shipmentNavigationGroup) {
        shipmentNavigationGroup.remove();
    }

    elementsToRemove.forEach(function (elementId) {
        const element = document.getElementById(elementId);
        if (element) {
            const label = document.querySelector(`label[for="${elementId}"]`);
            if (label) {
                label.remove();
            }
            element.remove();
        }
    });

    const currentLocation = document.getElementById('current-location');
    if (currentLocation) {
        currentLocation.readOnly = true;
    }

    // Customer sees status as a plain read-only field, not a selection box —
    // swap the <select> for a text input carrying over the already-selected value.
    const shippingStatus = document.getElementById('shipping-status');
    if (shippingStatus) {
        const statusValue = shippingStatus.value;
        const statusInput = document.createElement('input');
        statusInput.type = 'text';
        statusInput.id = 'shipping-status';
        statusInput.value = statusValue;
        statusInput.readOnly = true;
        shippingStatus.replaceWith(statusInput);
    }

    const shipmentActions = document.getElementById('shipment-body-shipment-actions');
    if (shipmentActions) {
        shipmentActions.remove();
    }

    const dependentList = document.querySelector('.shipment-body-dependents-list');
    if (dependentList) {
        dependentList.remove();
    }
}