const params = new URLSearchParams(window.location.search);

async function payloadExtractor() {
    const employeeId = params.get("employeeId");
    const url = `/logistic/employee/fetch?employeeId=${employeeId}`;
    const methodType = 'GET';
    const response = await ajaxCall(url, methodType, null);
    valueInitializer(response);
    dynamicLayoutRender(response.employeeId);
}
payloadExtractor();

function valueInitializer(response) {
    const employeeId = document.getElementById('employee-id');
    const employeeName = document.getElementById('employee-name');
    const employeePhoneNo = document.getElementById('employee-phone-no');
    const employeeDepartment = document.getElementById('employee-department');
    const employeeJoiningDate = document.getElementById('employee-joining-date');
    const employeeStatus = document.getElementById('employee-status');
    const reportingManagerId = document.getElementById('reporting-manager-id');
    const accountId = document.getElementById('account-id');
    const createdAt = document.getElementById('created-at');
    const updatedAt = document.getElementById('updated-at');
    const updatedBy = document.getElementById('updated-by');

    if (!response.employeeId) {
        alert('Employee not available.');
        return;
    }

    employeeId.value = response.employeeId;
    employeeName.value = response.employeeName;
    employeePhoneNo.value = response.employeePhoneNo;
    employeeDepartment.value = response.employeeDepartment;
    employeeJoiningDate.value = response.employeeJoiningDate;
    employeeStatus.value = response.employeeStatus;
    reportingManagerId.value = response.reportingManagerId;
    accountId.value = response.accountId;
    createdAt.value = response.createdAt;
    updatedAt.value = response.updatedAt;
    updatedBy.value = response.updatedBy;

    const userAction = params.get("userAction");
    if ((userAction === 'Read employee' || userAction === 'Administer employee') && response.accountVO) {
        const accountUsername = document.getElementById('account-username');
        const accountRole = document.getElementById('account-role');
        const accountStatus = document.getElementById('account-status');
        const accountEmail = document.getElementById('account-email');

        accountUsername.value = response.accountVO.accountUsername;
        accountRole.value = response.accountVO.accountRole;
        accountStatus.value = response.accountVO.accountStatus;
        accountEmail.value = response.accountVO.accountEmail;
    }
}

function dynamicLayoutRender(employeeId) {
    const userAction = params.get("userAction");

    if (userAction === 'Read employee' || userAction === 'Administrate employee') {
        const employeeHeaderActionsDivB = document.getElementById('employee-header-actions-b');
        if (employeeHeaderActionsDivB) {
            employeeHeaderActionsDivB.remove();
        }
        const employeeBodyCommonActionsDiv = document.getElementById('employee-body-common-actions');
        if (employeeBodyCommonActionsDiv) {
            employeeBodyCommonActionsDiv.remove();
        }

        const deleteBtn = document.getElementById('delete-btn');
        if (deleteBtn) {
            deleteBtn.setAttribute('data-employee-id', employeeId);
        }

    } else if (userAction === 'Entry employee') {
        const employeeHeaderActionsDivB = document.getElementById('employee-header-actions-b');
        if (employeeHeaderActionsDivB) {
            employeeHeaderActionsDivB.remove();
        }
        const employeeBodyActionsDiv = document.getElementById('employee-body-employee-actions');
        if (employeeBodyActionsDiv) {
            employeeBodyActionsDiv.remove();
        }
        const employeeBodyAccountDisplay = document.getElementById('employee-body-account-display');
        if (employeeBodyAccountDisplay) {
            employeeBodyAccountDisplay.remove();
        }

        const proceedBtn = document.getElementById('proceed-btn');
        if (proceedBtn) {
            proceedBtn.setAttribute('data-employee-id', employeeId);
        }
    }
}

function clickEventBinder() {
    const userAction = params.get("userAction");

    const dashboardBtn = document.getElementById('dashboard-btn');
    if (dashboardBtn) {
        dashboardBtn.addEventListener('click', function () {
            window.location.href = "/views/dashboard.html";
        }, {once: true});
    }

    const employeeListBtn = document.getElementById('employee-list-btn');
    if (employeeListBtn) {
        employeeListBtn.addEventListener('click', function () {
            window.location.href = `../../views/employee/employee-list.html?userAction=${userAction}`;
        }, {once: true});
    }

    const updateBtn = document.getElementById('update-btn');
    if (updateBtn) {
        updateBtn.addEventListener('click', async function () {
            const employeeId = document.getElementById('employee-id').value.trim();
            const employeeName = document.getElementById('employee-name').value.trim();
            const employeePhoneNo = document.getElementById('employee-phone-no').value.trim();
            const employeeDepartment = document.getElementById('employee-department').value.trim();
            const employeeJoiningDate = document.getElementById('employee-joining-date').value.trim();
            const employeeStatus = document.getElementById('employee-status').value.trim();
            const reportingManagerId = document.getElementById('reporting-manager-id').value.trim();
            const accountId = document.getElementById('account-id').value.trim();
            const createdAt = document.getElementById('created-at').value.trim();
            const updatedAt = document.getElementById('updated-at').value.trim();
            const updatedBy = document.getElementById('updated-by').value.trim();

            if (employeeId === '') {
                alert('Employee ID not available.');
            } else if (employeeName === '') {
                alert('Employee name not entered.');
            } else if (employeePhoneNo === '') {
                alert('Employee phone no not entered.');
            } else if (employeeDepartment === '') {
                alert('Employee department not entered.');
            } else if (employeeJoiningDate === '') {
                alert('Employee joining date not available.');
            } else if (employeeStatus === '') {
                alert('Employee status not entered.');
            } else if (reportingManagerId === '') {
                alert('Reporting manager ID not entered.');
            } else if (accountId === '') {
                alert('Account ID not available.');
            } else if (createdAt === '') {
                alert('Created date not available.');
            } else if (updatedAt === '') {
                alert('Updated date not entered.');
            } else if (updatedBy === '') {
                alert('Updated by not entered.');
            }

            const payload = {
                "employeeId": parseInt(employeeId, 10),
                "employeeName": employeeName,
                "employeePhoneNo": employeePhoneNo,
                "employeeDepartment": employeeDepartment,
                "employeeJoiningDate": employeeJoiningDate,
                "employeeStatus": employeeStatus,
                "reportingManagerId": parseInt(reportingManagerId, 10),
                "accountId": parseInt(accountId, 10),
                "createdAt": createdAt,
                "updatedAt": updatedAt,
                "updatedBy": parseInt(updatedBy, 10)
            };

            const response = await ajaxCall(`/logistic/employee/update`, 'PUT', payload);
            if (response) {
                alert(response);
            }
        });
    }

    const deleteBtn = document.getElementById('delete-btn');
    if (deleteBtn) {
        deleteBtn.addEventListener('click', async function () {
            const employeeId = this.dataset.employeeId;
            const url = `/logistic/employee/delete?employeeId=${employeeId}`;
            const methodType = 'DELETE';
            await ajaxCall(url, methodType, null);
        }, {once: true});
    }

    const proceedBtn = document.getElementById('proceed-btn');
    if (proceedBtn) {
        proceedBtn.addEventListener('click', function () {
            const employeeId = this.dataset.employeeId;
            window.location.href = `../../views/employee/employee-account-creation-form.html?userAction=${userAction}&employeeId=${employeeId}`;
        }, {once: true});
    }
}
clickEventBinder();