
/*=====================
fill site with products
      on refresh
======================*/

function getApiUrl() {
    const baseUrl = window.location.origin;

    if (window.location.pathname.startsWith('/main')) {
        return `${baseUrl}/product/forSale`;
    }

    if (window.location.pathname.startsWith('/category')) {
        const category = window.location.pathname.split('/')[2];
        return `${baseUrl}/product/category/${category}`;
    }

    if (window.location.pathname.startsWith('/my-products')) {
        return `${baseUrl}/product/my-products`;
    }

    if (window.location.pathname.startsWith('/bought-products')) {
        return `${baseUrl}/product/bought-products`;
    }

    return `${baseUrl}/product/forSale`;
}

fetch(getApiUrl())
    .then(res => res.json())
    .then(data => {
        const productTable = document.getElementById('product-table');
        const headerRow = productTable.querySelector('thead tr');
        const columnNames = Array.from(headerRow.children).map(cell => cell.textContent);

        data.forEach(item => {
            const row = productTable.insertRow();

            columnNames.forEach((columnName, index) => {
                if (item.hasOwnProperty(columnName.toLowerCase())) {
                    const cell = row.insertCell(index);
                    cell.textContent = item[columnName.toLowerCase()];
                }
            });

            const cellButtons = row.insertCell(-1);

            const buttonDiv = document.createElement('div');
            buttonDiv.classList.add('btn-group');

            const buyButton = document.createElement('button');
            buyButton.textContent = 'Kup';
            buyButton.classList.add('btn', 'btn-success');
            buyButton.dataset.itemId = item.id;
            buyButton.addEventListener('click', buyItem);

            const deleteButton = document.createElement('button');
            deleteButton.textContent = 'Usuń';
            deleteButton.classList.add('btn', 'btn-danger');
            deleteButton.dataset.itemId = item.id;
            deleteButton.addEventListener('click', deleteItem);

            buttonDiv.appendChild(buyButton);
            buttonDiv.appendChild(deleteButton);
            cellButtons.appendChild(buttonDiv);
        });
    })
    .catch(error => {
        console.error('Error:', error);
    });

