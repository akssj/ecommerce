
/*=====================
  check if logged in
======================*/

  async function performUserStatusCheck() {

    //username
    const username = localStorage.getItem('username');
    //token
    const token = localStorage.getItem('token');

    //create request data
    const userStatusCheck = {
      username: username
    };

    const jsonsuserStatusCheck = JSON.stringify(userStatusCheck);

    //create request
    const requestuserStatusCheck = {
      method: 'POST',
      headers: {
        'Authorization': 'Bearer '+ token,
        'Content-Type': 'application/json'
      },
      body: jsonsuserStatusCheck
    };

    //send request to check login status
    await fetch('http://localhost:8080/api/auth/userStatus', requestuserStatusCheck)
    .then(response => {
      if (response.ok) {
        return response.json().then(data => {
          for (const key in data) {
            localStorage.setItem(key, data[key]);
          }
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