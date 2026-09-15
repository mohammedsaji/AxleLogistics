const params = new URLSearchParams(window.location.search);

function payloadExtractor() {
    // employeeId from URL acts as reportingManagerId for the manager creating this employee
    const isManagerAccount = params.get("isManagerAccount");
    if(isManagerAccount === "false"){
        const reportingManagerId = params.get("managerId");

        if (reportingManagerId) {
            const reportingManagerIdDisplay = document.getElementById('reporting-manager-id-display');
            if (reportingManagerIdDisplay) {
                reportingManagerIdDisplay.style.display = 'block';
            }
            const reportingManagerIdInput = document.getElementById('reporting-manager-id');
            if (reportingManagerIdInput) {
                reportingManagerIdInput.value = reportingManagerId;
            }
        }
    }
    clickEventBinder(isManagerAccount);
}
payloadExtractor();

function valueInitializer(){
     const accountUserRole = params.get("accountUserRole");

    const employeeDepartment = document.getElementById('employee-department');

    if(accountUserRole){
        employeeDepartment.value = accountUserRole;
    }
}
valueInitializer();

function clickEventBinder(isManagerAccount) {

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
            const employeeJoiningDate = document.getElementById('employee-joining-date').value.trim();

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
            } else if (!reportingManagerId && isManagerAccount === "false") {
                alert('Kindly select the manager, because you are currently creating this employee account for non-managerial role.');
                return;
            }else if(employeeJoiningDate === null || employeeJoiningDate === undefined || employeeJoiningDate === ''){
                alert('Joining Date not entered.');
                return;
            }

            const payload = {
                "employeeName": employeeName,
                "employeePhoneNo": employeePhoneNo,
                "employeeDepartment": employeeDepartment,
                "employeeStatus": employeeStatus,
                "reportingManagerId": isManagerAccount === "true" ? null : parseInt(reportingManagerId, 10),
                "employeeJoiningDate":employeeJoiningDate
            };

            createForm.disabled = true;
            try{
                const accountUserName = params.get("accountUserName");
                const url = `/logistic/employee/save?accountUserName=${encodeURIComponent(accountUserName)}`;
                const methodType = 'POST';
                const response = await ajaxCall(url, methodType, payload);

                if (response) {
                    const employeeId = response.data.employeeId;
                    window.location.href = `../../views/employee/employee.html?employeeId=${employeeId}&userAction=Entry employee`;
                }
                createForm.disabled = false;
            }catch(error){
                console.log("Employee save failed:", error);
                createForm.disabled = false;
            }
        });
    }
}