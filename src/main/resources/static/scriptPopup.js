
/*=============
popup handling
==============*/

document.addEventListener('DOMContentLoaded', () => {

  fillUserData();

  const LoginPopup = document.getElementById('login-popup');
  const SignupPopup = document.getElementById('signup-popup');
  const AddItempopup = document.getElementById('add-item-popup');

  const MainPageLoginButton = document.getElementById('main-page-login-button');
  const MainPagesignupButton = document.getElementById('main-page-signup-button');
  const MainPageAddProductButton = document.getElementById('main-page-add-product-button');

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
  document.getElementById('login-error-text-field').innerText = "";

  document.getElementById('signup-popup').style.display = 'none';
  document.getElementById('signup-error-text-field').innerText = "";

  document.getElementById('add-item-popup').style.display = 'none';
  document.getElementById('add-item-error-text-field').innerText = "";
}

function fillUserData(){ //TODO going to be deprecated real fast
  document.getElementById('current_user_id').innerText = localStorage.getItem('id');
  document.getElementById('current_user_username').innerText = localStorage.getItem('username');
  document.getElementById('current_user_role').innerText = localStorage.getItem('roles');
  document.getElementById('current_user_ballance').innerText = localStorage.getItem('balance');
}

