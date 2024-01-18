function showRegisterForm() {
    document.getElementById('loggedInContent').classList.add('d-none');
    document.getElementById('loginFormContent').classList.add('d-none');
    document.getElementById('registerFormContent').classList.remove('d-none');
}

function showLoginForm() {
    document.getElementById('loggedInContent').classList.add('d-none');
    document.getElementById('registerFormContent').classList.add('d-none');
    document.getElementById('loginFormContent').classList.remove('d-none');
}
function showAccountOptions() {
    document.getElementById('registerFormContent').classList.add('d-none');
    document.getElementById('loginFormContent').classList.add('d-none');
    document.getElementById('loggedInContent').classList.remove('d-none');
}

  document.addEventListener('DOMContentLoaded', function () {
    const isLoggedIn = getCookie('loggedIn');
    const loggedInDropdown = document.getElementById('loggedInContent');
    if (isLoggedIn === 'true') {
      showAccountOptions();
    } else {
      showLoginForm();
    }
  });

function setCookie(name, value) {
    const expirationDate = new Date();
    expirationDate.setTime(expirationDate.getTime() + 10 * 60 * 1000);
    const cookieString = `${name}=${value}; expires=${expirationDate.toUTCString()}; path=/; secure; SameSite=None`;
    document.cookie = cookieString;
}

function getCookie(cname) {
  let name = cname + "=";
  let decodedCookie = decodeURIComponent(document.cookie);
  let ca = decodedCookie.split(';');
  for(let i = 0; i <ca.length; i++) {
    let c = ca[i];
    while (c.charAt(0) == ' ') {
      c = c.substring(1);
    }
    if (c.indexOf(name) == 0) {
      return c.substring(name.length, c.length);
    }
  }
  return "";
}