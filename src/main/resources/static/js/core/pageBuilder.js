// js/core/pageBuilder.js
import {
  switchAccountDropDown,
  getCookie,
  setCookie
} from '/js/util/utility.js';

let cachedCategories = [];

/* ----------------- NAVBAR ----------------- */
export async function loadNavbar() {
  const resp = await fetch('/component/navbar.html');
  if (!resp.ok) throw new Error('Cannot load navbar');
  document.getElementById('navbarPlaceholder').innerHTML = await resp.text();
}

/* --------------- KATEGORIE ---------------- */
export async function fetchCategories() {
  const res = await fetch(`${window.location.origin}/product/categories`);
  if (!res.ok) {
    console.error('Failed to fetch categories');
    return [];
  }
  const categories = await res.json();
  cachedCategories = categories;
  return categories;
}

export async function loadCategories() {
  const categories = await fetchCategories();
  if (!categories || categories.length === 0) return;

  if (document.getElementById('categoryPanel')) {
    renderCategoryPanel(); // używa cachedCategories
  }

  if (document.getElementById('itemCategory')) {
    updateAddItemForm(categories);
  }
}


/* ------- Kategorie do formularza „Dodaj” ------- */
function updateAddItemForm(categories) {
  const categorySelect = document.getElementById('itemCategory');
  const subCategorySelect = document.getElementById('itemSubCategory');
  if (!categorySelect || !subCategorySelect) return;

  categorySelect.innerHTML = '<option value="" disabled selected>Wybierz kategorię</option>';
  subCategorySelect.innerHTML = '<option value="" disabled selected>Wybierz podkategorię</option>';

  categories.forEach(cat => {
    categorySelect.insertAdjacentHTML(
      'beforeend',
      `<option value="${cat.name}">${cat.name}</option>`
    );
  });

  categorySelect.addEventListener('change', () => {
    const selected = categories.find(c => c.name === categorySelect.value);
    updateSubCategorySelect(selected?.subCategories ?? []);
  });
}

function updateSubCategorySelect(subCategories) {
  const subSelect = document.getElementById('itemSubCategory');
  if (!subSelect) return;

  subSelect.innerHTML = '<option value="" disabled selected>Wybierz podkategorię</option>';
  subCategories.forEach(sub =>
    subSelect.insertAdjacentHTML('beforeend', `<option value="${sub}">${sub}</option>`)
  );
}


/* ------------- PANEL KATEGORII POD NAVBAREM ------------- */
export function renderCategoryPanel() {
  const panel = document.getElementById('categoryPanel');
  if (!panel) return;

  panel.innerHTML = ''; // wyczyść

  const list = document.createElement('ul');
  list.className = 'main-category-list';

  // „Wszystkie kategorie”
  const all = document.createElement('li');
  all.className = 'main-category';
  all.innerHTML = `<a href="#" data-category="ALL">Wszystkie kategorie</a>`;
  list.appendChild(all);

  cachedCategories.forEach(cat => {
    const li = document.createElement('li');
    li.className = 'main-category position-relative';

    const link = document.createElement('a');
    link.href = '#';
    link.setAttribute('data-category', cat.name);
    link.textContent = cat.name;

    const submenu = document.createElement('ul');
    submenu.className = 'sub-category-list';

    cat.subCategories.forEach(sub => {
      const subLi = document.createElement('li');
      subLi.innerHTML = `<a href="#" data-category="${sub}">${sub}</a>`;
      submenu.appendChild(subLi);
    });

    li.appendChild(link);
    li.appendChild(submenu);
    list.appendChild(li);
  });

  panel.appendChild(list);

  // Event delegacja
  panel.querySelectorAll('a[data-category]').forEach(link => {
    link.addEventListener('click', e => {
      e.preventDefault();
      const category = e.target.getAttribute('data-category');
      localStorage.setItem('selectedCategory', category);
      document.dispatchEvent(new CustomEvent('categorySelected', { detail: category }));
      panel.classList.add('d-none');
    });
  });
}


/* ------------- USER DATA ---------------- */
export function loadUserData() {
  const box = document.getElementById('userDataContainer');
  if (!box) return;

  fetch(`${window.location.origin}/auth/userData`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: '{}',
  })
    .then(r => r.ok ? r.json() : Promise.reject('Failed to fetch user data'))
    .then(data => {
      document.getElementById('username').textContent = data.username;
      document.getElementById('email').textContent = data.email;
    })
    .catch(err => console.error('Error:', err));
}

/* ------------- GETTER do cache kategorii ------------- */
export async function getAllCategories() {
  if (cachedCategories.length > 0) return cachedCategories;

  const res = await fetch('/product/categories');
  if (!res.ok) throw new Error('Nie udało się pobrać kategorii');

  const json = await res.json();
  cachedCategories = json;
  return json;
}
