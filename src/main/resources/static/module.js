import { loadNavbar, loadCategories, loadUserData } from './scripts/pageBuilder.js';
import { login, signup, signOut } from './scripts/auth.js';
import { fillProducts } from './scripts/product.js';
import { addItem, buyItem, deleteItem } from './scripts/productHandling.js';
import { switchAccountDropDown, setCookie, getCookie } from './scripts/utility.js';

document.addEventListener('DOMContentLoaded', async function () {

    await loadNavbar();
    if (getCookie('loggedIn') === 'true') {
        switchAccountDropDown('showAccountOptions');
    }

    loadCategories()
    fillProducts();
    loadUserData();

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
            default:
                break;
        }
    });
});