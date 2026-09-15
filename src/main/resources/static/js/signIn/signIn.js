function clickEventBinder() {
    const submitBtn = document.getElementById('submit-btn');
    if (submitBtn) {
        submitBtn.addEventListener('click', async function (event) {
            event.preventDefault(); // stop native form submit — we're doing this via ajaxCall instead

            const username = document.getElementById('username').value.trim();
            const password = document.getElementById('password').value.trim();

            if (username === '' || password === '') {
                alert('Username and password are required.');
                return;
            }

            const response = await ajaxCall(`/logistic/account/signin`, 'POST', {
                username: username,
                password: password
            });

            // ajaxCall already alerts on error and returns null in that case —
            // only redirect when the call actually succeeded.
            if (response) {
                window.location.href = "/views/dashboard.html";
            }
        });
    }

    const forgotPasswordBtn = document.getElementById('forgot-password');
    if(forgotPasswordBtn){
        window.location.href = ``
    }
}
clickEventBinder();