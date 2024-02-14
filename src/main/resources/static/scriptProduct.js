
/*=====================
fill site with products
      on refresh
======================*/

function getApiUrl() {
    const baseUrl = window.location.origin;
    let apiUrl;

    switch (true) {
        case window.location.pathname.startsWith('/main'):
            apiUrl = `${baseUrl}/product/forSale`;
            break;
        case window.location.pathname.startsWith('/category'):
            const category = window.location.pathname.split('/')[2];
            apiUrl = `${baseUrl}/product/category/${category}`;
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

function fillProducts() {
    fetch(getApiUrl())
        .then(res => {
            if (!res.ok) {
                throw new Error('Network response was not ok');
            }
            return res.json();
        })
        .then(data => {
            const productTable = document.getElementById('product-table');
            const headerRow = productTable.querySelector('thead tr');
            const columnNames = Array.from(headerRow.children).map(cell => cell.textContent);
            const hasActionsColumn = columnNames.includes('Actions');
            const currentUser = getCookie('username');

            data.forEach(item => {
                const row = productTable.insertRow();

                columnNames.forEach((columnName, index) => {
                    if (item.hasOwnProperty(columnName.toLowerCase())) {
                        const cell = row.insertCell(index);
                        cell.textContent = item[columnName.toLowerCase()];
                    }
                });

                if (hasActionsColumn) {
                    const cellButtons = row.insertCell(-1);

                    const buttonDiv = document.createElement('div');
                    buttonDiv.classList.add('btn-group');

                    if(item.creator !== currentUser){
                        const buyButton = document.createElement('button');
                        buyButton.textContent = 'Kup';
                        buyButton.classList.add('btn', 'btn-success');
                        buyButton.dataset.itemId = item.id;
                        buyButton.addEventListener('click', buyItem);
                        buttonDiv.appendChild(buyButton);
                    }

                    if (item.creator == currentUser && item.buyer == '') {
                        const deleteButton = document.createElement('button');
                        deleteButton.textContent = 'Usuń';
                        deleteButton.classList.add('btn', 'btn-danger');
                        deleteButton.dataset.itemId = item.id;
                        deleteButton.addEventListener('click', deleteItem);
                        buttonDiv.appendChild(deleteButton);
                    }

                    cellButtons.appendChild(buttonDiv);
                }
            });
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

