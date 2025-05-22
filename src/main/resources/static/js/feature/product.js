import { getCookie } from '/js/util/utility.js';
import { buyItem, deleteItem, showEditModal, openBuyModal } from './productHandling.js';

/* ----------  JAKI ENDPOINT POBIERAĆ  ---------- */
export function getApiUrl() {
  const base = window.location.origin;
  const path = window.location.pathname;

  if (path.startsWith('/category')) {
    const category = path.split('/')[2];
    return `${base}/product/${category}/category`;
  }
  if (path.startsWith('/my-products')) return `${base}/product/my-products`;
  if (path.startsWith('/bought-products')) return `${base}/product/bought-products`;

  return `${base}/product/forSale`;
}

/* ----------  RENDER TABELI  ---------- */
export function renderProductTable(list, tableEl) {
  const tbody = tableEl.querySelector('tbody');
  tbody.innerHTML = '';

  const isLoggedIn = getCookie('loggedIn') === 'true';
  const currentUser = getCookie('username');
  const isMyProductsPage = window.location.pathname.startsWith('/my-products');

  list.forEach(item => {
    const row = tbody.insertRow();

    ['name', 'seller', 'description'].forEach(key => {
  const cell = row.insertCell();
  cell.textContent = item[key] ?? '';
});

const priceCell = row.insertCell();
priceCell.textContent = item.price?.toFixed(2) + ' zł';


    const cell = row.insertCell();
    const grp = document.createElement('div');
    grp.className = 'btn-group';

    if (isLoggedIn && item.seller !== currentUser && !item.buyer) {
      const buy = document.createElement('button');
      buy.textContent = 'Buy';
      buy.className = 'btn btn-success btn-sm';
      buy.dataset.itemId = item.id;
      buy.addEventListener('click', () => openBuyModal(item.id));
      grp.appendChild(buy);
    }

    if (isLoggedIn && item.seller === currentUser && !item.buyer) {
      if (isMyProductsPage) {
        const edit = document.createElement('button');
        edit.textContent = 'Edytuj';
        edit.className = 'btn btn-warning btn-sm';
        edit.addEventListener('click', () => showEditModal(item));
        grp.appendChild(edit);
      }

      const del = document.createElement('button');
      del.textContent = 'Usuń';
      del.className = 'btn btn-danger btn-sm';
      del.dataset.itemId = item.id;
      del.addEventListener('click', deleteItem);
      grp.appendChild(del);
    }

    cell.appendChild(grp);
  });
}

/* ----------  POBIERANIE I WYPEŁNIANIE  ---------- */
export function fillProducts() {
  const table = document.getElementById('product-table');
  if (!table) return;

  const isLoggedIn = getCookie('loggedIn') === 'true';
  const headerRow = table.querySelector('thead tr');
  const actionsIdx = [...headerRow.cells].findIndex(c => c.textContent === 'Actions');
  if (actionsIdx !== -1) {
    headerRow.cells[actionsIdx].style.display = isLoggedIn ? '' : 'none';
  }

  fetch(getApiUrl())
    .then(r => {
      if (!r.ok) {
        alert(`Błąd: ${r.status}`);
        return [];
      }
      return r.json();
    })
    .then(list => renderProductTable(list, table))
    .catch(err => console.error('Error:', err));
}
