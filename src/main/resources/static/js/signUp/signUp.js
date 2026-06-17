const params = new URLSearchParams(window.location.search);

function dynamicLayoutRender() {
    const userAction = params.get("userAction");
    const select = document.getElementById('account-holder-role');

    if (userAction === 'Entry employee') {
        const optionsToRemove = ['ADMIN', 'DEVELOPER', 'BUSINESS-ANALYST', 'DATA-ENGINEER', 'SOFTWARE ENGINEER'];
        optionsToRemove.forEach(val => {
            const opt = select.querySelector(`option[value="${val}"]`);
            if (opt) opt.remove();
        });
    } else if (userAction === 'Entry manager' || userAction === 'Entry driver' || userAction === 'Entry federate') {
        const optionsToRemove = ['FEDERATE-MANAGER', 'FEDERATE-DRIVER'];
        optionsToRemove.forEach(val => {
            const opt = select.querySelector(`option[value="${val}"]`);
            if (opt) opt.remove();
        });
    }
}
dynamicLayoutRender();

function clickEventBinder() {
    const userAction = params.get("userAction");
    const operatorId = params.get("operatorId");

    const submitBtn = document.getElementById('submit-btn');
    if (submitBtn) {
        submitBtn.addEventListener('click', async function (e) {
            e.preventDefault();

            const username = document.getElementById('signup-username').value.trim();
            const email = document.getElementById('signup-email').value.trim();
            const password = document.getElementById('signup-password').value;
            const role = document.getElementById('account-holder-role').value.trim();

            const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
            if (!emailRegex.test(email)) {
                alert('Invalid email, try entering a valid email address.');
                return;
            }

            if (password.length < 8) {
                alert('Password must be at least 8 characters.');
                return;
            }

            if (userAction === 'Sign up') {
                const validRoles = ['FEDERATE-MANAGER', 'FEDERATE-DRIVER'];
                if (!validRoles.includes(role)) {
                    alert('Invalid partner dependent role.');
                    return;
                }
            } else if (userAction === 'Entry employee') {
                const validRoles = ['ADMIN', 'DEVELOPER', 'BUSINESS-ANALYST', 'DATA-ENGINEER', 'SOFTWARE ENGINEER'];
                if (!validRoles.includes(role)) {
                    alert('Invalid employee role.');
                    return;
                }
            }

            const payload = {
                "accountUsername": username,
                "accountPassword": password,
                "accountEmail": email,
                "accountRole": role,
                "accountStatus": "ACTIVE"
            };

            const response = await ajaxCall(`/logistic/auth/signup`, 'POST', payload);

            if (response) {
                const responseUsername = response.valueMap.Username;
                const userRole = response.valueMap.Role;

                if (userRole === 'EMPLOYEE') {
                    const userAction =  'Entry employee';
                    window.location.href = `../../views/employee/employee-account-creation-form.html?userAction=${userAction}&username=${responseUsername}`;
                } else if (userRole === 'FEDERATE-MANAGER') {
                    const userAction =  'Entry manager';
                    let url;
                    if(operatorId){
                        url = `../../views/manager/manager-creation-form.html?userAction=${userAction}&username=${responseUsername}&operatorId=${operatorId}`
                    }else{
                        url = `../../views/operator/transport-types.html?userAction=${userAction}&username=${responseUsername}`;
                    }
                    if(url){
                        window.location.href = url;
                    }
                } else if (userRole === 'FEDERATE-DRIVER') {
                    const userAction =  'Entry driver';
                    let url;
                    if(operatorId){
                        url = `../../views/driver/driver-creation-form.html?userAction=${userAction}&username=${responseUsername}&operatorId=${operatorId}`
                    }else{
                        url = `../../views/operator/transport-types.html?userAction=${userAction}&username=${responseUsername}`;
                    }
                    if(url){
                        window.location.href = url;
                    }
                }
            }
        }, {once: true});
    }
}
clickEventBinder();