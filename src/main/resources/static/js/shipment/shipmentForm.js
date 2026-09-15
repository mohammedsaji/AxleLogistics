const params = new URLSearchParams(window.location.search);

function payloadExtractor() {
    const operatorId = params.get("operatorId");
    const driverId = params.get("driverId");
    const vehicleId = params.get("vehicleId");

    const operatorIdInput = document.getElementById('operator-id');
    const driverIdInput = document.getElementById('driver-id');
    const vehicleIdInput = document.getElementById('vehicle-id');

    if (operatorIdInput && operatorId) {
        operatorIdInput.value = operatorId;
    }
    if (driverIdInput && driverId) {
        driverIdInput.value = driverId;
    }
    if (vehicleIdInput && vehicleId) {
        vehicleIdInput.value = vehicleId;
    }
}
payloadExtractor();

// Generalized so it can drive any of the three pickers on this form —
// takes the suggestion container as a parameter instead of a hardcoded id.
function locationTracker(inputEl, latitudeEl, longitudeEl, suggestionsEl) {
    if (!inputEl) return;

    inputEl.addEventListener('input', async function () {
        suggestionsEl.innerHTML = '';

        const results = await searchLocation(this.value.trim());

        results.forEach(function (result) {
            const properties = result.properties;

            const suggestion = document.createElement('div');
            suggestion.textContent = [
                properties.name,
                properties.city,
                properties.state,
                properties.country
            ].filter(Boolean).join(', ');

            suggestion.addEventListener('click',  function () {
                inputEl.value = suggestion.textContent;

                const coordinates = result.geometry.coordinates; // [longitude, latitude]
                latitudeEl.value = coordinates[1];
                longitudeEl.value = coordinates[0];

                suggestionsEl.innerHTML = '';
            });

            suggestionsEl.appendChild(suggestion);
        });
    });
}

// Origin (shipping-from)
locationTracker(
    document.getElementById('shipping-from'),
    document.getElementById('origin-latitude'),
    document.getElementById('origin-longitude'),
    document.getElementById('location-suggestions-origin')
);

// Active operator leg destination (current-location)
locationTracker(
    document.getElementById('current-location'),
    document.getElementById('current-latitude'),
    document.getElementById('current-longitude'),
    document.getElementById('location-suggestions')
);

// Final delivery destination (shipping-to)
locationTracker(
    document.getElementById('shipping-to'),
    document.getElementById('destination-latitude'),
    document.getElementById('destination-longitude'),
    document.getElementById('location-suggestions-destination')
);



function clickEventBinder() {

    const dashboardBtn = document.getElementById('dashboard-btn');
    if (dashboardBtn) {
        dashboardBtn.addEventListener('click', function () {
            window.location.href = "/views/dashboard.html";
        }, {once: true});
    }

    const createForm = document.querySelector('form');
    if (createForm) {
        createForm.addEventListener('submit', async function (event) {
            event.preventDefault();

            // Cargo Details
            const cargoName = document.getElementById('cargo-name').value.trim();
            const cargoWeight = document.getElementById('cargo-weight').value.trim();
            const cargoQuantity = document.getElementById('cargo-quantity').value.trim();
            const cargoType = document.getElementById('cargo-type').value.trim();
            const cargoDescription = document.getElementById('cargo-description').value.trim();

            // Customer Details
            const customerName = document.getElementById('customer-name').value.trim();
            const customerEmail = document.getElementById('customer-email').value.trim();
            const customerPhoneno = document.getElementById('customer-phoneno').value.trim();

            // Shipment From Details
            const shippingFrom = document.getElementById('shipping-from').value.trim();
            const originLatitude = document.getElementById('origin-latitude').value.trim();
            const originLongitude = document.getElementById('origin-longitude').value.trim();

            // Current Operator Leg Details
            const currentLocation = document.getElementById('current-location').value.trim();
            const currentLatitude = document.getElementById('current-latitude').value.trim();
            const currentLongitude = document.getElementById('current-longitude').value.trim();

            // Shipment To Details
            const shippingTo = document.getElementById('shipping-to').value.trim();
            const destinationLatitude = document.getElementById('destination-latitude').value.trim();
            const destinationLongitude = document.getElementById('destination-longitude').value.trim();

            const deliveryDate = document.getElementById('delivery-date').value.trim();

            // Carrier Information
            const operatorId = document.getElementById('operator-id').value.trim();
            const driverId = document.getElementById('driver-id').value.trim();
            const vehicleId = document.getElementById('vehicle-id').value.trim();

            // Validation
            if (cargoName === '') {
                alert('Cargo Name not entered.');
                return;
            }
            if (cargoWeight === '') {
                alert('Cargo Weight not entered.');
                return;
            }
            if (cargoQuantity === '') {
                alert('Cargo Quantity not entered.');
                return;
            }
            if (cargoType === '') {
                alert('Cargo Type not entered.');
                return;
            }
            if (cargoDescription === '') {
                alert('Cargo Description not entered.');
                return;
            }
            if (customerName === '') {
                alert('Customer Name not entered.');
                return;
            }
            if (customerEmail === '') {
                alert('Customer Email not entered.');
                return;
            }
            if (customerPhoneno === '') {
                alert('Customer Phone No not entered.');
                return;
            }
            if (shippingFrom === '') {
                alert('Shipping From not entered.');
                return;
            }
            if (originLatitude === '' || originLongitude === '') {
                alert('Please select the Shipping From location from the suggestions.');
                return;
            }
            if (currentLocation === '') {
                alert('Operator destination not entered.');
                return;
            }
            if (currentLatitude === '' || currentLongitude === '') {
                alert('Please select the operator destination from the suggestions.');
                return;
            }
            if (shippingTo === '') {
                alert('Shipping To not entered.');
                return;
            }
            if (destinationLatitude === '' || destinationLongitude === '') {
                alert('Please select the Shipping To location from the suggestions.');
                return;
            }
            if (deliveryDate === '') {
                alert('Delivery Date not entered.');
                return;
            }
            if (operatorId === '') {
                alert('Operator ID not available.');
                return;
            }
            if (driverId === '') {
                alert('Driver ID not available.');
                return;
            }
            if (vehicleId === '') {
                alert('Vehicle ID not available.');
                return;
            }

            const payload = {
                customerRequest: {
                    customerName: customerName,
                    customerEmail: customerEmail,
                    customerPhoneno: customerPhoneno
                },
                cargoRequest: {
                    cargoName: cargoName,
                    cargoWeight: cargoWeight,
                    cargoQuantity: cargoQuantity,
                    cargoType: cargoType,
                    cargoDescription: cargoDescription
                },
                shipmentRequest: {
                    shippingFrom: shippingFrom,
                    originLatitude: originLatitude,
                    originLongitude: originLongitude,
                    currentLocation: currentLocation,
                    currentLatitude: currentLatitude,
                    currentLongitude: currentLongitude,
                    shippingTo: shippingTo,
                    destinationLatitude: destinationLatitude,
                    destinationLongitude: destinationLongitude,
                    deliveryDate: deliveryDate
                },
                operatorRequest: {
                    operatorId: parseInt(operatorId, 10)
                },
                driverRequest: {
                    driverId: parseInt(driverId, 10)
                },
                vehicleRequest: {
                    vehicleId: parseInt(vehicleId, 10)
                }
            };

            createForm.disabled = true;

            try{
                const url = `/logistic/shipment/save`;
                const methodType = 'POST';
                const response = await ajaxCall(url, methodType, payload);

                if (response) {
                    const shippingId = response.data.shippingId;
                    window.location.href = `../../views/shipment/shipment.html?shippingId=${shippingId}&userAction=Read shipment`;
                }
                createForm.disbaled = false;
            }catch(error){
                console.log("Shipment save failed:", error);
                createForm.disabled = false;
            }
        });
    }
}
clickEventBinder();