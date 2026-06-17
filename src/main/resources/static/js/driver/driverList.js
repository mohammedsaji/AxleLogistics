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
    await fetchDriverList(operatorId, currentPageNo);
}
payloadExtractor();

async function fetchDriverList(operatorId, pageNo) {
    const url = `/logistic/driver/fetchall?operatorId=${operatorId}&pageNo=${pageNo}`;
    const methodType = 'GET';
    const response = await ajaxCall(url, methodType, null);

    currentResponse = response;

    if (response && response.valueMap) {
        totalPages = response.valueMap.TotalPages || 1;
        const driverList = response.valueMap.DriverList || [];
        renderDriverList(driverList);
    } else if (Array.isArray(response)) {
        renderDriverList(response);
    } else if (response) {
        renderDriverList([response]);
    }
}

function renderDriverList(driverList) {
    const listContainer = document.getElementById('driver-list-container');
    if (listContainer) {
        listContainer.innerHTML = '';
    }

    driverList.forEach(driver => {
        if (!driver) return;

        const driverDiv = document.createElement('div');
        driverDiv.className = 'driver-list-item';
        driverDiv.setAttribute('data-driver-id', driver.driverId);
        driverDiv.setAttribute('data-driver-name', driver.driverName);

        const driverInfoDiv = document.createElement('div');
        driverInfoDiv.className = 'driver-info';

        const driverIdP = document.createElement('p');
        driverIdP.textContent = `Driver ID: ${driver.driverId}`;
        driverInfoDiv.append(driverIdP);

        const driverNameP = document.createElement('p');
        driverNameP.className = 'driver-name-display';
        driverNameP.textContent = `Driver Name: ${driver.driverName}`;
        driverInfoDiv.append(driverNameP);

        const viewBtn = document.createElement('button');
        viewBtn.className = 'view-btn';
        viewBtn.setAttribute('data-driver-id', driver.driverId);
        viewBtn.setAttribute('data-operator-id', driver.operatorId);
        viewBtn.textContent = 'View';
        driverInfoDiv.append(viewBtn);

        driverDiv.append(driverInfoDiv);
        listContainer.append(driverDiv);
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
        }, {once: true});
    }

    const createDriverBtn = document.getElementById('create-driver-btn');
    if (createDriverBtn) {
        createDriverBtn.addEventListener('click', function () {
            window.location.href = `../../views/signUp/sign-up.html?userAction=Entry driver&operatorId=${operatorId}`;
        }, {once: true});
    }

    const viewBtnArray = document.querySelectorAll('.view-btn');
    viewBtnArray.forEach(btn => {
        btn.addEventListener('click', function () {
            const driverId = this.getAttribute('data-driver-id');
            const operatorId = this.getAttribute('data-operator-id');
            window.location.href = `../../views/driver/driver.html?driverId=${driverId}&userAction=${userAction}&operatorId=${operatorId}`;
        }, {once: true});
    });

    const previousPageBtn = document.getElementById('previous-page-btn');
    if (previousPageBtn) {
        previousPageBtn.addEventListener('click', async function () {
            if (currentPageNo > 1) {
                currentPageNo--;
                await fetchDriverList(operatorId, currentPageNo);
            }
        }, {once: true});
    }

    const nextPageBtn = document.getElementById('next-page-btn');
    if (nextPageBtn) {
        nextPageBtn.addEventListener('click', async function () {
            if (currentPageNo < totalPages) {
                currentPageNo++;
                await fetchDriverList(operatorId, currentPageNo);
            }
        }, {once: true});
    }
}

function searchClickEvent() {
    const searchBtn = document.getElementById('search-btn');
    const searchInput = document.getElementById('search');

    if (searchBtn) {
        searchBtn.addEventListener('click', async function () {
            const searchValue = searchInput.value.trim().toLowerCase();
            const driverItems = document.querySelectorAll('.driver-list-item');
            let hasMatch = false;

            driverItems.forEach(item => {
                const driverName = item.getAttribute('data-driver-name').toLowerCase();
                if (driverName.includes(searchValue) || searchValue === '') {
                    item.style.display = '';
                    hasMatch = true;
                } else {
                    item.style.display = 'none';
                }
            });

            if (!hasMatch && searchValue !== '') {
                const response = await ajaxCall(`/logistic/driver/fetchByName?driverName=${searchValue}`, 'GET', null);
                if (response) {
                    let driverList = [];

                    // Handle both response formats: valueMap wrapper and direct array
                    if (response && response.valueMap) {
                        driverList = response.valueMap.DriverList || [];
                    } else if (Array.isArray(response)) {
                        driverList = response;
                    } else if (response) {
                        driverList = [response];
                    }

                    if (driverList.length > 0) {
                        renderDriverList(driverList);
                    }
                }
            }
        }, {once: true});
    }

    if (searchInput) {
        searchInput.addEventListener('input', function () {
            const searchValue = this.value.trim().toLowerCase();

            if (!searchValue) {
                const driverItems = document.querySelectorAll('.driver-list-item');
                driverItems.forEach(item => {
                    item.style.display = '';
                });
            }
        });
    }
}