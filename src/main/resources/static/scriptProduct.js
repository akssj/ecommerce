
/*=====================
fill site with products
      on refresh
======================*/

fetch('http://localhost:8080/product/forSale')
  .then(res => {
    return res.json();
  })
  .then(data => {
    const ProductList = document.getElementById('product-list');

    data.forEach(item => {
      const Div = document.createElement('div');
      Div.classList.add('list-item', 'card', 'mb-3');

      const CardBody = document.createElement('div');
      CardBody.classList.add('card-body');

      const Title = document.createElement('h5');
      Title.classList.add('card-title');
      Title.textContent = `${item.name}`;

      const Creator = document.createElement('p');
      Creator.classList.add('card-text');
      Creator.textContent = `Creator: ${item.creator}`;

      const Description = document.createElement('p');
      Description.classList.add('card-text');
      Description.textContent = `Description: ${item.description}`;

      const Price = document.createElement('p');
      Price.classList.add('card-text');
      Price.textContent = `Price: ${item.price}`;

      const BuyButton = document.createElement('button');
      BuyButton.textContent = 'Kup';
      BuyButton.classList.add('btn', 'btn-success', 'mr-2');
      BuyButton.dataset.itemId = item.id;
      BuyButton.addEventListener('click', buyItem);

      const DeleteButton = document.createElement('button');
      DeleteButton.textContent = 'Usuń';
      DeleteButton.classList.add('btn', 'btn-danger');
      DeleteButton.dataset.itemId = item.id;
      DeleteButton.addEventListener('click', deleteItem);

      CardBody.appendChild(Title);
      CardBody.appendChild(Creator);
      CardBody.appendChild(Description);
      CardBody.appendChild(Price);
      CardBody.appendChild(BuyButton);
      CardBody.appendChild(DeleteButton);

      Div.appendChild(CardBody);
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