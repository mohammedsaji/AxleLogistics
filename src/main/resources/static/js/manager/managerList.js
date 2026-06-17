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
    await fetchManagerList(operatorId, currentPageNo);
}
payloadExtractor();

async function fetchManagerList(operatorId, pageNo) {
    const url = `/logistic/manager/fetchall?operatorId=${operatorId}&pageNo=${pageNo}`;
    const methodType = 'GET';
    const response = await ajaxCall(url, methodType, null);

    currentResponse = response;

    if (response && response.valueMap) {
        totalPages = response.valueMap.TotalPages || 1;
        const managerList = response.valueMap.ManagerList || [];
        renderManagerList(managerList);
    } else if (Array.isArray(response)) {
        renderManagerList(response);
    } else if (response) {
        renderManagerList([response]);
    }
}

function renderManagerList(managerList) {
    const listContainer = document.getElementById('manager-list-container');
    if (listContainer) {
        listContainer.innerHTML = '';
    }

    managerList.forEach(manager => {
        if (!manager) return;

        const managerDiv = document.createElement('div');
        managerDiv.className = 'manager-list-item';
        managerDiv.setAttribute('data-manager-id', manager.managerId);
        managerDiv.setAttribute('data-manager-name', manager.managerName);

        const managerInfoDiv = document.createElement('div');
        managerInfoDiv.className = 'manager-info';

        const managerIdP = document.createElement('p');
        managerIdP.textContent = `Manager ID: ${manager.managerId}`;
        managerInfoDiv.append(managerIdP);

        const managerNameP = document.createElement('p');
        managerNameP.className = 'manager-name-display';
        managerNameP.textContent = `Manager Name: ${manager.managerName}`;
        managerInfoDiv.append(managerNameP);

        const managerStatusP = document.createElement('p');
        managerStatusP.textContent = `Status: ${manager.managerStatus}`;
        managerInfoDiv.append(managerStatusP);

        const viewBtn = document.createElement('button');
        viewBtn.className = 'view-btn';
        viewBtn.setAttribute('data-manager-id', manager.managerId);
        viewBtn.setAttribute('data-operator-id', manager.operatorId);
        viewBtn.textContent = 'View';
        managerInfoDiv.append(viewBtn);

        managerDiv.append(managerInfoDiv);
        listContainer.append(managerDiv);
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

    const createManagerBtn = document.getElementById('create-manager-btn');
    if (createManagerBtn) {
        createManagerBtn.addEventListener('click', function () {
            window.location.href = `../../views/signUp/sign-up.html?userAction=Entry manager&operatorId=${operatorId}`;
        }, {once: true});
    }

    const viewBtnArray = document.querySelectorAll('.view-btn');
    viewBtnArray.forEach(btn => {
        btn.addEventListener('click', function () {
            const managerId = this.getAttribute('data-manager-id');
            const operatorId = this.getAttribute('data-operator-id');
            window.location.href = `../../views/manager/manager.html?managerId=${managerId}&userAction=${userAction}&operatorId=${operatorId}`;
        }, {once: true});
    });

    const previousPageBtn = document.getElementById('previous-page-btn');
    if (previousPageBtn) {
        previousPageBtn.addEventListener('click', async function () {
            if (currentPageNo > 1) {
                currentPageNo--;
                await fetchManagerList(operatorId, currentPageNo);
            }
        }, {once: true});
    }

    const nextPageBtn = document.getElementById('next-page-btn');
    if (nextPageBtn) {
        nextPageBtn.addEventListener('click', async function () {
            if (currentPageNo < totalPages) {
                currentPageNo++;
                await fetchManagerList(operatorId, currentPageNo);
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
            const managerItems = document.querySelectorAll('.manager-list-item');
            let hasMatch = false;

            managerItems.forEach(item => {
                const managerName = item.getAttribute('data-manager-name').toLowerCase();
                if (managerName.includes(searchValue) || searchValue === '') {
                    item.style.display = '';
                    hasMatch = true;
                } else {
                    item.style.display = 'none';
                }
            });

            if (!hasMatch && searchValue !== '') {
                const response = await ajaxCall(`/logistic/manager/fetchByName?managerName=${searchValue}`, 'GET', null);
                if (response) {
                    let managerList = [];

                    // Handle both response formats: valueMap wrapper and direct array
                    if (response && response.valueMap) {
                        managerList = response.valueMap.ManagerList || [];
                    } else if (Array.isArray(response)) {
                        managerList = response;
                    } else if (response) {
                        managerList = [response];
                    }

                    if (managerList.length > 0) {
                        renderManagerList(managerList);
                    }
                }
            }
        }, {once: true});
    }

    if (searchInput) {
        searchInput.addEventListener('input', function () {
            const searchValue = this.value.trim().toLowerCase();

            if (!searchValue) {
                const managerItems = document.querySelectorAll('.manager-list-item');
                managerItems.forEach(item => {
                    item.style.display = '';
                });
            }
        });
    }
}