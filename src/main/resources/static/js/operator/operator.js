const params = new URLSearchParams(window.location.search);

async function payloadExtractor() {

    const operatorId = params.get("operatorId");

    const url = `/logistic/operator/fetch?operatorId=${operatorId}`;
    const methodType = 'GET';
    const response = await ajaxCall(url, methodType, null);
    valueInitializer(response);
    dynamicLayoutRender(response.operatorId);
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
    operatorCreatedAt.value = response.createdAt;
    operatorUpdatedAt.value = response.updatedAt;
    operatorUpdatedBy.value = response.updatedBy;
}


function dynamicLayoutRender(operatorId){
    const userAction = params.get("userAction");

    if(userAction === 'Reassign operator'||
        userAction === 'Entry shipping' ){
        const operatorHeaderActionsDivA = document.querySelector('.operator-header-actions-a');
        if(operatorHeaderActionsDivA){
            operatorHeaderActionsDivA.remove();
        }
        const operatorHeaderActionsDivB = document.querySelector('.operator-header-actions-b');
        if(operatorHeaderActionsDivB){
            operatorHeaderActionsDivB.remove();
        }
        const operatorBodyActionsDiv = document.querySelector('.operator-body-operator-actions');
        if(operatorBodyActionsDiv){
            operatorBodyActionsDiv.remove();
        }

        const proceedBtn = document.getElementById('proceed-btn');
        proceedBtn.setAttribute('data-operator-id',operatorId);

    }else if(userAction === 'Entry Manager' ||
        userAction === 'Entry Driver'){
        const operatorHeaderActionsDivA = document.querySelector('.operator-header-actions-a');
        if(operatorHeaderActionsDivA){
            operatorHeaderActionsDivA.remove();
        }
        const operatorBodyActionsDiv = document.querySelector('.operator-body-operator-actions');
        if(operatorBodyActionsDiv){
            operatorBodyActionsDiv.remove();
        }
        const entryManagerBtn = document.getElementById('entry-manager-btn');
        entryManagerBtn.setAttribute('data-operator-id',operatorId);

        const entryDriverBtn = document.getElementById('entry-driver-btn');
        entryDriverBtn.setAttribute('data-operator-id',operatorId);

    }else if(userAction === 'Entry operator' ||
        userAction === 'Read operator'){

        const operatorHeaderActionsDivB = document.querySelector('.operator-header-actions-b');
        if(operatorHeaderActionsDivB){
            operatorHeaderActionsDivB.remove();
        }
        const operatorBodyCommonActionsDiv = document.querySelector('.operator-body-common-actions');
        if(operatorBodyCommonActionsDiv){
            operatorBodyCommonActionsDiv.remove();
        }
        if(userAction === 'Entry operator'){
            const operatorBodyDependentsList = document.querySelector('.operator-body-dependents-list');
            if(operatorBodyDependentsList){
                operatorBodyDependentsList.remove();
            }
        }

        const entryVehicleBtn = document.getElementById('entry-vehicle-btn');
        entryVehicleBtn.setAttribute('data-operator-id',operatorId);
    }

}

function clickEventBinder(){

    const userAction = params.get("userAction");

    const operatorListBtn = document.getElementById('operator-list-btn');
    if(operatorListBtn){
        operatorListBtn.addEventListener('click',function(){
            const userAction = 'Read operator';
            window.location.href = `../../views/operator/transport-types.html?userAction=${userAction}`;
        },{once:true});
    }

    const entryOperatorBtn = document.getElementById('entry-operator-btn');
    if(entryOperatorBtn){
        entryOperatorBtn.addEventListener('click',function(){
            const userAction = 'Entry operator';
            window.location.href = `../../views/operator/transport-types.html?userAction=${userAction}`;
        },{once:true});
    }

    const proceedBtn = document.getElementById('proceed-btn');
    if(proceedBtn){
        proceedBtn.addEventListener('click',function(){
            let url;
            if(userAction === 'Entry shipping'){
                const operatorId = this.dataset.operatorId;
                url = `../../views/shipment/shipment-form.html?userAction=${userAction}&operatorId=${operatorId}`;
            }else if(userAction === 'Reassign operator'){
                const operatorId = this.dataset.operatorId;
                url = `../../views/status/status.html?userAction=${userAction}&operatorId=${operatorId}`;
            }else if(userAction === 'Entry manager'){
                const operatorId = this.dataset.operatorId;
                url = `../../views/manager/manager-creation-form.html?userAction=${userAction}&operatorId=${operatorId}`;
            }else if(userAction === 'Entry driver'){
                const operatorId = this.dataset.operatorId;
                url = `../../views/shipment/driver-creation-form.html?userAction=${userAction}&operatorId=${operatorId}`;
            }
            if(url){
                window.location.href = url;
            }
        },{once:true});
    }

    const entryManagerBtn = document.getElementById('entry-manager-btn');
    if(entryManagerBtn){
        entryManagerBtn.addEventListener('click',function (){
            const userAction = 'Entry manager';
            const operatorId = this.dataset.operatorId;
            window.location.href = `../../views/signUp/sign-up.html?userAction=${userAction}&operatorId=${operatorId}`;
        },{once:true});
    }

    const entryVehicleBtn = document.getElementById('entry-vehicle-btn');
    if(entryVehicleBtn){
        entryVehicleBtn.addEventListener('click',function (){
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
            } else if(operatorName === ''){
                alert('Operator Name not entered.');
            }else if(transportationType === ''){
                alert('Transportation Type not entered.');
            }else if(operatorManagerID === ''){
                alert('Manager ID not entered.');
            }else if(operatorCreatedAt === ''){
                alert('Created date not entered.');
            }else if(operatorUpdatedAt === ''){
                alert('Updated date not entered.');
            }else if(operatorUpdatedBy === ''){
                alert('Updated by not entered.');
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

            const response = await ajaxCall(`/logistic/operator/update`, 'POST', payload);
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
            await ajaxCall(url, methodType, null);
        }, {once: true});
    }

    const viewManagersBtn = document.getElementById('view-managers-btn');
    if(viewManagersBtn){
        viewManagersBtn.addEventListener('click', async function () {
            const operatorId = document.getElementById('operator-id').value.trim();
            if(operatorId){
                window.location.href = `../../views/manager/manager-list.html?operatorId=${operatorId}&userAction=Read operator`;
            }
        }, {once: true});
    }
}
clickEventBinder();