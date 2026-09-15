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
    await fetchEmployeeList(currentPageNo);
}
payloadExtractor();

async function fetchEmployeeList(pageNo) {
    const url = `/logistic/employee/fetchall?pageNo=${pageNo}`;
    const methodType = 'GET';
    const response = await ajaxCall(url, methodType, null);

    currentResponse = response;

    if (response && response.data) {
        const employeeList = Array.isArray(response.data.employeeList) ? response.data.employeeList : [response.data.employeeList];
        totalPages = response.data.totalPages;
        renderEmployeeList(employeeList);
    }
}

function renderEmployeeList(employeeList) {
    const userAction = params.get("userAction");

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
        if(userAction === 'Entry employee'){
            viewBtn.className = 'select-as-manager-btn';
            viewBtn.textContent = 'Select as Manager';

            viewBtn.addEventListener('click', function () {
                const employeeId = this.getAttribute('data-employee-id');
                const isManagerAccount = false;
                const accountUserRole = params.get("accountUserRole");
                const accountUserName = params.get("accountUserName");
                window.location.href = `../../views/employee/employee-account-creation-form.html?managerId=${employeeId}&userAction=${userAction}&isManagerAccount=${isManagerAccount}&accountUserRole=${accountUserRole}&accountUserName=${accountUserName}&managerId=${employeeId}`;
            }, {once: true});

        }else{
            viewBtn.className = 'view-btn';
            viewBtn.textContent = 'View';

            viewBtn.addEventListener('click', function () {
                const employeeId = this.getAttribute('data-employee-id');
                window.location.href = `../../views/employee/employee.html?employeeId=${employeeId}&userAction=${userAction}`;
            }, {once: true});
        }
        viewBtn.setAttribute('data-employee-id', employee.employeeId);
        employeeInfoDiv.append(viewBtn);

        employeeDiv.append(employeeInfoDiv);
        listContainer.append(employeeDiv);
    });
}

function clickEventBinder() {
    const userAction = params.get("userAction");

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

    const previousPageBtn = document.getElementById('previous-page-btn');
    if (previousPageBtn) {
        previousPageBtn.addEventListener('click', async function () {
            if (currentPageNo > 1) {
                currentPageNo--;
                await fetchEmployeeList(currentPageNo);
            }
        });
    }

    const nextPageBtn = document.getElementById('next-page-btn');
    if (nextPageBtn) {
        nextPageBtn.addEventListener('click', async function () {
            if (currentPageNo < totalPages) {
                currentPageNo++;
                await fetchEmployeeList(currentPageNo);
            }
        });
    }
}
clickEventBinder();

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

                    if (response && response.data) {
                        employeeList = [response.data];
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
searchClickEvent();

function employeeNavigationBinder() {
    const employeeNavigationBtn = document.getElementById('employee-navigation-btn');

    if (employeeNavigationBtn) {
        employeeNavigationBtn.addEventListener('click', function () {
            toggleEmployeeNavigationMenu();
        });
    }
}

employeeNavigationBinder();

function toggleEmployeeNavigationMenu() {
    const employeeNavigationMenu = document.getElementById('employee-navigation-menu');

    if (!employeeNavigationMenu) {
        return;
    }

    employeeNavigationMenu.classList.toggle('active');
}