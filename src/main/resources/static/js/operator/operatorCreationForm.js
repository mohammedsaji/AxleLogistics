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
            window.location.href = `../../views/operator/transport-types.html?userAction=Read operator`;
        }, {once: true});
    }

    const createOperatorBtn = document.getElementById('create-operator-btn');
    if (createOperatorBtn) {
        createOperatorBtn.addEventListener('click', function () {
            window.location.href = `../../views/operator/operator-creation-form.html?userAction=Entry operator`;
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
                const operatorId = response.data.operatorId;
                window.location.href = `../../views/operator/operator.html?operatorId=${operatorId}&userAction=Entry operator`;
            }
        });
    }
}
clickEventBinder();

function operatorNavigationBinder() {

    const operatorNavigationBtn = document.getElementById('operator-navigation-btn');

    if (operatorNavigationBtn) {
        operatorNavigationBtn.addEventListener('click', function () {
            toggleOperatorNavigationMenu();
        });
    }
}

operatorNavigationBinder();

function toggleOperatorNavigationMenu() {

    const operatorNavigationMenu = document.getElementById('operator-navigation-menu');

    if (!operatorNavigationMenu) {
        return;
    }

    operatorNavigationMenu.classList.toggle('active');
}