function clickEventBinder() {
    const signInBtn = document.getElementById('sign-in-btn');
    if (signInBtn) {
        signInBtn.addEventListener('click', function () {
            window.location.href = "/views/signIn/sign-in.html";
        }, { once: true });
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
        const shippingId = trackingInput.value.trim();
        if (shippingId === '') {
            alert('Please enter a shipment ID.');
            trackingInput.focus();
            return;
        }
        if (!/^\d+$/.test(shippingId)) {
            alert('Please enter a valid shipment ID.');
            trackingInput.focus();
            return;
        }
        const userAction = "Customer shipment tracking";
        window.location.href = `/views/shipment/shipment.html?shippingId=${encodeURIComponent(shippingId)}&userAction=${userAction}`;
    }, { once: true });
}

customerShipmentTrackingBinder();