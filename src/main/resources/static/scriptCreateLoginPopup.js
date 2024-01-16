function createLoginPopup() {
  var existingPopup = document.getElementById('login-popup');

  if (existingPopup) {
    existingPopup.style.display = 'block';
    return;
  }

  var loginPopup = document.createElement('div');
  loginPopup.id = 'login-popup';
  loginPopup.className = 'popup';

  var closeButton = document.createElement('a');
  closeButton.href = '#';
  closeButton.className = 'popup-close-button';
  closeButton.innerHTML = 'X';
  closeButton.onclick = function() {
    closePopups('login-popup', 'login-error-text-field');
  };

  var heading = document.createElement('h2');
  heading.innerHTML = 'Please login';

  var usernameInput = document.createElement('input');
  usernameInput.type = 'text';
  usernameInput.placeholder = 'Username';
  usernameInput.id = 'login-username-input';

  var passwordInput = document.createElement('input');
  passwordInput.type = 'password';
  passwordInput.placeholder = 'Password';
  passwordInput.id = 'login-password-input';

  var loginButton = document.createElement('a');
  loginButton.href = '#';
  loginButton.className = 'button';
  loginButton.innerHTML = 'Login';
  loginButton.onclick = function() {
    login();
  };

  var signupButton = document.createElement('a');
  signupButton.href = '#';
  signupButton.className = 'button';
  signupButton.innerHTML = 'Signup';
  signupButton.onclick = function() {
    createSignupPopup();
    closePopups('login-popup', 'login-error-text-field');
  };
  signupButton.id = 'main-page-signup-button';

  var errorTextField = document.createElement('a');
  errorTextField.id = 'login-error-text-field';

  loginPopup.appendChild(closeButton);
  loginPopup.appendChild(heading);
  loginPopup.appendChild(usernameInput);
  loginPopup.appendChild(passwordInput);
  loginPopup.appendChild(loginButton);
  loginPopup.appendChild(signupButton);
  loginPopup.appendChild(errorTextField);

  document.body.appendChild(loginPopup);
}