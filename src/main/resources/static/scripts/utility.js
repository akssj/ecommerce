export function switchAccountDropDown(action) {
    const registerForm = document.querySelector('#registerForm');
    const loginForm = document.querySelector('#loginForm');
    const loggedInContent = document.querySelector('#accountDropdown');
    const accountButton = document.querySelector('#accountButton');

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
            accountButton.textContent = getCookie('username');
            break;
        default:
            console.error('Invalid action:', action);
            break;
    }
}

export function hideModal(modalId) {
   const modal = document.getElementById(modalId);
   if (modal) {
       const modalInstance = bootstrap.Modal.getInstance(modal);
       if (modalInstance) {
           modalInstance.hide();
       }
   }
}

export function setCookie(name, value, expirationMinutes = 10) {
    const expirationDate = new Date(Date.now() + expirationMinutes * 60 * 1000);
    const cookieString = `${name}=${value}; expires=${expirationDate.toUTCString()}; path=/; secure; SameSite=None`;
    document.cookie = cookieString;
}

export function getCookie(cookieName) {
    const name = cookieName + "=";
    const decodedCookie = decodeURIComponent(document.cookie);
    const cookieArray = decodedCookie.split(';');
    for (let i = 0; i < cookieArray.length; i++) {
        let cookie = cookieArray[i].trim();
        if (cookie.indexOf(name) === 0) {
            return cookie.substring(name.length);
        }
    }
    return "";
}