
/*=====================
      Add item 
======================*/

function addItem() {
  const ItemName = document.getElementById('item-name-input').value;
  const ItemDescription = document.getElementById('item-description-input').value;
  const ItemPrice = document.getElementById('item-price-input').value;

  const Username = localStorage.getItem('username');
  const Token = localStorage.getItem('token');

  const AddItemErrorTextField = document.getElementById('add-item-error-text-field');

  try{
    if (Username === null || Token === null) {
      AddItemErrorTextField.innerText = "You are not logged in";
      throw new Error("You are not logged in");
    }
    if (ItemName === "" || ItemDescription === "" || ItemPrice === "") {
      AddItemErrorTextField.innerText = "Fill empty fileds";
      throw new Error("Fill empty fileds");
    }
  }catch(error){
    return;
  }
  
  const AddItemData = {
    name: ItemName,
    price: ItemPrice,
    description: ItemDescription,
    creator_username: Username
  };

  const JsonAddItemData = JSON.stringify(AddItemData);

  const RequestAddItem = {
    method: 'POST',
    headers: {
      'Authorization': 'Bearer '+ Token,
      'Content-Type': 'application/json'
    },
    body: JsonAddItemData
  };

  fetch('http://localhost:8080/api/product/add', RequestAddItem)
  .then(response => {
    if (response.ok) {
      window.location.reload(); //TODO make so it makes more sense
      return response.json();
    } else {
      return response.json().then( data => {
        const errorMessage = data.message;
        AddItemErrorTextField.innerText = errorMessage;
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
=====================*/

function buyItem(event){
  const ItemId = event.target.dataset.itemId;

  const Username = localStorage.getItem('username');
  const Token = localStorage.getItem('token');

  try{
    if (Username === null || Token === null) {
      throw new Error("You are not logged in");
    }
    if (ItemId === null) {
      throw new Error("Item no longer exists");
    }
  }catch(error){
    return;
  }

  const BuyItemData = {
    item_id: ItemId,
    buyer_username: Username
  };

  const JsonBuyItemData = JSON.stringify(BuyItemData);

  const RequestBuyItem = {
    method: 'PUT',
    headers: {
      'Authorization': 'Bearer '+ Token,
      'Content-Type': 'application/json'
    },
    body: JsonBuyItemData
  };

  fetch(`http://localhost:8080/api/product/buyout/${ItemId}`, RequestBuyItem)
  .then(response => {
    if (response.ok) {
    performUserStatusCheck();
    }
  })
  .catch(error => {
    console.error(error);
  });
}

/*=====================
      Delete item 
======================*/

function deleteItem(event){
  const ItemId = event.target.dataset.itemId;

  const Username = localStorage.getItem('username');
  const Token = localStorage.getItem('token');

  try{
    if (Username === null || Token === null) {
      throw new Error("You are not logged in");
    }
    if (ItemId === null) {
      throw new Error("Item no longer exists");
    }
  }catch(error){
    return;
  }

  const DeleteItemData = {
    item_id: ItemId,
    creator_username: Username
  };

  const JsonDeleteItemData = JSON.stringify(DeleteItemData);

  const RequestDeleteItem = {
    method: 'DELETE',
    headers: {
      'Authorization': 'Bearer '+ Token,
      'Content-Type': 'application/json'
    },
    body: JsonDeleteItemData
  };

  fetch(`http://localhost:8080/api/product/delete/${ItemId}`, RequestDeleteItem)
  .then(response => {
    if (response.ok) {
    window.location.reload(); //TODO make so it makes more sense
    }
  })
  .catch(error => {
    console.error(error);
  });
}
