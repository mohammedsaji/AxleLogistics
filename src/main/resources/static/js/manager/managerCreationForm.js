document.addEventListener('DOMContentLoaded', function () {

    function LayoutRender() {

        const operatorId = localStorage.getItem("operatorIdForManagerCreation");

        const viewState = localStorage.getItem("viewState");

        previousPageNavigation(viewState);

        const managerStatusOptions = ["ACTIVE", "IN_ACTIVE"];

        if (viewState === managerViewState().MANAGER_PROFILE.CREATE) {

            const managerShellWrapperBody = document.querySelector('.manager-shell-wrapper-body');

            const formFields = managerFormFields();

            formFields.forEach(field => {

                const clsName = camelToKebabCase(field);

                const value = whiteSpacedCamelCase(field);

                const elementDiv = document.createElement('div');
                elementDiv.className = 'manager-shell-inner-body';

                const elementLabel = document.createElement('label');
                elementLabel.htmlFor = clsName;
                elementLabel.textContent = `${field.charAt(0).toUpperCase() + whiteSpacedCamelCase(field).slice(1)}`;
                elementDiv.append(elementLabel);

                if (field !== "operatorId" && field !== "managerStatus") {
                    const elementInput = document.createElement('input');
                    elementInput.id = clsName;
                    elementInput.type = "text";
                    elementInput.required = true;
                    if (field === "managerName") {
                        elementInput.max = "250";
                    }
                    elementDiv.append(elementInput);
                } else if (field === "managerStatus") {
                    const elementSelect = document.createElement('select');
                    elementSelect.id = clsName;
                    elementSelect.required = true;

                    const elementDefaultOption = document.createElement('option');
                    elementDefaultOption.value = "";
                    elementDefaultOption.textContent = "Select Status";
                    elementDefaultOption.disabled = true;
                    elementDefaultOption.selected = true;
                    elementSelect.append(elementDefaultOption);

                    managerStatusOptions.forEach(statusOption => {
                        const elementDBStatusOption = document.createElement('option');
                        elementDBStatusOption.value = statusOption;
                        elementDBStatusOption.textContent = statusOption;
                        elementSelect.append(elementDBStatusOption);
                    });
                    elementDiv.append(elementSelect);
                } else if (field === "operatorId") {
                    const elementP = document.createElement('p');
                    elementP.id = clsName;
                    elementP.setAttribute(`data-${clsName}`, operatorId);
                    elementP.textContent = operatorId;

                    elementDiv.append(elementP);
                }
                managerShellWrapperBody.append(elementDiv);
            });

            const elementDiv = document.createElement('div');
            elementDiv.className = 'manager-shell-inner-body'

            const createBtn = document.createElement('button');
            createBtn.className = 'create-btn';
            createBtn.textContent = 'Create';
            elementDiv.append(createBtn);
            managerShellWrapperBody.append(elementDiv);
        }
        ClickEventBinder(operatorId, viewState);
    }

    LayoutRender();
});

function managerFormFields() {

    const formFields = ["managerName", "managerContactNo", "operatorId", "managerStatus"];

    return formFields;
}


function ClickEventBinder(operatorId, viewState) {

    const dashboardBtn = document.querySelector('.dashboard-btn');
    if (dashboardBtn) {
        dashboardBtn.addEventListener('click', function () {
            localStorage.setItem("viewStatus",dashboardViewState().DASHBOARD);
            window.location.href = "/views/dashboard.html";
        },{once:true});
    }

    if(viewState === managerViewState().MANAGER_PROFILE.CREATE){
        const createBtn = document.querySelector('.create-btn');
        if(createBtn){
            createBtn.addEventListener('click', async function (e) {
                e.preventDefault();
                const formFields = managerFormFields();
                const payload = {};
                formFields.forEach(field => {
                    const kebabCaseKey = camelToKebabCase(field);
                    const domElement = document.getElementById(`${kebabCaseKey}`);
                    if (domElement) {
                        let fieldValue ;
                        if (domElement.tagName === 'INPUT' || domElement.tagName === 'SELECT') {
                            fieldValue = domElement.value ? domElement.value.trim() : null;
                        }else{
                            fieldValue = domElement.textContent ? domElement.textContent.trim() : null;
                        }
                        if(field === "operatorId" && fieldValue){
                            fieldValue = parseInt(fieldValue,10);
                        }
                        payload[field] = fieldValue;
                    }
                });
                if(!payload.managerName || !payload.managerContactNo || !payload.managerStatus){
                    alert("Please fill in all required fields.");
                    return;
                }
                payload["accountUserName"] = localStorage.getItem("accountCreationUsername");
                const url = `/logistic/manager/save`;
                const methodType = 'POST';
                const response = await ajaxCall(url, methodType, payload);

                if (response !== null) {
                    localStorage.setItem("newlySavedManagerId", response.managerId);
                    localStorage.setItem("operatorIdForNewlySavedManager", operatorId);
                    localStorage.setItem("viewState",viewState);
                    window.location.href = "../../views/manager/manager.html";
                }
            });
        }
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