document.addEventListener('DOMContentLoaded', function () {

    function LayoutRender() {

        const operatorId = localStorage.getItem("operatorIdForDriverCreation");

        const viewState = localStorage.getItem("viewState");

        previousPageNavigation(viewState);

        const driverShellWrapperBody = document.querySelector('.driver-shell-wrapper-body');

        if(!driverShellWrapperBody)return

        driverShellWrapperBody.innerHTML = '';

        if (viewState === driverViewState().DRIVER_PROFILE.CREATE) {

            const formFields = driverFormFields();

            formFields.forEach(field => {

                const clsName = camelToKebabCase(field);

                const value = whiteSpacedCamelCase(field);

                const elementDiv = document.createElement('div');
                elementDiv.className = 'driver-shell-inner-body';

                const elementLabel = document.createElement('label');
                elementLabel.htmlFor = clsName;
                elementLabel.textContent = `${field.charAt(0).toUpperCase() + whiteSpacedCamelCase(field).slice(1)}`;
                elementDiv.append(elementLabel);

                if (field !== "operatorId") {
                    const elementInput = document.createElement('input');
                    elementInput.id = clsName;
                    elementInput.type = "text";
                    elementInput.required = true;
                    elementInput.readOnly = false;
                    if (field === "driverName") {
                        elementInput.max = "250";
                    } else if (field === "driverPhoneNo") {
                        elementInput.max = "25";
                    } else if (field === "driverLicenseNo") {
                        elementInput.max = "20";
                    }
                    elementDiv.append(elementInput);
                }
                if (field === "operatorId") {
                    const elementP = document.createElement('p');
                    elementP.id = clsName;
                    elementP.setAttribute(`data-${clsName}`, operatorId);
                    elementP.textContent = operatorId;
                    elementDiv.append(elementP);
                }
                driverShellWrapperBody.append(elementDiv);
            });

            const elementDiv = document.createElement('div');
            elementDiv.className = 'driver-shell-inner-body'

            const createBtn = document.createElement('button');
            createBtn.className = 'create-btn';
            createBtn.textContent = 'Create';
            elementDiv.append(createBtn);
            driverShellWrapperBody.append(elementDiv);
        }

        ClickEventBinder(operatorId);
    }

    LayoutRender();
});


function driverFormFields() {

    const formFields = ["driverName", "driverPhoneNo", "OperatorId", "driverLicenseNo"];

    return formFields;
}


function ClickEventBinder(operatorId) {

    const dashboardBtn = document.querySelector('.dashboard-btn');
    if (dashboardBtn) {
        dashboardBtn.addEventListener('click', function () {
            localStorage.setItem("viewStatus",dashboardViewState().DASHBOARD);
            window.location.href = "/views/dashboard.html";
        },{once : true});
    }

    const createBtn = document.querySelector('.create-btn');

    createBtn.addEventListener('click', async function () {

        const formFields = driverFormFields();

        const payload = {};

        formFields.forEach(field => {
            const kebabCaseKey = camelToKebabCase(field);

            const domElement = document.getElementById(`${kebabCaseKey}`);

            if (domElement) {

                const fieldValue = (domElement.tagName === 'INPUT' || domElement.tagName === 'P')
                    ? domElement.value
                    : domElement.textContent;

                payload[field] = fieldValue;
            }
        });

        const url = `/logistic/driver/save`;
        const methodType = 'POST';

        const response = await ajaxCall(url, methodType, payload);

        if (response) {
            localStorage.setItem("driverId", response.driverId);
            localStorage.setItem("operatorId", operatorId);
            localStorage.setItem("viewState", driverViewState().DRIVER_PROFILE.CREATE);
            window.location.href = "../../views/driver/driver.html";
        }
    });
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