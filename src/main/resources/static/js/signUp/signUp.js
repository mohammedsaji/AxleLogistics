const params = new URLSearchParams(window.location.search);

function dynamicLayoutRender() {
    const userAction = params.get("userAction");
    const select = document.getElementById('account-holder-role');
    const specificAction = params.get("specificAction");

    if (userAction === 'Entry employee') {
        const optionsToRemove = ['FEDERATE-MANAGER', 'FEDERATE-DRIVER'];
        optionsToRemove.forEach(val => {
            const opt = select.querySelector(`option[value="${val}"]`);
            if (opt) opt.remove();
        });
    } else if (userAction === 'Entry federate') {
        const label = document.querySelector('label[for="employee-manager-role"]');
        if(label){
            label.remove();
        }
        const accountOption = document.getElementById('employee-manager-role');
        if(accountOption){
            accountOption.remove();
        }

        let optionsToRemove = ['ADMIN', 'DEVELOPER', 'BUSINESS-ANALYST', 'DATA-ENGINEER', 'SOFTWARE ENGINEER','MANAGER','CEO'];

        if(specificAction){
            if(specificAction === 'Entry Manager'){
                optionsToRemove.push('FEDERATE-DRIVER');
            }else if(specificAction === 'Entry Driver'){
                optionsToRemove.push('FEDERATE-MANAGER');
            }
        }
        optionsToRemove.forEach(val => {
            const opt = select.querySelector(`option[value="${val}"]`);
            console.log(opt);
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
            let isManager = document.getElementById('employee-manager-role');

            if(isManager){
                isManager = isManager.value.trim();
            }

            const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
            if (!emailRegex.test(email)) {
                alert('Invalid email, try entering a valid email address.');
                return;
            }

            if (password.length < 8) {
                alert('Password must be at least 8 characters.');
                return;
            }


            if (userAction === 'Entry federate') {
                const validRoles = ['FEDERATE-MANAGER', 'FEDERATE-DRIVER'];
                if (!validRoles.includes(role)) {
                    alert('Invalid partner dependent role.');
                    return;
                }
            } else if (userAction === 'Entry employee') {
                const validRoles = ['ADMIN', 'DEVELOPER', 'BUSINESS-ANALYST', 'DATA-ENGINEER', 'SOFTWARE ENGINEER','MANAGER','CEO'];
                if (!validRoles.includes(role)) {
                    alert('Invalid employee role.');
                    return;
                }

                if(!isManager || (isManager !== 'MANAGER' && isManager !== 'NOT-MANAGER')){
                    alert('Select account belongs to  manager or not-manager role.')
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

            submitBtn.disabled = true;

            try {
                const response = await ajaxCall(`/logistic/account/signup`, 'POST', payload);

                if (response) {
                    const responseUsername = response.data.accountUsername;
                    const responseUserRole = response.data.accountRole;

                    const owningCompanySpecificEmployeeRoles = ['ADMIN', 'DEVELOPER', 'BUSINESS-ANALYST', 'DATA-ENGINEER', 'SOFTWARE ENGINEER','MANAGER','CEO'];
                    if (owningCompanySpecificEmployeeRoles.includes(responseUserRole)) {
                        if(isManager === 'MANAGER'){
                            const userAction = 'Entry employee';
                            const isManagerAccount = true;
                            window.location.href = `../../views/employee/employee-account-creation-form.html?userAction=${userAction}&accountUserName=${responseUsername}&isManagerAccount=${isManagerAccount}&accountUserRole=${responseUserRole}`;
                        }else{
                            const userAction = 'Entry employee';
                            window.location.href = `../../views/employee/employee-list.html?userAction=${userAction}&accountUserName=${responseUsername}&accountUserRole=${responseUserRole}&accountUserRole=${responseUserRole}`;
                        }
                    } else if (responseUserRole === 'FEDERATE-MANAGER') {
                        const userAction = 'Entry manager';
                        if (operatorId) {
                            window.location.href = `../../views/manager/manager-creation-form.html?userAction=${userAction}&accountUserName=${responseUsername}&operatorId=${operatorId}`
                        } else {
                            window.location.href = `../../views/operator/transport-types.html?userAction=${userAction}&accountUserName=${responseUsername}`;
                        }
                    } else if (responseUserRole === 'FEDERATE-DRIVER') {
                        const userAction = 'Entry driver';
                        if (operatorId) {
                            window.location.href = `../../views/driver/driver-creation-form.html?userAction=${userAction}&accountUserName=${responseUsername}&operatorId=${operatorId}`
                        } else {
                            window.location.href = `../../views/operator/transport-types.html?userAction=${userAction}&accountUserName=${responseUsername}`;
                        }
                    }
                }
                submitBtn.disabled = false;
            } catch (error) {
                console.log("Signup request failed:", error);
                submitBtn.disabled = false;
            }
        });
    }
}

clickEventBinder();