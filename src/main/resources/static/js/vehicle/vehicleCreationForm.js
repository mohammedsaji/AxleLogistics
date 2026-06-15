document.addEventListener('DOMContentLoaded', function () {

    function LayoutRender() {

        const operatorId = localStorage.getItem("operatorIdForVehicleCreation");
        const viewState = localStorage.getItem("viewState");

         previousPageNavigation(viewState);

        if (viewState === operatorViewState().OPERATOR.READ || viewState === operatorViewState().OPERATOR.CREATE) {

            const vehicleShellWrapperBody = document.querySelector('.vehicle-shell-wrapper-body');

            const formFields = vehicleFormFields();

            formFields.forEach(field => {

                const clsName = camelToKebabCase(field);
                const value = whiteSpacedCamelCase(field);

                const elementDiv = document.createElement('div');
                elementDiv.className = 'vehicle-shell-inner-body';

                const elementLabel = document.createElement('label');
                elementLabel.htmlFor = clsName;
                elementLabel.textContent = `${field.charAt(0).toUpperCase() + whiteSpacedCamelCase(field).slice(1)}`;
                elementDiv.append(elementLabel);

                if (field !== "operatorId") {
                    const elementInput = document.createElement('input');
                    elementInput.className = clsName;
                    elementInput.type = "text";
                    elementInput.required = true;
                    if (field === "vehicleType") {
                        elementInput.max = "50";
                    } else if (field === "vehicleNumber") {
                        elementInput.max = "50";
                    }
                    elementDiv.append(elementInput);
                }

                if (field === "operatorId") {
                    const elementP = document.createElement('p');
                    elementP.className = clsName;
                    elementP.setAttribute(`data-${clsName}`, operatorId);
                    elementP.textContent = operatorId;
                    elementDiv.append(elementP);
                }

                vehicleShellWrapperBody.append(elementDiv);
            });

            const elementDiv = document.createElement('div');
            elementDiv.className = 'vehicle-shell-inner-body';

            const createBtn = document.createElement('button');
            createBtn.className = 'create-btn';
            createBtn.textContent = 'Create';
            elementDiv.append(createBtn);

            vehicleShellWrapperBody.append(elementDiv);
        }
        ClickEventBinder(operatorId, viewState);
    }

    LayoutRender();
});


function vehicleFormFields(){

    const formFields = ["operatorId","vehicleType","vehicleNumber"];

    return formFields;
}


function ClickEventBinder(operatorId, viewState) {

    const dashboardBtn = document.querySelector('.dashboard-btn');
    if (dashboardBtn) {
        dashboardBtn.addEventListener('click', function () {
            localStorage.setItem("viewStatus",dashboardViewState().DASHBOARD);
            window.location.href = "/views/dashboard.html";
        },{once : true});
    }


    const createBtn = document.querySelector('.create-btn');

    createBtn.addEventListener('click', async function () {

        const formFields = vehicleFormFields();

        const payload = {};

        formFields.forEach(field => {
            const kebabCaseKey = camelToKebabCase(field);

            const domElement = document.createElement(`.${kebabCaseKey}`);

            if (domElement) {

                const fieldValue = domElement.textContent || null;

                payload[field] = fieldValue;
            }
        });

        const url = `/logistic/vehicle/save`;
        const methodType = 'POST';

        const response = await ajaxCall(url, methodType, payload);

        if (response !== null) {
            localStorage.setItem("vehicleId", response.vehicleId);
            localStorage.setItem("operatorId", operatorId);
            localStorage.setItem("viewState", viewState);
            window.location.href = "../../views/vehicle/vehicle.html";
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