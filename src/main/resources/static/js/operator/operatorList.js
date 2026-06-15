document.addEventListener("DOMContentLoaded", function () {
    async function PayloadExtractor() {

        const viewState = localStorage.getItem("viewState");

        const operatorState = operatorViewState().OPERATOR;
        const shipmentState = shipmentViewState().SHIPMENT;
        const statusState = statusViewState().SHIPMENT_ASSIGNMENT;

        previousPageNavigation(viewState);

        if (viewState !== operatorViewState().READ) {
            const createOperatorBtn = document.querySelector('.create-operator-btn');
            const parentDiv = createOperatorBtn.closest('.operator-shell-inner-header');
            if (parentDiv) {
                parentDiv.remove();
            }
        }
        if(viewState === operatorState.READ || viewState === shipmentState.CREATE || viewState === statusState.OPERATOR_REASSIGN){
            const pageNo = localStorage.getItem("pageNo") != null ? localStorage.getItem("pageNo") : 1;
            const transportType = localStorage.getItem("transportType");
            const url = `/logistic/operator/fetchall?operatorTransportType=${transportType}&pageNo=${pageNo}`;
            const methodType = 'GET';
            const response = await ajaxCall(url, methodType, null);
            LayoutRenderer(viewState, response);
        }
    }

    PayloadExtractor();
});

function LayoutRenderer(viewState, response) {
    let operatorList = [];

    if (response && response.valueMap) {
        operatorList = response.valueMap.OperatorList || [];

        localStorage.setItem("totalPages", response.valueMap.TotalPages || 1);
        localStorage.setItem("totalElements", response.valueMap.TotalElements || 0);
    } else {
        operatorList = Array.isArray(response) ? response : [response];
    }
    const operatorShellWrapperBody = document.querySelector('.operator-shell-wrapper-body');

    if (operatorShellWrapperBody) {
        operatorShellWrapperBody.innerHTML = '';
    }

    operatorList.forEach(operator => {
        if (!operator) return;

        const elementDiv = document.createElement('div');
        elementDiv.className = 'operator-shell-inner-body';

        Object.entries(operator).forEach(([keyName, keyValue]) => {

            if (keyName === "operatorId" || keyName === "operatorName" || keyName === "operatorTransportType") {
                const elementP = document.createElement('p');
                elementP.className = camelToKebabCase(keyName);
                elementP.textContent = `${keyName.charAt(0).toUpperCase()+whiteSpacedCamelCase(keyName).slice(1)} : ${keyValue}`;
                elementDiv.append(elementP);
            }
        });
        const elementBtnDiv = document.createElement('div');
        elementBtnDiv.className = 'operator-shell-event'

        const elementBtn = document.createElement('button');
        elementBtn.className = 'view-btn';
        elementBtn.setAttribute('data-operator-id', operator.operatorId);
        elementBtn.textContent = 'View';
        elementBtnDiv.append(elementBtn);

        elementDiv.append(elementBtnDiv);
        operatorShellWrapperBody.append(elementDiv);
    })
    ClickEventBinder(viewState);
    searchOptionClickEvent(response, viewState);
}

function ClickEventBinder(viewState) {

    if(viewState === operatorViewState().OPERATOR.READ || viewState === shipmentViewState().SHIPMENT.CREATE || viewState === statusViewState().SHIPMENT_ASSIGNMENT.OPERATOR_REASSIGN){
        const viewBtnArray = document.querySelectorAll('.view-btn');
        viewBtnArray.forEach(current => {
            current.addEventListener('click', function () {
                const operatorId = this.dataset.operatorId;
                localStorage.setItem("operatorId", operatorId);
                localStorage.setItem("viewState", viewState);
                window.location.href = "../../views/operator/operator.html";
            }, {once: true});
        });
    }

    const createOperatorBtn = document.querySelector('.create-operator-btn');
    if(createOperatorBtn){
        createOperatorBtn.addEventListener('click', function () {
            localStorage.setItem("viewState", operatorViewState().OPERATOR.CREATE);
            window.location.href = "../../views/operator/operator-creation-form.html";
        }, {once: true});
    }
}


function searchOptionClickEvent(response, viewState) {

    const searchBtn = document.querySelector('.search-btn');
    if(searchBtn){
        searchBtn.addEventListener('click', async function () {
            const searchInput = document.getElementById('search').value.trim();
            const operatorNameList = document.querySelectorAll('.operator-name');
            operatorNameList.forEach(function (operator) {
                const operatorName = operator.textContent.replace("Operator Name : ", "").trim().toLowerCase();
                const parentElement = operator.closest('.operator-shell-inner-body');
                if (parentElement) {
                    if (operatorName !== searchInput && searchInput !== "") {
                        parentElement.style.display = 'none';
                    } else {
                        parentElement.style.display = '';
                    }
                }
            });
            const activeElements = Array.from(document.querySelectorAll('.operator-shell-inner-body')).filter(element => element.style.display !== 'none');
            if (activeElements.length === 0 && searchInput) {
                const fallbackResponse = await ajaxCall(`/logistic/operator/fetchByName?operatorName=${searchInput}`);
                if (fallbackResponse) {
                    LayoutRenderer(fallbackResponse, localStorage.getItem("viewState"));
                }
            }
        });
    }

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
            window.location.href = "../../views/operator/operator-list.html";
        }
    }, {once: true});

    const nextRecordPageBtn = document.getElementById('next-page-btn');

    nextRecordPageBtn.addEventListener('click',function (){
        let currentPage = parseInt(localStorage.getItem("pageNo")) || 1;
        if(currentPage > 0 && currentPage < lastPage){
            const pageNo = currentPage+1;
            localStorage.setItem("pageNo",pageNo);
            window.location.href = "../../views/operator/operator-list.html";
        }
    }, {once: true});

}
recordPageEventBinding();