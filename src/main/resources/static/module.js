import { loadNavbar } from './scripts/pageCreator.js';

import { login, signup, signOut } from './scripts/auth.js';

import { fillProducts } from './scripts/product.js';

import { addItem, buyItem, deleteItem } from './scripts/productHandling.js';

import { switchAccountDropDown, setCookie, getCookie } from './scripts/utility.js';

loadNavbar();

document.addEventListener('DOMContentLoaded', function () {

    fillProducts();

    document.addEventListener('click', function (e) {
        if (getCookie('loggedIn') === 'true') {
            switchAccountDropDown('showAccountOptions');
        }
        switch (e.target.getAttribute('data-action')) {
            case 'login':
                e.preventDefault();
                login();
                break;
            case 'register':
                e.preventDefault();
                signup();
                break;
            case 'logout':
                e.preventDefault();
                signOut();
                break;
            case 'loginForm':
                e.preventDefault();
                switchAccountDropDown('showLoginForm');
                break;
            case 'registerForm':
                e.preventDefault();
                switchAccountDropDown('showRegisterForm');
                break;
            default:
                break;
        }
    });




});