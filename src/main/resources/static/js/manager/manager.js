document.addEventListener('DOMContentLoaded', function (event) {
    async function PayloadExtractor() {

        const viewState = localStorage.getItem("viewState");
        const managerStates = managerViewState().MANAGER_PROFILE;
        const operatorStates = operatorViewState().OPERATOR;

        previousPageNavigation(viewState);

        if (viewState === operatorStates.READ || viewState === managerStates.CREATE) {
            const managerId = localStorage.getItem("managerId");
            if(managerId){
                const url = `/logistic/manager/fetch?managerId=${managerId}`;
                const methodType = 'GET';
                const response = await ajaxCall(url, methodType, null);
                LayoutRender(managerId, response);
            }
        }
    }
    PayloadExtractor();
});

function LayoutRender(managerId, response) {

    const viewState = localStorage.getItem("viewState");
    const managerStatus = response.managerStatus;
    const managerShellWrapperBody = document.querySelector('.manager-shell-wrapper-body');

    if (!managerShellWrapperBody) return;

    managerShellWrapperBody.innerHTML = '';

    Object.entries(response).forEach(([keyName, keyValue]) => {

        if(keyName === "managerId"){
            const updateManagerBtn = document.querySelector('.update-manager-btn');
            const deleteManagerBtn = document.querySelector('.delete-manager-btn');

            updateManagerBtn.setAttribute(`data-${camelToKebabCase(keyName)}`,keyValue);
            deleteManagerBtn.setAttribute(`data-${camelToKebabCase(keyName)}`,keyValue);
        }

        const elementDiv = document.createElement('div');
        elementDiv.className = 'manager-shell-inner-body';

        const elementLabel = document.createElement('label');
        elementLabel.htmlFor = camelToKebabCase(keyName);
        elementLabel.textContent = `${keyName.charAt(0).toUpperCase()+whiteSpacedCamelCase(keyName).slice(1)}`;
        elementDiv.append(elementLabel);

        const elementInput = document.createElement('input');
        elementInput.id = camelToKebabCase(keyName);
        elementInput.value = keyValue != null ? keyValue : "";

        if (keyName !== "managerId" && keyName !== "createdAt" && keyName !== "updatedAt" && keyName !== "updatedBy") {
            elementInput.readOnly = false;
        }else{
            elementInput.readOnly = true;
        }
        elementDiv.append(elementInput);

        managerShellWrapperBody.append(elementDiv);
    });

    ClickEventBinder(viewState, managerStatus, response);
}

function ClickEventBinder(viewState, managerStatus, response) {

    const dashboardBtn = document.querySelector('.dashboard-btn');
    dashboardBtn.addEventListener('click', function () {
        window.location.href = "/views/dashboard.html";
    },{once : true});

    const createManagerBtn = document.querySelector('.create-manager-btn');
    if(createManagerBtn){
        createManagerBtn.addEventListener('click', function () {
            localStorage.setItem("viewState",federateViewState().FEDERATE_ACC.CREATE);
            window.location.href = "../../views/signUp/sign-up.html";
        },{once : true});
    }

    const updateManagerBtn = document.querySelector('.update-manager-btn');
    if(updateManagerBtn){
        updateManagerBtn.addEventListener('click', async function () {
            const payload = {};
            Object.keys(response).forEach((key) => {

                const kebabCaseKey = camelToKebabCase(key);
                const domElementInput = document.getElementById(`${kebabCaseKey}`);
                if (domElementInput) {
                    const elementInputValue = domElementInput.value.trim();

                    if (key === "managerId" || key === "updatedBy") {
                        payload[key] = elementInputValue ? parseInt(elementInputValue, 10) : null;
                    } else {
                        payload[key] = elementInputValue || null;
                    }
                }

            });
            const fallbackResponse = await ajaxCall(`/logistic/manager/save`, 'POST', payload);
            if(fallbackResponse){
                alert(fallbackResponse);
            }
        });
    }

    const deleteManagerBtn = document.querySelector('.delete-manager-btn');
    if(deleteManagerBtn){
        deleteManagerBtn.addEventListener('click', async function () {
            const url = `/logistic/manager/delete?managerId=${this.dataset.managerId}`;
            const methodType = 'DELETE';
            await ajaxCall(url, methodType, null);
        },{once : true});
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