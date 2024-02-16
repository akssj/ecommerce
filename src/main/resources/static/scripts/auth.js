import { switchAccountDropDown, setCookie, getCookie } from './utility.js';

/*============
login request
============*/
export function login() {
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

return fetch('http://localhost:8080/auth/login', requestLogin)
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            return response.json().then(data => {
                const errorMessage = data.message || "An error occurred during login.";
                alert(errorMessage);
                throw new Error(errorMessage);
            });
        }
    })
    .then(data => {
        return getUserStatus().then(() => {
            setCookie('loggedIn', 'true');
            switchAccountDropDown('showAccountOptions');
        });
    })
    .catch(error => {
        console.error("Network error:", error);
        alert("An error occurred during login. Please try again later.");
    });
}

/*============
signup request
============*/
export function signup() {
  const usernameInput = document.getElementById('DropdownFormUsernameRegister');
  const passwordInput = document.getElementById('DropdownFormPasswordRegister');
  const emailInput = document.getElementById('DropdownFormEmailRegister');
  const confirmPasswordInput = document.getElementById('DropdownFormPasswordRegisterConfirm');

  if (passwordInput.value !== confirmPasswordInput.value) {
    alert("Password does not match!");
    return;
  }

  if (!usernameInput.value || !passwordInput.value || !emailInput.value || !confirmPasswordInput.value) {
    alert("Please fill in all fields!");
    return;
  }

  const signupData = {
    username: usernameInput.value,
    password: passwordInput.value,
    email: emailInput.value
  };

  const jsonSignupData = JSON.stringify(signupData);

  const requestSignup = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: jsonSignupData
  };

return fetch('http://localhost:8080/auth/signup', requestSignup)
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            return response.json().then(data => {
                const errorMessage = data.message || "An error occurred during signup.";
                throw new Error(errorMessage);
            });
        }
    })
    .then(data => {
        return getUserStatus().then(() => {
            setCookie('loggedIn', 'true');
            switchAccountDropDown('showAccountOptions');
        });
    })
    .catch(error => {
        console.error("Network error:", error);
        alert("An error occurred during signup. Please try again later.");
    });

}

/*==================
user status request
==================*/
export function getUserStatus() {
  return fetch('http://localhost:8080/auth/userStatus', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json'
    }
  })
    .then(response => {
      if (response.ok) {
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
   SignOut request
==================*/
export function signOut() {
  localStorage.clear();

  const cookies = document.cookie.split("; ");
  for (const cookie of cookies) {
    const [name, _] = cookie.split("=");
    document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/; secure; SameSite=None`;
  }

  return fetch('http://localhost:8080/auth/logout', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    }
  })
    .then(response => {
      if (response.ok) {
        return response.json();
      } else {
        return response.json().then(data => {
          const errorMessage = data.message || 'Error logging out.';
          throw new Error(errorMessage);
        });
      }
    })
    .then(data => {
      window.location.href = "http://localhost:8080/main";
    })
    .catch(error => {
      console.error(error);
    });
}
