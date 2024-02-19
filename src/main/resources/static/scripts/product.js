import { getCookie } from './utility.js';
import { buyItem, deleteItem } from './productHandling.js';

/*=====================
Fill site with products on refresh
======================*/

export function getApiUrl() {
    const baseUrl = window.location.origin;
    let apiUrl;

    switch (true) {
        case window.location.pathname.startsWith('/main'):
            apiUrl = `${baseUrl}/product/forSale`;
            break;
        case window.location.pathname.startsWith('/category'):
            const category = window.location.pathname.split('/')[2];
            apiUrl = `${baseUrl}/product/${category}/category`;
            break;
        case window.location.pathname.startsWith('/my-products'):
            apiUrl = `${baseUrl}/product/my-products`;
            break;
        case window.location.pathname.startsWith('/bought-products'):
            apiUrl = `${baseUrl}/product/bought-products`;
            break;
        default:
            apiUrl = `${baseUrl}/product/forSale`;
            break;
    }

    return apiUrl;
}

export function fillProducts() {
    const productTable = document.getElementById('product-table');
    if (!productTable) return;

    const tbody = productTable.querySelector('tbody');
    const headerRow = productTable.querySelector('thead tr');
    const columnNames = Array.from(headerRow.cells).map(cell => cell.textContent);
    const actionsColumnIndex = columnNames.findIndex(name => name === 'Actions');
    const currentUser = getCookie('username');

    fetch(getApiUrl())
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            data.forEach(item => {
                const row = tbody.insertRow();

                columnNames.forEach((columnName, index) => {
                    const cell = row.insertCell();
                    cell.textContent = item[columnName.toLowerCase()] || '';

                    if (index === actionsColumnIndex) {
                        const cellButtons = document.createElement('div');
                        cellButtons.classList.add('btn-group');

                        if (item.seller !== currentUser && item.buyer === null) {
                            const buyButton = document.createElement('button');
                            buyButton.textContent = 'Buy';
                            buyButton.classList.add('btn', 'btn-success');
                            buyButton.dataset.itemId = item.id;
                            buyButton.addEventListener('click', buyItem);
                            cellButtons.appendChild(buyButton);
                        }

                        if (item.seller === currentUser && item.buyer === null) {
                            const deleteButton = document.createElement('button');
                            deleteButton.textContent = 'Delete';
                            deleteButton.classList.add('btn', 'btn-danger');
                            deleteButton.dataset.itemId = item.id;
                            deleteButton.addEventListener('click', deleteItem);
                            cellButtons.appendChild(deleteButton);
                        }

                        cell.appendChild(cellButtons);
                    }
                });
            });

        })
        .catch(error => {
            console.error('Error:', error);
        });
}

