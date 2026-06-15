document.addEventListener('DOMContentLoaded', function (event) {
    async function PayloadExtractor() {

        const viewState = localStorage.getItem("viewState");
        const employeeStates = employeeViewState().EMPLOYEE_ACCOUNT;

        previousPageNavigation(viewState);

        if (viewState === employeeStates.READ || viewState === employeeStates.ADMINSTR || viewState === employeeStates.CREATE) {
            const employeeId = localStorage.getItem("employeeId");
            if (employeeId) {
                const url = `/logistic/employee/fetch?employeeId=${employeeId}`;
                const methodType = 'GET';
                const response = await ajaxCall(url, methodType, null);
                LayoutRender(employeeId, response, viewState);
            }
        }
    }
    PayloadExtractor();
});

function LayoutRender(employeeId, response, viewState) {

    const employeeStatus = response.employeeStatus;
    const employeeShellWrapperBody = document.querySelector('.employee-shell-wrapper-body');

    if (!employeeShellWrapperBody) return;

    employeeShellWrapperBody.innerHTML = '';

    Object.entries(response).forEach(([keyName, keyValue]) => {

        if (typeof keyValue === 'object' && keyValue !== null) {
            return;
        }

        if (keyName === "employeeId") {
            const updateEmployeeBtn = document.querySelector('.update-employee-btn');
            const deleteEmployeeBtn = document.querySelector('.delete-employee-btn');

            if (updateEmployeeBtn) updateEmployeeBtn.setAttribute(`data-${camelToKebabCase(keyName)}`, keyValue);
            if (deleteEmployeeBtn) deleteEmployeeBtn.setAttribute(`data-${camelToKebabCase(keyName)}`, keyValue);
        }

        const elementDiv = document.createElement('div');
        elementDiv.className = 'employee-shell-inner-body';

        const elementLabel = document.createElement('label');
        elementLabel.htmlFor = camelToKebabCase(keyName);
        elementLabel.textContent = `${keyName.charAt(0).toUpperCase() + whiteSpacedCamelCase(keyName).slice(1)}`;
        elementDiv.append(elementLabel);

        const elementInput = document.createElement('input');
        elementInput.id = camelToKebabCase(keyName);
        elementInput.value = keyValue != null ? keyValue : "";

        if (
            keyName !== "employeeId" &&
            keyName !== "accountId" &&
            keyName !== "createdAt" &&
            keyName !== "updatedAt" &&
            keyName !== "updatedBy"
        ) {
            elementInput.readOnly = false;
        }else{
            elementInput.readOnly = true;
        }
        elementDiv.append(elementInput);

        employeeShellWrapperBody.append(elementDiv);
    });

    const elementDiv = document.createElement('div');
    elementDiv.className = 'employee-shell-inner-body';

    const createBtn = document.createElement('button');
    createBtn.className = 'set-as-manager-btn';
    createBtn.textContent = 'Set as Manager';
    createBtn.setAttribute('data-manager-id',response.employeeId);
    elementDiv.append(createBtn);
    employeeShellWrapperBody.append(elementDiv);

    ClickEventBinder(viewState, employeeStatus, response);
}

function ClickEventBinder(viewState, employeeStatus, response) {

    const dashboardBtn = document.querySelector('.dashboard-btn');
    if (dashboardBtn) {
        dashboardBtn.addEventListener('click', function () {
            localStorage.setItem("viewState",dashboardViewState().DASHBOARD);
            window.location.href = "/views/dashboard.html";
        },{once:true});
    }

    if(viewState === employeeViewState().EMPLOYEE_ACCOUNT.READ || viewState === employeeViewState().EMPLOYEE_ACCOUNT.ADMINSTR){
        const updateEmployeeBtn = document.querySelector('.update-employee-btn');
        if(updateEmployeeBtn){
            updateEmployeeBtn.addEventListener('click', async function () {
                const payload = {};
                Object.keys(response).forEach((key) => {

                    const kebabCaseKey = camelToKebabCase(key);
                    const domElementInput = document.getElementById(`${kebabCaseKey}`);
                    if (domElementInput) {
                        const elementInputValue = domElementInput.value.trim();

                        if (
                            key === "employeeId" ||
                            key === "reportingManagerId" ||
                            key === "accountId" ||
                            key === "updatedBy"
                        ) {
                            payload[key] = elementInputValue ? parseInt(elementInputValue, 10) : null;
                        } else {
                            payload[key] = elementInputValue || null;
                        }
                    }
                });
                const fallbackResponse = await ajaxCall(`/logistic/employee/save`, 'POST', payload);
                if(fallbackResponse){
                    alert(fallbackResponse);
                }
            });
        }

        const deleteEmployeeBtn = document.querySelector('.delete-employee-btn');
        if(deleteEmployeeBtn){
            deleteEmployeeBtn.addEventListener('click', async function () {
                const url = `/logistic/employee/delete?employeeId=${this.dataset.employeeId}`;
                const methodType = 'DELETE';
                await ajaxCall(url, methodType, null);
            },{once : true});
        }
    }

    if(viewState === employeeViewState().EMPLOYEE_ACCOUNT.CREATE){
        const setAsManagerBtn = document.querySelector('.set-as-manager-btn');
        setAsManagerBtn.addEventListener('click',function (){
            localStorage.setItem("employeeManagerId",this.dataset.managerId);
            window.location.href = "../../views/employee/employee-account-creation-form.html";
        },{once:true});
    }
}


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