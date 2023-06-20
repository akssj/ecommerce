
  /*=============
  popup handling
  ==============*/
  document.addEventListener('DOMContentLoaded', () => {

  fillUserData();

  //popup body
  const loginPopup = document.getElementById('login-popup');
  const signupPopup = document.getElementById('signup-popup');
  const additempopup = document.getElementById('add-item-popup');

  //main site buttons
  const mainPageLoginButton = document.getElementById('mainPageLoginButton');
  const mainPagesignupButton = document.getElementById('mainPageSignupButton');
  const mainPageAddProductButton = document.getElementById('mainPageAddProductButton');

  //popupbuttons
  const popupLoginCloseButton = document.getElementById('popupLoginCloseButton');
  const popupSignupCloseButton = document.getElementById('popupSignupCloseButton');
  const popupAddItemCloseButton = document.getElementById('popupAddItemCloseButton');

  //error text fields
  const loginErrorTextField = document.getElementById('loginErrorTextField');
  const signupErrorTextField = document.getElementById('signupErrorTextField');
  const addItemErrorTextField = document.getElementById('addItemErrorTextField');
    

  //display popups
  mainPageLoginButton.addEventListener('click', () => {
    closePopups();
    loginPopup.style.display = 'block';
  });

  mainPagesignupButton.addEventListener('click', () => {
    closePopups();
    signupPopup.style.display = 'block';
  });

  mainPageAddProductButton.addEventListener('click', () => {
    closePopups();
    additempopup.style.display = 'block';
  });


  //close buttons
  popupLoginCloseButton.addEventListener('click', () => {
    loginErrorTextField.innerText = "";
    loginPopup.style.display = 'none';
  });

  popupSignupCloseButton.addEventListener('click', () => {
    signupErrorTextField.innerText = "";
    signupPopup.style.display = 'none';
  });

  popupAddItemCloseButton.addEventListener('click', () => {
    addItemErrorTextField.innerText = "";
    additempopup.style.display = 'none';
  });

  //end
  loginPopup.style.display = 'none';
  signupPopup.style.display = 'none';
  additempopup.style.display = 'none';
});

function closePopups(){

  document.getElementById('login-popup').style.display = 'none';
  document.getElementById('loginErrorTextField').innerText = "";

  document.getElementById('signup-popup').style.display = 'none';
  document.getElementById('signupErrorTextField').innerText = "";

  document.getElementById('add-item-popup').style.display = 'none';
  document.getElementById('addItemErrorTextField').style.display = 'none';

}

function fillUserData(){
  document.getElementById('current_user_id').innerText = localStorage.getItem('id');
  document.getElementById('current_user_username').innerText = localStorage.getItem('username');
  document.getElementById('current_user_role').innerText = localStorage.getItem('roles');
  document.getElementById('current_user_ballance').innerText = localStorage.getItem('balance');
}

