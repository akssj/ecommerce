
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
  bought produscts                 
======================*/    //WNF
function createRequest(){ //TODO unnecessery at this point
  //username
  const Username = localStorage.getItem('username');
  //token
  const Token = localStorage.getItem('token');

  //create request data
  const UserDataCheck = {
    username: Username
  };

  const JsonUserDataCheck = JSON.stringify(UserDataCheck);

  //create request
  const RequestUserBoughtProductList = {
    method: 'POST',
    headers: {
      'Authorization': 'Bearer '+ Token,
      'Content-Type': 'application/json'
    },
    body: JsonUserDataCheck
  };
  return RequestUserBoughtProductList;
}
  //send request to check login status
  fetch('http://localhost:8080/api/product/bought', createRequest())
    .then(res => {
      return res.json();
    })
    .then(data => {

        //target
        const DataList = document.getElementById('bought-product-list');
        //text panel
        document.getElementById('boughtItemsTextField').textContent = "Kupione przedmioty"; //kek fix this shit

      data.forEach(item => {
        const Div = document.createElement('div');
        Div.classList.add('list-item');

        //create item data
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