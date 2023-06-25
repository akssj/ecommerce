
/*=============
popup handling
==============*/

document.addEventListener('DOMContentLoaded', () => {

  fillUserData();

  const LoginPopup = document.getElementById('login-popup');
  const SignupPopup = document.getElementById('signup-popup');
  const AddItempopup = document.getElementById('add-item-popup');

  const MainPageLoginButton = document.getElementById('mainPageLoginButton');
  const MainPagesignupButton = document.getElementById('mainPageSignupButton');
  const MainPageAddProductButton = document.getElementById('mainPageAddProductButton');

  MainPageLoginButton.addEventListener('click', () => {
    closePopups();
    LoginPopup.style.display = 'block';
  });

  MainPagesignupButton.addEventListener('click', () => {
    closePopups();
    SignupPopup.style.display = 'block';
  });

  MainPageAddProductButton.addEventListener('click', () => {
    closePopups();
    AddItempopup.style.display = 'block';
  });

  LoginPopup.style.display = 'none';
  SignupPopup.style.display = 'none';
  AddItempopup.style.display = 'none';
});

function closePopups(){ //TODO make it more universal ie by button class or smht
  document.getElementById('login-popup').style.display = 'none';
  document.getElementById('loginErrorTextField').innerText = "";

  document.getElementById('signup-popup').style.display = 'none';
  document.getElementById('signupErrorTextField').innerText = "";

  document.getElementById('add-item-popup').style.display = 'none';
  document.getElementById('addItemErrorTextField').style.display = 'none';
}

function fillUserData(){ //TODO going to be deprecated real fast
  document.getElementById('current_user_id').innerText = localStorage.getItem('id');
  document.getElementById('current_user_username').innerText = localStorage.getItem('username');
  document.getElementById('current_user_role').innerText = localStorage.getItem('roles');
  document.getElementById('current_user_ballance').innerText = localStorage.getItem('balance');
}

