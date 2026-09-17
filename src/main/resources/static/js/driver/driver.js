const params = new URLSearchParams(window.location.search);

async function payloadExtractor() {

    const driverId = params.get("driverId");
    const url = `/logistic/driver/fetch?driverId=${driverId}`;
    const methodType = 'GET';
    const response = await ajaxCall(url, methodType, null);
    if (response && response.data) {
        const loginUserMap = await ajaxCall(`/logistic/account/user-info`, 'GET', null);
        const roleArray = loginUserMap?.data?.RoleList;
        if(roleArray && roleArray.length > 0){
            valueInitializer(response.data);
            dynamicLayoutRender(response.data.driverId, response.data.operatorId, roleArray);
            clickEventBinder();
        }
    }
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
    createdAt.value = formatDateTime(response.createdAt);
    updatedAt.value = formatDateTime(response.updatedAt);
    updatedBy.value = response.updatedBy;
}

function dynamicLayoutRender(driverId, operatorId, roleArray) {
    const userAction = params.get("userAction");

    if(!roleArray.includes("ADMIN")){
        const backToOperatorBtn = document.getElementById('back-to-operator-btn');
        if(backToOperatorBtn) {
            backToOperatorBtn.remove();
        }

        const driverHeaderSectionDivB = document.querySelector('.driver-header-section-b');
        if(driverHeaderSectionDivB){
            driverHeaderSectionDivB.remove();
        }

        const driverBodyDriverActionsDiv = document.querySelector('.driver-body-driver-actions');
        if(driverBodyDriverActionsDiv){
            driverBodyDriverActionsDiv.remove();
        }
    }

    if (userAction === 'Read operator') {
        const driverBodyCommonActionsDiv = document.querySelector('.driver-body-common-actions');
        if (driverBodyCommonActionsDiv) {
            driverBodyCommonActionsDiv.remove();
        }

        const driverListBtn = document.getElementById('driver-list-btn');
        driverListBtn.setAttribute('data-operator-id', operatorId);

        const createDriverBtn = document.getElementById('create-driver-btn');
        createDriverBtn.setAttribute('data-operator-id', operatorId);

        const deleteBtn = document.getElementById('delete-btn');
        if (deleteBtn) {
            deleteBtn.setAttribute('data-driver-id', driverId);
        }

    } else if (userAction === 'Entry driver') {
        const backToOperatorBtn = document.getElementById('back-to-operator-btn');
        if (backToOperatorBtn) {
            backToOperatorBtn.remove();
        }
        const driverHeaderSectionDivB = document.querySelector('.driver-header-section-b');
        if (driverHeaderSectionDivB) {
            driverHeaderSectionDivB.remove();
        }
        const driverBodyCommonActionsDiv = document.querySelector('.driver-body-common-actions');
        if (driverBodyCommonActionsDiv) {
            driverBodyCommonActionsDiv.remove();
        }

        if(roleArray.includes("ADMIN")){
            const deleteBtn = document.getElementById('delete-btn');
            if (deleteBtn) {
                deleteBtn.setAttribute('data-driver-id', driverId);
            }

            const proceedBtn = document.getElementById('proceed-btn');
            if (proceedBtn) {
                proceedBtn.setAttribute('data-driver-id', driverId);
                proceedBtn.setAttribute('data-operator-id', operatorId);
            }
        }

    } else if (userAction === 'Entry shipping') {
        const driverHeaderSectionDivB = document.querySelector('.driver-header-section-b');
        if (driverHeaderSectionDivB) {
            driverHeaderSectionDivB.remove();
        }
        const driverBodyActionsDiv = document.querySelector('.driver-body-driver-actions');
        if (driverBodyActionsDiv) {
            driverBodyActionsDiv.remove();
        }

        if(roleArray.includes("ADMIN")){
            const proceedBtn = document.getElementById('proceed-btn');
            if (proceedBtn) {
                proceedBtn.setAttribute('data-driver-id', driverId);
                proceedBtn.setAttribute('data-operator-id', operatorId);
            }
        }

    } else if (userAction === 'Reassign driver') {
        const backToOperatorBtn = document.getElementById('back-to-operator-btn');
        if (backToOperatorBtn) {
            backToOperatorBtn.remove();
        }
        const driverHeaderSectionDivB = document.querySelector('.driver-header-section-b');
        if (driverHeaderSectionDivB) {
            driverHeaderSectionDivB.remove();
        }
        const driverBodyActionsDiv = document.querySelector('.driver-body-driver-actions');
        if (driverBodyActionsDiv) {
            driverBodyActionsDiv.remove();
        }

        if(roleArray.includes("ADMIN") || roleArray.includes("FEDERATE-MANAGER")){
            const proceedBtn = document.getElementById('proceed-btn');
            if (proceedBtn) {
                proceedBtn.setAttribute('data-driver-id', driverId);
                proceedBtn.setAttribute('data-operator-id', operatorId);
            }
        }
    }
}

function clickEventBinder() {

    const userAction = params.get("userAction");

    const driverListBtn = document.getElementById('driver-list-btn');
    if (driverListBtn) {
        driverListBtn.addEventListener('click', function () {
            const userAction = 'Read operator';
            const operatorId = this.dataset.operatorId;
            window.location.href = `../../views/driver/driver-list.html?userAction=${userAction}&operatorId=${operatorId}`;
        }, {once: true});
    }

    const createDriverBtn = document.getElementById('create-driver-btn');
    if (createDriverBtn) {
        createDriverBtn.addEventListener('click', function () {
            const userAction = 'Entry federate';
            const specificAction = 'Entry Driver';
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

    const proceedBtn = document.getElementById('proceed-btn');
    if (proceedBtn) {
        proceedBtn.addEventListener('click', async function () {
            if (userAction === 'Entry shipping') {
                const driverId = this.dataset.driverId;
                const operatorId = this.dataset.operatorId;
                window.location.href = `../../views/vehicle/vehicle-list.html?userAction=${userAction}&driverId=${driverId}&operatorId=${operatorId}`;
            } else if (userAction === 'Reassign driver') {
                const driverId = this.dataset.driverId;
                const operatorId = this.dataset.operatorId;
                const shippingId = params.get("shippingId");
                const updatedUserAction = 'Reassign vehicle';
                window.location.href = `../../views/vehicle/vehicle-list.html?userAction=${updatedUserAction}&shippingId=${shippingId}&operatorId=${operatorId}&driverId=${driverId}`;
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
                return;
            } else if (driverName === '') {
                alert('Driver Name not entered.');
                return;
            } else if (driverPhoneNo === '') {
                alert('Driver Phone No not entered.');
                return;
            } else if (driverLicenseNo === '') {
                alert('Driver License No not entered.');
                return;
            } else if (operatorId === '') {
                alert('Operator ID not entered.');
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
                "driverId": driverId,
                "driverName": driverName,
                "driverPhoneNo": driverPhoneNo,
                "driverLicenseNo": driverLicenseNo,
                "operatorId": operatorId,
                "createdAt": createdAt,
                "updatedAt": updatedAt,
                "updatedBy": updatedBy
            };

            const response = await ajaxCall(`/logistic/driver/update`, 'PUT', payload);
            if (response) {
                alert(response.message || 'Driver updated successfully!');
            }
        });
    }

    const deleteBtn = document.getElementById('delete-btn');
    if (deleteBtn) {
        deleteBtn.addEventListener('click', async function () {
            const driverId = document.getElementById('driver-id').value.trim();
            if (driverId === '') {
                alert("DriverId not entered.");
                return;
            }
            const url = `/logistic/driver/delete?driverId=${driverId}`;
            const methodType = 'DELETE';
            const response = await ajaxCall(url, methodType, null);

            if (response && response.success) {
                alert(response.message || 'Driver deleted successfully.');
                window.location.href = "/views/dashboard.html";
            }
        });
    }
}

function driverNavigationBinder() {

    const driverNavigationBtn = document.getElementById('driver-navigation-btn');

    if (driverNavigationBtn) {
        driverNavigationBtn.addEventListener('click', function () {
            toggleDriverNavigationMenu();
        });
    }
}

driverNavigationBinder();

function toggleDriverNavigationMenu() {

    const driverNavigationMenu = document.getElementById('driver-navigation-menu');

    if (!driverNavigationMenu) {
        return;
    }

    driverNavigationMenu.classList.toggle('active');
}