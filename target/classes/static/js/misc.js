(function($) {
  'use strict';
  $(function() {
    var body = $('body');
    var sidebar = $('.sidebar');

    // Agrega la clase activa a nav-link basado en la URL de manera dinámica
    // La clase activa también puede ser codificada directamente en el archivo HTML según sea necesario

    function addActiveClass(element) {
      var current = location.pathname.split("/").slice(-2)[0].replace(/^\/|\/$/g, '');
      if (current == "users" || current == "admin") {
        // para las páginas principales "users" o "admin"
        if (element.attr('href').indexOf(current) !== -1 && !$('.nav-item.active').length) {
            element.parents('.nav-item').last().addClass('active');
            if (element.parents('.sub-menu').length) {
                element.closest('.collapse').addClass('show');
                element.addClass('active');
            }
        }
      } else {
        // para las demás páginas
        if (element.attr('href').indexOf(current) !== -1) {
            element.parents('.nav-item').last().addClass('active');
            if (element.parents('.sub-menu').length) {
                element.closest('.collapse').addClass('show');
                element.addClass('active');
            }
              if (element.parents('.submenu-item').length) {
                element.addClass('active');
              }
        }
      }
    }
    
    
    $('.nav li a', sidebar).each(function() {
      var $this = $(this);
      addActiveClass($this);
    })

    $('.horizontal-menu .nav li a').each(function() {
      var $this = $(this);
      addActiveClass($this);
    })

    //Cierra otros elementos de menú cuando se abre uno

    sidebar.on('show.bs.collapse', '.collapse', function() {
      sidebar.find('.collapse.show').collapse('hide');
    });


    // Cambia la altura de la barra lateral y del contenedor de contenido
    applyStyles();

    function applyStyles() {
      // Aplicando perfect scrollbar
      if (!body.hasClass("rtl")) {
        if ($('.settings-panel .tab-content .tab-pane.scroll-wrapper').length) {
          const settingsPanelScroll = new PerfectScrollbar('.settings-panel .tab-content .tab-pane.scroll-wrapper');
        }
        if ($('.chats').length) {
          const chatsScroll = new PerfectScrollbar('.chats');
        }
        if (body.hasClass("sidebar-fixed")) {
          var fixedSidebarScroll = new PerfectScrollbar('#sidebar .nav');
        }
      }
    }

    $('[data-toggle="minimize"]').on("click", function() {
      if ((body.hasClass('sidebar-toggle-display')) || (body.hasClass('sidebar-absolute'))) {
        body.toggleClass('sidebar-hidden');
      } else {
        body.toggleClass('sidebar-icon-only');
      }
    });

    //checkbox y radios
    $(".form-check label,.form-radio label").append('<i class="input-helper"></i>');

    //pantalla completa
    $("#fullscreen-button").on("click", function toggleFullScreen() {
      if ((document.fullScreenElement !== undefined && document.fullScreenElement === null) || (document.msFullscreenElement !== undefined && document.msFullscreenElement === null) || (document.mozFullScreen !== undefined && !document.mozFullScreen) || (document.webkitIsFullScreen !== undefined && !document.webkitIsFullScreen)) {
        if (document.documentElement.requestFullScreen) {
          document.documentElement.requestFullScreen();
        } else if (document.documentElement.mozRequestFullScreen) {
          document.documentElement.mozRequestFullScreen();
        } else if (document.documentElement.webkitRequestFullScreen) {
          document.documentElement.webkitRequestFullScreen(Element.ALLOW_KEYBOARD_INPUT);
        } else if (document.documentElement.msRequestFullscreen) {
          document.documentElement.msRequestFullscreen();
        }
      } else {
        if (document.cancelFullScreen) {
          document.cancelFullScreen();
        } else if (document.mozCancelFullScreen) {
          document.mozCancelFullScreen();
        } else if (document.webkitCancelFullScreen) {
          document.webkitCancelFullScreen();
        } else if (document.msExitFullscreen) {
          document.msExitFullscreen();
        }
      }
    });
  });
})(jQuery);