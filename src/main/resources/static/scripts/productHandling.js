/*=====================
      Add item
======================*/
export function addItem() {
    const itemName = document.getElementById('itemName').value;
    const itemDescription = document.getElementById('itemDescription').value;
    const itemPrice = document.getElementById('itemPrice').value;
    const itemCategory = document.getElementById('itemSubCategory').value;

    const addItemData = {
        newProductName: itemName,
        newProductPrice: parseFloat(itemPrice),
        newProductDescription: itemDescription,
        newProductCategory: itemCategory
    };

    fetch('http://localhost:8080/product/handling/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(addItemData)
    })
    .then(response => {
        if (response.ok) {
            window.location.reload();
        } else {
            return response.json().then(data => {
                throw new Error(data.message || "An error occurred while adding the item");
            });
        }
    })
    .catch(error => {
        console.error(error);
    });
}

/*=====================
      Buy item 
=====================*/
export function buyItem(event) {
    const itemId = event.target.dataset.itemId;

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

    fetch(`http://localhost:8080/product/handling/${itemId}/delete`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (response.ok) {
            window.location.reload();
        }
    })
    .catch(error => {
        console.error(error);
    });
}
