const params = new URLSearchParams(window.location.search);

function payloadExtractor() {
    const userAction = params.get("userAction");

    if (!userAction) {
        alert('Invalid parameters.');
        return;
    }

    clickEventBinder();
}
payloadExtractor();

function clickEventBinder() {
    const userAction = params.get("userAction");
    const shippingStatusId = params.get("shippingStatusId");

    const dashboardBtn = document.getElementById('dashboard-btn');
    if (dashboardBtn) {
        dashboardBtn.addEventListener('click', function () {
            window.location.href = "/views/dashboard.html";
        }, { once: true });
    }

    const transportTypeBtns = document.querySelectorAll('[data-transport-type]');
    transportTypeBtns.forEach(btn => {
        btn.addEventListener('click', function () {
            const transportType = this.getAttribute('data-transport-type');

            if (userAction === 'Reassign operator') {
                window.location.href = `../../views/operator/operator-list.html?userAction=${userAction}&transportType=${transportType}&shippingStatusId=${shippingStatusId}`;
            } else {
                // Read operator, Entry operator, Entry manager, Entry driver, Entry shipping
                window.location.href = `../../views/operator/operator-list.html?userAction=${userAction}&transportType=${transportType}`;
            }
        }, { once: true });
    });
}