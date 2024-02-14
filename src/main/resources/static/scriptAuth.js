
/*============
login request
============*/
document.getElementById('loginFormContent').addEventListener('submit', function (e) {
  e.preventDefault();
  login();
});

function login() {
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
        showAccountOptions();
        setCookie('loggedIn', 'true');
        getUserStatus();
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
  const usernameInput = document.getElementById('DropdownFormUsernameRegister');
  const passwordInput = document.getElementById('DropdownFormPasswordRegister');
  const confirmPasswordInput = document.getElementById('DropdownFormPasswordRegisterConfirm');

  if (passwordInput.value !== confirmPasswordInput.value) {
    alert("Password does not match!");
    return;
  }

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
        getUserStatus();
        setCookie('loggedIn', 'true');
        return response.json();
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

/*==================
user status request
==================*/
function getUserStatus() {
  fetch('http://localhost:8080/auth/userStatus', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json'
    }
  })
  .then(response => {
    if (response.ok) {
      return response.json().then(data => {
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
  sign out
============*/
function signOut() {
  localStorage.clear();

  const cookies = document.cookie.split("; ");
  for (const cookie of cookies) {
    const [name, _] = cookie.split("=");
    document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/; secure; SameSite=None`;
  }

  fetch('http://localhost:8080/auth/logout', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    }
  })
  .then(response => {
    if (response.ok) {
      return response.json().then(data => {
       window.location.href = "http://localhost:8080/main";
      });
    } else {
      return response.json().then(data => {
        const errorMessage = data.message || 'Error logging out.';
        throw new Error(errorMessage);
      });
    }
  })
  .catch(error => {
    console.error(error);
  });
}
