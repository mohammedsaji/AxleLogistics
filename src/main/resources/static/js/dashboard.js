const params = new URLSearchParams(window.location.search);

async function payloadExtractor() {
    const url = `/logistic/account/user-info`;
    const methodType = 'GET';
    const response = await ajaxCall(url, methodType, null);

    if (response && response.data) {
        const rolesArray = response.data.RoleList || [];
        const accountId = response.data.userId;

        let myOperatorId = null;

        if (rolesArray.includes("FEDERATE-DRIVER")) {
            const driverResponse = await ajaxCall(`/logistic/driver/fetchByAccountId`, 'GET', null);
            if (driverResponse && driverResponse.data) {
                myOperatorId = driverResponse.data.operatorId;
            }
        } else if (rolesArray.includes("FEDERATE-MANAGER")) {
            const managerResponse = await ajaxCall(`/logistic/manager/fetchByAccountId?`, 'GET', null); // <-- confirm this endpoint
            if (managerResponse && managerResponse.data) {
                myOperatorId = managerResponse.data.operatorId;
            }
        }

        dynamicLayoutRender(rolesArray, myOperatorId);
        clickEventBinder(accountId, rolesArray, response, myOperatorId);
    }
}
payloadExtractor();

function dynamicLayoutRender(rolesArray, myOperatorId) {
    const isAdmin = rolesArray.includes("ADMIN");
    const isFederateManager = rolesArray.includes("FEDERATE-MANAGER");
    const isFederateDriver = rolesArray.includes("FEDERATE-DRIVER");
    const isFederate = isFederateManager || isFederateDriver;

    if (isFederate) {
        const shipmentEntryBtn = document.getElementById('dashboard-nav-shipment-entry-btn');
        if (shipmentEntryBtn) shipmentEntryBtn.remove();

        const employeeGroup = document.getElementById('dashboard-nav-employee');
        if (employeeGroup) employeeGroup.remove();

        const operatorManageBtn = document.getElementById('dashboard-nav-operator-manage-btn');
        if (operatorManageBtn) operatorManageBtn.remove();

        const partnerRegisterBtn = document.getElementById('dashboard-nav-partner-register-btn');
        if (partnerRegisterBtn) partnerRegisterBtn.remove();

        const myOperatorBtn = document.getElementById('dashboard-nav-my-operator-btn');
        if (myOperatorBtn && myOperatorId) {
            myOperatorBtn.setAttribute('data-operator-id', myOperatorId);
        }
    } else {
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

        const myOperatorBtn = document.getElementById('dashboard-nav-my-operator-btn');
        if (myOperatorBtn) myOperatorBtn.remove();
    }
}
function clickEventBinder(employeeId, rolesArray, response, myOperatorId) {
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

    const myOperatorBtn = document.getElementById('dashboard-nav-my-operator-btn');
    if (myOperatorBtn) {
        myOperatorBtn.addEventListener('click', function () {
            const operatorId = this.dataset.operatorId || myOperatorId;
            if (operatorId) {
                window.location.href = `../../views/operator/operator.html?operatorId=${operatorId}&userAction=Read operator`;
            } else {
                alert('No operator found for your account.');
            }
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