export function loadNavbar() {
  $(document).ready(function () {
    $.get("/static/navbar.html", function (data) {
      $("#navbarPlaceholder").html(data);
    });
  });
}