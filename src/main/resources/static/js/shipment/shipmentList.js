const params = new URLSearchParams(window.location.search);
let currentPageNo = 1;
let totalPages = 1;
let currentResponse = null;

async function payloadExtractor() {
    const userAction = params.get("userAction");

    if (!userAction) {
        alert('Invalid parameters.');
        return;
    }

    currentPageNo = 1;
    await fetchShipmentList(currentPageNo);
}
payloadExtractor();

async function fetchShipmentList(pageNo) {
    const url = `/logistic/shipment/fetchall?pageNo=${pageNo}`;
    const methodType = 'GET';
    const response = await ajaxCall(url, methodType, null);

    currentResponse = response;

    if (response && response.valueMap) {
        totalPages = response.valueMap.TotalPages || 1;
        const shipmentList = response.valueMap.ShipmentList || [];
        renderShipmentList(shipmentList);
    } else if (Array.isArray(response)) {
        renderShipmentList(response);
    } else if (response) {
        renderShipmentList([response]);
    }
}

function renderShipmentList(shipmentList) {
    const listContainer = document.getElementById('shipment-list-container');
    if (listContainer) {
        listContainer.innerHTML = '';
    }

    shipmentList.forEach(shipment => {
        if (!shipment) return;

        const shipmentDiv = document.createElement('div');
        shipmentDiv.className = 'shipment-list-item';
        shipmentDiv.setAttribute('data-shipping-id', shipment.shippingId);
        shipmentDiv.setAttribute('data-shipment-from', shipment.shipmentFrom);
        shipmentDiv.setAttribute('data-shipment-to', shipment.shipmentTo);

        const shipmentInfoDiv = document.createElement('div');
        shipmentInfoDiv.className = 'shipment-info';

        const shippingIdP = document.createElement('p');
        shippingIdP.textContent = `Shipping ID: ${shipment.shippingId}`;
        shipmentInfoDiv.append(shippingIdP);

        const shipmentFromP = document.createElement('p');
        shipmentFromP.textContent = `From: ${shipment.shipmentFrom}`;
        shipmentInfoDiv.append(shipmentFromP);

        const shipmentToP = document.createElement('p');
        shipmentToP.textContent = `To: ${shipment.shipmentTo}`;
        shipmentInfoDiv.append(shipmentToP);

        const viewBtn = document.createElement('button');
        viewBtn.className = 'view-btn';
        viewBtn.setAttribute('data-shipping-id', shipment.shippingId);
        viewBtn.textContent = 'View';
        shipmentInfoDiv.append(viewBtn);

        shipmentDiv.append(shipmentInfoDiv);
        listContainer.append(shipmentDiv);
    });

    clickEventBinder();
    searchClickEvent();
}

function clickEventBinder() {
    const userAction = params.get("userAction");

    const dashboardBtn = document.getElementById('dashboard-btn');
    if (dashboardBtn) {
        dashboardBtn.addEventListener('click', function () {
            window.location.href = "/views/dashboard.html";
        }, { once: true });
    }

    const createShipmentBtn = document.getElementById('create-shipment-btn');
    if (createShipmentBtn) {
        createShipmentBtn.addEventListener('click', function () {
            window.location.href = `../../views/operator/transport-types.html?userAction=Entry shipping`;
        }, { once: true });
    }

    const viewBtnArray = document.querySelectorAll('.view-btn');
    viewBtnArray.forEach(btn => {
        btn.addEventListener('click', function () {
            const shippingId = this.getAttribute('data-shipping-id');
            window.location.href = `../../views/shipment/shipment.html?shippingId=${shippingId}&userAction=${userAction}`;
        }, { once: true });
    });

    const previousPageBtn = document.getElementById('previous-page-btn');
    if (previousPageBtn) {
        previousPageBtn.addEventListener('click', async function () {
            if (currentPageNo > 1) {
                currentPageNo--;
                await fetchShipmentList(currentPageNo);
            }
        }, { once: true });
    }

    const nextPageBtn = document.getElementById('next-page-btn');
    if (nextPageBtn) {
        nextPageBtn.addEventListener('click', async function () {
            if (currentPageNo < totalPages) {
                currentPageNo++;
                await fetchShipmentList(currentPageNo);
            }
        }, { once: true });
    }
}

function searchClickEvent() {
    const searchBtn = document.getElementById('search-btn');
    const searchInput = document.getElementById('search');

    if (searchBtn) {
        searchBtn.addEventListener('click', async function () {
            const searchValue = searchInput.value.trim();
            const shipmentItems = document.querySelectorAll('.shipment-list-item');
            let hasMatch = false;

            shipmentItems.forEach(item => {
                const shippingId = item.getAttribute('data-shipping-id');
                if (parseInt(shippingId, 10) === parseInt(searchValue, 10) || searchValue === '') {
                    item.style.display = '';
                    hasMatch = true;
                } else {
                    item.style.display = 'none';
                }
            });

            if (!hasMatch && searchValue !== '') {
                const response = await ajaxCall(`/logistic/shipment/fetchbyid?shippingId=${searchValue}`, 'GET', null);
                if (response) {
                    let shipmentList = [];

                    // Handle both response formats: valueMap wrapper and direct array
                    if (response && response.valueMap) {
                        shipmentList = response.valueMap.ShipmentList || [];
                    } else if (Array.isArray(response)) {
                        shipmentList = response;
                    } else if (response) {
                        shipmentList = [response];
                    }

                    if (shipmentList.length > 0) {
                        renderShipmentList(shipmentList);
                    }
                }
            }
        }, { once: true });
    }

    if (searchInput) {
        searchInput.addEventListener('input', function () {
            const searchValue = this.value.trim();

            if (!searchValue) {
                const shipmentItems = document.querySelectorAll('.shipment-list-item');
                shipmentItems.forEach(item => {
                    item.style.display = '';
                });
            }
        });
    }
}