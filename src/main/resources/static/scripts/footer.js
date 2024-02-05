//Slouží k ukotvení footeru ke spodní straně obrazu, pokud je nedostatečná výška obsahu nad ním
window.onload = function () {
    let headerHeight = document.querySelector("nav").offsetHeight;
    let articleHeight = document.querySelector("article").offsetHeight;

    let contentHeigth = headerHeight + articleHeight;

    let footer = document.querySelector("footer");

    if (contentHeigth < window.innerHeight) {
        footer.style.position = "fixed";
        footer.style.bottom = "0";
    }

}