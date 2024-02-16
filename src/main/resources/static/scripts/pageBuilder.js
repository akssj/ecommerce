import { switchAccountDropDown, setCookie, getCookie } from './utility.js';

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
    const dropdownContainer = document.getElementById("categoryContainer");
    if (!dropdownContainer) return;

    fetch("http://localhost:8080/product/categories")
        .then(response => {
            if (!response.ok) {
                throw new Error("Failed to fetch categories");
            }
            return response.json();
        })
        .then(categories => {
            categories.forEach(category => {
                const div = document.createElement("div");
                div.classList.add("d-grid", "gap-2", "dropend");

                const button = document.createElement("button");
                button.type = "button";
                button.classList.add("btn", "btn-primary", "dropdown-toggle-text", "bg-transparent", "text-dark", "border-black", "square-btn");
                button.setAttribute("data-bs-toggle", "dropdown");
                button.innerText = category.name;

                const ul = document.createElement("ul");
                ul.classList.add("dropdown-menu");

                category.subCategories.forEach(subCategory => {
                    const li = document.createElement("li");
                    const a = document.createElement("a");
                    a.classList.add("dropdown-item");
                    a.href = `/category/${subCategory}`;
                    a.innerText = subCategory;
                    li.appendChild(a);
                    ul.appendChild(li);
                });

                div.appendChild(button);
                div.appendChild(ul);
                dropdownContainer.appendChild(div);
            });
        })
        .catch(error => {
            console.error(error);
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
        if (!response.ok) {
            throw new Error('Failed to fetch user data');
        }
        return response.json();
    })
    .then(data => {
        document.getElementById('username').textContent = data.username;
        document.getElementById('email').textContent = data.email;
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

export function fetchAndCacheModalContent(modalUrl, storageKey, modalId) {
    if (window.location.href === 'http://localhost:8080/my-profile') {
        const modalContent = localStorage.getItem(storageKey);
        if (modalContent) {
            document.getElementById(modalId).querySelector('.modal-content').innerHTML = modalContent;
            return;
        }

        fetch(modalUrl)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch modal content');
                }
                return response.text();
            })
            .then(modalHtml => {
                document.getElementById(modalId).querySelector('.modal-content').innerHTML = modalHtml;
                localStorage.setItem(storageKey, modalHtml);
            })
            .catch(error => {
                console.error('Error fetching modal content:', error);
            });
    }
}





