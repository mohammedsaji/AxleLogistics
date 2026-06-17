const params = new URLSearchParams(window.location.search);

function payloadExtractor() {
    const operatorId = params.get("operatorId");

    if (operatorId) {
        const operatorIdDisplay = document.getElementById('operator-id-display');
        if (operatorIdDisplay) {
            operatorIdDisplay.style.display = 'block';
        }
        const operatorIdInput = document.getElementById('operator-id');
        if (operatorIdInput) {
            operatorIdInput.value = operatorId;
        }
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

            const vehicleType = document.getElementById('vehicle-type').value.trim();
            const vehicleNumber = document.getElementById('vehicle-number').value.trim();
            const operatorIdInput = document.getElementById('operator-id');
            const operatorId = operatorIdInput ? operatorIdInput.value.trim() : null;

            if (vehicleType === '') {
                alert('Vehicle Type not entered.');
                return;
            } else if (vehicleNumber === '') {
                alert('Vehicle Number not entered.');
                return;
            } else if (!operatorId) {
                alert('Operator ID not available.');
                return;
            }

            const payload = {
                "vehicleType": vehicleType,
                "vehicleNumber": vehicleNumber,
                "operatorId": parseInt(operatorId, 10)
            };

            const url = `/logistic/vehicle/save`;
            const methodType = 'POST';
            const response = await ajaxCall(url, methodType, payload);

            if (response) {
                const vehicleId = response.vehicleId;
                window.location.href = `../../views/vehicle/vehicle.html?vehicleId=${vehicleId}&userAction=Entry operator&operatorId=${operatorId}`;
            }
        });
    }
}
clickEventBinder();