function clickEventBinder() {
    const passwordResetForm = document.getElementById('password-reset-form');

    if (passwordResetForm) {
        passwordResetForm.addEventListener('submit', async function (event) {
            event.preventDefault();

            const urlParams = new URLSearchParams(window.location.search);
            const emailId = urlParams.get('emailId');

            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirm-password').value;
            const passwordResetBtn = document.getElementById('password-reset-btn');

            if (!emailId) {
                alert('Invalid password reset link.');
                return;
            }

            if (password !== confirmPassword) {
                alert('Password and confirm password do not match.');
                return;
            }

            passwordResetBtn.disabled = true;

            try {
                const response = await ajaxCall('/logistic/account/password/reset?emailId=' + encodeURIComponent(emailId) + '&password=' + encodeURIComponent(password) + '&confirmPassword=' + encodeURIComponent(confirmPassword), 'PATCH', null);

                if (response) {
                    alert(response.message || 'Password updated successfully.');
                    passwordResetForm.reset();
                }
            } finally {
                passwordResetBtn.disabled = false;
            }
        });
    }
}

clickEventBinder();
