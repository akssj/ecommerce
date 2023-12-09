
/*=====================
  check if logged in
======================*/

  async function performUserStatusCheck() {

    const token = localStorage.getItem('token');
    Long id = localStorage.getItem('id');

    const requestUserStatusCheck = {
      method: 'GET',
      headers: {
        'Authorization': 'Bearer '+ token,
        'Content-Type': 'application/json'
      },
    };

    await fetch(`http://localhost:8080/auth/${id}/userStatus`, requestUserStatusCheck)
    .then(response => {
      if (response.ok) {
        return response.json().then(data => {
          for (const key in data) {
            localStorage.setItem(key, data[key]);
          }
          fillUserData();
          window.location.reload(); //TODO does it have to reload ?
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