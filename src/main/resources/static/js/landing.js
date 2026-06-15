document.addEventListener('DOMContentLoaded', function (){
    function signingClickEvent(){

        const signInBtn = document.querySelector('.sign-in-btn');

        signInBtn.addEventListener('click',function(){
            localStorage.setItem("viewState",dashboardViewState().DASHBOARD);
            window.location.href = "/views/signIn/sign-in.html";
        },{once : true});

        previousPageNavigation(dashboardViewState().DASHBOARD);

    }
    signingClickEvent();
})

v