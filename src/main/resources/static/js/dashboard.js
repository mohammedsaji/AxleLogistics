document.addEventListener('DOMContentLoaded',function (){
    async function payloadExtractor() {

        const viewState = localStorage.getItem("viewState");

        const dashboardState = dashboardViewState().DASHBOARD;

         previousPageNavigation(viewState);

        if(viewState === dashboardState){
            const url = `/logistic/auth/role`;
            const methodType = 'GET';
            const response = await ajaxCall(url, methodType, null);

            LayoutRender(response);
        }
    }
    payloadExtractor();
});

function LayoutRender(response){

    const rolesArray = response.valueMap.Role;

    const shipmentCreateEvent = document.querySelector('.dashboard-shell-btn-shipment-create');
    const operatorReadEvent = document.querySelector('.dashboard-shell-btn-operator-read');
    const federateAccEvent = document.querySelector('.dashboard-shell-btn-federate-acc');
    const employeeCreateEvent = document.querySelector('.dashboard-shell-btn-employee-create');
    const employeeAdminstrEvent = document.querySelector('.dashboard-shell-btn-employee-adminstr');

    if(rolesArray.includes("Manager".toUpperCase()) || rolesArray.includes("Driver".toUpperCase())){
        shipmentCreateEvent.remove();
        federateAccEvent.remove();
        employeeCreateEvent.remove();
        employeeAdminstrEvent.remove();
        operatorReadEvent.remove();
    }else if(!rolesArray.includes("Admin".toUpperCase())){
        shipmentCreateEvent.remove();
        federateAccEvent.remove();
        employeeCreateEvent.remove();
        employeeAdminstrEvent.remove();
        operatorReadEvent.remove();
    }

    ClickEventBinder();
}
function ClickEventBinder() {
    const shipmentStates = shipmentViewState().SHIPMENT;
    const operatorStates = operatorViewState().OPERATOR;
    const federateStates = federateViewState().FEDERATE_ACC;
    const employeeStates = employeeViewState();

    const shipmentCreateEvent = document.querySelector('.dashboard-shell-btn-shipment-create');
    if (shipmentCreateEvent) {
        shipmentCreateEvent.addEventListener('click', function () {
            localStorage.setItem("viewState", shipmentStates.CREATE);
            window.location.href = "/views/operator/transport-type.html";
        },{once : true});
    }

    const shipmentListReadEvent = document.querySelector('.dashboard-shell-btn-shipment-list');
    if (shipmentListReadEvent) {
        shipmentListReadEvent.addEventListener('click', function () {
            localStorage.setItem("viewState", shipmentStates.READ);
            window.location.href = "/views/shipment/shipment-list.html";
        },{once : true});
    }

    const operatorReadEvent = document.querySelector('.dashboard-shell-btn-operator-read');
    if (operatorReadEvent) {
        operatorReadEvent.addEventListener('click', function () {
            localStorage.setItem("viewState", operatorStates.READ);
            window.location.href = "/views/operator/operator-list.html";
        },{once : true});
    }

    const federateAccEvent = document.querySelector('.dashboard-shell-btn-federate-acc');
    if (federateAccEvent) {
        federateAccEvent.addEventListener('click', function () {
            localStorage.setItem("viewState", federateStates.CREATE);
            window.location.href = "/views/signUp/sign-up.html";
        },{once : true});
    }

    const employeeCreateEvent = document.querySelector('.dashboard-shell-btn-employee-create');
    if (employeeCreateEvent) {
        employeeCreateEvent.addEventListener('click', function () {
            localStorage.setItem("viewState", employeeStates.CREATE);
            window.location.href = "/views/employee/employee-list.html";
        },{once : true});
    }

    const employeeAdminstrEvent = document.querySelector('.dashboard-shell-btn-employee-adminstr');
    if (employeeAdminstrEvent) {
        employeeAdminstrEvent.addEventListener('click', function () {
            localStorage.setItem("viewState", employeeStates.ADMINSTR);
            window.location.href = "/views/employee/employee-list.html";
        },{once : true});
    }
}

function previousPageNavigation(viewState) {
    const previousFormBtn = document.querySelector('.previous-form-btn');
    if (!previousFormBtn) return;

    // document.referrer = where user came from (free, no setup needed)
    // fallback to history.back() only if referrer is missing
    const previousPage = document.referrer;

    if (!previousPage && window.history.length <= 1) {
        // truly no way back — disable button
        previousFormBtn.disabled = true;
        return;
    }

    previousFormBtn.disabled = false;

    previousFormBtn.addEventListener('click', function () {
        localStorage.setItem("viewState", viewState);

        if (previousPage) {
            window.location.href = previousPage; // reliable path
        } else {
            window.history.back(); // last resort fallback
        }

    }, { once: true });
}