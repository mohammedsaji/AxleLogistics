function clickEventBinder() {
    const signInBtn = document.getElementById('sign-in-btn');
    if (signInBtn) {
        signInBtn.addEventListener('click', function () {
            window.location.href = "/views/signIn/sign-in.html";
        }, { once: true });
    }
}
clickEventBinder();