function clickEventBinder() {
    const passwordResetForm = document.getElementById('password-reset-form');

    if (passwordResetForm) {
        passwordResetForm.addEventListener('submit', async function (event) {
            event.preventDefault();

            const gmailId = document.getElementById('signed-up-user-gmail-id').value;
            const passwordResetBtn = document.getElementById('password-reset-btn');

            passwordResetBtn.disabled = true;

            try {
                const response = await ajaxCall(
                    '/logistic/account/reset-password?signedUpUserGmailId=' + encodeURIComponent(gmailId),
                    'POST',
                    null
                );

                if (response) {
                    alert(response.message || 'Check OTP mail Inbox, Spam for password reset.');
                }
            } finally {
                passwordResetBtn.disabled = false;
            }
        }, { once: true });
    }
}

clickEventBinder();