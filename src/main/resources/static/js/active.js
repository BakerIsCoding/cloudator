document.addEventListener("DOMContentLoaded", function() {
    // Selecciona todos los elementos que contengan la clase 'active'
    document.querySelectorAll('.nav-item.menu-items.active').forEach(function(el) {
      el.classList.remove('active'); // Remueve la clase 'active'
    });
    document.querySelectorAll('.nav-item.menu-items').forEach(function(el) {
      if (el.querySelector('a').href === window.location.href) {
        el.classList.add('active'); // Agrega la clase 'active'
      }
    });
  });