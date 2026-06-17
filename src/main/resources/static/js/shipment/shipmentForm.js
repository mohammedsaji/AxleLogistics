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

            // Shipment Details
            const shippingFrom = document.getElementById('shipping-from').value.trim();
            const shippingTo = document.getElementById('shipping-to').value.trim();
            const deliveryDate = document.getElementById('delivery-date').value.trim();

            // Carrier Information
            const operatorId = document.getElementById('operator-id').value.trim();
            const driverId = document.getElementById('driver-id').value.trim();
            const vehicleId = document.getElementById('vehicle-id').value.trim();

            // Validation
            if (cargoName === '') {
                alert('Cargo Name not entered.');
                return;
            } else if (cargoWeight === '') {
                alert('Cargo Weight not entered.');
                return;
            } else if (cargoQuantity === '') {
                alert('Cargo Quantity not entered.');
                return;
            } else if (cargoType === '') {
                alert('Cargo Type not entered.');
                return;
            } else if (cargoDescription === '') {
                alert('Cargo Description not entered.');
                return;
            } else if (customerName === '') {
                alert('Customer Name not entered.');
                return;
            } else if (customerEmail === '') {
                alert('Customer Email not entered.');
                return;
            } else if (customerPhoneno === '') {
                alert('Customer Phone No not entered.');
                return;
            } else if (shippingFrom === '') {
                alert('Shipping From not entered.');
                return;
            } else if (shippingTo === '') {
                alert('Shipping To not entered.');
                return;
            } else if (deliveryDate === '') {
                alert('Delivery Date not entered.');
                return;
            } else if (operatorId === '') {
                alert('Operator ID not available.');
                return;
            } else if (driverId === '') {
                alert('Driver ID not available.');
                return;
            } else if (vehicleId === '') {
                alert('Vehicle ID not available.');
                return;
            }

            const payload = {
                "cargoName": cargoName,
                "cargoWeight": cargoWeight,
                "cargoQuantity": cargoQuantity,
                "cargoType": cargoType,
                "cargoDescription": cargoDescription,
                "customerName": customerName,
                "customerEmail": customerEmail,
                "customerPhoneno": customerPhoneno,
                "shippingFrom": shippingFrom,
                "shippingTo": shippingTo,
                "deliveryDate": deliveryDate,
                "operatorId": parseInt(operatorId, 10),
                "driverId": parseInt(driverId, 10),
                "vehicleId": parseInt(vehicleId, 10)
            };

            const url = `/logistic/shipment/save`;
            const methodType = 'POST';
            const response = await ajaxCall(url, methodType, payload);

            if (response) {
                const shippingId = response.shippingId;
                window.location.href = `../../views/shipment/shipment.html?shippingId=${shippingId}&userAction=Entry shipping`;
            }
        });
    }
}
clickEventBinder();