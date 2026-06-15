document.addEventListener("DOMContentLoaded", function () {
    async function PayloadExtractor() {

        const viewState = localStorage.getItem("viewState");

        previousPageNavigation(viewState);

        if (viewState === operatorViewState().OPERATOR.READ) {
            const operatorId = localStorage.getItem("operatorId");
            const pageNo = localStorage.getItem("pageNo") != null ? localStorage.getItem("pageNo") : 1;
            const url = `/logistic/manager/fetchall?operatorId=${operatorId}&pageNo=${pageNo}`;
            const methodType = 'GET';
            const response = await ajaxCall(url, methodType, null);
            LayoutRenderer(response, viewState);
        }
    }
    PayloadExtractor();
});


function LayoutRenderer(response, viewState) {
    let managerList = [];

    if (response && response.valueMap) {
        managerList = response.valueMap.ManagerList || [];

        localStorage.setItem("totalPages", response.valueMap.TotalPages || 1);
        localStorage.setItem("totalElements", response.valueMap.TotalElements || 0);
    } else {
        managerList = Array.isArray(response) ? response : [response];
    }
    const managerShellWrapperBody = document.querySelector('.manager-shell-wrapper-body');

    if (managerShellWrapperBody) {
        managerShellWrapperBody.innerHTML = '';
    }

    managerList.forEach(manager => {

        if (!manager) return;

        const elementDiv = document.createElement('div');
        elementDiv.className = 'manager-shell-inner-body';

        Object.entries(manager).forEach(([keyName, keyValue]) => {
            if (keyName === "operatorId" || keyName === "managerId" || keyName === "managerName" || keyName === "managerStatus") {
                const elementP = document.createElement('p');
                elementP.className = camelToKebabCase(keyName);
                elementP.textContent = `${keyName.charAt(0).toUpperCase()+whiteSpacedCamelCase(keyName).slice(1)} : ${keyValue}`;
                elementDiv.append(elementP);
            }
        });

        const elementBtnDiv = document.createElement('div');
        elementBtnDiv.className = 'manager-shell-event';

        const elementBtn = document.createElement('button');
        elementBtn.className = 'view-btn';
        elementBtn.setAttribute('data-operator-id', manager.operatorId);
        elementBtn.setAttribute('data-manager-id', manager.managerId);
        elementBtn.textContent = 'View';
        elementBtnDiv.append(elementBtn);

        elementDiv.append(elementBtnDiv);
        managerShellWrapperBody.append(elementDiv);
    });
    ClickEventBinder(viewState);
    searchClickEvent(response, viewState);
}

function ClickEventBinder(viewState) {
    const viewBtnArray = document.querySelectorAll('.view-btn');

    viewBtnArray.forEach(current => {
        current.addEventListener('click', function () {
            localStorage.setItem("operatorId", this.dataset.operatorId);
            localStorage.setItem("managerId", this.dataset.managerId);
            localStorage.setItem("viewState", viewState);
            window.location.href = "../../views/manager/manager.html";
        }, {once: true});
    });
}

function searchClickEvent(response,viewState) {
    const searchBtn = document.querySelector('.search-btn');

    searchBtn.addEventListener('click', async function () {
        const searchInput = document.getElementById('search').value.trim().toLowerCase();
        const managerNameList = document.querySelectorAll('.manager-name');

        managerNameList.forEach(function (manager) {
            const managerName = manager.textContent.replace("Manager Name : ", "").trim().toLowerCase();
            const parentElement = manager.closest('.manager-shell-inner-body');
            if (parentElement) {
                if (managerName !== searchInput && searchInput !== "") {
                    parentElement.style.display = 'none';
                } else {
                    parentElement.style.display = '';
                }
            }
        });
        const activeElement = Array.from(document.querySelectorAll('.manager-shell-inner-body')).filter(element => element.style.display !== 'none');
        if (activeElement.length === 0 && searchInput) {
            const fallbackResponse = await ajaxCall(`/logistic/manager/fetchByName?managerName=${searchInput}`);
            if (fallbackResponse) {
                LayoutRenderer(fallbackResponse, viewState);
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
            window.location.href = "../../views/manager/manager-list.html";
        }
    }, {once: true});

    const nextRecordPageBtn = document.getElementById('next-page-btn');

    nextRecordPageBtn.addEventListener('click',function (){
        let currentPage = parseInt(localStorage.getItem("pageNo")) || 1;
        if(currentPage > 0 && currentPage < lastPage){
            const pageNo = currentPage+1;
            localStorage.setItem("pageNo",pageNo);
            window.location.href = "../../views/manager/manager-list.html";
        }
    }, {once: true});

}
recordPageEventBinding();