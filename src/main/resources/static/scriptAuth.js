  
  /*===========
  login request
  ============*/ 

  function performLogin() {
    //username
    const loginValue = document.getElementById('loginInput').value;
    //password
    const passwordValue = document.getElementById('passwordInput').value;
    //error message field
    const errorElement = document.getElementById('loginErrorTextField');

    //create request data
    const credentials = {
      username: loginValue,
      password: passwordValue
    };

    const jsonCredentials = JSON.stringify(credentials);

    //create request
    const requestLogin = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: jsonCredentials
    };

    //send request to login user
    fetch('http://localhost:8080/api/auth/login', requestLogin)
    .then(response => {
      if (response.ok) {
        return response.json().then(data => {
          for (const key in data) {
            localStorage.setItem(key, data[key]);
          }
          closePopups();
          fillUserData();
          window.location.reload();
        });
      } else {
        return response.json().then( data => {
          const errorMessage = data.message;
          errorElement.innerText = errorMessage;
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

  function performSignup() {
    //username
    const signUpValue = document.getElementById('signupInput').value;
    //password
    const signUpPasswordValue = document.getElementById('signupPasswordInput').value;
    //password confirmation
    const confirmPasswordValue = document.getElementById('confirmSignupPasswordInput').value;
    //error message field
    const errorElement = document.getElementById('signupErrorTextField');

    //check if password match
    if (signUpPasswordValue !== confirmPasswordValue) {
      errorElement.innerText = 'Passwords do not match';
      return;
    }

    //create request data
    const userData = {
      username: signUpValue,
      password: signUpPasswordValue
    };

    const jsonUserData = JSON.stringify(userData);

    //create request
    const requestSignup = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: jsonUserData
    };

    //send request to create user
    fetch('http://localhost:8080/api/auth/signup', requestSignup)
    .then(response => {
      if (response.ok) {
        closePopups();
        return response.json();
      }else{
        return response.json().then( data => {
          const errorMessage = data.message;
          errorElement.innerText = errorMessage;
          throw new Error(errorMessage);
        });
      }
    })
    .catch(error => {
      console.error(error);
    });
  };

  /*=====
  Log out 
  ======*/

  function signOut() {
    localStorage.clear();
    window.location.reload();
  }
