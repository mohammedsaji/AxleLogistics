const params = new URLSearchParams(window.location.search);
let currentPageNo = 1;
let totalPages = 1;
let currentResponse = null;

async function payloadExtractor() {
    const userAction = params.get("userAction");
    const accountUserName = params.get("accountUserName");

    if (!userAction) {
        alert('Invalid parameters.');
        return;
    }

    currentPageNo = 1;
    await fetchEmployeeList(currentPageNo);
}
payloadExtractor();

async function fetchEmployeeList(pageNo) {
    const url = `/logistic/employee/fetchall?pageNo=${pageNo}`;
    const methodType = 'GET';
    const response = await ajaxCall(url, methodType, null);

    currentResponse = response;

    if (response && response.valueMap) {
        totalPages = response.valueMap.TotalPages || 1;
        const employeeList = response.valueMap.EmployeeList || [];
        renderEmployeeList(employeeList);
    } else if (Array.isArray(response)) {
        renderEmployeeList(response);
    } else if (response) {
        renderEmployeeList([response]);
    }
}

function renderEmployeeList(employeeList) {
    const listContainer = document.getElementById('employee-list-container');
    if (listContainer) {
        listContainer.innerHTML = '';
    }

    employeeList.forEach(employee => {
        if (!employee) return;

        const employeeDiv = document.createElement('div');
        employeeDiv.className = 'employee-list-item';
        employeeDiv.setAttribute('data-employee-id', employee.employeeId);
        employeeDiv.setAttribute('data-employee-name', employee.employeeName);

        const employeeInfoDiv = document.createElement('div');
        employeeInfoDiv.className = 'employee-info';

        const employeeIdP = document.createElement('p');
        employeeIdP.textContent = `Employee ID: ${employee.employeeId}`;
        employeeInfoDiv.append(employeeIdP);

        const employeeNameP = document.createElement('p');
        employeeNameP.className = 'employee-name-display';
        employeeNameP.textContent = `Employee Name: ${employee.employeeName}`;
        employeeInfoDiv.append(employeeNameP);

        const employeeDepartmentP = document.createElement('p');
        employeeDepartmentP.textContent = `Department: ${employee.employeeDepartment}`;
        employeeInfoDiv.append(employeeDepartmentP);

        const viewBtn = document.createElement('button');
        viewBtn.className = 'view-btn';
        viewBtn.setAttribute('data-employee-id', employee.employeeId);
        viewBtn.textContent = 'View';
        employeeInfoDiv.append(viewBtn);

        employeeDiv.append(employeeInfoDiv);
        listContainer.append(employeeDiv);
    });

    clickEventBinder();
    searchClickEvent();
}

function clickEventBinder() {
    const userAction = params.get("userAction");
    const accountUserName = params.get("accountUserName");

    const dashboardBtn = document.getElementById('dashboard-btn');
    if (dashboardBtn) {
        dashboardBtn.addEventListener('click', function () {
            window.location.href = "/views/dashboard.html";
        }, {once: true});
    }

    const createEmployeeBtn = document.getElementById('create-employee-btn');
    if (createEmployeeBtn) {
        createEmployeeBtn.addEventListener('click', function () {
            window.location.href = `../../views/signUp/sign-up.html?userAction=Entry employee`;
        }, {once: true});
    }

    const viewBtnArray = document.querySelectorAll('.view-btn');
    viewBtnArray.forEach(btn => {
        btn.addEventListener('click', function () {
            const employeeId = this.getAttribute('data-employee-id');
            window.location.href = `../../views/employee/employee.html?employeeId=${employeeId}&userAction=${userAction}`;
        }, {once: true});
    });

    const previousPageBtn = document.getElementById('previous-page-btn');
    if (previousPageBtn) {
        previousPageBtn.addEventListener('click', async function () {
            if (currentPageNo > 1) {
                currentPageNo--;
                await fetchEmployeeList(currentPageNo);
            }
        }, {once: true});
    }

    const nextPageBtn = document.getElementById('next-page-btn');
    if (nextPageBtn) {
        nextPageBtn.addEventListener('click', async function () {
            if (currentPageNo < totalPages) {
                currentPageNo++;
                await fetchEmployeeList(currentPageNo);
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
            const employeeItems = document.querySelectorAll('.employee-list-item');
            let hasMatch = false;

            employeeItems.forEach(item => {
                const employeeName = item.getAttribute('data-employee-name').toLowerCase();
                if (employeeName.includes(searchValue) || searchValue === '') {
                    item.style.display = '';
                    hasMatch = true;
                } else {
                    item.style.display = 'none';
                }
            });

            if (!hasMatch && searchValue !== '') {
                const response = await ajaxCall(`/logistic/employee/fetchByName?employeeName=${searchValue}`, 'GET', null);
                if (response) {
                    let employeeList = [];

                    // Handle both response formats: valueMap wrapper and direct array
                    if (response && response.valueMap) {
                        employeeList = response.valueMap.EmployeeList || [];
                    } else if (Array.isArray(response)) {
                        employeeList = response;
                    } else if (response) {
                        employeeList = [response];
                    }

                    if (employeeList.length > 0) {
                        renderEmployeeList(employeeList);
                    }
                }
            }
        }, {once: true});
    }

    if (searchInput) {
        searchInput.addEventListener('input', function () {
            const searchValue = this.value.trim().toLowerCase();

            if (!searchValue) {
                const employeeItems = document.querySelectorAll('.employee-list-item');
                employeeItems.forEach(item => {
                    item.style.display = '';
                });
            }
        });
    }
}