
/*=====================
      Add item 
======================*/

export function addItem() {
  const ItemName = document.getElementById('item-name-input').value;
  const ItemDescription = document.getElementById('item-description-input').value;
  const ItemPrice = document.getElementById('item-price-input').value;

  const Token = localStorage.getItem('token');

  const AddItemErrorTextField = document.getElementById('add-item-error-text-field');

  try{
    if (Token === null) {
      AddItemErrorTextField.innerText = "You are not logged in";
      throw new Error("You are not logged in");
    }
    if (ItemName === "" || ItemDescription === "" || ItemPrice === "") {
      AddItemErrorTextField.innerText = "Fill empty filed";
      throw new Error("Fill empty filed");
    }
  }catch(error){
    return;
  }
  
  const AddItemData = {
    name: ItemName,
    price: ItemPrice,
    description: ItemDescription
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

  fetch('http://localhost:8080/product/handling/add', RequestAddItem)
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

export function buyItem(event) {
    const itemId = event.target.dataset.itemId;
    const token = getCookie('token');

    try {
        if (token === null) {
            throw new Error("You are not logged in");
        }
        if (itemId === null) {
            throw new Error("Item does not exist!");
        }
    } catch (error) {
        console.error(error);
        return;
    }

    const requestBuyItem = {
        method: 'PUT',
        headers: {
            'Authorization': 'Bearer ' + token,
            'Content-Type': 'application/json'
        },
    };

    fetch(`http://localhost:8080/product/handling/${itemId}/buy`, requestBuyItem)
        .then(response => {
            if (response.ok) {
            }
        })
        .catch(error => {
            console.error(error);
        });
}

/*=====================
      Delete item 
======================*/

export function deleteItem(event){
  const ItemId = event.target.dataset.itemId;

  const Token = localStorage.getItem('token');

  try{
    if (Token === null) {
      throw new Error("You are not logged in");
    }
    if (ItemId === null) {
      throw new Error("Item no longer exists");
    }
  }catch(error){
    return;
  }
  const RequestDeleteItem = {
    method: 'DELETE',
    headers: {
      'Authorization': 'Bearer '+ Token,
      'Content-Type': 'application/json'
    },
  };

  fetch(`http://localhost:8080/product/handling/${ItemId}/delete`, RequestDeleteItem)
  .then(response => {
    if (response.ok) {
    window.location.reload(); //TODO make so it makes more sense and respond with message
    }
  })
  .catch(error => {
    console.error(error);
  });
}
