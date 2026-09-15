const params = new URLSearchParams(window.location.search);

async function payloadExtractor() {
    const vehicleId = params.get("vehicleId");
    const url = `/logistic/vehicle/fetch?vehicleId=${vehicleId}`;
    const methodType = 'GET';
    const response = await ajaxCall(url, methodType, null);
    if (response && response.data) {
        const loginUserMap = await ajaxCall(`/logistic/account/user-info`, 'GET', null);
        const roleArray = loginUserMap?.data?.RoleList;
        if(roleArray && roleArray.length > 0) {
            valueInitializer(response.data);
            dynamicLayoutRender(response.data.vehicleId, response.data.operatorId, roleArray);
            clickEventBinder();
        }
    }
}

payloadExtractor();

function valueInitializer(response) {
    const vehicleId = document.getElementById('vehicle-id');
    const vehicleType = document.getElementById('vehicle-type');
    const vehicleNumber = document.getElementById('vehicle-number');
    const operatorId = document.getElementById('operator-id');
    const createdAt = document.getElementById('created-at');
    const updatedAt = document.getElementById('updated-at');
    const updatedBy = document.getElementById('updated-by');

    if (!response.vehicleId) {
        alert('Vehicle not available.');
        return;
    }

    vehicleId.value = response.vehicleId;
    vehicleType.value = response.vehicleType;
    vehicleNumber.value = response.vehicleNumber;
    operatorId.value = response.operatorId;
    createdAt.value = formatDateTime(response.createdAt);
    updatedAt.value = formatDateTime(response.updatedAt);
    updatedBy.value = response.updatedBy;
}

function dynamicLayoutRender(vehicleId, operatorId, roleArray) {
    const userAction = params.get("userAction");

    if(!roleArray.includes("ADMIN")){
        const backToOperatorBtn = document.getElementById('back-to-operator-btn');
        if(backToOperatorBtn) {
            backToOperatorBtn.remove();
        }
    }

    if (userAction === 'Entry shipping' ||
        userAction === 'Reassign vehicle' ) {

        const vehicleHeaderSectionDivB = document.getElementById('vehicle-header-section-b');
        if (vehicleHeaderSectionDivB) {
            vehicleHeaderSectionDivB.remove();
        }
        const vehicleBodyActionsDiv = document.getElementById('vehicle-body-vehicle-actions');
        if (vehicleBodyActionsDiv) {
            vehicleBodyActionsDiv.remove();
        }

        const backToOperatorBtn = document.getElementById('back-to-operator-btn');
        if (backToOperatorBtn) {
            backToOperatorBtn.setAttribute('data-operator-id', operatorId);
        }

        const proceedBtn = document.getElementById('proceed-btn');
        if (proceedBtn) {
            proceedBtn.setAttribute('data-vehicle-id', vehicleId);
            if (userAction === 'Entry shipping') {
                const driverId = params.get("driverId");
                proceedBtn.setAttribute('data-operator-id', operatorId);
                proceedBtn.setAttribute('data-driver-id', driverId);
            }
        }

    } else if (userAction === 'Read operator' ||
        userAction === 'Entry operator') {

        const vehicleBodyCommonActionsDiv = document.getElementById('vehicle-body-common-actions');
        if (vehicleBodyCommonActionsDiv) {
            vehicleBodyCommonActionsDiv.remove();
        }

        if(roleArray.includes("ADMIN")){
            const deleteBtn = document.getElementById('delete-btn');
            if (deleteBtn) {
                deleteBtn.setAttribute('data-vehicle-id', vehicleId);
            }

            const entryVehicleBtn = document.getElementById('entry-vehicle-btn');
            if (entryVehicleBtn) {
                entryVehicleBtn.setAttribute('data-operator-id', operatorId);
            }

            const backToOperatorBtn = document.getElementById('back-to-operator-btn');
            if (backToOperatorBtn) {
                backToOperatorBtn.setAttribute('data-operator-id', operatorId);
            }
        }else{
            const deleteBtn = document.getElementById('delete-btn');
            if (deleteBtn) {
                deleteBtn.remove();
            }

            const entryVehicleBtn = document.getElementById('entry-vehicle-btn');
            if (entryVehicleBtn) {
                entryVehicleBtn.remove();
            }
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

    const vehicleListBtn = document.getElementById('vehicle-list-btn');
    if (vehicleListBtn) {
        vehicleListBtn.addEventListener('click', function () {
            const operatorId = params.get("operatorId");
            window.location.href = `../../views/vehicle/vehicle-list.html?userAction=${userAction}&operatorId=${operatorId}`;
        }, {once: true});
    }

    const backToOperatorBtn = document.getElementById('back-to-operator-btn');
    if (backToOperatorBtn) {
        backToOperatorBtn.addEventListener('click', function () {
            const operatorId = this.dataset.operatorId;
            window.location.href = `../../views/operator/operator.html?userAction=${userAction}&operatorId=${operatorId}`;
        }, {once: true});
    }

    const entryVehicleBtn = document.getElementById('entry-vehicle-btn');
    if (entryVehicleBtn) {
        entryVehicleBtn.addEventListener('click', function () {
            const operatorId = this.dataset.operatorId;
            window.location.href = `../../views/vehicle/vehicle-creation-form.html?userAction=${userAction}&operatorId=${operatorId}`;
        }, {once: true});
    }

    const updateBtn = document.getElementById('update-btn');
    if (updateBtn) {
        updateBtn.addEventListener('click', async function () {
            const vehicleId = document.getElementById('vehicle-id').value.trim();
            const vehicleType = document.getElementById('vehicle-type').value.trim();
            const vehicleNumber = document.getElementById('vehicle-number').value.trim();
            const operatorId = document.getElementById('operator-id').value.trim();
            const createdAt = document.getElementById('created-at').value.trim();
            const updatedAt = document.getElementById('updated-at').value.trim();
            const updatedBy = document.getElementById('updated-by').value.trim();

            if (vehicleId === '') {
                alert('Vehicle ID not available.');
                return;
            } else if (vehicleType === '') {
                alert('Vehicle type not entered.');
                return;
            } else if (vehicleNumber === '') {
                alert('Vehicle number not entered.');
                return;
            } else if (operatorId === '') {
                alert('Operator ID not available.');
                return;
            } else if (createdAt === '') {
                alert('Created date not available.');
                return;
            } else if (updatedAt === '') {
                alert('Updated date not entered.');
                return;
            } else if (updatedBy === '') {
                alert('Updated by not entered.');
                return;
            }

            const payload = {
                "vehicleId": vehicleId,
                "vehicleType": vehicleType,
                "vehicleNumber": vehicleNumber,
                "operatorId": operatorId,
                "createdAt": createdAt,
                "updatedAt": updatedAt,
                "updatedBy": updatedBy
            };

            const response = await ajaxCall(`/logistic/vehicle/update`, 'PUT', payload);
            if (response) {
                alert(response);
            }
        });
    }

    const deleteBtn = document.getElementById('delete-btn');
    if (deleteBtn) {
        deleteBtn.addEventListener('click', async function () {
            const vehicleId = this.dataset.vehicleId;
            const url = `/logistic/vehicle/delete?vehicleId=${vehicleId}`;
            const methodType = 'DELETE';
            const response = await ajaxCall(url, methodType, null);

            if (response && response.success) {
                alert(response.message || 'Vehicle deleted successfully.');
                window.location.href = "/views/dashboard.html";
            }
        }, {once: true});
    }

    const proceedBtn = document.getElementById('proceed-btn');
    if (proceedBtn) {
        proceedBtn.addEventListener('click', async function () {
            if (userAction === 'Entry shipping') {
                const operatorId = this.dataset.operatorId;
                const driverId = this.dataset.driverId;
                const vehicleId = this.dataset.vehicleId;
                window.location.href = `../../views/shipment/shipment-form.html?userAction=${userAction}&operatorId=${operatorId}&driverId=${driverId}&vehicleId=${vehicleId}`;
            } else if (userAction === 'Reassign vehicle') {
                const shippingId = params.get("shippingId");
                const vehicleId = this.dataset.vehicleId;
                const operatorId = params.get("operatorId");
                const driverId = params.get("driverId");
                const updatedUserAction = "Reassign driver";
                window.location.href = `../../views/shipment/shipment.html?userAction=${updatedUserAction}&shippingId=${shippingId}&operatorId=${operatorId}&driverId=${driverId}&vehicleId=${vehicleId}`;
            }
        }, {once: true});
    }
}

function vehicleNavigationBinder() {
    const vehicleNavigationBtn = document.getElementById('vehicle-navigation-btn');

    if (vehicleNavigationBtn) {
        vehicleNavigationBtn.addEventListener('click', function () {
            toggleVehicleNavigationMenu();
        });
    }
}

vehicleNavigationBinder();

function toggleVehicleNavigationMenu() {
    const vehicleNavigationMenu = document.getElementById('vehicle-navigation-menu');

    if (!vehicleNavigationMenu) {
        return;
    }

    vehicleNavigationMenu.classList.toggle('active');
}

