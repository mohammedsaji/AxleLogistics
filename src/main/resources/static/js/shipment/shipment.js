const params = new URLSearchParams(window.location.search);
let userAction = params.get("userAction");

async function payloadExtractor() {
    if(userAction !== 'Customer shipment tracking'){
        const shippingId = params.get("shippingId");
        const shipmentUrl = `/logistic/shipment/fetch?shippingId=${shippingId}`;
        const shipmentMethodType = 'GET';
        const shipmentFetchResponse = await ajaxCall(shipmentUrl, shipmentMethodType, null);
        const shipmentCompositeData = shipmentFetchResponse?.data;
        if(shipmentCompositeData && shipmentCompositeData.shipmentResponse){
            const statusUrl = `/logistic/shipmentLog/fetch?shippingId=${shippingId}`;
            const statusMethodType = 'GET';
            const statusResponse = await ajaxCall(statusUrl, statusMethodType, null);
            const statusData = statusResponse?.data;
            if(statusData){
                const loginUserMap = await ajaxCall(`/logistic/account/user-info`, 'GET', null);
                const loginUserData = loginUserMap?.data;
                if(loginUserData && loginUserData?.RoleList?.length > 0){
                    valueInitializer(shipmentCompositeData, statusData);
                    clickEventBinder();
                    dynamicLayoutRender(shipmentCompositeData.shipmentResponse.shippingId,
                        statusData.shippingStatusLogId,
                        statusData.operatorId,
                        loginUserData);
                }
            }else{
                console.error("Failed to load status details.");
                alert("Unable to load shipment status log details.");
                windows.location.href = "/views/dashboard.html";
            }
        }else{
            console.error("Failed to load shipment details.");
            alert("Unable to load shipment details.");
            windows.location.href = "/views/dashboard.html";
        }
    }
}

payloadExtractor();

function valueInitializer(shipmentCompositeData, statusResponse) {

    // Shipment Related Fields
    const shippingId = document.getElementById('shipping-id');
    const shippingFrom = document.getElementById('shipping-from');
    const originLatitude = document.getElementById('origin-latitude');
    const originLongitude = document.getElementById('origin-longitude');
    const currentLocation = document.getElementById('current-location');
    const currentLatitude = document.getElementById('current-latitude');
    const currentLongitude = document.getElementById('current-longitude');
    const shippingTo = document.getElementById('shipping-to');
    const destinationLatitude = document.getElementById('destination-latitude');
    const destinationLongitude = document.getElementById('destination-longitude');
    const deliveryDate = document.getElementById('delivery-date');
    const createdAt = document.getElementById('created-at');
    const shipmentUpdatedAt = document.getElementById('shipment-updated-at');
    const shipmentUpdatedBy = document.getElementById('shipment-updated-by');
    const customerId = document.getElementById('customer-id');
    const customerName = document.getElementById('customer-name');
    const cargoId = document.getElementById('cargo-id');
    const cargoName = document.getElementById('cargo-name');
    const cargoQuantity = document.getElementById('cargo-quantity');
    const cargoType = document.getElementById('cargo-type');

    // Status Related Fields
    const shippingStatusLogId = document.getElementById('shipping-status-log-id');
    const shipmentRefId = document.getElementById('shipping-id-ref');
    const currentLocationStatus = document.getElementById('current-location-status');
    const currentLatitudeStatus = document.getElementById('current-latitude-status');
    const currentLongitudeStatus = document.getElementById('current-longitude-status');
    const shippingStatus = document.getElementById('shipping-status');
    const statusUpdatedAt = document.getElementById('status-updated-at');
    const statusUpdatedBy = document.getElementById('status-updated-by');
    const operatorId = document.getElementById('operator-id');
    const operatorName = document.getElementById('operator-name');
    const driverId = document.getElementById('driver-id');
    const driverName = document.getElementById('driver-name');
    const vehicleId = document.getElementById('vehicle-id');
    const vehicleNumber = document.getElementById('vehicle-number');
    const assignedBy = document.getElementById('assigned-by');
    const assignedByManagerName = document.getElementById('assigned-by-manager-name');

    const cargoResponse = shipmentCompositeData.cargoResponse;
    const customerResponse = shipmentCompositeData.customerResponse;
    const shipmentResponse = shipmentCompositeData.shipmentResponse;
    shippingId.value = shipmentResponse.shippingId;
    shippingFrom.value = shipmentResponse.shippingFrom;
    originLatitude.value = shipmentResponse.originLatitude;
    originLongitude.value = shipmentResponse.originLongitude;
    if(userAction === 'Reassign operator'){
        currentLocation.value = '';
        currentLatitude.value = '';
        currentLongitude.value = '';
    }else{
        currentLocation.value = shipmentResponse.currentLocation;
        currentLatitude.value = shipmentResponse.currentLatitude;
        currentLongitude.value = shipmentResponse.currentLongitude;
    }
    destinationLatitude.value = shipmentResponse.destinationLatitude;
    destinationLongitude.value = shipmentResponse.destinationLongitude;
    shippingTo.value = shipmentResponse.shippingTo;
    deliveryDate.value = formatDateTime(shipmentResponse.deliveryDate);
    createdAt.value = formatDateTime(shipmentResponse.createdAt);
    shipmentUpdatedAt.value = formatDateTime(shipmentResponse.updatedAt);
    shipmentUpdatedBy.value = shipmentResponse.updatedBy;
    customerId.value = customerResponse.customerId;
    customerName.value = customerResponse.customerName;
    cargoId.value = cargoResponse.cargoId;
    cargoName.value = cargoResponse.cargoName;
    cargoQuantity.value = cargoResponse.cargoQuantity;
    cargoType.value = cargoResponse.cargoType;

    shippingStatusLogId.value = statusResponse.shippingStatusLogId;
    shipmentRefId.value = statusResponse.shippingId;
    statusUpdatedAt.value = formatDateTime(statusResponse.updatedAt);
    statusUpdatedBy.value = statusResponse.updatedBy;
    if(userAction === 'Reassign operator'){
        operatorId.value = params.get("operatorId");
        operatorName.value = params.get("operatorName");

        currentLocationStatus.value = '';
        currentLatitudeStatus.value = '';
        currentLongitudeStatus.value = '';
        driverId.value = '';
        driverName.value = '';
        vehicleId.value = '';
        vehicleNumber.value = '';
    }else{
        operatorId.value = statusResponse.operatorId;
        operatorName.value = statusResponse.operatorName;

        currentLocationStatus.value = statusResponse.currentLocation;
        currentLatitudeStatus.value = statusResponse.currentLatitude;
        currentLongitudeStatus.value = statusResponse.currentLongitude;
        driverId.value = statusResponse.driverId;
        driverName.value = statusResponse.driverName;
        vehicleId.value = statusResponse.vehicleId;
        vehicleNumber.value = statusResponse.vehicleNumber;
    }

    if(userAction === 'Reassign driver'){
        driverId.value = params.get("driverId");
        driverName.value = '';
        vehicleId.value = params.get("vehicleId");
        vehicleNumber.value = '';
    }

    assignedBy.value = statusResponse.assignedBy;
    assignedByManagerName.value = statusResponse.assignedByManagerName;

    const shippingStatusOptions = shippingStatus.options;
    for (let i = 0; i < shippingStatusOptions.length; i++) {
        if (shippingStatusOptions[i].value === statusResponse.shippingStatus) {
            shippingStatusOptions[i].selected = true;
            break;
        }
    }

    locationTracker(currentLocation, currentLatitude, currentLongitude); // -> Used to fetch Geo locations from photon api and to inject into current-location field.

    const viewCustomerBtn = document.getElementById('view-customer-btn');
    if (viewCustomerBtn) {
        viewCustomerBtn.setAttribute('data-customer-id', shipmentResponse.customerId);
        viewCustomerBtn.setAttribute('data-shipping-id', shipmentResponse.shippingId);
    }
    const viewCargoBtn = document.getElementById('view-cargo-btn');
    if (viewCargoBtn) {
        viewCargoBtn.setAttribute('data-cargo-id', shipmentResponse.cargoId);
        viewCargoBtn.setAttribute('data-shipping-id', shipmentResponse.shippingId);
    }
}

function locationTracker(currentLocation, currentLatitude, currentLongitude){
    if (currentLocation) {
        currentLocation.addEventListener('input', async function () {

            const locationSuggestions =
                document.getElementById('location-suggestions');

            locationSuggestions.innerHTML = '';

            const results = await searchLocation(this.value.trim());

            results.forEach(function (result) {

                const properties = result.properties;

                const suggestion = document.createElement('div');

                suggestion.textContent = [
                    properties.name,
                    properties.city,
                    properties.state,
                    properties.country
                ]
                    .filter(Boolean)
                    .join(', ');

                suggestion.addEventListener('click', function () {

                    currentLocation.value = suggestion.textContent;

                    const coordinates = result.geometry.coordinates;

                    const longitude = coordinates[0];
                    const latitude = coordinates[1];

                    currentLatitude.value = latitude;
                    currentLongitude.value = longitude;

                    console.log("Selected location:", suggestion.textContent);
                    console.log("Latitude:", latitude);
                    console.log("Longitude:", longitude);

                    locationSuggestions.innerHTML = '';
                });

                locationSuggestions.appendChild(suggestion);
            });
        });
    }
}

function dynamicLayoutRender(shippingId, shippingStatusLogId, operatorId, loginUserMap) {
    const roleArray = loginUserMap.RoleList;

    if (userAction === 'Read shipment') {
        if (roleArray.includes("ADMIN")) {
            const dependentUpdateBtn = document.getElementById('update-dependent-btn');
            if (dependentUpdateBtn)
                dependentUpdateBtn.remove();
            const statusUpdateBtn = document.getElementById('update-status-btn');
            if (statusUpdateBtn)
                statusUpdateBtn.remove();
        } else if (roleArray.includes("FEDERATE-MANAGER")) {
            const operatorUpdateBtn = document.getElementById('update-operator-btn');
            if (operatorUpdateBtn)
                operatorUpdateBtn.remove();
            const statusUpdateBtn = document.getElementById('update-status-btn');
            if (statusUpdateBtn)
                statusUpdateBtn.remove();
            const currentLocationField = document.getElementById('current-location');
            if (currentLocationField) {
                currentLocationField.readOnly = true;
            }
        } else if (roleArray.includes("FEDERATE-DRIVER")) {
            const operatorUpdateBtn = document.getElementById('update-operator-btn');
            if (operatorUpdateBtn)
                operatorUpdateBtn.remove();
            const dependentUpdateBtn = document.getElementById('update-dependent-btn');
            if (dependentUpdateBtn)
                dependentUpdateBtn.remove();
            const currentLocationField = document.getElementById('current-location');
            if (currentLocationField) {
                currentLocationField.readOnly = true;
            }
        }

    } else if (userAction === 'Reassign operator') {
        const statusUpdateBtn = document.getElementById('update-status-btn');
        if (statusUpdateBtn)
            statusUpdateBtn.remove();
        const dependentUpdateBtn = document.getElementById('update-dependent-btn');
        if (dependentUpdateBtn)
            dependentUpdateBtn.remove();
    } else if (userAction === 'Reassign driver') {
        const statusUpdateBtn = document.getElementById('update-status-btn');
        if (statusUpdateBtn)
            statusUpdateBtn.remove();
        const operatorUpdateBtn = document.getElementById('update-operator-btn');
        if (operatorUpdateBtn)
            operatorUpdateBtn.remove();
    }

    if (roleArray.includes("ADMIN")) {
        const reassignDriverBtn = document.getElementById('reassign-driver-btn');
        if (reassignDriverBtn) reassignDriverBtn.remove();

        const reassignOperatorBtn = document.getElementById('reassign-operator-btn');
        if (reassignOperatorBtn) {
            reassignOperatorBtn.setAttribute('data-shipping-id', shippingId);
        }

    } else if (roleArray.includes("FEDERATE-MANAGER")) {
        const reassignOperatorBtn = document.getElementById('reassign-operator-btn');
        if (reassignOperatorBtn) reassignOperatorBtn.remove();

        const reassignDriverBtn = document.getElementById('reassign-driver-btn');
        if (reassignDriverBtn) {
            reassignDriverBtn.setAttribute('data-operator-id', operatorId);
            reassignDriverBtn.setAttribute('data-shipping-id', shippingId);
            reassignDriverBtn.setAttribute('data-manager-id', loginUserMap.userId);
        }

    } else {
        const reassignOperatorBtn = document.getElementById('reassign-operator-btn');
        if (reassignOperatorBtn) reassignOperatorBtn.remove();

        const reassignDriverBtn = document.getElementById('reassign-driver-btn');
        if (reassignDriverBtn) reassignDriverBtn.remove();
    }
}

function clickEventBinder() {

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

    const reassignOperatorBtn = document.getElementById('reassign-operator-btn');
    if (reassignOperatorBtn) {
        reassignOperatorBtn.addEventListener('click', function () {
            const shippingId = this.dataset.shippingId;
            const userAction = 'Reassign operator';
            window.location.href = `../../views/operator/transport-types.html?userAction=${userAction}&shippingId=${shippingId}`;
        }, {once: true});
    }

    const reassignDriverBtn = document.getElementById('reassign-driver-btn');
    if (reassignDriverBtn) {
        reassignDriverBtn.addEventListener('click', function () {
            const operatorId = this.dataset.operatorId;
            const shippingId = this.dataset.shippingId;
            const userAction = 'Reassign driver';
            window.location.href = `../../views/driver/driver-list.html?userAction=${userAction}&operatorId=${operatorId}&shippingId=${shippingId}`;
        }, {once: true});
    }

    const updateOperatorBtn = document.getElementById('update-operator-btn');
    if (updateOperatorBtn) {
        updateOperatorBtn.addEventListener('click', async function (event) {
            event.preventDefault();

            const shipmentId = document.getElementById('shipping-id-ref').value.trim();
            const operatorId = params.get('operatorId'); // the newly picked operator
            const currentLocation = document.getElementById('current-location').value.trim(); // carried forward
            const shippingStatus = document.getElementById('shipping-status').value.trim();   // carried forward
            const currentLatitude = document.getElementById('current-latitude').value.trim();
            const currentLongitude = document.getElementById('current-longitude').value.trim();

            if (shipmentId === '' || !operatorId || currentLocation === '' || shippingStatus === '') {
                alert('Missing required fields.');
                return;
            }

            if (updateOperatorBtn.disabled)
                return;
            updateOperatorBtn.disabled = true;

            try{
                const payload = {
                    "shipmentRequest" : {
                        shippingId: shipmentId,
                        currentLocation: currentLocation,
                        currentLatitude: currentLatitude,
                        currentLongitude: currentLongitude
                    },
                    "shipmentStatusLogRequest":{
                        shippingId: shipmentId,
                        operatorId: parseInt(operatorId, 10),
                        currentLocation : currentLocation,
                        shippingStatus: shippingStatus,
                        currentLatitude: currentLatitude,
                        currentLongitude: currentLongitude
                    }
                }

                const response = await ajaxCall(`/logistic/shipment/update`, 'PUT', payload);
                if(response){
                    alert( response.data + " " + response.message);
                    userAction = 'Read shipment';
                }
                window.location.href = `../../views/shipment/shipment.html?shippingId=${shipmentId}&userAction=Read shipment`;
            } catch(error){
                console.error('Unable to update operator for shipment.', error);
            } finally {
                updateOperatorBtn.disabled = false;
            }
        });
    }

    const updateDependentBtn = document.getElementById('update-dependent-btn');
    if (updateDependentBtn) {
        updateDependentBtn.addEventListener('click', async function (event) {
            event.preventDefault();

            try {
                const shipmentId = document.getElementById('shipping-id-ref').value.trim();
                const operatorId = params.get('operatorId');
                const driverId = params.get('driverId');   // the newly picked driver
                const vehicleId = params.get('vehicleId'); // the newly picked vehicle
                const currentLocation = document.getElementById('current-location').value.trim(); // carried forward
                const currentLatitudeStatus = document.getElementById('current-latitude-status').value.trim();
                const currentLongitudeStatus = document.getElementById('current-longitude-status').value.trim();
                const shippingStatus = document.getElementById('shipping-status').value.trim();   // carried forward

                if (shipmentId === '' || !driverId || !vehicleId || currentLocation === '' || shippingStatus === '') {
                    alert('Missing required fields.');
                    return;
                }

                if (updateDependentBtn.disabled)
                    return;

                updateDependentBtn.disabled = true;

                const response = await ajaxCall(`/logistic/shipmentLog/dependent/save`, 'POST', {
                    shippingId: shipmentId,
                    operatorId: operatorId ? parseInt(operatorId, 10) : null,
                    driverId: parseInt(driverId, 10),
                    vehicleId: parseInt(vehicleId, 10),
                    currentLocation: currentLocation,
                    currentLatitude: currentLatitudeStatus,
                    currentLongitude: currentLongitudeStatus,
                    shippingStatus: shippingStatus
                });
                if (response) {
                    alert(response.message);
                    userAction = 'Read shipment';
                }
                window.location.href = `../../views/shipment/shipment.html?shippingId=${shipmentId}&userAction=Read shipment`;
            } catch(error){
                console.error('Unable to update driver for shipment:', error);
            }finally {
                updateDependentBtn.disabled = false;
            }
        });
    }

    const updateStatusBtn = document.getElementById('update-status-btn');
    if (updateStatusBtn) {
        updateStatusBtn.addEventListener('click', async function (event) {
            event.preventDefault();

            const shipmentId = document.getElementById('shipping-id-ref').value.trim(); // new value
            const shippingStatus = document.getElementById('shipping-status').value.trim();   // new value
            const operatorId = document.getElementById('operator-id').value.trim();  // carried forward
            const driverId = document.getElementById('driver-id').value.trim();      // carried forward
            const vehicleId = document.getElementById('vehicle-id').value.trim();    // carried forward

            if (shipmentId === '' || shippingStatus === '' ||
                operatorId === '' || driverId === '' || vehicleId === '') {
                alert('Missing required fields.');
                updateStatusBtn.disabled = false;
                return;
            }

            if (updateStatusBtn.disabled)
                return;

            updateStatusBtn.disabled = true;

            try {
                const location = await getCurrentGeoLocation();

                console.log('Driver latitude:', location.latitude);
                console.log('Driver longitude:', location.longitude);
                console.log('GPS accuracy:', location.accuracy);

                const currentLocation = await reverseGeocode(
                    location.latitude,
                    location.longitude
                );

                console.log('Driver current location:', currentLocation);

                const payload = {
                    shippingId: shipmentId,
                    currentLocation : currentLocation,
                    currentLatitude: location.latitude,
                    currentLongitude: location.longitude,
                    locationAccuracy: location.accuracy,
                    shippingStatus: shippingStatus,
                    operatorId: parseInt(operatorId, 10),
                    driverId: parseInt(driverId, 10),
                    vehicleId: parseInt(vehicleId, 10)
                }

                const response = await ajaxCall(`/logistic/shipmentLog/status/save`, 'POST', payload);
                if(response){
                    alert(response.message);
                }
                window.location.href = `../../views/shipment/shipment.html?shippingId=${shipmentId}&userAction=Read shipment`;

            } catch (error) {

                console.error('Unable to obtain location:', error);

                if (error.code === 1) {
                    alert('Location permission was denied. Please allow location access.');
                } else if (error.code === 2) {
                    alert('Unable to determine your current location.');
                } else if (error.code === 3) {
                    alert('Location request timed out. Please try again.');
                } else {
                    alert('Unable to obtain your current location.');
                }
            }finally {
                updateStatusBtn.disabled = false;
            }
        });
    }
}

function shipmentNavigationBinder() {
    const shipmentNavigationBtn = document.getElementById('shipment-navigation-btn');

    if (shipmentNavigationBtn) {
        shipmentNavigationBtn.addEventListener('click', function () {
            toggleShipmentNavigationMenu();
        });
    }
}

shipmentNavigationBinder();

function toggleShipmentNavigationMenu() {
    const shipmentNavigationMenu = document.getElementById('shipment-navigation-menu');

    if (!shipmentNavigationMenu) {
        return;
    }

    shipmentNavigationMenu.classList.toggle('active');
}