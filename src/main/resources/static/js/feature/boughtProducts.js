export async function fetchBoughtOrders() {
  const response = await fetch('/orders/bought', {
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include'
  });

  if (!response.ok) {
    throw new Error('Błąd podczas pobierania zakupionych przedmiotów');
  }

  return await response.json();
}

export function renderBoughtTable(orders, tableEl) {
  const tbody = tableEl.querySelector('tbody');
  tbody.innerHTML = '';

  orders.forEach(order => {
    const row = tbody.insertRow();

    row.insertCell().textContent = order.name;
    row.insertCell().textContent = order.seller;
    row.insertCell().textContent = order.description;
    row.insertCell().textContent = order.price.toFixed(2) + ' zł';
    row.insertCell().textContent = `${order.country}, ${order.city}, ${order.street}, ${order.postalCode}`;
    row.insertCell().textContent = order.paymentMethod;
    row.insertCell().textContent = new Date(order.purchaseDate).toLocaleString();
  });
}
