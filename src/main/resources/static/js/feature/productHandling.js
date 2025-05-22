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
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(addItemData)
    })
        .then(response => {
            if (response.ok) {
                alert('Dodano przedmiot!');
                window.location.reload();
            } else {
                return response.json().then(data => {
                    throw new Error(data.message || "An error occurred while adding the item");
                });
            }
        })
        .catch(error => alert(error.message));
}

/*=====================
      Buy item (stare)
======================*/
// NIE UŻYWAJ: zostaje tylko do porównania
export function buyItem(event) {
    const itemId = event.target.dataset.itemId;
    openBuyModal(itemId); // zamień na modal zakupu
}

/*=====================
      Delete item 
======================*/
export function deleteItem(event) {
    const itemId = event.target.dataset.itemId;

    const confirmBox = confirm("Czy na pewno chcesz usunąć ten produkt?");
    if (!confirmBox) return;

    fetch(`http://localhost:8080/product/handling/${itemId}/delete`, {
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' }
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(msg => { throw new Error(msg); });
            } else {
                alert('Usunięto przedmiot!');
                window.location.reload();
            }
        })
        .catch(error => alert(error.message));
}

/*=========================
      Edit modal setup
=========================*/
export function showEditModal(item) {
    const modal = document.getElementById('editItemModal');
    if (!modal) return;

    modal.querySelector('#editItemId').value = item.id;
    modal.querySelector('#editItemName').value = item.name;
    modal.querySelector('#editItemPrice').value = item.price;
    modal.querySelector('#editItemDescription').value = item.description;
    modal.querySelector('#editItemCategory').value = item.category;

    const bootstrapModal = new bootstrap.Modal(modal);
    bootstrapModal.show();
}

/*=========================
      Update item
=========================*/
export function updateItem() {
    const id = document.getElementById('editItemId').value;
    const updatedData = {
        name: document.getElementById('editItemName').value,
        price: parseFloat(document.getElementById('editItemPrice').value),
        description: document.getElementById('editItemDescription').value,
        category: document.getElementById('editItemCategory').value
    };

    fetch(`http://localhost:8080/product/handling/${id}/update`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updatedData)
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(msg => { throw new Error(msg); });
            } else {
                const modal = bootstrap.Modal.getInstance(document.getElementById('editItemModal'));
                modal.hide();
                alert('Edytowano przedmiot!');
                window.location.reload();
            }
        })
        .catch(error => alert(error.message));
}

/*=========================
 Fetch products owned by current user
==========================*/
export function fetchMyProducts() {
    return fetch('/product/my-products')
        .then(res => {
            if (!res.ok) throw new Error('Błąd pobierania produktów użytkownika');
            return res.json();
        });
}

/*=========================
     Modal zakupu z adresem
==========================*/
let selectedProductId = null;

export function openBuyModal(productId) {
    selectedProductId = productId;
    const modal = new bootstrap.Modal(document.getElementById('purchaseModal'));
    modal.show();
}

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('purchaseForm');
    if (!form) return;

    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        const orderData = {
            productId: selectedProductId,
            country: document.getElementById('shippingCountry').value,
            city: document.getElementById('shippingCity').value,
            street: document.getElementById('shippingStreet').value,
            postalCode: document.getElementById('shippingZip').value,
            paymentMethod: document.querySelector('input[name="paymentMethod"]:checked').value
        };

        // ✅ WALIDACJA przed wysłaniem
        if (!orderData.productId || !orderData.country || !orderData.city || !orderData.street || !orderData.postalCode || !orderData.paymentMethod) {
            alert("Wypełnij wszystkie pola, zanim złożysz zamówienie.");
            return;
        }

        console.log("Dane do zakupu:", orderData);

        // W tym miejscu możesz wysłać dane na backend, jeśli chcesz
        fetch('/orders', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify(orderData)
        })
            .then(res => {
                if (!res.ok) throw new Error("Błąd składania zamówienia");
                return res.json();
            })
            .then(() => {
                alert('Zamówienie zostało złożone!');
                window.location.reload();
            })
            .catch(err => alert(err.message));

        const modalEl = document.getElementById('purchaseModal');
        bootstrap.Modal.getInstance(modalEl).hide();
        form.reset();
    });
});
