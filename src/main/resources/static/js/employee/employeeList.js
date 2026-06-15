document.addEventListener("DOMContentLoaded", function () {
    async function PayloadExtractor() {
        const viewState = localStorage.getItem("viewState");

        previousPageNavigation(viewState);

        if(viewState === employeeViewState().EMPLOYEE_ACCOUNT.READ || viewState === employeeViewState().EMPLOYEE_ACCOUNT.CREATE){
            const pageNo = localStorage.getItem("pageNo") != null ? localStorage.getItem("pageNo") : 1;
            const url = `/logistic/employee/fetchall?pageNo=${pageNo}`;
            const methodType = 'GET';
            const response = await ajaxCall(url, methodType, null);
            LayoutRenderer(response, viewState);
        }
    }
    PayloadExtractor();
});

function LayoutRenderer(response, viewState) {
    let employeeList = [];

    if (response && response.valueMap) {
        employeeList = response.valueMap.EmployeeList || [];

        localStorage.setItem("totalPages", response.valueMap.TotalPages || 1);
        localStorage.setItem("totalElements", response.valueMap.TotalElements || 0);
    } else {
        employeeList = Array.isArray(response) ? response : [response];
    }
    const employeeShellWrapperBody = document.querySelector('.employee-shell-wrapper-body');

    if (employeeShellWrapperBody) {
        employeeShellWrapperBody.innerHTML = '';
    }

    employeeList.forEach(employee => {

        if (!employee) return;

        const elementDiv = document.createElement('div');
        elementDiv.className = 'employee-shell-inner-body';

        Object.entries(employee).forEach(([keyName, keyValue]) => {
            if (
                keyName === "employeeId" ||
                keyName === "employeeName" ||
                keyName === "employeeDepartment"
            ) {
                const elementP = document.createElement('p');
                elementP.className = camelToKebabCase(keyName);
                elementP.textContent = `${keyName} : ${keyValue}`;
                elementDiv.append(elementP);
            }else if( keyName === "AccountVO"){
                const accountDiv = document.createElement('div');
                accountDiv.className = 'employee-shell-group-body'
                Object.entries(keyValue).forEach(([innerKeyName,innerKeyValue])=>{
                    const elementDiv = document.createElement('div');
                    elementDiv.className = 'employee-shell-section-body';
                    if (
                        innerKeyName === "accountUsername" ||
                        innerKeyName === "accountRole" ||
                        innerKeyName === "accountStatus" ||
                        innerKeyName === "accountEmail"
                    ) {
                        const elementLabel = document.createElement('label');
                        elementLabel.htmlFor = camelToKebabCase(innerKeyName);
                        elementLabel.textContent = `${innerKeyName.charAt(0).toUpperCase()+whiteSpacedCamelCase(innerKeyName).slice(1)}`;
                        elementDiv.append(elementLabel);

                        const elementInput = document.createElement('input');
                        elementInput.id = camelToKebabCase(innerKeyName);
                        elementInput.value = innerKeyValue != null ? innerKeyValue : "";
                        elementInput.readOnly = false;
                        elementInput.max = "50";
                        elementDiv.append(elementInput);
                    }
                    accountDiv.append(elementDiv);
                });
                elementDiv.append(accountDiv);
            }
        });

        const elementBtnDiv = document.createElement('div');
        elementBtnDiv.className = 'employee-shell-event';

        const elementBtn = document.createElement('button');
        elementBtn.className = 'view-btn';
        elementBtn.setAttribute('data-account-id', employee.accountId);
        elementBtn.setAttribute('data-employee-id', employee.employeeId);
        elementBtn.textContent = 'View';
        elementBtnDiv.append(elementBtn);

        elementDiv.append(elementBtnDiv);
        employeeShellWrapperBody.append(elementDiv);
    });
    ClickEventBinder(viewState);
    searchClickEvent(response, viewState);
}

function ClickEventBinder(viewState) {
    const viewBtnArray = document.querySelectorAll('.view-btn');

    viewBtnArray.forEach(current => {
        current.addEventListener('click', function () {
            localStorage.setItem("employeeId", this.dataset.employeeId);
            localStorage.setItem("viewState", viewState);
            if (viewState === employeeViewState().EMPLOYEE_ACCOUNT.ADMINSTR) {
                localStorage.setItem("accountId", this.dataset.accountId);
            }
            window.location.href = "../../views/employee/employee.html";
        },{once : true});
    });
}

function searchClickEvent(response, viewState) {
    const searchBtn = document.querySelector('.search-btn');

    searchBtn.addEventListener('click', async function () {
        const searchInput = document.getElementById('search').value.trim().toLowerCase();
        const employeeNameList = document.querySelectorAll('.employee-name');

        employeeNameList.forEach(function (employee) {
            const employeeName = employee.textContent.trim().toLowerCase();
            const parentElement = employee.closest('.employee-shell-inner-body');
            if (parentElement) {
                if (employeeName !== searchInput && searchInput !== "") {
                    parentElement.style.display = 'none';
                } else {
                    parentElement.style.display = '';
                }
            }
        });
        const activeElements = Array.from(document.querySelectorAll('.employee-shell-inner-body')).filter(element => element.style.display !== 'none');
        if (activeElements.length === 0 && searchInput) {
            const fetchEmployee = await ajaxCall(`/logistic/employee/fetchByName?employeeName=${searchInput}`);
            if (fetchEmployee) {
                LayoutRenderer(fetchEmployee);
            }
        }
    });

    const searchInput = document.getElementById('search');

    searchInput.addEventListener('input', function () {
        const searchValue = this.value.trim().toLowerCase();

        if (!searchValue) {
            LayoutRenderer(response, viewState);
            return;
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


function recordPageEventBinding(){

    const lastPage = parseInt(localStorage.getItem("totalPages")) || 1;

    const previousRecordPageBtn = document.getElementById('previous-page-btn');

    previousRecordPageBtn.addEventListener('click',function (){
        let currentPage = parseInt(localStorage.getItem("pageNo")) || 1;
        if(currentPage > 1){
            const pageNo = currentPage-1;
            localStorage.setItem("pageNo",pageNo);
            window.location.href = "../../views/employee/employee-list.html";
        }
    }, {once: true});

    const nextRecordPageBtn = document.getElementById('next-page-btn');

    nextRecordPageBtn.addEventListener('click',function (){
        let currentPage = parseInt(localStorage.getItem("pageNo")) || 1;
        if(currentPage > 0 && currentPage < lastPage){
            const pageNo = currentPage+1;
            localStorage.setItem("pageNo",pageNo);
            window.location.href = "../../views/employee/employee-list.html";
        }
    }, {once: true});

}
recordPageEventBinding();