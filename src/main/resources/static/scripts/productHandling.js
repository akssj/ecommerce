/*=====================
      Add item
======================*/

export function addItem() {
    const itemName = document.getElementById('item-name-input').value;
    const itemDescription = document.getElementById('item-description-input').value;
    const itemPrice = document.getElementById('item-price-input').value;
    const addItemErrorTextField = document.getElementById('add-item-error-text-field');

    try {
        if (!token) {
            throw new Error("You are not logged in");
        }
        if (!itemName || !itemDescription || !itemPrice) {
            throw new Error("Fill in all fields");
        }
    } catch (error) {
        addItemErrorTextField.innerText = error.message;
        console.error(error);
        return;
    }

    const addItemData = {
        name: itemName,
        price: itemPrice,
        description: itemDescription
    };

    fetch('http://localhost:8080/product/handling/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(addItemData)
    })
    .then(response => {
        if (response.ok) {
            window.location.reload(); //TODO make so it makes more sense
        } else {
            return response.json().then(data => {
                throw new Error(data.message || "An error occurred while adding the item");
            });
        }
    })
    .catch(error => {
        addItemErrorTextField.innerText = error.message;
        console.error(error);
    });
}

/*=====================
      Buy item 
=====================*/

export function buyItem(event) {
    const itemId = event.target.dataset.itemId;
    const token = getCookie('token');

    try {
        if (!token) {
            throw new Error("You are not logged in");
        }
        if (!itemId) {
            throw new Error("Item does not exist!");
        }
    } catch (error) {
        console.error(error);
        return;
    }

    fetch(`http://localhost:8080/product/handling/${itemId}/buy`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .catch(error => {
        console.error(error);
    });
}

/*=====================
      Delete item 
======================*/

export function deleteItem(event) {
    const itemId = event.target.dataset.itemId;
    const token = localStorage.getItem('token');

    try {
        if (!token) {
            throw new Error("You are not logged in");
        }
        if (!itemId) {
            throw new Error("Item no longer exists");
        }
    } catch (error) {
        console.error(error);
        return;
    }

    fetch(`http://localhost:8080/product/handling/${itemId}/delete`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (response.ok) {
            window.location.reload(); //TODO make so it makes more sense and respond with message
        }
    })
    .catch(error => {
        console.error(error);
    });
}
