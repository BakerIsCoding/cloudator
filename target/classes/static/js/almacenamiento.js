 // Datos de archivos subidos hoy (reemplazar con tus datos reales)
 var archivosSubidosData = {
    labels: ["Documentos", "Imagenes", "Videos", "Audios"],
    datasets: [{
        data: [12, 19, 3, 5], // Cantidad de archivos subidos hoy
        backgroundColor: [
            'rgba(255, 99, 132, 0.5)',
            'rgba(54, 162, 235, 0.5)',
            'rgba(255, 206, 86, 0.5)',
            'rgba(153, 102, 255, 0.5)'
        ],
        borderColor: [
            'rgba(255, 99, 132, 1)',
            'rgba(54, 162, 235, 1)',
            'rgba(255, 206, 86, 1)',
            'rgba(153, 102, 255, 1)'
        ],
        borderWidth: 1,
        labels: {
            color: 'white' // Cambiar el color del texto a negro
        }
    }]
};


if ($("#archivosSubidosChart").length) {
    var archivosSubidosChartCanvas = $("#archivosSubidosChart").get(0).getContext("2d");
    var archivosSubidosChart = new Chart(archivosSubidosChartCanvas, {
        type: 'bar',
        data: archivosSubidosData,
        options: {
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true
                    }
                }]
            },
            legend: {
                display: false // Ocultar la leyenda
            },
            // Configuración del plugin datalabels para el color del texto
            plugins: {
                datalabels: {
                    color: 'white' // Establecer el color del texto a blanco
                }
            }
        }
    });
}