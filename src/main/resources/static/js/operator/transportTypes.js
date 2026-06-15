document.addEventListener('DOMContentLoaded', function () {

    async function PayloadExtractor() {

        const viewState = localStorage.getItem("viewState");

        previousPageNavigation(viewState);

        if( viewState === operatorViewState().OPERATOR.CREATE ||
            viewState === operatorViewState().OPERATOR.READ ||
            viewState === managerViewState().MANAGER_PROFILE.CREATE ||
            viewState === driverViewState().DRIVER_PROFILE.CREATE ||
            viewState === shipmentViewState().SHIPMENT.CREATE ||
            viewState === statusViewState().SHIPMENT_ASSIGNMENT.OPERATOR_REASSIGN
        ){

            const url = '/logistic/operator/plans';
            const response = await ajaxCall(url, 'GET', null);
            LayoutRenderer(response, viewState);
        }
    }
    PayloadExtractor();
});

function LayoutRenderer(response, viewState) {

    const transportTypeShellWrapperBody = document.querySelector('.transport-type-shell-wrapper-body');

    const transportTypes = response.carrierOptionEnumList;

    transportTypes.forEach(current => {
        const elementDiv = document.createElement('div');
        elementDiv.className = `transport-type-shell-inner-body`;

        const elementBtn = document.createElement('button');
        elementBtn.className = `transport-type`;
        elementBtn.setAttribute('data-operator-type', current);
        elementBtn.textContent = current;

        elementDiv.append(elementBtn);
        transportTypeShellWrapperBody.append(elementDiv);
    });

    ClickEventBinder(viewState);
}

function ClickEventBinder(viewState) {
    const transportType = document.querySelectorAll('.transport-type');
    transportType.forEach(current => {

        current.addEventListener('click', async function () {
            localStorage.setItem("transportType", this.dataset.operatorType);
            localStorage.setItem("viewState", viewState);
            window.location.href = "../../views/operator/operator-list.html";
        },{once : true});
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