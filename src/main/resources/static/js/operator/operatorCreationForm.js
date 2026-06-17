const params = new URLSearchParams(window.location.search);

function clickEventBinder() {

    const dashboardBtn = document.getElementById('dashboard-btn');
    if (dashboardBtn) {
        dashboardBtn.addEventListener('click', function () {
            window.location.href = "/views/dashboard.html";
        }, {once: true});
    }

    const operatorListBtn = document.getElementById('operator-list-btn');
    if (operatorListBtn) {
        operatorListBtn.addEventListener('click', function () {
            const userAction = 'Read operator';
            window.location.href = `../../views/operator/transport-types.html?userAction=${userAction}`;
        }, {once: true});
    }

    const entryOperatorBtn = document.getElementById('entry-operator-btn');
    if (entryOperatorBtn) {
        entryOperatorBtn.addEventListener('click', function () {
            const userAction = 'Entry operator';
            window.location.href = `../../views/operator/transport-types.html?userAction=${userAction}`;
        }, {once: true});
    }

    const createForm = document.querySelector('form');
    if (createForm) {
        createForm.addEventListener('submit', async function (event) {
            event.preventDefault();

            const operatorName = document.getElementById('operator-name').value.trim();
            const transportationType = document.getElementById('operator-transport-type').value.trim();

            if (operatorName === '') {
                alert('Operator Name not entered.');
                return;
            } else if (transportationType === '') {
                alert('Transportation Type not entered.');
                return;
            }

            const payload = {
                "operatorName": operatorName,
                "operatorTransportType": transportationType
            };

            const url = `/logistic/operator/save`;
            const methodType = 'POST';
            const response = await ajaxCall(url, methodType, payload);
            if (response) {
                const operatorId = response.operatorId;
                window.location.href = `../../views/operator/operator.html?operatorId=${operatorId}&userAction=Entry operator`;
            }
        });
    }
}
clickEventBinder();