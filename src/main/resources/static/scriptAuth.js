  
/*==============
  login request
==============*/ 

function login() {
  const Username = document.getElementById('loginInput').value;
  const Password = document.getElementById('passwordInput').value;

  const LoginErrorTextField = document.getElementById('loginErrorTextField');

  //TODO make some if to check validity or smth

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

  fetch('http://localhost:8080/api/auth/login', RequestLogin)
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
  const Username = document.getElementById('signupInput').value;
  const Password = document.getElementById('signupPasswordInput').value;
  const ConfirmPassword = document.getElementById('confirmSignupPasswordInput').value;

  const SignupErrorTextField = document.getElementById('signupErrorTextField');

  if (Password !== ConfirmPassword) {
    SignupErrorTextField.innerText = 'Passwords does not match';
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

  fetch('http://localhost:8080/api/auth/signup', RequestSignup)
  .then(response => {
    if (response.ok) {
      closePopups();
      return response.json();
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
  signout
============*/

function signOut() { //TODO make it so it deletes session
  localStorage.clear();
  window.location.reload();
}
