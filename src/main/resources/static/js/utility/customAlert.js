function showAlert(message) {
    const customAlert = document.getElementById('custom-alert');
    const customAlertMessage = document.getElementById('custom-alert-message');
    const customAlertClose = document.getElementById('custom-alert-close');

    if (!customAlert || !customAlertMessage || !customAlertClose) {
        return;
    }

    customAlertMessage.textContent = message;
    customAlert.style.display = 'flex';

    customAlertClose.onclick = function () {
        customAlert.style.display = 'none';
    };
}

window.alert = function (message) {
    showAlert(message);
};

