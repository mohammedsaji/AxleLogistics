document.addEventListener('DOMContentLoaded',function (){

    function LayoutRender(){

        const viewState = localStorage.getItem("viewState");

        const transportTypeOptions = ['SEA','AIR','RAIL','ROAD'];

        previousPageNavigation(viewState);

        if(viewState === operatorViewState().OPERATOR.CREATE){

            const operatorShellWrapperBody = document.querySelector('.operator-shell-wrapper-body');

            const formFields = operatorFormFields();

            formFields.forEach(field=>{

                const clsName = camelToKebabCase(field);

                const value = whiteSpacedCamelCase(field);

                const elementDiv = document.createElement('div');
                elementDiv.className = clsName;

                const elementLabel = document.createElement('label');
                elementLabel.htmlFor = clsName;
                elementLabel.textContent = `${field.charAt(0).toUpperCase() + whiteSpacedCamelCase(field).slice(1)}`;
                elementDiv.append(elementLabel);

                if (field === "operatorTransportType") {
                    const elementSelect = document.createElement('select');
                    elementSelect.id = clsName;
                    elementSelect.required = true;

                    const elementDefaultOption = document.createElement('option');
                    elementDefaultOption.value = "";
                    elementDefaultOption.textContent = "Select Status";
                    elementDefaultOption.disabled = true;
                    elementDefaultOption.selected = true;
                    elementSelect.append(elementDefaultOption);

                    transportTypeOptions.forEach(statusOption => {
                        const elementDBStatusOption = document.createElement('option');
                        elementDBStatusOption.value = statusOption;
                        elementDBStatusOption.textContent = statusOption;
                        elementSelect.append(elementDBStatusOption);
                    });
                    elementDiv.append(elementSelect);
                }else if(field === "operatorName"){
                    const elementInput = document.createElement('input');
                    elementInput.id = clsName;
                    elementInput.type = "text";
                    elementInput.required = true;
                    elementInput.max = "250";
                    elementDiv.append(elementInput);
                }
                operatorShellWrapperBody.append(elementDiv);
            });

            const elementDiv = document.createElement('div');
            elementDiv.className = 'manager-shell-inner-body'

            const createBtn = document.createElement('button');
            createBtn.className = 'create-btn';
            createBtn.textContent = 'Create';
            elementDiv.append(createBtn);
            operatorShellWrapperBody.append(elementDiv);
        }

        ClickEventBinder();
    }
    LayoutRender();
});

function ClickEventBinder(){

    const dashboardBtn = document.querySelector('.dashboard-btn');
    if (dashboardBtn) {
        dashboardBtn.addEventListener('click', function () {
            localStorage.setItem("viewStatus",dashboardViewState().DASHBOARD);
            window.location.href = "/views/dashboard.html";
        },{once:true});
    }

    const createBtn = document.querySelector('.create-btn');
    if(createBtn){
        createBtn.addEventListener('click',async function (){
            const formFields = operatorFormFields();
            const payload = {};
            formFields.forEach(current=>{
                const kebabCaseKey = camelToKebabCase(current);
                const domElement = document.getElementById(`${kebabCaseKey}`);
                if(domElement) {
                    const fieldValue = domElement.value.trim() || null;
                    payload[current] = fieldValue;
                }
            });

            const url = `/logistic/operator/save`;
            const methodType = 'POST';
            const response = await ajaxCall(url,methodType,payload);
            if(response){
                localStorage.setItem("operatorId",response.operatorId);
                localStorage.setItem("viewState",operatorViewState().OPERATOR.READ)
                window.location.href = "../../views/operator/operator.html";
            }
        });
    }
}


function operatorFormFields(){

    const formFields = ["operatorName","operatorTransportType"];

    return formFields;
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