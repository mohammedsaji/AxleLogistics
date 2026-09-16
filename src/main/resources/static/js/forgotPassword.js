function clickEventBinder() {
    const passwordResetForm = document.getElementById('password-forgot-form');

    if (passwordResetForm) {
        passwordResetForm.addEventListener('submit', async function (event) {
            event.preventDefault();

            const gmailId = document.getElementById('signed-up-user-email-id').value;
            const passwordResetBtn = document.getElementById('password-forgot-btn');

            passwordResetBtn.disabled = true;

            try {
                const response = await ajaxCall(
                    '/logistic/account/password/forgot?emailId=' + encodeURIComponent(gmailId),
                    'POST',
                    null
                );

                if (response) {
                    alert(response.message || 'Check your email Inbox or Spam folder for the password reset.');
                }
            } finally {
                passwordResetBtn.disabled = false;
            }
        });
    }
}

clickEventBinder();