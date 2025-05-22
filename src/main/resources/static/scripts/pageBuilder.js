import { switchAccountDropDown, setCookie, getCookie, refreshAccessToken } from './utility.js';

export function loadNavbar() {
    return new Promise((resolve, reject) => {
        const xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    document.getElementById('navbarPlaceholder').innerHTML = xhr.responseText;
                    resolve();
                } else {
                    reject(new Error('Failed to load navbar'));
                }
            }
        };
        xhr.open('GET', '/static/component/navbar.html', true);
        xhr.send();
    });
}

export function loadCategories() {
    const categoryContainer = document.getElementById("categoryContainer");
    const addItemModal = document.getElementById("addItemModal");

    fetch("http://localhost:8080/product/categories")
        .then(response => {
            if (!response.ok) {
                throw new Error("Failed to fetch categories");
            }
            return response.json();
        })
        .then(categories => {
            switch (true) {
                case !!categoryContainer:
                    updateCategoryContainer(categories);
                    break;
                case !!addItemModal:
                    updateAddItemModal(categories);
                    break;
                default:
                    break;
            }
        })
        .catch(error => {
            console.error("Error loading categories:", error);
        });
}

function updateCategoryContainer(categories) {
    const categoryElements = categories.map(category => {
        const subCategoryElements = category.subCategories.map(subCategory => {
            return `<li><a class="dropdown-item" href="/category/${subCategory}">${subCategory}</a></li>`;
        }).join('');

        return `
            <div class="d-grid gap-2 dropend">
                <button type="button" class="btn btn-primary dropdown-toggle-text bg-transparent text-dark border-black square-btn" data-bs-toggle="dropdown">
                    ${category.name}
                </button>
                <ul class="dropdown-menu">
                    ${subCategoryElements}
                </ul>
            </div>
        `;
    }).join('');

    categoryContainer.innerHTML = categoryElements;
}

function updateAddItemModal(categories) {
    const categorySelect = addItemModal.querySelector('#itemCategory');
    categorySelect.innerHTML = '';

    const defaultOption = document.createElement('option');
    defaultOption.value = '';
    defaultOption.innerText = 'Select a category';
    defaultOption.disabled = true;
    defaultOption.selected = true;
    categorySelect.appendChild(defaultOption);

    categories.forEach(category => {
        const categoryOption = document.createElement('option');
        categoryOption.value = category.name;
        categoryOption.innerText = category.name;
        categorySelect.appendChild(categoryOption);
    });

    categorySelect.addEventListener('change', () => {
        const selectedCategory = categorySelect.value;
        const selectedCategoryObject = categories.find(category => category.name === selectedCategory);

        if (selectedCategoryObject) {
            updateSubCategorySelect(selectedCategoryObject.subCategories);
        }
    });
}

function updateSubCategorySelect(subCategories) {
    const subCategorySelect = addItemModal.querySelector('#itemSubCategory');
    subCategorySelect.innerHTML = '';

    const defaultOption = document.createElement('option');
    defaultOption.value = '';
    defaultOption.innerText = 'Select a subcategory';
    defaultOption.disabled = true;
    defaultOption.selected = true;
    subCategorySelect.appendChild(defaultOption);

    subCategories.forEach(subCategory => {
        const subCategoryOption = document.createElement('option');
        subCategoryOption.value = subCategory;
        subCategoryOption.innerText = subCategory;
        subCategorySelect.appendChild(subCategoryOption);
    });
}

export function loadUserData() {
    const userDataContainer = document.getElementById('userDataContainer');
    if (!userDataContainer) {
        return;
    }

    fetch('http://localhost:8080/auth/userData', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({})
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        }else if (response.status === 401) {
            refreshAccessToken();
        }else{
            throw new Error('Failed to fetch user data');
        }
    })
    .then(data => {
        document.getElementById('username').textContent = data.username;
        document.getElementById('email').textContent = data.email;
    })
    .catch(error => {
        console.error('Error:', error);
    });
}







