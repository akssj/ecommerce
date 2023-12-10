  
/*==============
  login request
==============*/ 

function login() {
  const Username = document.getElementById('login-username-input').value;
  const Password = document.getElementById('login-password-input').value;

  const LoginErrorTextField = document.getElementById('login-error-text-field');

  try{
    if (Username === "" || Username === "") {
      LoginErrorTextField.innerText = "Fill empty filed!";
      throw new Error("Fill empty filed!");
    }
  }catch(error){
    return;
  }

  const LoginData = {
    username: Username,
    password: Password
  };

  const JsonLoginData = JSON.stringify(LoginData);

  const RequestLogin = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JsonLoginData
  };

  fetch('http://localhost:8080/auth/login', RequestLogin)
  .then(response => {
    if (response.ok) {
      return response.json().then(data => {
        for (const key in data) {
          localStorage.setItem(key, data[key]); //TODO make it session
        }
        closePopups();
        fillUserData();
        window.location.reload();
      });
    } else {
      return response.json().then( data => {
        const errorMessage = data.message;
        LoginErrorTextField.innerText = errorMessage;
        throw new Error(errorMessage);
      });
    }
  })
  .catch(error => {
    console.error(error);
  });
};


/*============
signup request
============*/

function signup() {
  const Username = document.getElementById('signup-username-input').value;
  const Password = document.getElementById('signup-password-input').value;
  const ConfirmPassword = document.getElementById('confirm-signup-password-input').value;

  const SignupErrorTextField = document.getElementById('signup-error-text-field');

  try{
    if (Username === "" || Password === "") {
      SignupErrorTextField.innerText = "Fill empty fileds";
      throw new Error("Fill empty fileds");
    }
    if (Password !== ConfirmPassword) {
      SignupErrorTextField.innerText = "Passwords does not match";
      throw new Error("Passwords does not match");
    }
  }catch(error){
    return;
  }

  const SignupData = {
    username: Username,
    password: Password
  };

  const JsonSignupData = JSON.stringify(SignupData);

  const RequestSignup = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JsonSignupData
  };

  fetch('http://localhost:8080/auth/signup', RequestSignup)
  .then(response => {
    if (response.ok) {
      closePopups();
      return response.json();   //TODO make it so you are logged in after signup
    }else{
      return response.json().then( data => {
        const errorMessage = data.message;
        SignupErrorTextField.innerText = errorMessage;
        throw new Error(errorMessage);
      });
    }
  })
  .catch(error => {
    console.error(error);
  });
};

/*============
  sign out
============*/

function signOut() { //TODO make it so it deletes session
  localStorage.clear();
  window.location.reload();
}
