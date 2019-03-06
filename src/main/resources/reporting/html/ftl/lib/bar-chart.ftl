// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#292b2c';

var ctx = document.getElementById("${chart.id}");
ctx.height = ${chart.height};

var chart = new Chart(ctx, {
  type: 'bar',
  data: {
    labels: ${chart.jsonLabels},
    datasets: [{
      label: "${chart.YLabel}",
      backgroundColor: "rgba(2,117,216,1)",
      borderColor: "rgba(2,117,216,1)",
      data: ${chart.jsonValues},
    }],
  },
  options: {
    maintainAspectRatio: false,
    layout: {
      padding: {
        left: 0,
        right: 25,
        top: 25,
        bottom: 25
      }
    },
    scales: {
      xAxes: [{
        display: true,
        time: {
          unit: ''
        },
        gridLines: {
          display: false
        },
        ticks: {
          autoSkip: false,
          maxRotation: 90,
          minRotation: 0
        }
      }],
      yAxes: [{
        ticks: {
          min: 0,
          max: Math.max.apply(this, ${chart.jsonValues}),
          maxTicksLimit: 5
        },
        gridLines: {
          display: true
        }
      }],
    },
    legend: {
      display: false
    }
  }
});