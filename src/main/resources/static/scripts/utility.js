export function switchAccountDropDown(action) {
    const registerForm = document.querySelector('#registerForm');
    const loginForm = document.querySelector('#loginForm');
    const loggedInContent = document.querySelector('#loggedInContent');

    switch (action) {
        case 'showRegisterForm':
            loginForm.classList.add('d-none');
            registerForm.classList.remove('d-none');
            break;
        case 'showLoginForm':
            registerForm.classList.add('d-none');
            loginForm.classList.remove('d-none');
            break;
        case 'showAccountOptions':
            registerForm.classList.add('d-none');
            loginForm.classList.add('d-none');
            loggedInContent.classList.remove('d-none');
            break;
        default:
            console.error('Invalid action:', action);
            break;
    }
}



export function setCookie(name, value) {
    const expirationDate = new Date();
    expirationDate.setTime(expirationDate.getTime() + 10 * 60 * 1000);
    const cookieString = `${name}=${value}; expires=${expirationDate.toUTCString()}; path=/; secure; SameSite=None`;
    document.cookie = cookieString;
}

export function getCookie(cookie_name) {
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