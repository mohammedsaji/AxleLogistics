const params = new URLSearchParams(window.location.search);

async function payloadExtractor() {
    const url = `/logistic/account/user-info`;
    const methodType = 'GET';
    const response = await ajaxCall(url, methodType, null);

    if (response && response.data) {
        const rolesArray = response.data.RoleList || [];
        const accountId = response.data.userId;
        dynamicLayoutRender(rolesArray);
        clickEventBinder(accountId, rolesArray, response);
    }
}
payloadExtractor();

function dynamicLayoutRender(rolesArray) {
    const isAdmin = rolesArray.includes("ADMIN");

    // Only ADMIN can see these buttons — remove for everyone else
    if (!isAdmin) {
        const shipmentEntryBtn = document.getElementById('dashboard-nav-shipment-entry-btn');
        if (shipmentEntryBtn) shipmentEntryBtn.closest('.dashboard-navigation-group').remove();

        const operatorManageBtn = document.getElementById('dashboard-nav-operator-manage-btn');
        if (operatorManageBtn) operatorManageBtn.closest('.dashboard-navigation-group').remove();

        const partnerRegisterBtn = document.getElementById('dashboard-nav-partner-register-btn');
        if (partnerRegisterBtn) partnerRegisterBtn.closest('.dashboard-navigation-group').remove();

        const employeeCreateBtn = document.getElementById('dashboard-nav-employee-create-btn');
        if (employeeCreateBtn) employeeCreateBtn.closest('.dashboard-navigation-group').remove();

        const employeeAdminstrBtn = document.getElementById('dashboard-nav-employee-manage-btn');
        if (employeeAdminstrBtn) employeeAdminstrBtn.closest('.dashboard-navigation-group').remove();
    }
}
function clickEventBinder(employeeId, rolesArray, response) {
    const signOutBtn = document.getElementById('sign-out-btn');
    const username = response.data.username;
    signOutBtn.addEventListener('click', async function () {
        const response = await ajaxCall(`/logistic/account/signout`, 'POST', { username: username });
        if (response) {
            localStorage.clear();
            sessionStorage.clear();
            window.location.href = "/views/signIn/sign-in.html";
        }
    }, { once: true });

    const shipmentCreateBtn = document.getElementById('dashboard-nav-shipment-entry-btn');
    if (shipmentCreateBtn) {
        shipmentCreateBtn.addEventListener('click', function () {
            window.location.href = "/views/operator/transport-types.html?userAction=Entry shipping";
        }, { once: true });
    }

    const shipmentListBtn = document.getElementById('dashboard-nav-shipment-manage-btn');
    if (shipmentListBtn) {
        shipmentListBtn.addEventListener('click', function () {
            window.location.href = "/views/shipment/shipment-list.html?userAction=Read shipment";
        }, { once: true });
    }

    const operatorManageBtn = document.getElementById('dashboard-nav-operator-manage-btn');
    if (operatorManageBtn) {
        operatorManageBtn.addEventListener('click', function () {
            window.location.href = "/views/operator/transport-types.html?userAction=Read operator";
        }, { once: true });
    }

    const partnerRegisterBtn = document.getElementById('dashboard-nav-partner-register-btn');
    if (partnerRegisterBtn) {
        partnerRegisterBtn.addEventListener('click', function () {
            window.location.href = "/views/signUp/sign-up.html?userAction=Entry federate";
        }, { once: true });
    }

    const employeeCreateBtn = document.getElementById('dashboard-nav-employee-create-btn');
    if (employeeCreateBtn) {
        employeeCreateBtn.addEventListener('click', function () {
            window.location.href = "/views/signUp/sign-up.html?userAction=Entry employee";
        }, { once: true });
    }

    const employeeAdminstrBtn = document.getElementById('dashboard-nav-employee-manage-btn');
    if (employeeAdminstrBtn) {
        employeeAdminstrBtn.addEventListener('click', function () {
            window.location.href = "/views/employee/employee-list.html?userAction=Administrate employee";
        }, { once: true });
    }

    const profileViewBtn = document.getElementById('profile-view-btn');
    if(profileViewBtn){
        profileViewBtn.addEventListener('click', function () {
            window.location.href = `/views/profile.html`;
        }, { once: true });
    }
}

function dashboardNavigationBinder() {
    const shipmentNavBtn = document.getElementById('dashboard-nav-shipment-btn');
    const employeeNavBtn = document.getElementById('dashboard-nav-employee-btn');
    const partnerNavBtn = document.getElementById('dashboard-nav-partner-btn');

    if (shipmentNavBtn) {
        shipmentNavBtn.addEventListener('click', function () {
            toggleNavigationMenu('dashboard-nav-shipment-menu');
        });
    }

    if (employeeNavBtn) {
        employeeNavBtn.addEventListener('click', function () {
            toggleNavigationMenu('dashboard-nav-employee-menu');
        });
    }

    if (partnerNavBtn) {
        partnerNavBtn.addEventListener('click', function () {
            toggleNavigationMenu('dashboard-nav-partner-menu');
        });
    }
}

dashboardNavigationBinder();

function toggleNavigationMenu(menuId) {
    const menu = document.getElementById(menuId);

    if (!menu) {
        return;
    }

    const isOpen = menu.classList.contains('active');

    closeNavigationMenus();

    if (!isOpen) {
        menu.classList.add('active');
    }
}

function closeNavigationMenus() {
    const menus = document.querySelectorAll('.dashboard-navigation-menu');

    menus.forEach(function (menu) {
        menu.classList.remove('active');
    });
}