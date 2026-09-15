const shipmentParams = new URLSearchParams(window.location.search);

async function CustomerBasedPayloadExtractor() {
    const userAction = shipmentParams.get("userAction");
    if(userAction === 'Customer shipment tracking'){
        const shippingId = shipmentParams.get("shippingId");
        const shipmentUrl = `/logistic/shipment/tracking?shippingId=${shippingId}`;
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
        'shipmentHeaderActionsDivA',
        'shipmentHeaderActionsDivB',
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
        'current-latitude-status',
        'current-longitude-status',
        'assigned-by-name',
        'status-updated-at',
        'status-updated-by',
        'operator-id',
        'driver-id',
        'vehicle-id',
        'assigned-by',

        'update-operator-btn',
        'update-dependent-btn',
        'update-status-btn',

        'reassign-operator-btn',
        'reassign-driver-btn',

        'view-customer-btn',
        'view-cargo-btn',

        'location-suggestions'
    ];

    const shipmentListBtn = document.getElementById('shipment-list-btn');
    if(shipmentListBtn){
        shipmentListBtn.remove();
    }

    const shipmentHeaderActionsDivB =
        document.getElementById('shipment-header-actions-b');

    if (shipmentHeaderActionsDivB) {
        shipmentHeaderActionsDivB.remove();
    }

    elementsToRemove.forEach(function (elementId) {

        const element = document.getElementById(elementId);

        if (element) {
            element.remove();
        }
    });

    const currentLocation =
        document.getElementById('current-location');

    if (currentLocation) {
        currentLocation.readOnly = true;
    }

    const shippingStatus =
        document.getElementById('shipping-status');

    if (shippingStatus) {
        shippingStatus.disabled = true;
    }

    const shipmentActions =
        document.getElementById('shipment-body-shipment-actions');

    if (shipmentActions) {
        shipmentActions.remove();
    }

    const dependentList =
        document.querySelector('.shipment-body-dependents-list');

    if (dependentList) {
        dependentList.remove();
    }
}