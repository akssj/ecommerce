// js/core/main.js
import {
    loadNavbar,
    loadCategories,
    loadUserData,
    renderCategoryPanel,
    getAllCategories
  } from '/js/core/pageBuilder.js';
  
  import {
    login, signup, signOut,
    deleteAccount, changePassword
  } from '/js/feature/auth.js';
  
  import {
    fillProducts,
    renderProductTable
  } from '/js/feature/product.js';
  
  import { addItem, updateItem } from '/js/feature/productHandling.js';
  import { switchAccountDropDown, getCookie } from '/js/util/utility.js';
  import { initSearchBar } from '/js/feature/search.js';
  
  /* ------------------- DOM READY ------------------- */
  document.addEventListener('DOMContentLoaded', async () => {
    // 1. Navbar + wyszukiwarka
    await loadNavbar();
    initSearchBar();
  
    // 2. Kategorie: pobranie i panel
    await loadCategories(); // ładuje + renderuje panel
  
    // 3. Konto: dropdown zależnie od logowania
    const loggedIn = getCookie('loggedIn') === 'true';
    switchAccountDropDown(loggedIn ? 'showAccountOptions' : 'showLoginForm');
  
    // 4. Dane strony
    // 4. Dane strony (nie ładuj produktów na stronie wyszukiwania)
const path = window.location.pathname;
if (
  !path.includes('search.html') &&
  !path.includes('bought-products.html') &&
  !path.includes('add-item.html')
) {
  await fillProducts();
}
await loadUserData();

  
    // 5. Rozwijanie panelu kategorii
    const catToggle = document.getElementById('categoryToggle');
    catToggle?.addEventListener('click', () => {
      document.getElementById('categoryPanel')?.classList.toggle('d-none');
    });

    // 5a. Ukrywanie panelu po kliknięciu poza nim
document.addEventListener('click', (event) => {
  const panel = document.getElementById('categoryPanel');
  const toggle = document.getElementById('categoryToggle');

  if (!panel || panel.classList.contains('d-none')) return;

  // Jeśli kliknięcie nie było na przycisku ani wewnątrz panelu
  if (!panel.contains(event.target) && !toggle.contains(event.target)) {
    panel.classList.add('d-none');
  }
});

  
    // 6. Obsługa klików globalnych
    document.addEventListener('click', async (e) => {
      const action = e.target.getAttribute('data-action');
      const cat    = e.target.getAttribute('data-category');
  
      if (action) {
        switch (action) {
          case 'login':           await login(); break;
          case 'register':        await signup(); break;
          case 'logout':          await signOut(); break;
          case 'loginForm':       switchAccountDropDown('showLoginForm'); break;
          case 'registerForm':    switchAccountDropDown('showRegisterForm'); break;
          case 'deleteAccount':   await deleteAccount(); break;
          case 'changePassword':  await changePassword(); break;
          case 'addItem':         addItem(); break;
        }
      }
  
      if (cat) {
        try {
          const allCats = await getAllCategories();
          let urls = [];
  
          const mainCat = allCats.find(c => c.name === cat);
          if (cat === 'ALL') {
            urls.push('/product/forSale');
          } else if (mainCat) {
            urls = mainCat.subCategories.map(sub => `/product/${sub}/category`);
          } else {
            urls.push(`/product/${cat}/category`);
          }
  
          const allProducts = [];
          for (const url of urls) {
            const res = await fetch(url);
            if (res.ok) {
              const json = await res.json();
              allProducts.push(...json);
            }
          }
  
          const table = document.getElementById('product-table');
          if (table) {
            renderProductTable(allProducts, table);
            const label = document.getElementById('currentCategory');
            if (label) {
              label.textContent = (cat === 'ALL') ? 'Wszystkie kategorie' : `Kategoria: ${cat}`;
            }
          }
        } catch (err) {
          console.error('Category load error:', err);
          alert('Nie udało się załadować produktów.');
        }
      }
    });
// Obsługa kliku zapisywania zmian
    document.addEventListener('DOMContentLoaded', () => {
      const saveBtn = document.getElementById('saveEditBtn');
      if (saveBtn) {
        saveBtn.addEventListener('click', updateItem);
      }
    });
  });

  //Obsługa krajów i miast
  const countries = ['Poland', 'Germany', 'France', 'Spain', 'Italy', 'United States', 'United Kingdom'];

const countryList = document.getElementById('countryList');
countries.forEach(c => {
  const option = document.createElement('option');
  option.value = c;
  countryList.appendChild(option);
});

const cityList = document.getElementById('cityList');
const cityMap = {
  Poland: ['Warsaw', 'Krakow', 'Gdansk', 'Wroclaw'],
  Germany: ['Berlin', 'Munich', 'Hamburg'],
  France: ['Paris', 'Lyon', 'Marseille']
};

document.getElementById('shippingCountry').addEventListener('input', e => {
  const selected = e.target.value;
  const cities = cityMap[selected] || [];

  cityList.innerHTML = '';
  cities.forEach(city => {
    const option = document.createElement('option');
    option.value = city;
    cityList.appendChild(option);
  });
});

  