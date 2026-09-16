async function payloadExtractor() {
    const response = await ajaxCall('/logistic/account/user-info', 'GET', null);
    if (!response?.data) {
        return;
    }

    const loginUserData = response.data;
    const roleArray = loginUserData.RoleList || [];
    let profileUrl;

    if (roleArray.includes('FEDERATE-MANAGER')) {
        profileUrl = '/logistic/manager/profile';
    } else if (roleArray.includes('FEDERATE-DRIVER')) {
        profileUrl = '/logistic/driver/profile';
    } else {
        profileUrl = '/logistic/employee/profile';
    }

    const profileResponse = await ajaxCall(profileUrl, 'GET', null);
    if (!profileResponse?.data) {
        console.error('Failed to load profile details.');
        return;
    }

    const profileData = profileResponse.data;
    const accountData = profileData.accountResponse;
    const managerData = profileData.managerResponse;
    const operatorData = profileData.operatorResponse;

    if (!accountData) {
        console.error('Account information not available.');
        return;
    }

    valueInitializer(accountData, profileData, managerData, operatorData);
}

payloadExtractor();

function valueInitializer(accountData, profileData, managerData, operatorData) {
    const accountRole = accountData.accountRole;
    const profileUsername = document.getElementById('profile-username');
    const profileContact = document.getElementById('profile-contact');
    const profileEmail = document.getElementById('profile-email');
    const profileRole = document.getElementById('profile-role');
    const profileManagerIdRef = document.getElementById('profile-manager-id-ref');
    const profileStatus = document.getElementById('profile-status');

    profileEmail.value = accountData.accountEmail || '';
    profileRole.value = accountRole || '';
    profileStatus.value = accountData.accountStatus || '';

    if (accountRole === 'FEDERATE-MANAGER') {
        const manager = profileData.managerResponse;

        if (!manager) {
            console.error('Manager information not available.');
            return;
        }

        profileUsername.value = manager.managerName || '';
        profileContact.value = manager.managerContactNo || '';
        profileManagerIdRef.remove();

        document.getElementById('profile-manager-id').value = manager.managerId || '';
        document.getElementById('profile-manager-name').value = manager.managerName || '';
        document.getElementById('profile-manager-contact-no').value = manager.managerContactNo || '';
        document.getElementById('profile-manager-status').value = manager.managerStatus || '';

        if (operatorData) {
            document.getElementById('profile-operator-id').value = operatorData.operatorId || '';
            document.getElementById('profile-operator-name').value = operatorData.operatorName || '';
            document.getElementById('profile-operator-transport-type').value = operatorData.operatorTransportType || '';
        } else {
            document.getElementById('profile-operator-section').remove();
        }

        return;
    }

    if (accountRole === 'FEDERATE-DRIVER') {
        const driver = profileData.driverResponse;
        const manager = profileData.managerResponse;

        if (!driver) {
            console.error('Driver information not available.');
            return;
        }

        profileUsername.value = driver.driverName || '';
        profileContact.value = driver.driverPhoneNo || '';
        profileManagerIdRef.remove();

        if (manager) {
            document.getElementById('profile-manager-id').value = manager.managerId || '';
            document.getElementById('profile-manager-name').value = manager.managerName || '';
            document.getElementById('profile-manager-contact-no').value = manager.managerContactNo || '';
            document.getElementById('profile-manager-status').value = manager.managerStatus || '';
        } else {
            document.getElementById('profile-manager-section').remove();
        }

        if (operatorData) {
            document.getElementById('profile-operator-id').value = operatorData.operatorId || '';
            document.getElementById('profile-operator-name').value = operatorData.operatorName || '';
            document.getElementById('profile-operator-transport-type').value = operatorData.operatorTransportType || '';
        } else {
            document.getElementById('profile-operator-section').remove();
        }

        return;
    }

    const employee = profileData.employeeResponse;

    if (!employee) {
        console.error('Employee information not available.');
        return;
    }

    profileUsername.value = employee.employeeName || '';
    profileContact.value = employee.employeeContactNo || employee.employeePhoneNo || employee.employeeContact || '';

    if (employee.reportingManagerId !== undefined && employee.reportingManagerId !== null) {
        profileManagerIdRef.value = employee.reportingManagerId;
    } else {
        const label = document.querySelector('label[for="profile-manager-id-ref"]');
        if(label){
            label.remove();
        }
        profileManagerIdRef.remove();
    }

    if (managerData) {
        document.getElementById('profile-manager-id').value = managerData.managerId || '';
        document.getElementById('profile-manager-name').value = managerData.managerName || '';
        document.getElementById('profile-manager-contact-no').value = managerData.managerContactNo || '';
        document.getElementById('profile-manager-status').value = managerData.managerStatus || '';
    } else {
        document.getElementById('profile-manager-section').remove();
    }

    document.getElementById('profile-operator-section').remove();
}

function clickEventBinder() {
    const backBtn = document.getElementById('profile-back-btn');

    if (backBtn) {
        backBtn.addEventListener('click', function () {
            window.location.href = '/views/dashboard.html';
        }, { once: true });
    }

    const signOutBtn = document.getElementById('profile-sign-out-btn');

    if (signOutBtn) {
        signOutBtn.addEventListener('click', async function () {
            const response = await ajaxCall('/logistic/account/signout', 'POST', {
                username: typeof getCookie === 'function' ? getCookie('username') : null
            });

            if (response) {
                localStorage.clear();
                sessionStorage.clear();
                window.location.href = '/views/signIn/sign-in.html';
            }
        }, { once: true });
    }
}

clickEventBinder();