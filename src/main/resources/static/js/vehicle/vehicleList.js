const params = new URLSearchParams(window.location.search);
let currentPageNo = 1;
let totalPages = 1;
let currentResponse = null;

async function payloadExtractor() {
    const userAction = params.get("userAction");
    const operatorId = params.get("operatorId");

    if (!userAction || !operatorId) {
        alert('Invalid parameters.');
        return;
    }

    currentPageNo = 1;
    await fetchVehicleList(operatorId, currentPageNo);
}
payloadExtractor();

async function fetchVehicleList(operatorId, pageNo) {
    const url = `/logistic/vehicle/fetchall?operatorId=${operatorId}&pageNo=${pageNo}`;
    const methodType = 'GET';
    const response = await ajaxCall(url, methodType, null);

    currentResponse = response;

    if (response && response.valueMap) {
        totalPages = response.valueMap.TotalPages || 1;
        const vehicleList = response.valueMap.VehicleList || [];
        renderVehicleList(vehicleList);
    } else if (Array.isArray(response)) {
        renderVehicleList(response);
    } else if (response) {
        renderVehicleList([response]);
    }
}

function renderVehicleList(vehicleList) {
    const userAction = params.get("userAction");
    const driverId = params.get("driverId");

    const listContainer = document.getElementById('vehicle-list-container');
    if (listContainer) {
        listContainer.innerHTML = '';
    }

    vehicleList.forEach(vehicle => {
        if (!vehicle) return;

        const vehicleDiv = document.createElement('div');
        vehicleDiv.className = 'vehicle-list-item';
        vehicleDiv.setAttribute('data-vehicle-id', vehicle.vehicleId);
        vehicleDiv.setAttribute('data-vehicle-number', vehicle.vehicleNumber);
        vehicleDiv.setAttribute('data-operator-id', vehicle.operatorId);

        const vehicleInfoDiv = document.createElement('div');
        vehicleInfoDiv.className = 'vehicle-info';

        const vehicleIdP = document.createElement('p');
        vehicleIdP.textContent = `Vehicle ID: ${vehicle.vehicleId}`;
        vehicleInfoDiv.append(vehicleIdP);

        const vehicleNumberP = document.createElement('p');
        vehicleNumberP.textContent = `Vehicle Number: ${vehicle.vehicleNumber}`;
        vehicleInfoDiv.append(vehicleNumberP);

        const operatorIdP = document.createElement('p');
        operatorIdP.textContent = `Operator ID: ${vehicle.operatorId}`;
        vehicleInfoDiv.append(operatorIdP);

        const viewBtn = document.createElement('button');
        viewBtn.className = 'view-btn';
        viewBtn.setAttribute('data-vehicle-id', vehicle.vehicleId);
        viewBtn.setAttribute('data-operator-id', vehicle.operatorId);
        // carry driverId forward for Entry shipping flow
        if (userAction === 'Entry shipping' && driverId) {
            viewBtn.setAttribute('data-driver-id', driverId);
        }
        viewBtn.textContent = 'View';
        vehicleInfoDiv.append(viewBtn);

        vehicleDiv.append(vehicleInfoDiv);
        listContainer.append(vehicleDiv);
    });

    clickEventBinder();
    searchClickEvent();
}

function clickEventBinder() {
    const userAction = params.get("userAction");
    const operatorId = params.get("operatorId");

    const dashboardBtn = document.getElementById('dashboard-btn');
    if (dashboardBtn) {
        dashboardBtn.addEventListener('click', function () {
            window.location.href = "/views/dashboard.html";
        }, { once: true });
    }

    const createVehicleBtn = document.getElementById('create-vehicle-btn');
    if (createVehicleBtn) {
        createVehicleBtn.addEventListener('click', function () {
            window.location.href = `../../views/vehicle/vehicle-creation-form.html?userAction=Entry operator&operatorId=${operatorId}`;
        }, { once: true });
    }

    const viewBtnArray = document.querySelectorAll('.view-btn');
    viewBtnArray.forEach(btn => {
        btn.addEventListener('click', function () {
            const vehicleId = this.getAttribute('data-vehicle-id');
            const operatorId = this.getAttribute('data-operator-id');

            if (userAction === 'Entry shipping') {
                const driverId = this.getAttribute('data-driver-id');
                window.location.href = `../../views/vehicle/vehicle.html?vehicleId=${vehicleId}&userAction=${userAction}&operatorId=${operatorId}&driverId=${driverId}`;
            } else if (userAction === 'Reassign vehicle') {
                const shippingStatusId = params.get("shippingStatusId");
                window.location.href = `../../views/vehicle/vehicle.html?vehicleId=${vehicleId}&userAction=${userAction}&operatorId=${operatorId}&shippingStatusId=${shippingStatusId}`;
            } else {
                // Read operator
                window.location.href = `../../views/vehicle/vehicle.html?vehicleId=${vehicleId}&userAction=${userAction}&operatorId=${operatorId}`;
            }
        }, { once: true });
    });

    const previousPageBtn = document.getElementById('previous-page-btn');
    if (previousPageBtn) {
        previousPageBtn.addEventListener('click', async function () {
            if (currentPageNo > 1) {
                currentPageNo--;
                await fetchVehicleList(operatorId, currentPageNo);
            }
        }, { once: true });
    }

    const nextPageBtn = document.getElementById('next-page-btn');
    if (nextPageBtn) {
        nextPageBtn.addEventListener('click', async function () {
            if (currentPageNo < totalPages) {
                currentPageNo++;
                await fetchVehicleList(operatorId, currentPageNo);
            }
        }, { once: true });
    }
}

function searchClickEvent() {
    const searchBtn = document.getElementById('search-btn');
    const searchInput = document.getElementById('search');

    if (searchBtn) {
        searchBtn.addEventListener('click', async function () {
            const searchValue = searchInput.value.trim().toLowerCase();
            const vehicleItems = document.querySelectorAll('.vehicle-list-item');
            let hasMatch = false;

            vehicleItems.forEach(item => {
                const vehicleNumber = item.getAttribute('data-vehicle-number').toLowerCase();
                if (vehicleNumber.includes(searchValue) || searchValue === '') {
                    item.style.display = '';
                    hasMatch = true;
                } else {
                    item.style.display = 'none';
                }
            });

            if (!hasMatch && searchValue !== '') {
                const response = await ajaxCall(`/logistic/vehicle/fetchByNumber?vehicleNumber=${searchValue}`, 'GET', null);
                if (response) {
                    let vehicleList = [];

                    // Handle both response formats: valueMap wrapper and direct array
                    if (response && response.valueMap) {
                        vehicleList = response.valueMap.VehicleList || [];
                    } else if (Array.isArray(response)) {
                        vehicleList = response;
                    } else if (response) {
                        vehicleList = [response];
                    }

                    if (vehicleList.length > 0) {
                        renderVehicleList(vehicleList);
                    }
                }
            }
        }, { once: true });
    }

    if (searchInput) {
        searchInput.addEventListener('input', function () {
            const searchValue = this.value.trim();

            if (!searchValue) {
                const vehicleItems = document.querySelectorAll('.vehicle-list-item');
                vehicleItems.forEach(item => {
                    item.style.display = '';
                });
            }
        });
    }
}