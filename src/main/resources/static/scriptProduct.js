
/*=====================
fill site with products
      on refresh
======================*/

  fetch('http://localhost:8080/product/forSale')
    .then(res => {
      return res.json();
    })
    .then(data => {
      const ProductTable = document.getElementById('product-table');

      data.forEach(item => {
        const row = ProductTable.insertRow();

        const cellName = row.insertCell(0);
        const cellCreator = row.insertCell(1);
        const cellDescription = row.insertCell(2);
        const cellPrice = row.insertCell(3);
        const cellButtons = row.insertCell(4);

        cellName.textContent = item.name;
        cellCreator.textContent = item.creator;
        cellDescription.textContent = item.description;
        cellPrice.textContent = item.price;

        const buyButton = document.createElement('button');
        buyButton.textContent = 'Kup';
        buyButton.classList.add('btn', 'btn-success', 'mr-2');
        buyButton.dataset.itemId = item.id;
        buyButton.addEventListener('click', buyItem);

        const deleteButton = document.createElement('button');
        deleteButton.textContent = 'Usuń';
        deleteButton.classList.add('btn', 'btn-danger');
        deleteButton.dataset.itemId = item.id;
        deleteButton.addEventListener('click', deleteItem);

        cellButtons.appendChild(buyButton);
        cellButtons.appendChild(deleteButton);
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