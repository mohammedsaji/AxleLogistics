const params = new URLSearchParams(window.location.search);

async function payloadExtractor() {

    const driverId = params.get("driverId");

    const url = `/logistic/driver/fetch?driverId=${driverId}`;
    const methodType = 'GET';
    const response = await ajaxCall(url, methodType, null);
    valueInitializer(response);
    dynamicLayoutRender(response.driverId, response.operatorId);
}
payloadExtractor();

function valueInitializer(response) {
    const driverId = document.getElementById('driver-id');
    const driverName = document.getElementById('driver-name');
    const driverPhoneNo = document.getElementById('driver-phone-no');
    const driverLicenseNo = document.getElementById('driver-license-no');
    const operatorId = document.getElementById('operator-id');
    const createdAt = document.getElementById('created-at');
    const updatedAt = document.getElementById('updated-at');
    const updatedBy = document.getElementById('updated-by');

    if (!response.driverId) {
        alert('Driver not available.');
        return;
    }

    driverId.value = response.driverId;
    driverName.value = response.driverName;
    driverPhoneNo.value = response.driverPhoneNo;
    driverLicenseNo.value = response.driverLicenseNo;
    operatorId.value = response.operatorId;
    createdAt.value = response.createdAt;
    updatedAt.value = response.updatedAt;
    updatedBy.value = response.updatedBy;
}

function dynamicLayoutRender(driverId, operatorId) {
    const userAction = params.get("userAction");

    if (userAction === 'Read operator') {
        const driverHeaderActionsDivA = document.querySelector('.driver-header-actions-a');
        if (driverHeaderActionsDivA) {
            driverHeaderActionsDivA.remove();
        }
        const driverHeaderActionsDivB = document.querySelector('.driver-header-actions-b');
        if (driverHeaderActionsDivB) {
            driverHeaderActionsDivB.remove();
        }
        const driverBodyCommonActionsDiv = document.querySelector('.driver-body-common-actions');
        if (driverBodyCommonActionsDiv) {
            driverBodyCommonActionsDiv.remove();
        }

        const deleteBtn = document.getElementById('delete-btn');
        if (deleteBtn) {
            deleteBtn.setAttribute('data-driver-id', driverId);
        }

    } else if (userAction === 'Entry driver') {
        const driverHeaderActionsDivA = document.querySelector('.driver-header-actions-a');
        if (driverHeaderActionsDivA) {
            driverHeaderActionsDivA.remove();
        }
        const driverHeaderActionsDivB = document.querySelector('.driver-header-actions-b');
        if (driverHeaderActionsDivB) {
            driverHeaderActionsDivB.remove();
        }
        const driverBodyCommonActionsDiv = document.querySelector('.driver-body-common-actions');
        if (driverBodyCommonActionsDiv) {
            driverBodyCommonActionsDiv.remove();
        }

        const deleteBtn = document.getElementById('delete-btn');
        if (deleteBtn) {
            deleteBtn.setAttribute('data-driver-id', driverId);
        }

    } else if (userAction === 'Entry shipping') {
        const driverHeaderActionsDivA = document.querySelector('.driver-header-actions-a');
        if (driverHeaderActionsDivA) {
            driverHeaderActionsDivA.remove();
        }
        const driverHeaderActionsDivB = document.querySelector('.driver-header-actions-b');
        if (driverHeaderActionsDivB) {
            driverHeaderActionsDivB.remove();
        }
        const driverBodyActionsDiv = document.querySelector('.driver-body-driver-actions');
        if (driverBodyActionsDiv) {
            driverBodyActionsDiv.remove();
        }

        const proceedBtn = document.getElementById('proceed-btn');
        if (proceedBtn) {
            proceedBtn.setAttribute('data-driver-id', driverId);
            proceedBtn.setAttribute('data-operator-id', operatorId);
        }

    } else if (userAction === 'Reassign driver') {
        const driverHeaderActionsDivA = document.querySelector('.driver-header-actions-a');
        if (driverHeaderActionsDivA) {
            driverHeaderActionsDivA.remove();
        }
        const driverHeaderActionsDivB = document.querySelector('.driver-header-actions-b');
        if (driverHeaderActionsDivB) {
            driverHeaderActionsDivB.remove();
        }
        const driverBodyActionsDiv = document.querySelector('.driver-body-driver-actions');
        if (driverBodyActionsDiv) {
            driverBodyActionsDiv.remove();
        }

        const proceedBtn = document.getElementById('proceed-btn');
        if (proceedBtn) {
            proceedBtn.setAttribute('data-driver-id', driverId);
        }
    }
}

function clickEventBinder() {

    const userAction = params.get("userAction");

    const driverListBtn = document.getElementById('driver-list-btn');
    if (driverListBtn) {
        driverListBtn.addEventListener('click', function () {
            const userAction = 'Read operator';
            window.location.href = `../../views/driver/driver-list.html?userAction=${userAction}`;
        }, {once: true});
    }

    const entryDriverBtn = document.getElementById('entry-driver-btn');
    if (entryDriverBtn) {
        entryDriverBtn.addEventListener('click', function () {
            const userAction = 'Entry driver';
            const operatorId = document.getElementById('operator-id').value.trim();
            window.location.href = `../../views/signUp/sign-up.html?userAction=${userAction}&operatorId=${operatorId}`;
        }, {once: true});
    }

    const dashboardBtn = document.getElementById('dashboard-btn');
    if (dashboardBtn) {
        dashboardBtn.addEventListener('click', function () {
            window.location.href = "/views/dashboard.html";
        }, {once: true});
    }

    const proceedBtn = document.getElementById('proceed-btn');
    if (proceedBtn) {
        proceedBtn.addEventListener('click', async function () {
            if (userAction === 'Entry shipping') {
                const driverId = this.dataset.driverId;
                const operatorId = this.dataset.operatorId;
                window.location.href = `../../views/vehicle/vehicle-list.html?userAction=${userAction}&driverId=${driverId}&operatorId=${operatorId}`;
            } else if (userAction === 'Reassign driver') {
                const driverId = this.dataset.driverId;
                const shippingStatusId = params.get("shippingStatusId");
                const url = `/logistic/status/update`;
                const methodType = 'POST';
                const payload = {
                    "shippingStatusId": shippingStatusId,
                    "driverId": parseInt(driverId, 10)
                };
                const response = await ajaxCall(url, methodType, payload);
                if (response) {
                    window.location.href = `../../views/status/status.html?userAction=${userAction}&shippingStatusId=${response.shippingStatusId}`;
                }
            }
        }, {once: true});
    }

    const updateBtn = document.getElementById('update-btn');
    if (updateBtn) {
        updateBtn.addEventListener('click', async function () {
            const driverId = document.getElementById('driver-id').value.trim();
            const driverName = document.getElementById('driver-name').value.trim();
            const driverPhoneNo = document.getElementById('driver-phone-no').value.trim();
            const driverLicenseNo = document.getElementById('driver-license-no').value.trim();
            const operatorId = document.getElementById('operator-id').value.trim();
            const createdAt = document.getElementById('created-at').value.trim();
            const updatedAt = document.getElementById('updated-at').value.trim();
            const updatedBy = document.getElementById('updated-by').value.trim();

            if (driverId === '') {
                alert('Driver ID not available.');
            } else if (driverName === '') {
                alert('Driver Name not entered.');
            } else if (driverPhoneNo === '') {
                alert('Driver Phone No not entered.');
            } else if (driverLicenseNo === '') {
                alert('Driver License No not entered.');
            } else if (operatorId === '') {
                alert('Operator ID not entered.');
            } else if (createdAt === '') {
                alert('Created date not entered.');
            } else if (updatedAt === '') {
                alert('Updated date not entered.');
            } else if (updatedBy === '') {
                alert('Updated by not entered.');
            }

            const payload = {
                "driverId": driverId,
                "driverName": driverName,
                "driverPhoneNo": driverPhoneNo,
                "driverLicenseNo": driverLicenseNo,
                "operatorId": operatorId,
                "createdAt": createdAt,
                "updatedAt": updatedAt,
                "updatedBy": updatedBy
            };

            const response = await ajaxCall(`/logistic/driver/update`, 'POST', payload);
            if (response) {
                alert(response);
            }
        });
    }

    const deleteBtn = document.getElementById('delete-btn');
    if (deleteBtn) {
        deleteBtn.addEventListener('click', async function () {
            const driverId = this.dataset.driverId;
            const url = `/logistic/driver/delete?driverId=${driverId}`;
            const methodType = 'DELETE';
            await ajaxCall(url, methodType, null);
        }, {once: true});
    }
}
clickEventBinder();