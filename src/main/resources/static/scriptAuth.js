
/*============
login request
============*/
document.getElementById('registerFormContent').addEventListener('submit', function (e) {
  e.preventDefault();
  signup();
});

function login() {
  const form = document.getElementById('login-form');
  const usernameInput = document.getElementById('DropdownFormUsername');
  const passwordInput = document.getElementById('DropdownFormPassword');

  const loginData = {
    username: usernameInput.value,
    password: passwordInput.value
  };

  const jsonLoginData = JSON.stringify(loginData);

  const requestLogin = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: jsonLoginData
  };

  fetch('http://localhost:8080/auth/login', requestLogin)
    .then(response => {
      if (response.ok) {
        return response.json().then(data => {
          for (const key in data) {
            if (key === "roles") {
              const roleAuthorities = data[key].map(role => role.authority);
              localStorage.setItem(key, JSON.stringify(roleAuthorities));
            } else {
              localStorage.setItem(key, data[key]);
            }
          }
          document.getElementById('loggedOffDropdown').classList.add('d-none');
          document.getElementById('loggedInDropdown').classList.remove('d-none');
        });
      } else {
        return response.json().then(data => {
          const errorMessage = data.message;
          alert(errorMessage);
          throw new Error(errorMessage);
        });
      }
    })
    .catch(error => {
      console.error(error);
    });
}

/*============
signup request
============*/
document.getElementById('registerFormContent').addEventListener('submit', function (e) {
  e.preventDefault();
  signup();
});

function signup() {
  const form = document.getElementById('registerFormContent');
  const usernameInput = document.getElementById('DropdownFormUsernameRegister');
  const passwordInput = document.getElementById('DropdownFormPasswordRegister');
  const confirmPasswordInput = document.getElementById('DropdownFormPasswordRegisterConfirm');

  const signupData = {
    username: usernameInput.value,
    password: passwordInput.value
  };

  const jsonSignupData = JSON.stringify(signupData);

  const requestSignup = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: jsonSignupData
  };

  fetch('http://localhost:8080/auth/signup', requestSignup)
    .then(response => {
      if (response.ok) {
        return response.json(); // TODO: make it so you are logged in after signup
      } else {
        return response.json().then(data => {
          const errorMessage = data.message;
          alert(errorMessage);
          throw new Error(errorMessage);
        });
      }
    })
    .catch(error => {
      console.error(error);
    });
}

/*============
  sign out
============*/

function signOut() { //TODO make it so it deletes session
  localStorage.clear();
  window.location.reload();
}

/*============
  status check
============*/

function isUserLoggedIn() {
    var token = localStorage.getItem('token');
    var id = localStorage.getItem('id');
    return token && id;
}
