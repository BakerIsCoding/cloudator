var data = {
    labels: ["Espacio Ocupado", "Espacio Libre"],
    datasets: [{
        data: [1.5, 8.5], // Datos de espacio ocupado y libre AÑADIR VARIABLES.
        backgroundColor: [
            'rgba(255, 0, 0, 0.5)', // Color para "Espacio Ocupado"
            'rgba(0, 255, 127, 0.5)' // Color para "Espacio Libre"
        ],
        borderColor: [
            'rgba(255, 0, 0, 1)',
            'rgba(0, 255, 127, 1)'
        ],
        borderWidth: 1
    }]
};

// Calcular la suma de los valores
var sumaValores = data.datasets[0].data.reduce(function (a, b) {
    return a + b;
}, 0);

var options = {
    cutoutPercentage: 70,
    legend: {
        display: true,
        position: 'bottom',
        labels: {
            fontColor: 'white'
        }
    }
}

if ($("#doughnutChart").length) {
    var doughnutChartCanvas = $("#doughnutChart").get(0).getContext("2d");
    var doughnutChart = new Chart(doughnutChartCanvas, {
        type: 'doughnut',
        data: data,
        options: options
    });
}