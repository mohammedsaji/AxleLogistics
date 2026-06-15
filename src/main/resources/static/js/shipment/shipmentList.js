document.addEventListener("DOMContentLoaded", function () {
    async function PayloadExtractor() {

        const viewState = localStorage.getItem("viewState");
        const shipmentStates = shipmentViewState().SHIPMENT;

        previousPageNavigation(viewState);

        if (viewState === shipmentStates.READ) {
            const pageNo = localStorage.getItem("pageNo") != null ? localStorage.getItem("pageNo") : 1;
            const url = `/logistic/shipment/fetchall?pageNo=${pageNo}`;
            const methodType = 'GET';
            const response = await ajaxCall(url, methodType, null);
            LayoutRender(response, viewState);
        }
    }

    PayloadExtractor();
});

function LayoutRender(response, viewState) {
    let shipmentList = [];

    if (response && response.valueMap) {
        shipmentList = response.valueMap.ShipmentList || [];

        localStorage.setItem("totalPages", response.valueMap.TotalPages || 1);
        localStorage.setItem("totalElements", response.valueMap.TotalElements || 0);
    } else {
        shipmentList = Array.isArray(response) ? response : [response];
    }
    const shipmentShellWrapperBody = document.querySelector('.shipment-shell-wrapper-body');

    if (shipmentShellWrapperBody) {
        shipmentShellWrapperBody.innerHTML = '';
    }

    shipmentList.forEach(shipment => {
        if (!shipment) return;

        const elementDiv = document.createElement('div');
        elementDiv.className = 'shipment-shell-inner-body';

        Object.entries(shipment).forEach(([keyName, keyValue]) => {
            if (keyName === "shippingId" || keyName === "shipmentFrom" || keyName === "shipmentTo") {
                const elementP = document.createElement('p');
                elementP.className = camelToKebabCase(keyName);
                elementP.setAttribute(`data-${camelToKebabCase(keyName)}`, keyValue);
                elementP.textContent = `${keyName.charAt(0).toUpperCase() + whiteSpacedCamelCase(keyName).slice(1)} : ${keyValue}`;
                elementDiv.append(elementP);
            }
        });

        const elementBtnDiv = document.createElement('div');
        elementBtnDiv.className = 'shipment-shell-event';

        const elementBtn = document.createElement('button');
        elementBtn.className = 'view-btn';
        elementBtn.setAttribute('data-shipment-id', shipment.shippingId);
        elementBtn.textContent = 'View';
        elementBtnDiv.append(elementBtn);

        elementDiv.append(elementBtnDiv);
        shipmentShellWrapperBody.append(elementDiv);
    });

    ClickEventBinder(viewState);
    searchClickEvent(response, viewState)
}

function ClickEventBinder(viewState) {

    if (viewState === shipmentViewState().SHIPMENT.READ) {
        const viewBtnArray = document.querySelectorAll('.view-btn');

        viewBtnArray.forEach(current => {
            current.addEventListener('click', function () {
                localStorage.setItem("shipmentId", this.dataset.shipmentId);
                localStorage.setItem("viewState", viewState);
                window.location.href = "../../views/shipment/shipment.html";
            },{once : true});
        });

        const createShipmentBtn = document.querySelector('.create-shipment-btn');

        createShipmentBtn.addEventListener('click', function () {
            localStorage.setItem("viewState", shipmentViewState().SHIPMENT.CREATE);
            window.location.href = "../../views/operator/transport-type.html";
        },{once : true});
    }
}

function searchClickEvent(response, viewState) {
    const searchBtn = document.querySelector('.search-btn');
    if(searchBtn){
        searchBtn.addEventListener('click', async function () {
            const searchInput = document.getElementById('search').value.trim().toLowerCase();
            const shipmentFromList = document.querySelectorAll('.shipping-id');

            shipmentFromList.forEach(function (shipment) {
                const shipmentId = shipment.textContent.replace("Shipment Id : ", "").trim().toLowerCase();
                const parentElement = shipment.closest('.operator-shell-inner-body');
                if (parentElement) {
                    if (parseInt(shipmentId,10) !== parseInt(searchInput,10) && searchInput !== "") {
                        parentElement.style.display = 'none';
                    } else {
                        parentElement.style.display = '';
                    }
                }
            });

            const activeElements = Array.from(document.querySelectorAll('.shipment-shell-inner-body')).filter(element => element.style.display !== 'none');
            if (activeElements.length === 0 && searchInput) {
                const fallbackResponse = await ajaxCall(`/logistic/shipment/fetchbyid?shipmentId=${searchInput}`, 'GET', null);
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
            window.location.href = "../../views/shipment/shipment-list.html";
        }
    }, {once: true});

    const nextRecordPageBtn = document.getElementById('next-page-btn');

    nextRecordPageBtn.addEventListener('click',function (){
        let currentPage = parseInt(localStorage.getItem("pageNo")) || 1;
        if(currentPage > 0 && currentPage < lastPage){
            const pageNo = currentPage+1;
            localStorage.setItem("pageNo",pageNo);
            window.location.href = "../../views/shipment/shipment-list.html";
        }
    }, {once: true});

}
recordPageEventBinding();