
/*=====================
fill site with products
      on refresh
======================*/

fetch('http://localhost:8080/api/product/forSale')
  .then(res => {
    return res.json();
  })
  .then(data => {
      const dataList = document.getElementById('product-list');

      data.forEach(item => {
        const div = document.createElement('div');
        div.classList.add('list-item');

        //create item data
        div.textContent = `
          ${item.id}.
          ${item.name}
          Price: ${item.price}
          Description: ${item.description}
          Creator: ${item.creator}
        `;
        
        //create buy button
        const buybutton = document.createElement('button');
        buybutton.textContent = 'Kup';
        buybutton.classList.add('buy-item-button');
        buybutton.id = ('buy-item-button');
        buybutton.dataset.itemId = item.id;
        buybutton.addEventListener('click', buyOutRequest);
        
        //create delete button
        const button = document.createElement('button');
        button.textContent = 'Usuń';
        button.classList.add('delete-item-button');
        button.id = ('delete-item-button');
        button.dataset.itemId = item.id;
        button.addEventListener('click', deleteRequest);

        div.appendChild(buybutton);
        div.appendChild(button);
        dataList.appendChild(div);
      });
    })
    .catch(error => {
      console.error('Error:', error);
});

/*=====================
fill site with users
  bought produscts
======================*/
function createRequest(){
  //username
  const username = localStorage.getItem('username');
  //token
  const token = localStorage.getItem('token');

  //create request data
  const userDataCheck = {
    username: username
  };

  const jsonUserDataCheck = JSON.stringify(userDataCheck);

  //create request
  const requestuserBoughtProductList = {
    method: 'POST',
    headers: {
      'Authorization': 'Bearer '+ token,
      'Content-Type': 'application/json'
    },
    body: jsonUserDataCheck
  };
  return requestuserBoughtProductList;
}
  //send request to check login status
  fetch('http://localhost:8080/api/product/bought', createRequest())
    .then(res => {
      return res.json();
    })
    .then(data => {

        //target
        const dataList = document.getElementById('bought-product-list');
        //text panel
        document.getElementById('boughtItemsTextField').textContent= "Kupione przedmioty";

      data.forEach(item => {
        const div = document.createElement('div');
        div.classList.add('list-item');

        //create item data
        div.textContent = `
          ${item.id}.
          ${item.name}
          Description: ${item.description}
          Creator: ${item.creator}
        `;
        dataList.appendChild(div);
      });
    })
    .catch(error => {
      console.error('Error:', error);
  });

/*=====================
      Add item 
======================*/

function addItem() {
  
  //item Name
  const itemNameInput = document.getElementById('itemNameInput').value;
  //item Description
  const itemDescriptionInput = document.getElementById('itemDescriptionInput').value;
  //item Price
  const itemPriceInput = document.getElementById('itemPriceInput').value;

  //username
  const username = localStorage.getItem('username');
  //token
  const token = localStorage.getItem('token');

  //error message field
  const addItemErrorTextField = document.getElementById('addItemErrorTextField');

  //check if logged in
  if (username === null || token === null) {
    addItemErrorTextField.innerText = 'You are not logged in';
  }

  //check if filed !empty
  if (itemNameInput === null || itemDescriptionInput === null || itemPriceInput === null) {
    addItemErrorTextField.innerText = 'Fill empty fileds';
    return;
  }

  //create request data
  const addItemData = {
    name: itemNameInput,
    price: itemPriceInput,
    description: itemDescriptionInput,
    creator_username: username
  };

  const jsonAddItemData = JSON.stringify(addItemData);

  //create request
  const requestAddItem = {
    method: 'POST',
    headers: {
      'Authorization': 'Bearer '+ token,
      'Content-Type': 'application/json'
    },
    body: jsonAddItemData
  };

   //send request to add item
   fetch('http://localhost:8080/api/product/add', requestAddItem)
   .then(response => {
     if (response.ok) {
       window.location.reload();
       return response.json();
     }else{
       return response.json().then( data => {
         const errorMessage = data.message;
         errorElement.innerText = errorMessage;
         throw new Error(errorMessage);
       });
     }
   })
   .catch(error => {
     console.error(error);
   });
 };

 /*=====================
      Buy item 
======================*/

async function buyOutRequest(event){
  try {
    //item id
    const itemId = event.target.dataset.itemId;
    //username
    const username = localStorage.getItem('username');
    //token
    const token = localStorage.getItem('token');

    //create request data
    const buyOutRequestData = {
      item_id: itemId,
      buyer_username: username
    };

    const jsonbuyOutRequestData = JSON.stringify(buyOutRequestData);

     //create request
     const requestBuyOutCheck = {
      method: 'PUT',
      headers: {
        'Authorization': 'Bearer '+ token,
        'Content-Type': 'application/json'
      },
      body: jsonbuyOutRequestData
    };

    //send request to check login status
    //const deleteResponse = await 
    await fetch(`http://localhost:8080/api/product/buyout/${itemId}`, requestBuyOutCheck)
    .then(response => {
      if (response.ok) {
      console.log(`Item ${itemId} bought successfully.`);
      performUserStatusCheck();
      }
    })
    .catch(error => {
      console.error(error);
    });
  }catch{
    console.error(error);
  }
}

/*=====================
      Delete item 
======================*/

async function deleteRequest(event){
  try {
    //item id
    const itemId = event.target.dataset.itemId;
    //username
    const username = localStorage.getItem('username');
    //token
    const token = localStorage.getItem('token');

    //create request data
    const deleteRequestData = {
      item_id: itemId,
      creator_username: username
    };

    const jsondeleteRequestData = JSON.stringify(deleteRequestData);

     //create request
     const requestDeleteCheck = {
      method: 'DELETE',
      headers: {
        'Authorization': 'Bearer '+ token,
        'Content-Type': 'application/json'
      },
      body: jsondeleteRequestData
    };

    //send request to check login status
    //const deleteResponse = await 
    await fetch(`http://localhost:8080/api/product/delete/${itemId}`, requestDeleteCheck)
    .then(response => {
      if (response.ok) {
      console.log(`Item ${itemId} delete successfully.`);
      window.location.reload();
      }
    })
    .catch(error => {
      console.error(error);
    });
  }catch{
    console.error(error);
  }
}
