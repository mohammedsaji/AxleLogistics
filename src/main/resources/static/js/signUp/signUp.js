document.addEventListener('DOMContentLoaded', function () {
    function authenticationProcess() {

        const viewState = localStorage.getItem("viewState");

        const elementSelect = document.getElementById('account-holder-role');

         previousPageNavigation(viewState);

        if (viewState === federateViewState().FEDERATE_ACC.CREATE) {
            const managerOption = document.createElement('option');
            managerOption.value = "FEDERATE-MANAGER";
            managerOption.textContent = "Federate Manager";
            elementSelect.append(managerOption);

            const driverOption = document.createElement('option');
            driverOption.value = "FEDERATE-DRIVER";
            driverOption.textContent = "Federate Driver";
            elementSelect.append(driverOption);
        } else if (viewState === employeeViewState().EMPLOYEE_ACCOUNT.CREATE) {

            const employeeRoles = ["ADMIN", "DEVELOPER", "BUSINESS-ANALYST", "DATA-ENGINEER", "SOFTWARE ENGINEER"];

            employeeRoles.forEach((employeeRole)=>{

                const whiteSpacedCase = kebabToWhiteSpacedCase(employeeRole);

                const employeeOption = document.createElement('option');
                employeeOption.value = employeeRole;
                employeeOption.textContent = `${whiteSpacedCase.charAt(0).toUpperCase() + whiteSpacedCamelCase(whiteSpacedCase).slice(1)}`;
                elementSelect.append(employeeOption);
            })
        }

        const submitBtn = document.querySelector('.submit-btn');

        submitBtn.addEventListener('click', async function (e) {

            e.preventDefault();

            const username = document.getElementById('signup-username').value.trim();
            const email = document.getElementById('signup-email').value.trim();
            const password = document.getElementById('signup-password').value;
            const role = document.getElementById('account-holder-role').value.trim();


            const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

            if (!emailRegex.test(email)) {
                alert("Invalid Email, try entering valid Email address.");
                return;
            }

            if (password.length < 8 && password.length <= 0) {
                alert("Password must be at least 8 characters");
                return;
            }

            if (viewState === federateViewState().FEDERATE_ACC.CREATE) {
                const roles = [ "FEDERATE-MANAGER", "FEDERATE-DRIVER"];
                if (!roles.includes(role)) {
                    alert("Invalid Partner Dependent Role.");
                }
            } else if (viewState === employeeViewState().EMPLOYEE_ACCOUNT.CREATE) {
                const roles = [ "ADMIN",  "DEVELOPER", "BUSINESS-ANALYST", "DATA-ENGINEER", "SOFTWARE ENGINEER"];
                if (!roles.includes(role)) {
                    alert("Invalid Employee Role.");
                }
            }

            const payload = {
                "accountUsername": username,
                "accountPassword": password,
                "accountEmail": email,
                "accountRole": role,
                "accountStatus": "ACTIVE"
            };

            localStorage.setItem("accountCreationUsername", username);

            localStorage.setItem("viewState", viewState);

            const response = await ajaxCall(`/logistic/auth/signup`, 'POST', payload);

            if (response) {

                const username = response.valueMap.Username;
                const userRole = response.valueMap.Role;

                if (username) {
                    localStorage.setItem("accountCreationUsername", username);
                }

                if (userRole && userRole === "EMPLOYEE") {
                    localStorage.setItem("viewState", employeeViewState().EMPLOYEE_ACCOUNT.CREATE)
                    window.location.href = "../../views/employee/employee-account-creation-form.html";
                } else if (userRole && userRole === "MANAGER") {
                    localStorage.setItem("viewState", managerViewState().MANAGER_PROFILE.CREATE);
                    window.location.href = "../../views/operator/transport-type.html";
                } else if (userRole && userRole === "DRIVER") {
                    localStorage.setItem("viewState", driverViewState().DRIVER_PROFILE.CREATE);
                    window.location.href = "../../views/operator/transport-type.html";
                }
            }
        });
    }
    authenticationProcess();
});


function previousPageNavigation(viewState) {

    const previousFormBtn = document.querySelector('.previous-form-btn');

    if (!previousFormBtn) return;

    if (window.history.length <= 1) {
        previousFormBtn.disabled = true;
    } else {
        previousFormBtn.disabled = false;

        previousFormBtn.addEventListener('click', function () {
            localStorage.setItem("viewState", viewState);
            window.history.back();
        }, { once: true });
    }
}