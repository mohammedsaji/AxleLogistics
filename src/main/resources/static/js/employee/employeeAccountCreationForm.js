const params = new URLSearchParams(window.location.search);

function payloadExtractor() {
    // employeeId from URL acts as reportingManagerId for the manager creating this employee
    const reportingManagerId = params.get("employeeId");
    const accountUserName = params.get("accountUserName");

    if (reportingManagerId) {
        const reportingManagerIdDisplay = document.getElementById('reporting-manager-id-display');
        if (reportingManagerIdDisplay) {
            reportingManagerIdDisplay.style.display = 'block';
        }
        const reportingManagerIdInput = document.getElementById('reporting-manager-id');
        if (reportingManagerIdInput) {
            reportingManagerIdInput.value = reportingManagerId;
        }
    } else {
        window.location.href = `../../views/employee/employee-list.html?accountUserName=${accountUserName}`;
    }
}
payloadExtractor();

function clickEventBinder() {

    const dashboardBtn = document.getElementById('dashboard-btn');
    if (dashboardBtn) {
        dashboardBtn.addEventListener('click', function () {
            window.location.href = "/views/dashboard.html";
        }, {once: true});
    }

    const createForm = document.querySelector('form');
    if (createForm) {
        createForm.addEventListener('submit', async function (event) {
            event.preventDefault();

            const employeeName = document.getElementById('employee-name').value.trim();
            const employeePhoneNo = document.getElementById('employee-phone-no').value.trim();
            const employeeDepartment = document.getElementById('employee-department').value.trim();
            const employeeStatus = document.getElementById('employee-status').value.trim();
            const reportingManagerIdInput = document.getElementById('reporting-manager-id');
            const reportingManagerId = reportingManagerIdInput ? reportingManagerIdInput.value.trim() : null;

            if (employeeName === '') {
                alert('Employee Name not entered.');
                return;
            } else if (employeePhoneNo === '') {
                alert('Employee Phone No not entered.');
                return;
            } else if (employeeDepartment === '') {
                alert('Employee Department not entered.');
                return;
            } else if (employeeStatus === '') {
                alert('Employee Status not entered.');
                return;
            } else if (!reportingManagerId) {
                alert('Kindly select the manager');
                return;
            }

            const payload = {
                "employeeName": employeeName,
                "employeePhoneNo": employeePhoneNo,
                "employeeDepartment": employeeDepartment,
                "employeeStatus": employeeStatus,
                "reportingManagerId": parseInt(reportingManagerId, 10),
                "accountUserName": params.get("accountUserName")
            };

            const url = `/logistic/employee/save`;
            const methodType = 'POST';
            const response = await ajaxCall(url, methodType, payload);

            if (response) {
                const employeeId = response.employeeId;
                window.location.href = `../../views/employee/employee.html?employeeId=${employeeId}&userAction=Entry employee`;
            }
        });
    }
}
clickEventBinder();