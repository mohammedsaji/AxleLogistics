const params = new URLSearchParams(window.location.search);

async function payloadExtractor() {

    const operatorId = params.get("operatorId");

    const url = `/logistic/operator/fetch?operatorId=${operatorId}`;
    const methodType = 'GET';
    const response = await ajaxCall(url, methodType, null);
    if (response && response.data) {
        const loginUserMap = await ajaxCall(`/logistic/account/user-info`, 'GET', null);
        const roleArray = loginUserMap?.data?.RoleList;
        if(roleArray && roleArray.length > 0) {
            valueInitializer(response.data);
            dynamicLayoutRender(response.data.operatorId, response.data.operatorName, roleArray);
            clickEventBinder();
        }
    }
}
payloadExtractor();

function valueInitializer(response){
    const operatorId = document.getElementById('operator-id');
    const operatorName = document.getElementById('operator-name');
    const transportationType = document.getElementById('operator-transport-type');
    const operatorManagerID = document.getElementById('operator-manager');
    const operatorCreatedAt = document.getElementById('operator-created-at');
    const operatorUpdatedAt = document.getElementById('operator-updated-at');
    const operatorUpdatedBy = document.getElementById('operator-updated-by');

    if(!response.operatorId){
        alert('Operator not available.');
        return;
    }

    operatorId.value = response.operatorId;
    operatorName.value = response.operatorName;
    transportationType.value = response.operatorTransportType;
    operatorManagerID.value = response.managerId;
    operatorCreatedAt.value = formatDateTime(response.createdAt);
    operatorUpdatedAt.value = formatDateTime(response.updatedAt);
    operatorUpdatedBy.value = response.updatedBy;
}


function dynamicLayoutRender(operatorId, operatorName, roleArray){
    const userAction = params.get("userAction");

    if(userAction === 'Reassign operator'||
        userAction === 'Entry shipping' ){

        const createOperatorBtn = document.getElementById('create-operator-btn');
        if(createOperatorBtn){
            createOperatorBtn.remove();
        }
        const createVehicleBtn = document.getElementById('create-vehicle-btn');
        if(createVehicleBtn){
            createVehicleBtn.remove();
        }

        const operatorBodyActionsDiv = document.querySelector('.operator-body-operator-actions');
        if(operatorBodyActionsDiv){
            operatorBodyActionsDiv.remove();
        }

        if(roleArray.includes("ADMIN")){
            const proceedBtn = document.getElementById('proceed-btn');
            if(proceedBtn){
                proceedBtn.setAttribute('data-operator-id',operatorId);
            }
        }else{
            const proceedBtn = document.getElementById('proceed-btn');
            if(proceedBtn){
                proceedBtn.remove();
            }
        }

    }else if(userAction === 'Entry manager' ||
        userAction === 'Entry driver'){
        const operatorBodyActionsDiv = document.querySelector('.operator-body-operator-actions');
        if(operatorBodyActionsDiv){
            operatorBodyActionsDiv.remove();
        }

        if(roleArray.includes("ADMIN")){
            const entryManagerBtn = document.getElementById('entry-manager-btn');
            if(entryManagerBtn){
                entryManagerBtn.setAttribute('data-operator-id',operatorId);
            }

            const entryDriverBtn = document.getElementById('entry-driver-btn');
            if(entryDriverBtn){
                entryDriverBtn.setAttribute('data-operator-id',operatorId);
            }

            const proceedBtn = document.getElementById('proceed-btn');
            if(proceedBtn){
                proceedBtn.setAttribute('data-operator-id',operatorId);
                if(userAction === 'Reassign operator'){
                    proceedBtn.setAttribute('data-operator-name',operatorName);
                }
            }
        }else{
            const entryManagerBtn = document.getElementById('entry-manager-btn');
            if(entryManagerBtn){
                entryManagerBtn.remove();
            }

            const entryDriverBtn = document.getElementById('entry-driver-btn');
            if(entryDriverBtn){
                entryDriverBtn.remove();
            }

            const proceedBtn = document.getElementById('proceed-btn');
            if(proceedBtn){
                proceedBtn.remove();
            }
        }

    }else if(userAction === 'Entry operator' ||
        userAction === 'Read operator'){

        const operatorBodyCommonActionsDiv = document.querySelector('.operator-body-common-actions');
        if(operatorBodyCommonActionsDiv){
            operatorBodyCommonActionsDiv.remove();
        }

        if(userAction === 'Entry operator' || !roleArray.includes("ADMIN")){
            const viewManagersBtn = document.getElementById('view-managers-btn');
            if(viewManagersBtn){
                viewManagersBtn.remove();
            }

            const viewDriversBtn = document.getElementById('view-drivers-btn');
            if(viewDriversBtn){
                viewDriversBtn.remove();
            }

            const viewVehiclesBtn = document.getElementById('view-vehicles-btn');
            if(viewVehiclesBtn){
                viewVehiclesBtn.remove();
            }
        }

        if(roleArray.includes("ADMIN")){
            const createVehicleBtn = document.getElementById('create-vehicle-btn');
            if(createVehicleBtn){
                createVehicleBtn.setAttribute('data-operator-id',operatorId);
            }
        }
    }

}

function clickEventBinder(){

    const userAction = params.get("userAction");
    const accountUserName = params.get("accountUserName");

    const operatorListBtn = document.getElementById('operator-list-btn');
    if(operatorListBtn){
        operatorListBtn.addEventListener('click',function(){
            const userAction = 'Read operator';
            window.location.href = `../../views/operator/transport-types.html?userAction=${userAction}`;
        },{once:true});
    }

    const createOperatorBtn = document.getElementById('create-operator-btn');
    if(createOperatorBtn){
        createOperatorBtn.addEventListener('click',function(){
            const userAction = 'Entry operator';
            window.location.href = `../../views/operator/transport-types.html?userAction=${userAction}`;
        },{once:true});
    }

    const proceedBtn = document.getElementById('proceed-btn');
    if(proceedBtn){
        proceedBtn.addEventListener('click',async function(){
            const operatorId = this.dataset.operatorId || params.get("operatorId");
            const operatorName = this.dataset.operatorName || document.getElementById('operator-name').value.trim();
            const operatorManager = document.getElementById('operator-manager');
            if(userAction === 'Entry manager' || (operatorManager && operatorManager.value !== '' && operatorManager.value !== null)){
                if(userAction === 'Entry shipping'){
                    window.location.href = `../../views/driver/driver-list.html?userAction=${userAction}&operatorId=${operatorId}`;
                }else if(userAction === 'Reassign operator'){
                    const shippingId = params.get("shippingId");
                    window.location.href = `../../views/shipment/shipment.html?userAction=${userAction}&shippingId=${shippingId}&operatorId=${operatorId}&operatorName=${operatorName}`;
                }else if(userAction === 'Entry manager'){
                    const operatorManagerID = document.getElementById('operator-manager');
                    if(operatorManagerID.value.trim() === null || operatorManagerID.value.trim() === '' || operatorManagerID.value === 0){
                        const setCurrentAsManager = true;
                        window.location.href = `../../views/manager/manager-creation-form.html?userAction=${userAction}&operatorId=${operatorId}&accountUserName=${accountUserName}&setCurrentAsManager=${setCurrentAsManager}`;
                    }else{
                        window.location.href = `../../views/manager/manager-creation-form.html?userAction=${userAction}&operatorId=${operatorId}&accountUserName=${accountUserName}`;
                    }
                }else if(userAction === 'Entry driver'){
                    window.location.href = `../../views/driver/driver-creation-form.html?userAction=${userAction}&operatorId=${operatorId}&accountUserName=${accountUserName}`;
                }
            }else{
                alert("Operator may not had a manager, create or assign manager to proceed.");
            }
        });
    }

    const entryManagerBtn = document.getElementById('entry-manager-btn');
    if(entryManagerBtn){
        entryManagerBtn.addEventListener('click',function (){
            const userAction = 'Entry manager';
            const operatorId = this.dataset.operatorId;
            window.location.href = `../../views/signUp/sign-up.html?userAction=${userAction}&operatorId=${operatorId}`;
        },{once:true});
    }

    const createVehicleBtn = document.getElementById('create-vehicle-btn');
    if(createVehicleBtn){
        createVehicleBtn.addEventListener('click',function (){
            const operatorId = this.dataset.operatorId;
            window.location.href = `../../views/vehicle/vehicle-creation-form.html?userAction=${userAction}&operatorId=${operatorId}`;
        },{once:true});
    }

    const entryDriverBtn = document.getElementById('entry-driver-btn');
    if(entryDriverBtn){
        entryDriverBtn.addEventListener('click',function (){
            const userAction = 'Entry driver';
            const operatorId = this.dataset.operatorId;
            window.location.href = `../../views/signUp/sign-up.html?userAction=${userAction}&operatorId=${operatorId}`;
        },{once:true})
    }

    const dashboardBtn = document.getElementById('dashboard-btn');
    if(dashboardBtn){
        dashboardBtn.addEventListener('click',function () {
            window.location.href = "/views/dashboard.html";
        },{once : true});
    }

    const updateBtn = document.getElementById('update-btn');
    if(updateBtn){
        updateBtn.addEventListener('click',async function (){
            const operatorId = document.getElementById('operator-id').value.trim();
            const operatorName = document.getElementById('operator-name').value.trim();
            const transportationType = document.getElementById('operator-transport-type').value.trim();
            const operatorManagerID = document.getElementById('operator-manager').value.trim();
            const operatorCreatedAt = document.getElementById('operator-created-at').value.trim();
            const operatorUpdatedAt = document.getElementById('operator-updated-at').value.trim();
            const operatorUpdatedBy = document.getElementById('operator-updated-by').value.trim();

            if(operatorId === ''){
                alert('Operator ID not available.');
                return;
            } else if(operatorName === ''){
                alert('Operator Name not entered.');
                return;
            }else if(transportationType === ''){
                alert('Transportation Type not entered.');
                return;
            }else if(operatorManagerID === ''){
                alert('Manager ID not entered.');
                return;
            }else if(operatorCreatedAt === ''){
                alert('Created date not entered.');
                return;
            }else if(operatorUpdatedAt === ''){
                alert('Updated date not entered.');
                return;
            }else if(operatorUpdatedBy === ''){
                alert('Updated by not entered.');
                return;
            }

            const payload = {
                "operatorId": operatorId,
                "operatorName": operatorName,
                "operatorTransportType": transportationType,
                "managerId": operatorManagerID,
                "createdAt": operatorCreatedAt,
                "updatedAt": operatorUpdatedAt,
                "updatedBy": operatorUpdatedBy
            };

            const response = await ajaxCall(`/logistic/operator/update`, 'PUT', payload);
            if (response) {
                alert(response);
            }
        });
    }

    const deleteBtn = document.getElementById('delete-btn');
    if(deleteBtn){
        deleteBtn.addEventListener('click', async function () {
            const operatorId = document.getElementById('operator-id').value.trim();
            const url = `/logistic/operator/delete?operatorId=${operatorId}`;
            const methodType = 'DELETE';
            const response = await ajaxCall(url, methodType, null);

            if (response && response.success) {
                alert(response.message || 'Operator deleted successfully.');
                window.location.href = "/views/dashboard.html";
            }
        }, {once: true});
    }

    const viewManagersBtn = document.getElementById('view-managers-btn');
    if(viewManagersBtn){
        viewManagersBtn.addEventListener('click', async function () {
            const operatorId = document.getElementById('operator-id').value.trim();
            if(operatorId){
                window.location.href = `../../views/manager/manager-list.html?operatorId=${operatorId}&userAction=${userAction}`;
            }
        }, {once: true});
    }

    const viewDriversBtn = document.getElementById('view-drivers-btn');
    if(viewDriversBtn){
        viewDriversBtn.addEventListener('click', async function () {
            const operatorId = document.getElementById('operator-id').value.trim();
            if(operatorId){
                window.location.href = `../../views/driver/driver-list.html?operatorId=${operatorId}&userAction=${userAction}`;
            }
        }, {once: true});
    }

    const viewVehiclesBtn = document.getElementById('view-vehicles-btn');
    if(viewVehiclesBtn){
        viewVehiclesBtn.addEventListener('click', async function () {
            const operatorId = document.getElementById('operator-id').value.trim();
            if(operatorId){
                window.location.href = `../../views/vehicle/vehicle-list.html?operatorId=${operatorId}&userAction=${userAction}`;
            }
        }, {once: true});
    }
}

function operatorNavigationBinder(){

    const operatorNavigationBtn = document.getElementById('operator-navigation-btn');

    if(operatorNavigationBtn){
        operatorNavigationBtn.addEventListener('click',function(){
            toggleOperatorNavigationMenu();
        });
    }
}

operatorNavigationBinder();

function toggleOperatorNavigationMenu(){

    const operatorNavigationMenu = document.getElementById('operator-navigation-menu');

    if(!operatorNavigationMenu){
        return;
    }

    operatorNavigationMenu.classList.toggle('active');
}