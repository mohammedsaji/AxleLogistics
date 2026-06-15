document.addEventListener('DOMContentLoaded',function (){

    function LayoutEventBinder(){

        const viewState = localStorage.getItem("viewState");
        if(viewState === dashboardViewState().DASHBOARD){

            previousPageNavigation(viewState);
        }
    }
    LayoutEventBinder();

})

function previousPageNavigation(viewState) {
    const previousFormBtn = document.querySelector('.previous-form-btn');
    if (!previousFormBtn) return;

    // document.referrer = where user came from (free, no setup needed)
    // fallback to history.back() only if referrer is missing
    const previousPage = document.referrer;

    if (!previousPage && window.history.length <= 1) {
        // truly no way back — disable button
        previousFormBtn.disabled = true;
        return;
    }

    previousFormBtn.disabled = false;

    previousFormBtn.addEventListener('click', function () {
        localStorage.setItem("viewState", viewState);

        if (previousPage) {
            window.location.href = previousPage; // reliable path
        } else {
            window.history.back(); // last resort fallback
        }

    }, { once: true });
}