const params = new URLSearchParams(window.location.search);

async function payloadExtractor() {

    const managerId = params.get("managerId");

    const url = `/logistic/manager/fetch?managerId=${managerId}`;
    const methodType = 'GET';
    const response = await ajaxCall(url, methodType, null);
    valueInitializer(response);
    dynamicLayoutRender(response.managerId, response.operatorId);
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
    createdAt.value = response.createdAt;
    updatedAt.value = response.updatedAt;
    updatedBy.value = response.updatedBy;
}

function dynamicLayoutRender(managerId, operatorId) {
    const userAction = params.get("userAction");

    if (userAction === 'Entry manager') {
        const managerHeaderActionsDivA = document.querySelector('.manager-header-actions-a');
        if (managerHeaderActionsDivA) {
            managerHeaderActionsDivA.remove();
        }
        const managerHeaderActionsDivB = document.querySelector('.manager-header-actions-b');
        if (managerHeaderActionsDivB) {
            managerHeaderActionsDivB.remove();
        }
        const managerBodyActionsDiv = document.querySelector('.manager-body-manager-actions');
        if (managerBodyActionsDiv) {
            managerBodyActionsDiv.remove();
        }

        const entryManagerBtn = document.getElementById('entry-manager-btn');
        entryManagerBtn.setAttribute('data-operator-id',operatorId);

    } else if (userAction === 'Read operator') {
        const managerHeaderActionsDivA = document.querySelector('.manager-header-actions-a');
        if (managerHeaderActionsDivA) {
            managerHeaderActionsDivA.remove();
        }
        const managerHeaderActionsDivB = document.querySelector('.manager-header-actions-b');
        if (managerHeaderActionsDivB) {
            managerHeaderActionsDivB.remove();
        }

        const deleteBtn = document.getElementById('delete-btn');
        if (deleteBtn) {
            deleteBtn.setAttribute('data-manager-id', managerId);
        }
    }
}

function clickEventBinder() {

    const userAction = params.get("userAction");

    const managerListBtn = document.getElementById('manager-list-btn');
    if (managerListBtn) {
        managerListBtn.addEventListener('click', function () {
            const userAction = 'Read operator';
            window.location.href = `../../views/manager/manager-list.html?userAction=${userAction}`;
        }, {once: true});
    }

    const entryManagerBtn = document.getElementById('entry-manager-btn');
    if (entryManagerBtn) {
        entryManagerBtn.addEventListener('click', function () {
            const userAction = 'Entry manager';
            const operatorId = this.dataset.operatorId;
            window.location.href = `../../views/signUp/sign-up.html?userAction=${userAction}&operatorId=${operatorId}`;
        }, {once: true});
    }

    const dashboardBtn = document.getElementById('dashboard-btn');
    if (dashboardBtn) {
        dashboardBtn.addEventListener('click', function () {
            window.location.href = "/views/dashboard.html";
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
            } else if (managerName === '') {
                alert('Manager Name not entered.');
            } else if (managerContactNo === '') {
                alert('Manager Contact No not entered.');
            } else if (operatorId === '') {
                alert('Operator ID not entered.');
            } else if (managerStatus === '') {
                alert('Manager Status not entered.');
            } else if (createdAt === '') {
                alert('Created date not entered.');
            } else if (updatedAt === '') {
                alert('Updated date not entered.');
            } else if (updatedBy === '') {
                alert('Updated by not entered.');
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

            const response = await ajaxCall(`/logistic/manager/update`, 'POST', payload);
            if (response) {
                alert(response);
            }
        });
    }

    const deleteBtn = document.getElementById('delete-btn');
    if (deleteBtn) {
        deleteBtn.addEventListener('click', async function () {
            const managerId = this.dataset.managerId;
            const url = `/logistic/manager/delete?managerId=${managerId}`;
            const methodType = 'DELETE';
            await ajaxCall(url, methodType, null);
        }, {once: true});
    }
}
clickEventBinder();