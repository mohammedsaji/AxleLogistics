const params = new URLSearchParams(window.location.search);

async function payloadExtractor() {
    const url = `/logistic/auth/role`;
    const methodType = 'GET';
    const response = await ajaxCall(url, methodType, null);

    if (response && response.valueMap) {
        const rolesArray = response.valueMap.Role || [];
        const employeeId = response.valueMap.employeeId;
        dynamicLayoutRender(rolesArray);
        clickEventBinder(employeeId);
    }
}
payloadExtractor();

function dynamicLayoutRender(rolesArray) {
    const isAdmin = rolesArray.includes("ADMIN");
    const isManagerOrDriver = rolesArray.includes("MANAGER") || rolesArray.includes("DRIVER");

    // Only ADMIN can see these buttons — remove for everyone else
    if (isManagerOrDriver || !isAdmin) {
        const shipmentCreateBtn = document.getElementById('dashboard-btn-shipment-create');
        if (shipmentCreateBtn) shipmentCreateBtn.closest('.dashboard-body-card').remove();

        const operatorReadBtn = document.getElementById('dashboard-btn-operator-read');
        if (operatorReadBtn) operatorReadBtn.closest('.dashboard-body-card').remove();

        const federateAccBtn = document.getElementById('dashboard-btn-federate-acc');
        if (federateAccBtn) federateAccBtn.closest('.dashboard-body-card').remove();

        const employeeCreateBtn = document.getElementById('dashboard-btn-employee-create');
        if (employeeCreateBtn) employeeCreateBtn.closest('.dashboard-body-card').remove();

        const employeeAdminstrBtn = document.getElementById('dashboard-btn-employee-adminstr');
        if (employeeAdminstrBtn) employeeAdminstrBtn.closest('.dashboard-body-card').remove();
    }
}

function clickEventBinder(employeeId) {
    const signOutBtn = document.getElementById('sign-out-btn');
    if (signOutBtn) {
        signOutBtn.addEventListener('click', async function () {
            const url = `/logistic/auth/signout?employeeId=${employeeId}`;
            const response = await ajaxCall(url, 'GET', null);
            if (response) {
                window.location.href = "/views/signIn/sign-in.html";
            }
        }, { once: true });
    }

    const shipmentCreateBtn = document.getElementById('dashboard-btn-shipment-create');
    if (shipmentCreateBtn) {
        shipmentCreateBtn.addEventListener('click', function () {
            window.location.href = "/views/operator/transport-types.html?userAction=Entry shipping";
        }, { once: true });
    }

    const shipmentListBtn = document.getElementById('dashboard-btn-shipment-list');
    if (shipmentListBtn) {
        shipmentListBtn.addEventListener('click', function () {
            window.location.href = "/views/shipment/shipment-list.html?userAction=Read shipment";
        }, { once: true });
    }

    const operatorReadBtn = document.getElementById('dashboard-btn-operator-read');
    if (operatorReadBtn) {
        operatorReadBtn.addEventListener('click', function () {
            window.location.href = "/views/operator/transport-types.html?userAction=Read operator";
        }, { once: true });
    }

    const federateAccBtn = document.getElementById('dashboard-btn-federate-acc');
    if (federateAccBtn) {
        federateAccBtn.addEventListener('click', function () {
            window.location.href = "/views/signUp/sign-up.html?userAction=Entry federate";
        }, { once: true });
    }

    const employeeCreateBtn = document.getElementById('dashboard-btn-employee-create');
    if (employeeCreateBtn) {
        employeeCreateBtn.addEventListener('click', function () {
            window.location.href = "/views/employee/employee-list.html?userAction=Entry employee";
        }, { once: true });
    }

    const employeeAdminstrBtn = document.getElementById('dashboard-btn-employee-adminstr');
    if (employeeAdminstrBtn) {
        employeeAdminstrBtn.addEventListener('click', function () {
            window.location.href = "/views/employee/employee-list.html?userAction=Administrate employee";
        }, { once: true });
    }
}