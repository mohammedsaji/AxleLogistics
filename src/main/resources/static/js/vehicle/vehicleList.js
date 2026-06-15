document.addEventListener("DOMContentLoaded", function () {
    async function PayloadExtractor() {

        const viewState = localStorage.getItem("viewState");
        const operatorStates = operatorViewState().OPERATOR;
        const shipmentStates = shipmentViewState().SHIPMENT;
        const statusStates = statusViewState().SHIPMENT_ASSIGNMENT;

         previousPageNavigation(viewState);

        if (viewState === operatorStates.READ || viewState === shipmentStates.CREATE || viewState === statusStates.VEHICLE_REASSIGN) {
            const operatorId = localStorage.getItem("operatorId");
            const pageNo = localStorage.getItem("pageNo") != null ? localStorage.getItem("pageNo") : 1;
            const url = `/logistic/vehicle/fetchall?operatorId=${operatorId}&pageNo=${pageNo}`;
            const methodType = 'GET';
            const response = await ajaxCall(url, methodType, null);
            LayoutRenderer(response, viewState);
            searchClickEvent(response, viewState);
        }
    }

    PayloadExtractor();
});

function LayoutRenderer(response, viewState) {
    let vehicleList = [];

    if (response && response.valueMap) {
        vehicleList = response.valueMap.VehicleList || [];

        localStorage.setItem("totalPages", response.valueMap.TotalPages || 1);
        localStorage.setItem("totalElements", response.valueMap.TotalElements || 0);
    } else {
        vehicleList = Array.isArray(response) ? response : [response];
    }
    const vehicleShellWrapperBody = document.querySelector('.vehicle-shell-wrapper-body');

    if (vehicleShellWrapperBody) {
        vehicleShellWrapperBody.innerHTML = '';
    }

    vehicleList.forEach(vehicle => {

        if (!vehicle) return;

        const elementDiv = document.createElement('div');
        elementDiv.className = 'vehicle-shell-inner-body';

        Object.entries(vehicle).forEach(([keyName, keyValue]) => {
            if (keyName === "vehicleId" || keyName === "vehicleNumber" || keyName === "operatorId") {
                const elementP = document.createElement('p');
                elementP.className = camelToKebabCase(keyName);
                elementP.textContent = `${keyName.charAt(0).toUpperCase() + whiteSpacedCamelCase(keyName).slice(1)} : ${keyValue}`;
                elementDiv.append(elementP);
            }
        });

        const elementBtnDiv = document.createElement('div');
        elementBtnDiv.className = 'vehicle-shell-event';

        const elementBtn = document.createElement('button');
        elementBtn.className = 'view-btn';
        elementBtn.setAttribute('data-operator-id', vehicle.operatorId);
        if (viewState === shipmentViewState().SHIPMENT.CREATE) {
            elementBtn.setAttribute('data-driver-id', localStorage.getItem("driverId"));
        }
        elementBtn.setAttribute('data-vehicle-id', vehicle.vehicleId);
        elementBtn.textContent = 'View';
        elementBtnDiv.append(elementBtn);

        elementDiv.append(elementBtnDiv);
        vehicleShellWrapperBody.append(elementDiv);
    });
    ClickEventBinder(viewState);
    searchClickEvent(response, viewState);
}

function ClickEventBinder(viewState) {

    if (viewState === shipmentViewState().SHIPMENT.CREATE || viewState === operatorViewState().OPERATOR.READ || viewState === statusViewState().SHIPMENT_ASSIGNMENT.VEHICLE_REASSIGN) {
        const viewBtnArray = document.querySelectorAll('.view-btn');

        viewBtnArray.forEach(current => {
            current.addEventListener('click', function () {
                localStorage.setItem("operatorId", this.dataset.operatorId);
                if (viewState === shipmentViewState().SHIPMENT.CREATE) {
                    localStorage.setItem("driverId", this.dataset.driverId);
                }
                localStorage.setItem("vehicleId", this.dataset.vehicleId);
                localStorage.setItem("viewState", viewState);
                window.location.href = "../../views/vehicle/vehicle.html";
            },{once : true});
        });
    }
}

function searchClickEvent(response, viewState) {
    const searchBtn = document.querySelector('.search-btn');

    searchBtn.addEventListener('click', async function () {
        const searchInput = document.getElementById('search').value.trim().toLowerCase();
        const vehicleNumberList = document.querySelectorAll('.vehicle-number');

        vehicleNumberList.forEach(function (vehicle) {
            const vehicleNumber = vehicle.textContent.replace("Vehicle Number : ", "").trim().toLowerCase();
            const parentElement = vehicle.closest('.vehicle-shell-inner-body');
            if (parentElement) {
                if (vehicleNumber !== searchInput && searchInput !== "") {
                    parentElement.style.display = 'none';
                } else {
                    parentElement.style.display = '';
                }
            }
        });
        const activeElements = Array.from(document.querySelectorAll('.vehicle-shell-inner-body')).filter(element => element.style.display !== 'none');
        if (activeElements.length === 0 && searchInput) {
            const fallbackResponse = await ajaxCall(`/logistic/vehicle/fetchByNumber?vehicleNumber=${searchInput}`);
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

    const lastPage = parseInt(localStorage.getItem("TotalPages")) || 1;

    const previousRecordPageBtn = document.getElementById('previous-page-btn');

    previousRecordPageBtn.addEventListener('click',function (){
        let currentPage = parseInt(localStorage.getItem("pageNo")) || 1;
        if(currentPage > 1){
            const pageNo = currentPage-1;
            localStorage.setItem("pageNo",pageNo);
        }
    }, {once: true});

    const nextRecordPageBtn = document.getElementById('next-page-btn');

    nextRecordPageBtn.addEventListener('click',function (){
        let currentPage = parseInt(localStorage.getItem("pageNo")) || 1;
        if(currentPage > 0 && currentPage < lastPage){
            const pageNo = currentPage+1;
            localStorage.setItem("pageNo",pageNo);
        }
    }, {once: true});

}
recordPageEventBinding();