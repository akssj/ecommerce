export function initSearchBar() {
  const form = document.getElementById('searchForm');
  if (!form) return;

  form.addEventListener('submit', async e => {
    e.preventDefault();
    const query = document.getElementById('searchInput').value.trim();
    if (!query) return;

    localStorage.setItem('lastQuery', query);
    window.location.href = '/search.html';
  });
}

export async function searchProducts(query) {
  const res = await fetch('/product/forSale');
  if (!res.ok) throw new Error('Błąd pobierania produktów');

  const products = await res.json();
  return products.filter(p =>
    p.name.toLowerCase().includes(query.toLowerCase())
  );
}
