const params = new URLSearchParams(window.location.search);

async function payloadExtractor() {

    const managerId = params.get("managerId");

    const url = `/logistic/manager/fetch?managerId=${managerId}`;
    const methodType = 'GET';
    const response = await ajaxCall(url, methodType, null);
    if (response && response.data) {
        valueInitializer(response.data);
        dynamicLayoutRender(response.data.managerId, response.data.operatorId);
        clickEventBinder();
    }
}

payloadExtractor();

function valueInitializer(response) {
    const managerId = document.getElementById('manager-id');
    const managerName = document.getElementById('manager-name');
    const managerContactNo = document.getElementById('manager-contact-no');
    const operatorId = document.getElementById('operator-id');
    const managerStatus = document.getElementById('manager-status');
    const createdAt = document.getElementById('created-at');
    const updatedAt = document.getElementById('updated-at');
    const updatedBy = document.getElementById('updated-by');

    if (!response.managerId) {
        alert('Manager not available.');
        return;
    }

    managerId.value = response.managerId;
    managerName.value = response.managerName;
    managerContactNo.value = response.managerContactNo;
    operatorId.value = response.operatorId;
    managerStatus.value = response.managerStatus;
    createdAt.value = formatDateTime(response.createdAt);
    updatedAt.value = formatDateTime(response.updatedAt);
    updatedBy.value = response.updatedBy;
}

function dynamicLayoutRender(managerId, operatorId) {
    const userAction = params.get("userAction");

    if (userAction === 'Entry manager') {

        const backToOperatorBtn = document.getElementById('back-to-operator-btn');
        if (backToOperatorBtn) {
            backToOperatorBtn.remove();
        }

        const managerHeaderSectionDivB = document.querySelector('.manager-header-section-b');
        if (managerHeaderSectionDivB) {
            managerHeaderSectionDivB.remove();
        }

    } else if (userAction === 'Read operator') {

        const managerListBtn = document.getElementById('manager-list-btn');
        managerListBtn.setAttribute('data-operator-id', operatorId);

        const entryManagerBtn = document.getElementById('entry-manager-btn');
        entryManagerBtn.setAttribute('data-operator-id', operatorId);
    }

    const deleteBtn = document.getElementById('delete-btn');
    if (deleteBtn) {
        deleteBtn.setAttribute('data-manager-id', managerId);
    }
}

function clickEventBinder() {

    const userAction = params.get("userAction");

    const managerListBtn = document.getElementById('manager-list-btn');
    if (managerListBtn) {
        managerListBtn.addEventListener('click', function () {
            const userAction = 'Read operator';
            const operatorId = document.getElementById('operator-id').value.trim();
            window.location.href = `../../views/manager/manager-list.html?userAction=${userAction}&operatorId=${operatorId}`;
        }, {once: true});
    }

    const entryManagerBtn = document.getElementById('entry-manager-btn');
    if (entryManagerBtn) {
        entryManagerBtn.addEventListener('click', function () {
            const userAction = 'Entry federate';
            const specificAction = 'Entry Manager';
            const operatorId = this.dataset.operatorId;
            window.location.href = `../../views/signUp/sign-up.html?userAction=${userAction}&operatorId=${operatorId}&specificAction=${specificAction}`;
        }, {once: true});
    }

    const dashboardBtn = document.getElementById('dashboard-btn');
    if (dashboardBtn) {
        dashboardBtn.addEventListener('click', function () {
            window.location.href = "/views/dashboard.html";
        }, {once: true});
    }

    const backToOperator = document.getElementById('back-to-operator-btn');
    if (backToOperator) {
        backToOperator.addEventListener('click', function () {
            const operatorId =document.getElementById('operator-id').value.trim();
            window.location.href = window.location.href = `../../views/operator/operator.html?operatorId=${operatorId}&userAction=${userAction}`;
        }, {once: true});
    }

    const updateBtn = document.getElementById('update-btn');
    if (updateBtn) {
        updateBtn.addEventListener('click', async function () {
            const managerId = document.getElementById('manager-id').value.trim();
            const managerName = document.getElementById('manager-name').value.trim();
            const managerContactNo = document.getElementById('manager-contact-no').value.trim();
            const operatorId = document.getElementById('operator-id').value.trim();
            const managerStatus = document.getElementById('manager-status').value.trim();
            const createdAt = document.getElementById('created-at').value.trim();
            const updatedAt = document.getElementById('updated-at').value.trim();
            const updatedBy = document.getElementById('updated-by').value.trim();

            if (managerId === '') {
                alert('Manager ID not available.');
                return;
            } else if (managerName === '') {
                alert('Manager Name not entered.');
                return;
            } else if (managerContactNo === '') {
                alert('Manager Contact No not entered.');
                return;
            } else if (operatorId === '') {
                alert('Operator ID not entered.');
                return;
            } else if (managerStatus === '') {
                alert('Manager Status not entered.');
                return;
            } else if (createdAt === '') {
                alert('Created date not entered.');
                return;
            } else if (updatedAt === '') {
                alert('Updated date not entered.');
                return;
            } else if (updatedBy === '') {
                alert('Updated by not entered.');
                return;
            }

            const payload = {
                "managerId": managerId,
                "managerName": managerName,
                "managerContactNo": managerContactNo,
                "operatorId": operatorId,
                "managerStatus": managerStatus,
                "createdAt": createdAt,
                "updatedAt": updatedAt,
                "updatedBy": updatedBy
            };

            const response = await ajaxCall(`/logistic/manager/update`, 'PUT', payload);
            if (response) {
                alert(response.message);
            }
        });
    }

    const deleteBtn = document.getElementById('delete-btn');
    if (deleteBtn) {
        deleteBtn.addEventListener('click', async function () {
            const managerId = this.dataset.managerId;
            const url = `/logistic/manager/delete?managerId=${managerId}`;
            const methodType = 'DELETE';
            const response = await ajaxCall(url, methodType, null);

            if (response && response.success) {
                alert(response.message || 'Manager deleted successfully.');
                window.location.href = "/views/dashboard.html";
            }
        }, {once: true});
    }
}

function managerNavigationBinder() {
    const managerNavigationBtn = document.getElementById('manager-navigation-btn');

    if (managerNavigationBtn) {
        managerNavigationBtn.addEventListener('click', function () {
            toggleManagerNavigationMenu();
        });
    }
}

managerNavigationBinder();

function toggleManagerNavigationMenu() {
    const managerNavigationMenu = document.getElementById('manager-navigation-menu');

    if (!managerNavigationMenu) {
        return;
    }

    managerNavigationMenu.classList.toggle('active');
}