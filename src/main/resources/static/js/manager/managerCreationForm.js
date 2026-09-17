const params = new URLSearchParams(window.location.search);

function payloadExtractor() {
    const operatorId = params.get("operatorId");

    if (operatorId) {
        const operatorIdDisplay = document.getElementById('operator-id-display');
        if (operatorIdDisplay) {
            operatorIdDisplay.style.display = 'flex';
        }
        const operatorIdInput = document.getElementById('operator-id');
        if (operatorIdInput) {
            operatorIdInput.value = operatorId;
        }
    }
}
payloadExtractor();

function valueInitializer(){
    const setCurrentAsManager = params.get("setCurrentAsManager");
    if(setCurrentAsManager === "true"){
        const managerStatus = document.querySelector('#manager-status option[value="IN_ACTIVE"]')?.remove();
    }
}
valueInitializer();

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
                "managerStatus": managerStatus.toUpperCase()
            };

            if (operatorId) {
                payload["operatorId"] = parseInt(operatorId, 10);
            }

            createForm.disabled = true;
            try{
                const accountUserName = params.get("accountUserName");
                const url = `/logistic/manager/save?accountUserName=${encodeURIComponent(accountUserName)}`;
                const methodType = 'POST';
                const response = await ajaxCall(url, methodType, payload);
                if (response) {
                    const managerId = response.data.managerId;

                    if (operatorId) {
                        // Flow B: User came from operator view
                        window.location.href = `../../views/manager/manager.html?managerId=${managerId}&userAction=${userAction}&operatorId=${operatorId}`;
                    } else {
                        // Flow A: User came from dashboard, needs to select operator
                        const accountUserName = params.get("accountUserName");
                        window.location.href = `../../views/operator/transport-types.html?userAction=Entry manager&accountUserName=${accountUserName}`;
                    }
                }
                createForm.disabled = false;
            }catch(error){
                console.log("Management save failed:", error);
                createForm.disabled = false;
            }
        });
    }
}
clickEventBinder();