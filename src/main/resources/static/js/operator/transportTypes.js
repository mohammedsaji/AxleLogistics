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
    const shippingId = params.get("shippingId");

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
                window.location.href = `../../views/operator/operator-list.html?userAction=${userAction}&transportType=${transportType}&shippingId=${shippingId}`;
            } else if(userAction === 'Entry operator') {
                window.location.href = `../../views/operator/operator-creation-form.html?userAction=Entry operator`;
            }else{
                // Read operator, Entry operator, Entry manager, Entry driver, Entry shipping
                const accountUserName = params.get("accountUserName");
                window.location.href = `../../views/operator/operator-list.html?userAction=${userAction}&transportType=${transportType}&accountUserName=${accountUserName}`;
            }
        }, { once: true });
    });
}