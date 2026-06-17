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

    const accountUserName = params.get("accountUserName");

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

            const managerName = document.getElementById('manager-name').value.trim();
            const managerContactNo = document.getElementById('manager-contact-no').value.trim();
            const managerStatus = document.getElementById('manager-status').value.trim();
            const operatorIdInput = document.getElementById('operator-id');
            const operatorId = operatorIdInput ? operatorIdInput.value.trim() : null;

            if (managerName === '') {
                alert('Manager Name not entered.');
                return;
            } else if (managerContactNo === '') {
                alert('Manager Contact No not entered.');
                return;
            } else if (managerStatus === '') {
                alert('Manager Status not entered.');
                return;
            }

            const payload = {
                "managerName": managerName,
                "managerContactNo": managerContactNo,
                "managerStatus": managerStatus,
                "accountUserName": accountUserName
            };

            if (operatorId) {
                payload["operatorId"] = parseInt(operatorId, 10);
            }

            const url = `/logistic/manager/save`;
            const methodType = 'POST';
            const response = await ajaxCall(url, methodType, payload);

            if (response) {
                const managerId = response.managerId;

                if (operatorId) {
                    // Flow B: User came from operator view
                    window.location.href = `../../views/manager/manager.html?managerId=${managerId}&userAction=Entry manager&operatorId=${operatorId}`;
                } else {
                    // Flow A: User came from dashboard, needs to select operator
                    const accountUserName = params.get("accountUserName");
                    window.location.href = `../../views/operator/transport-types.html?userAction=Entry manager&accountUserName=${accountUserName}`;
                }
            }
        });
    }
}
clickEventBinder();