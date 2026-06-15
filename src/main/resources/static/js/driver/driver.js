document.addEventListener('DOMContentLoaded', function (event) {
    async function PayloadExtractor() {

        const viewState = localStorage.getItem("viewState");

        const driverStates = driverViewState().DRIVER_PROFILE;
        const operatorStates = operatorViewState().OPERATOR;
        const shipmentStates = shipmentViewState().SHIPMENT;
        const statusStates = statusViewState().SHIPMENT_ASSIGNMENT;

        previousPageNavigation(viewState);

        if (viewState !== driverViewState().DRIVER_PROFILE.CREATE && viewState !== operatorStates.READ) {
            const createDriverBtn = document.querySelector('.create-driver-btn');
            const updateDriverBtn = document.querySelector('.update-driver-btn');
            const deleteDriverBtn = document.querySelector('.delete-driver-btn');

            if (createDriverBtn) {
                createDriverBtn.remove();
            }
            if (updateDriverBtn) {
                updateDriverBtn.remove();
            }
            if (deleteDriverBtn) {
                deleteDriverBtn.remove();
            }
        }

        if (
            viewState === operatorStates.READ ||
            viewState === driverStates.CREATE ||
            viewState === shipmentStates.CREATE ||
            viewState === statusStates.DRIVER_REASSIGN
        ) {
            const driverId = localStorage.getItem("driverId");

            if (driverId) {
                const url = `/logistic/driver/fetch?driverId=${driverId}`;
                const methodType = 'GET';
                const response = await ajaxCall(url, methodType, null);
                LayoutRenderer(driverId, response, viewState);
            }
        }
    }

    PayloadExtractor();
});

function LayoutRenderer(driverId, response, viewState) {
    const driverShellWrapperBody = document.querySelector('.driver-shell-wrapper-body');

    Object.entries(response).forEach(([keyName, keyValue]) => {

        if (keyName === "driverId") {
            const updateDriverBtn = document.querySelector('.update-driver-btn');
            const deleteDriverBtn = document.querySelector('.delete-driver-btn');

            if (updateDriverBtn) updateDriverBtn.setAttribute(`data-${camelToKebabCase(keyName)}`, keyValue);
            if (deleteDriverBtn) deleteDriverBtn.setAttribute(`data-${camelToKebabCase(keyName)}`, keyValue);
        }

        const elementDiv = document.createElement('div');
        elementDiv.className = 'driver-shell-inner-body';

        const elementLabel = document.createElement('label');
        elementLabel.htmlFor = camelToKebabCase(keyName);
        elementLabel.textContent = `${keyName.charAt(0).toUpperCase() + whiteSpacedCamelCase(keyName).slice(1)}`;
        elementDiv.append(elementLabel);

        const elementInput = document.createElement('input');
        elementInput.id = camelToKebabCase(keyName);
        elementInput.value = keyValue;

        if (keyName === "driverName" || keyName === "driverPhoneNo" || keyName === "driverLicenseNo") {
            const isEditableState = (viewState === driverViewState().DRIVER_PROFILE.CREATE ||
                viewState === operatorViewState().OPERATOR.READ);
            elementInput.readOnly = !isEditableState;

        } else {
            elementInput.readOnly = true;
        }
        elementDiv.append(elementInput);

        driverShellWrapperBody.append(elementDiv);
    });

    if (viewState === shipmentViewState().SHIPMENT.CREATE || viewState === statusViewState().SHIPMENT_ASSIGNMENT.DRIVER_REASSIGN) {
        const elementDiv = document.createElement('div');
        elementDiv.className = 'driver-shell-inner-body';

        if (viewState === shipmentViewState().SHIPMENT.CREATE) {
            const viewVehicleBtn = document.createElement('button');
            viewVehicleBtn.className = 'view-vehicle-btn';
            viewVehicleBtn.setAttribute('data-operator-id', response.operatorId);
            viewVehicleBtn.setAttribute('data-driver-id', response.driverId);
            viewVehicleBtn.textContent = "View vehicles";
            elementDiv.append(viewVehicleBtn);
            driverShellWrapperBody.append(elementDiv);
        } else if (viewState === statusViewState().SHIPMENT_ASSIGNMENT.DRIVER_REASSIGN) {
            const reassignDriverBtn = document.createElement('button');
            reassignDriverBtn.className = 'reassign-driver-btn';
            reassignDriverBtn.setAttribute('data-driver-id', response.driverId);
            reassignDriverBtn.textContent = "Reassign driver";
            elementDiv.append(reassignDriverBtn);
            driverShellWrapperBody.append(elementDiv);
        }
    }

    ClickEventBinder(viewState, driverId, response);
}

function ClickEventBinder(viewState, driverId, response) {

    const dashboardBtn = document.querySelector('.dashboard-btn');
    if (dashboardBtn) {
        dashboardBtn.addEventListener('click', function () {
            localStorage.setItem("viewState", dashboardViewState().DASHBOARD);
            window.location.href = "/views/dashboard.html";
        }, {once: true});
    }

    if (viewState === shipmentViewState().SHIPMENT.CREATE) {
        const viewVehicleBtn = document.querySelector('.view-vehicle-btn');
        if (viewState) {
            viewVehicleBtn.addEventListener('click', function () {
                localStorage.setItem("operatorId", this.dataset.operatorId);
                localStorage.setItem("driverId", this.dataset.driverId);
                localStorage.setItem("viewState", viewState);
                window.location.href = "../../views/vehicle/vehicle-list.html";
            }, {once: true});
        }
    }

    if (viewState === statusViewState().SHIPMENT_ASSIGNMENT.DRIVER_REASSIGN) {
        const reassignDriverBtn = document.querySelector('.reassign-driver-btn');
        if (reassignDriverBtn) {
            reassignDriverBtn.addEventListener('click', async function () {
                const url = `/logistic/status/update`;
                const methodType = 'POST';
                const payload = {
                    "shippingStatusId": localStorage.getItem("shippingStatusId"),
                    "driverId": parseInt(this.dataset.driverId, 10)
                };
                const response = await ajaxCall(url, methodType, payload);
                localStorage.setItem("statusId", response.shippingStatusId);
                localStorage.setItem("viewState", statusViewState().SHIPMENT_STATUS.READ);
                window.location.href = "../../views/status/status.html";
            }, {once: true});
        }
    }

    if (viewState === operatorViewState().OPERATOR.READ || viewState === driverViewState().DRIVER_PROFILE.CREATE) {
        const updateDriverBtn = document.querySelector('.update-driver-btn');
        if (updateDriverBtn) {
            updateDriverBtn.addEventListener('click', async function () {
                const payload = {};
                Object.keys(response).forEach((key) => {
                    const kebabCaseKey = camelToKebabCase(key);
                    const domElementInput = document.getElementById(`${kebabCaseKey}`);
                    if (domElementInput) {
                        const elementInputValue = domElementInput.value.trim();

                        if (key === "driverId" || key === "operatorId" || key === "updatedBy") {
                            payload[key] = elementInputValue ? parseInt(elementInputValue, 10) : null;
                        } else {
                            payload[key] = elementInputValue || null;
                        }
                    }
                });
                const fallbackResponse = await ajaxCall(`/logistic/driver/save`, 'POST', payload);
                if (fallbackResponse) {
                    alert(fallbackResponse);
                }
            });
        }

        const deleteDriverBtn = document.querySelector('.delete-driver-btn');
        if (deleteDriverBtn) {
            deleteDriverBtn.addEventListener('click', async function () {
                const url = `/logistic/driver/delete?driverId=${this.dataset.driverId}`;
                const methodType = 'DELETE';
                await ajaxCall(url, methodType, null);
            }, {once: true});
        }
    }
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