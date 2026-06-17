const params = new URLSearchParams(window.location.search);
let currentPageNo = 1;
let totalPages = 1;
let currentResponse = null;

async function payloadExtractor() {
    const userAction = params.get("userAction");
    const transportType = params.get("transportType");

    if (!userAction || !transportType) {
        alert('Invalid parameters.');
        return;
    }

    // Remove create-operator-btn if not in Read operator flow
    if (userAction !== 'Read operator') {
        const createOperatorBtn = document.getElementById('create-operator-btn');
        if (createOperatorBtn) {
            createOperatorBtn.remove();
        }
    }

    currentPageNo = 1;
    await fetchOperatorList(transportType, currentPageNo);
}
payloadExtractor();

async function fetchOperatorList(transportType, pageNo) {
    const url = `/logistic/operator/fetchall?operatorTransportType=${transportType}&pageNo=${pageNo}`;
    const methodType = 'GET';
    const response = await ajaxCall(url, methodType, null);

    currentResponse = response;

    if (response && response.valueMap) {
        totalPages = response.valueMap.TotalPages || 1;
        const operatorList = response.valueMap.OperatorList || [];
        renderOperatorList(operatorList);
    } else if (Array.isArray(response)) {
        renderOperatorList(response);
    } else if (response) {
        renderOperatorList([response]);
    }
}

function renderOperatorList(operatorList) {
    const listContainer = document.getElementById('operator-list-container');
    if (listContainer) {
        listContainer.innerHTML = '';
    }

    operatorList.forEach(operator => {
        if (!operator) return;

        const operatorDiv = document.createElement('div');
        operatorDiv.className = 'operator-list-item';
        operatorDiv.setAttribute('data-operator-id', operator.operatorId);
        operatorDiv.setAttribute('data-operator-name', operator.operatorName);

        const operatorInfoDiv = document.createElement('div');
        operatorInfoDiv.className = 'operator-info';

        const operatorIdP = document.createElement('p');
        operatorIdP.textContent = `Operator ID: ${operator.operatorId}`;
        operatorInfoDiv.append(operatorIdP);

        const operatorNameP = document.createElement('p');
        operatorNameP.textContent = `Operator Name: ${operator.operatorName}`;
        operatorInfoDiv.append(operatorNameP);

        const operatorTransportTypeP = document.createElement('p');
        operatorTransportTypeP.textContent = `Transport Type: ${operator.operatorTransportType}`;
        operatorInfoDiv.append(operatorTransportTypeP);

        const viewBtn = document.createElement('button');
        viewBtn.className = 'view-btn';
        viewBtn.setAttribute('data-operator-id', operator.operatorId);
        viewBtn.textContent = 'View';
        operatorInfoDiv.append(viewBtn);

        operatorDiv.append(operatorInfoDiv);
        listContainer.append(operatorDiv);
    });

    clickEventBinder();
    searchClickEvent();
}

function clickEventBinder() {
    const userAction = params.get("userAction");
    const transportType = params.get("transportType");

    const dashboardBtn = document.getElementById('dashboard-btn');
    if (dashboardBtn) {
        dashboardBtn.addEventListener('click', function () {
            window.location.href = "/views/dashboard.html";
        }, { once: true });
    }

    const createOperatorBtn = document.getElementById('create-operator-btn');
    if (createOperatorBtn) {
        createOperatorBtn.addEventListener('click', function () {
            window.location.href = `../../views/operator/operator-creation-form.html?userAction=Entry operator`;
        }, { once: true });
    }

    const viewBtnArray = document.querySelectorAll('.view-btn');
    viewBtnArray.forEach(btn => {
        btn.addEventListener('click', function () {
            const operatorId = this.getAttribute('data-operator-id');

            if (userAction === 'Reassign operator') {
                const shippingStatusId = params.get("shippingStatusId");
                window.location.href = `../../views/operator/operator.html?operatorId=${operatorId}&userAction=${userAction}&shippingStatusId=${shippingStatusId}`;
            } else {
                // Read operator, Entry shipping
                window.location.href = `../../views/operator/operator.html?operatorId=${operatorId}&userAction=${userAction}`;
            }
        }, { once: true });
    });

    const previousPageBtn = document.getElementById('previous-page-btn');
    if (previousPageBtn) {
        previousPageBtn.addEventListener('click', async function () {
            if (currentPageNo > 1) {
                currentPageNo--;
                await fetchOperatorList(transportType, currentPageNo);
            }
        }, { once: true });
    }

    const nextPageBtn = document.getElementById('next-page-btn');
    if (nextPageBtn) {
        nextPageBtn.addEventListener('click', async function () {
            if (currentPageNo < totalPages) {
                currentPageNo++;
                await fetchOperatorList(transportType, currentPageNo);
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
            const operatorItems = document.querySelectorAll('.operator-list-item');
            let hasMatch = false;

            operatorItems.forEach(item => {
                const operatorName = item.getAttribute('data-operator-name').toLowerCase();
                if (operatorName.includes(searchValue) || searchValue === '') {
                    item.style.display = '';
                    hasMatch = true;
                } else {
                    item.style.display = 'none';
                }
            });

            if (!hasMatch && searchValue !== '') {
                const response = await ajaxCall(`/logistic/operator/fetchByName?operatorName=${searchValue}`, 'GET', null);
                if (response) {
                    let operatorList = [];

                    // Handle both response formats: valueMap wrapper and direct array
                    if (response && response.valueMap) {
                        operatorList = response.valueMap.OperatorList || [];
                    } else if (Array.isArray(response)) {
                        operatorList = response;
                    } else if (response) {
                        operatorList = [response];
                    }

                    if (operatorList.length > 0) {
                        renderOperatorList(operatorList);
                    }
                }
            }
        }, { once: true });
    }

    if (searchInput) {
        searchInput.addEventListener('input', function () {
            const searchValue = this.value.trim();

            if (!searchValue) {
                const operatorItems = document.querySelectorAll('.operator-list-item');
                operatorItems.forEach(item => {
                    item.style.display = '';
                });
            }
        });
    }
}