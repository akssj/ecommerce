document.addEventListener('DOMContentLoaded', function () {
    updateButtonVisibility();
});

function updateButtonVisibility() {
    var logoutButton = document.getElementById('logoutButton');
    var addItemButton = document.getElementById('addItemButton');

    if (isUserLoggedIn()) {
        logoutButton.style.display = 'block';
        addItemButton.style.display = 'block';
    } else {
        logoutButton.style.display = 'none';
        addItemButton.style.display = 'none';
    }
}