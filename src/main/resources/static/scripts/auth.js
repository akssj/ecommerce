import { switchAccountDropDown, setCookie, getCookie } from './utility.js';

/*============
login request
============*/
export function login() {
  const usernameInput = document.getElementById('loginFormUsername');
  const passwordInput = document.getElementById('loginFormPassword');

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
  const usernameInput = document.getElementById('registerFormUsername');
  const emailInput = document.getElementById('registerFormEmail');
  const passwordInput = document.getElementById('registerFormPassword');
  const confirmPasswordInput = document.getElementById('registerFormConfirmPassword');

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

/*==================
   Delete request
==================*/
export function deleteAccount() {
  const usernameInput = document.getElementById('deleteAccountUsername');
  const passwordInput = document.getElementById('deleteAccountPassword');

  const deleteData = {
    username: usernameInput.value,
    password: passwordInput.value
  };

  const jsonDeleteData = JSON.stringify(deleteData);

  const requestDelete = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: jsonDeleteData
  };

  fetch('http://localhost:8080/auth/delete', requestDelete)
    .then(response => {
      if (response.ok) {
        return response.json();
      } else {
        return response.json().then(data => {
          const errorMessage = data.message || "An error occurred while deleting the account.";
          alert(errorMessage);
          throw new Error(errorMessage);
        });
      }
    })
    .then(data => {
      if (data.message === "User account has been deleted!") {
        signOut();
      } else {
        alert("Failed to delete user account!");
      }
    })
    .catch(error => {
      console.error("Network error:", error);
      alert("An error occurred while deleting the account. Please try again later.");
    });
}

/*=========================
   Update password request
===========================*/
export function changePassword() {
    const currentPasswordInput = document.getElementById('currentPassword');
    const newPasswordInput = document.getElementById('newPassword');
    const confirmNewPasswordInput = document.getElementById('confirmNewPassword');

    const currentPassword = currentPasswordInput.value;
    const newPassword = newPasswordInput.value;
    const confirmNewPassword = confirmNewPasswordInput.value;

    if (newPassword !== confirmNewPassword) {
        alert("Passwords do not match.");
        return;
    }

    const changePasswordData = {
        password: currentPassword,
        newPassword: newPassword,
        confirmPassword: confirmNewPassword
    };

    const jsonChangePasswordData = JSON.stringify(changePasswordData);

    const requestChangePassword = {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: jsonChangePasswordData
    };

    fetch('http://localhost:8080/auth/changePassword', requestChangePassword)
        .then(response => {
            if (!response.ok) {
                return response.json().then(data => {
                    const errorMessage = data.message || "An error occurred while changing the password.";
                    alert(errorMessage);
                    throw new Error(errorMessage);
                });
            }
            return response.json();
        })
        .then(data => {
            alert(data.message);
        })
        .catch(error => {
            console.error("Network error:", error);
            alert("An error occurred while changing the password. Please try again later.");
        });
}

