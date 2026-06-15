document.addEventListener('DOMContentLoaded',function (){
    function ClickEventBinder(){

        const viewState  = localStorage.getItem("viewState");

        previousPageNavigation(viewState);

        if(viewState === employeeViewState().EMPLOYEE_ACCOUNT.CREATE){

            const employeeManagerId = localStorage.getItem("employeeManagerId");

            if(employeeManagerId != null) {

                const employeeShellGroupBody = document.getElementById('employee-shell-group-body');

                employeeShellGroupBody.addEventListener('submit', async function (event) {

                    event.preventDefault()

                    const name = document.getElementById('employee-name').value.trim();
                    const phoneNo = document.getElementById('employee-phone-no').value.trim();
                    const department = document.getElementById('employee-department').value.trim();
                    const status = document.getElementById('employee-status').value.trim();

                    if (name !== "" && phoneNo !== "" && department !== "" && status !== "" && employeeManagerId) {

                        const payload = {
                            "employeeName": name,
                            "employeePhoneNo": phoneNo,
                            "employeeDepartment": department,
                            "employeeStatus": status,
                            "reportingManagerId":employeeManagerId,
                            "accountUserName": localStorage.getItem("accountCreationUsername")
                        };

                        const url = `/logistic/employee/save`;
                        const methodType = 'POST';

                        const response = await ajaxCall(url, methodType, payload);

                        if (response.ok) {
                            localStorage.removeItem("employeeManagerId");
                            localStorage.removeItem("accountCreationUsername");
                            window.location.href = "/views/landing.html";
                        }

                    } else {
                        alert("Employee details are partially entered, kindly enter to continue.");
                    }
                });
            }else{
                alert("Kindly select the manager");
            }
        }
    }
    ClickEventBinder();
});


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