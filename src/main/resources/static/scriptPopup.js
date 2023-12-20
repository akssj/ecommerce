
/*=============
popup handling
==============*/

document.addEventListener('DOMContentLoaded', () => {

  fillUserData();

});

function closePopups(popupId, errorTextFieldId) {
  var popup = document.getElementById(popupId);
  var errorTextField = document.getElementById(errorTextFieldId);

  if (popup && errorTextField) {
    popup.style.display = 'none';
    errorTextField.innerText = "";
  }
}

function fillUserData(){ //TODO going to be deprecated real fast
  document.getElementById('current_user_id').innerText = localStorage.getItem('id');
  document.getElementById('current_user_username').innerText = localStorage.getItem('username');
  document.getElementById('current_user_role').innerText = localStorage.getItem('roles');
  document.getElementById('current_user_balance').innerText = localStorage.getItem('balance');
}

