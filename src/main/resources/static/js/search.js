document.getElementById('searchInput').addEventListener('keypress', function(event) {
    if (event.key === 'Enter') {
      redirectToSearch();
    }
  });

  function redirectToSearch() {
    var searchQuery = document.getElementById('searchInput').value.trim();
    if (searchQuery.length > 0) {
      window.location.href = "/users/search/" + encodeURIComponent(searchQuery);
    }
    return false; // Para prevenir que el formulario haga submit y recargue la página
  }