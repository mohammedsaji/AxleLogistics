function clickEventBinder() {
    const signInBtn = document.getElementById('sign-in-btn');
    if (signInBtn) {
        signInBtn.addEventListener('click', function () {
            window.location.href = "/views/signIn/sign-in.html";
        }, {once: true});
    }
}

clickEventBinder();

function customerShipmentTrackingBinder() {
    const trackingInput =
        document.getElementById('shipment-tracking-id');
    const trackingBtn =
        document.getElementById('shipment-tracking-btn');
    if (!trackingInput || !trackingBtn) {
        return;
    }
    trackingBtn.addEventListener('click', function () {
        const trackingId = trackingInput.value.trim().toUpperCase();
        if (trackingId === '') {
            alert('Please enter a shipment tracking ID.');
            trackingInput.focus();
            return;
        }
        if (!/^PLSTRCK\d{8}$/.test(trackingId)) {
            alert('Please enter a valid shipment tracking ID.');
            trackingInput.focus();
            return;
        }
        const userAction = "Customer shipment tracking";
        window.location.href = `/views/shipment/shipment.html?trackingId=${encodeURIComponent(trackingId)}&userAction=${userAction}`;
    }, {once: true});
}

customerShipmentTrackingBinder();