export function loadNavbar() {
  $(document).ready(function () {
    $.get("/static/navbar.html", function (data) {
      $("#navbarPlaceholder").html(data);
    });
  });
}

export function loadCategories() {
  var dropdownContainer = document.getElementById("categoryContainer");
  if (!dropdownContainer) {
    return;
  }

  fetch("http://localhost:8080/product/categories")
    .then(response => response.json())
    .then(categories => {
      categories.forEach((category, index) => {
        var div = document.createElement("div");
        div.classList.add("d-grid", "gap-2", "dropend");

        var button = document.createElement("button");
        button.type = "button";
        button.classList.add("btn", "btn-primary", "dropdown-toggle");
        button.setAttribute("data-bs-toggle", "dropdown");
        button.setAttribute("aria-expanded", "false");
        button.innerText = category.name;

        var ul = document.createElement("ul");
        ul.classList.add("dropdown-menu");

        category.subCategories.forEach(subCategory => {
          var li = document.createElement("li");
          var a = document.createElement("a");
          a.classList.add("dropdown-item");
          a.href = "/category/" + subCategory;
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