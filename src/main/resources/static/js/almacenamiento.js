document.addEventListener("DOMContentLoaded", function () {
    var rawDataNormal = document.getElementById("fechasNormal").textContent;
    var dataNormal = normalizeData(rawDataNormal);

    var rawDataUser = document.getElementById("fechasUser").textContent;
    var dataUser = normalizeData(rawDataUser);

    var labels = getLast7Days();

    var subidasDiariasData = {
        labels: labels,
        datasets: [{
            label: 'En General',
            data: labels.map(date => dataNormal[date] || 0),
            fill: false,
            borderColor: '#1E90FF',
            tension: 0.4
        }, {
            label: 'Tú',
            data: labels.map(date => dataUser[date] || 0),
            fill: false,
            borderColor: '#00FF7F',
            tension: 0.4
        }]
    };

    if ($("#subidasDiariasChart").length) {
        var ctx = $("#subidasDiariasChart").get(0).getContext("2d");
        var subidasDiariasChart = new Chart(ctx, {
            type: 'line',
            data: subidasDiariasData,
            options: {
                scales: {
                    y: {
                        beginAtZero: true
                    }
                },
                plugins: {
                    legend: {
                        display: true
                    }
                }
            }
        });
    }
});

function normalizeData(rawData) {
    var data = rawData.split(',').reduce((acc, item) => {
        var parts = item.split(':');
        acc[parts[0]] = parseInt(parts[1], 10);
        return acc;
    }, {});
    return data;
}

function getLast7Days() {
    var dates = [];
    var today = new Date();
    for (var i = 6; i >= 0; i--) {
        var d = new Date(today);
        d.setDate(d.getDate() - i);
        dates.push(formatDate(d));
    }
    return dates;
}

function formatDate(date) {
    return date.toISOString().substring(0, 10); // Formato 'YYYY-MM-DD'
}