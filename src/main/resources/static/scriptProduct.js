
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

    return `${baseUrl}/product/forSale`;
}

fetch(getApiUrl())
    .then(res => res.json())
    .then(data => {
        const productTable = document.getElementById('product-table');

        data.forEach(item => {
            const row = productTable.insertRow();

            const cellName = row.insertCell(0);
            const cellCreator = row.insertCell(1);
            const cellDescription = row.insertCell(2);
            const cellPrice = row.insertCell(3);
            const cellButtons = row.insertCell(4);

            cellName.textContent = item.name;
            cellCreator.textContent = item.creator;
            cellDescription.textContent = item.description;
            cellPrice.textContent = item.price;

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



/*=====================
fill site with users                      
  bought products
======================*/

//TODO make it fetch only if logged in

function createRequest(){

    const token = localStorage.getItem('token');

    const RequestUserBoughtProductList = {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + token,
            'Content-Type': 'application/json'
        }
    }
    return RequestUserBoughtProductList;
}

  fetch('http://localhost:8080/product/bought', createRequest())
    .then(res => {
      return res.json();
    })
    .then(data => {
        const DataList = document.getElementById('bought-product-list');

      data.forEach(item => {
        const Div = document.createElement('div');
        Div.classList.add('list-item');

        Div.textContent = `
          ${item.id}.
          ${item.name}
          Description: ${item.description}
          Creator: ${item.creator}
        `;
        DataList.appendChild(Div);
      });
    })
    .catch(error => {
      console.error('Error:', error);
  });