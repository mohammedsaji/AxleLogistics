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
    const userAction = params.get("userAction");

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

            const driverName = document.getElementById('driver-name').value.trim();
            const driverPhoneNo = document.getElementById('driver-phone-no').value.trim();
            const driverLicenseNo = document.getElementById('driver-license-no').value.trim();
            const operatorIdInput = document.getElementById('operator-id');
            const operatorId = operatorIdInput ? operatorIdInput.value.trim() : null;

            if (driverName === '') {
                alert('Driver Name not entered.');
                return;
            } else if (driverPhoneNo === '') {
                alert('Driver Phone No not entered.');
                return;
            } else if (driverLicenseNo === '') {
                alert('Driver License No not entered.');
                return;
            }

            const payload = {
                "driverName": driverName,
                "driverPhoneNo": driverPhoneNo,
                "driverLicenseNo": driverLicenseNo
            };

            if (operatorId) {
                payload["operatorId"] = parseInt(operatorId, 10);
            }

            createForm.disabled = true;

            try{
                const accountUserName = params.get("accountUserName");
                const url = `/logistic/driver/save?accountUserName=${encodeURIComponent(accountUserName)}`;
                const methodType = 'POST';
                const response = await ajaxCall(url, methodType, payload);

                if (response) {
                    const driverId = response.data.driverId;

                    if (operatorId) {
                        // Flow B: User came from operator view
                        window.location.href = `../../views/driver/driver.html?driverId=${driverId}&userAction=${userAction}&operatorId=${operatorId}`;
                    } else {
                        // Flow A: User came from dashboard, needs to select operator
                        const accountUserName = params.get("accountUserName");
                        window.location.href = `../../views/operator/transport-types.html?userAction=Entry driver&accountUserName=${accountUserName}`;
                    }
                }
                createForm.disabled = false;
            }
            catch(error){
                console.log("Driver save failed:", error);
                createForm.disabled = false;
            }
        });
    }
}
clickEventBinder();