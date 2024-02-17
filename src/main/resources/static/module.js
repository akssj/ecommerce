import { loadNavbar, loadCategories, loadUserData, fetchAndCacheModalContent } from './scripts/pageBuilder.js';
import { login, signup, signOut, deleteAccount, changePassword } from './scripts/auth.js';
import { fillProducts } from './scripts/product.js';
import { addItem, buyItem, deleteItem } from './scripts/productHandling.js';
import { switchAccountDropDown, setCookie, getCookie } from './scripts/utility.js';

document.addEventListener('DOMContentLoaded', async function () {

    await loadNavbar();
    if (getCookie('loggedIn') === 'true') {
        switchAccountDropDown('showAccountOptions');
    }

    await Promise.all([loadCategories(), fillProducts(), loadUserData()]);

    await fetchAndCacheModalContent('/static/component/change_password_modal.html', 'changePasswordModalContent', 'changePasswordModal');
    await fetchAndCacheModalContent('/static/component/delete_account_modal.html', 'deleteAccountModalContent', 'deleteAccountModal');

    document.addEventListener('click', async function (e) {
        const action = e.target.getAttribute('data-action');
        switch (action) {
            case 'login':
                await login();
                break;
            case 'register':
                await signup();
                break;
            case 'logout':
                await signOut();
                break;
            case 'loginForm':
                switchAccountDropDown('showLoginForm');
                break;
            case 'registerForm':
                switchAccountDropDown('showRegisterForm');
                break;
            case 'deleteAccount':
                await deleteAccount();
                break;
            case 'changePassword':
                await changePassword();
                break;
            default:
                break;
        }
    });
});