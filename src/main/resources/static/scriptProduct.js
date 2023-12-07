
/*=====================
fill site with products
      on refresh
======================*/

fetch('http://localhost:8080/api/product/forSale')
  .then(res => {
    return res.json();
  })
  .then(data => {
    const ProductList = document.getElementById('product-list');

    data.forEach(item => {
      const Div = document.createElement('div');
      Div.classList.add('list-item');

      Div.textContent = `
        ${item.id}.
        ${item.name}
        Price: ${item.price}
        Description: ${item.description}
        Creator: ${item.creator}
      `;
      
      const BuyButton = document.createElement('button');
      BuyButton.textContent = 'Kup';
      BuyButton.classList.add('buy-item-button');
      BuyButton.id = ('buy-item-button');
      BuyButton.dataset.itemId = item.id;
      BuyButton.addEventListener('click', buyItem);
      
      const DeleteButton = document.createElement('button');
      DeleteButton.textContent = 'Usuń';
      DeleteButton.classList.add('delete-item-button');
      DeleteButton.id = ('delete-item-button');
      DeleteButton.dataset.itemId = item.id;
      DeleteButton.addEventListener('click', deleteItem);

      Div.appendChild(BuyButton);
      Div.appendChild(DeleteButton);
      ProductList.appendChild(Div);
    });
  })
  .catch(error => {
    console.error('Error:', error);
  });

/*=====================
fill site with users                      
  bought products
======================*/
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

  fetch('http://localhost:8080/api/product/bought', createRequest())
    .then(res => {
      return res.json();
    })
    .then(data => {
        const DataList = document.getElementById('bought-product-list');
        document.getElementById('boughtItemsTextField').textContent = "Kupione przedmioty";

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