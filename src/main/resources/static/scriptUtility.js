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

/*===========================
Website handling on page load
=============================*/
document.addEventListener('DOMContentLoaded', function () {
  fillProducts();
  const isLoggedIn = getCookie('loggedIn');
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

function getCookie(cookie_name) {
  let name = cookie_name + "=";
  let decodedCookie = decodeURIComponent(document.cookie);
  let cookieArray = decodedCookie.split(';');
  for(let i = 0; i <cookieArray.length; i++) {
    let cookie = cookieArray[i];
    while (cookie.charAt(0) == ' ') {
      cookie = cookie.substring(1);
    }
    if (cookie.indexOf(name) == 0) {
      return cookie.substring(name.length, cookie.length);
    }
  }
  return "";
}