
document.addEventListener('DOMContentLoaded', function () {
    var espacioOcupado = document.getElementById('espacioOcupado').innerText;
    var espacioLibre = document.getElementById('espacioLibre').innerText;

    var data = {
        labels: ["Espacio Ocupado", "Espacio Libre"],
        datasets: [{
            data: [espacioOcupado, espacioLibre],
            backgroundColor: [
                'rgba(255, 0, 0, 0.5)',  // rojo semi-transparente para "Espacio Ocupado"
                'rgba(0, 255, 127, 0.5)' // verde menta semi-transparente para "Espacio Libre"
            ],
            borderColor: [
                'rgba(255, 0, 0, 1)',  // rojo para el borde de "Espacio Ocupado"
                'rgba(0, 255, 127, 1)' // verde menta para el borde de "Espacio Libre"
            ],
            borderWidth: 1
        }]
    };

    var options = {
        cutoutPercentage: 70,
        legend: {
            display: true,
            position: 'bottom',
            labels: {
                fontColor: 'white' // texto blanco para las etiquetas de la leyenda
            }
        }
    };

    // Asegúrate de que el elemento canvas exista en tu HTML
    if ($("#doughnutChart").length) {
        var doughnutChartCanvas = $("#doughnutChart").get(0).getContext("2d");
        var doughnutChart = new Chart(doughnutChartCanvas, {
            type: 'doughnut',
            data: data,
            options: options
        });
    }
});


