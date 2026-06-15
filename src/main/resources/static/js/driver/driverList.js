document.addEventListener("DOMContentLoaded", function () {
    async function PayloadExtractor() {

        const viewState = localStorage.getItem("viewState");
        const operatorStates = operatorViewState().OPERATOR;
        const shipmentStates = shipmentViewState().SHIPMENT;
        const statusStates = statusViewState().SHIPMENT_ASSIGNMENT;

        previousPageNavigation(viewState);

        if(viewState === operatorStates.READ || viewState === shipmentStates.CREATE || statusStates.DRIVER_REASSIGN) {
            const pageNo = localStorage.getItem("pageNo") != null ? localStorage.getItem("pageNo") : 1;
            const operatorId = localStorage.getItem("operatorId");
            const url = `/logistic/driver/fetchall?operatorId=${operatorId}&pageNo=${pageNo}`;
            const methodType = 'GET';
            const response = await ajaxCall(url, methodType, null);
            LayoutRenderer(response, viewState);
        }
    }
    PayloadExtractor();
});

function LayoutRenderer(response, viewState) {
    let driverList = [];

    if (response && response.valueMap) {
        driverList = response.valueMap.DriverList || [];

        localStorage.setItem("totalPages", response.valueMap.TotalPages || 1);
        localStorage.setItem("totalElements", response.valueMap.TotalElements || 0);
    } else {
        driverList = Array.isArray(response) ? response : [response];
    }
    const driverShellWrapperBody = document.querySelector('.driver-shell-wrapper-body');

    if (driverShellWrapperBody) {
        driverShellWrapperBody.innerHTML = '';
    }

    driverList.forEach(driver => {

        if (!driver) return;

        const elementDiv = document.createElement('div');
        elementDiv.className = 'driver-shell-inner-body';

        Object.entries(driver).forEach(([keyName, keyValue]) => {
            if (keyName === "operatorId" || keyName === "driverId" || keyName === "driverName") {
                const elementP = document.createElement('p');
                elementP.className = camelToKebabCase(keyName);
                elementP.textContent = `${keyName} : ${keyValue}`;
                elementDiv.append(elementP);
            }
        });

        const elementBtnDiv = document.createElement('div');
        elementBtnDiv.className = 'driver-shell-event';

        const elementBtn = document.createElement('button');
        elementBtn.className = 'view-btn';
        elementBtn.setAttribute('data-operator-id', driver.operatorId);
        elementBtn.setAttribute('data-driver-id', driver.driverId);
        elementBtn.textContent = 'View';
        elementBtnDiv.append(elementBtn);

        elementDiv.append(elementBtnDiv);
        driverShellWrapperBody.append(elementDiv);
    });
    ClickEventBinder(viewState);
    searchClickEvent(response, viewState);
}

function ClickEventBinder(viewState) {

    if(viewState === operatorViewState().OPERATOR.READ || viewState === shipmentViewState().SHIPMENT.CREATE || viewState === statusViewState().SHIPMENT_ASSIGNMENT.DRIVER_REASSIGN){
        const viewBtnArray = document.querySelectorAll('.view-btn');

        viewBtnArray.forEach(current => {
            current.addEventListener('click', function () {
                localStorage.setItem("operatorId", this.dataset.operatorId);
                localStorage.setItem("driverId", this.dataset.driverId);
                localStorage.setItem("viewState", viewState);
                window.location.href = "../../views/driver/driver.html";
            }, {once: true});
        });
    }
}

function searchClickEvent(response, viewState) {
    const searchBtn = document.querySelector('.search-btn');

    searchBtn.addEventListener('click', async function () {
        const searchInput = document.getElementById('search').value.trim().toLowerCase();
        const driverNameList = document.querySelectorAll('.driver-name');

        driverNameList.forEach(function (driver) {
            const driverName = driver.textContent.replace("Driver Name : ", "").trim().toLowerCase();
            const parentElement = driver.closest('.driver-shell-inner-body');
            if (parentElement) {
                if (driverName !== searchInput && searchInput !== "") {
                    parentElement.style.display = 'none';
                } else {
                    parentElement.style.display = '';
                }
            }
        });
        const activeElements = Array.from(document.querySelectorAll('.driver-shell-inner-body')).filter(element => element.style.display !== 'none');
        if (activeElements.length === 0 && searchInput) {
            const fallbackResponse = await ajaxCall(`/logistic/driver/fetchByName?driverName=${searchInput}`);
            if (fallbackResponse) {
                LayoutRenderer(fallbackResponse, localStorage.getItem("viewState"));
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
            window.location.href = "../../views/driver/driver-list.html";
        }
    }, {once: true});

    const nextRecordPageBtn = document.getElementById('next-page-btn');

    nextRecordPageBtn.addEventListener('click',function (){
        let currentPage = parseInt(localStorage.getItem("pageNo")) || 1;
        if(currentPage > 0 && currentPage < lastPage){
            const pageNo = currentPage+1;
            localStorage.setItem("pageNo",pageNo);
            window.location.href = "../../views/driver/driver-list.html";
        }
    }, {once: true});

}
recordPageEventBinding();