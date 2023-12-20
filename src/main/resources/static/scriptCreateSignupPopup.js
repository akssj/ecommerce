function createSignupPopup() {
  var existingPopup = document.getElementById('signup-popup');

  if (existingPopup) {
    existingPopup.style.display = 'block';
    return;
  }

  var signupPopup = document.createElement('div');
  signupPopup.id = 'signup-popup';
  signupPopup.className = 'popup';

  var closeButton = document.createElement('a');
  closeButton.href = '#';
  closeButton.className = 'popup-close-button';
  closeButton.innerHTML = 'X';
  closeButton.onclick = function() {
    closePopups('signup-popup', 'signup-error-text-field');
  };

  var heading = document.createElement('h2');
  heading.innerHTML = 'Please signup';

  var usernameInput = document.createElement('input');
  usernameInput.type = 'text';
  usernameInput.placeholder = 'Username';
  usernameInput.id = 'signup-username-input';

  var passwordInput = document.createElement('input');
  passwordInput.type = 'password';
  passwordInput.placeholder = 'Password';
  passwordInput.id = 'signup-password-input';

  var confirmPasswordInput = document.createElement('input');
  confirmPasswordInput.type = 'password';
  confirmPasswordInput.placeholder = 'Confirm password';
  confirmPasswordInput.id = 'confirm-signup-password-input';

  var signupButton = document.createElement('a');
  signupButton.href = '#';
  signupButton.className = 'button';
  signupButton.innerHTML = 'Signup';
  signupButton.onclick = function() {
    signup();
  };

  var errorTextField = document.createElement('a');
  errorTextField.id = 'signup-error-text-field';

  signupPopup.appendChild(closeButton);
  signupPopup.appendChild(heading);
  signupPopup.appendChild(usernameInput);
  signupPopup.appendChild(passwordInput);
  signupPopup.appendChild(confirmPasswordInput);
  signupPopup.appendChild(signupButton);
  signupPopup.appendChild(errorTextField);

  document.body.appendChild(signupPopup);
}