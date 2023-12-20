function createProductPopup() {
  var existingPopup = document.getElementById('add-item-popup');

  if (existingPopup) {
    existingPopup.style.display = 'block';
    return;
  }

  var addItemPopup = document.createElement('div');
  addItemPopup.id = 'add-item-popup';
  addItemPopup.className = 'popup';

  var closeButton = document.createElement('a');
  closeButton.href = '#';
  closeButton.className = 'popup-close-button';
  closeButton.innerHTML = 'X';
  closeButton.onclick = function() {
    closePopups('add-item-popup', 'add-item-error-text-field');
  };

  var heading = document.createElement('h2');
  heading.innerHTML = 'Add item for sale';

  var itemNameInput = document.createElement('input');
  itemNameInput.type = 'text';
  itemNameInput.placeholder = 'Item name';
  itemNameInput.id = 'item-name-input';

  var itemDescriptionInput = document.createElement('input');
  itemDescriptionInput.type = 'text';
  itemDescriptionInput.placeholder = 'Item description';
  itemDescriptionInput.id = 'item-description-input';

  var itemPriceInput = document.createElement('input');
  itemPriceInput.type = 'number';
  itemPriceInput.placeholder = 'price';
  itemPriceInput.id = 'item-price-input';

  var addItemButton = document.createElement('a');
  addItemButton.href = '#';
  addItemButton.className = 'button';
  addItemButton.innerHTML = 'Sell';
  addItemButton.onclick = function() {
    addItem();
  };

  var errorTextField = document.createElement('a');
  errorTextField.id = 'add-item-error-text-field';

  addItemPopup.appendChild(closeButton);
  addItemPopup.appendChild(heading);
  addItemPopup.appendChild(itemNameInput);
  addItemPopup.appendChild(itemDescriptionInput);
  addItemPopup.appendChild(itemPriceInput);
  addItemPopup.appendChild(addItemButton);
  addItemPopup.appendChild(errorTextField);

  document.body.appendChild(addItemPopup);
}