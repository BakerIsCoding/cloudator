$(document).ready(function () {
      $('#fileTable').DataTable({
        "language": {
          "url": "//cdn.datatables.net/plug-ins/1.11.5/i18n/es-ES.json"
        },
        "lengthMenu": [10, 25, 50, 100, { label: 'Todos', value: -1 }],
        "searching": true,
        "paging": true
      });
    });
    