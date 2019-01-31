// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#292b2c';

var ctx = document.getElementById("locChart");
var lines = ${summary.jsonLines};

var locChart = new Chart(ctx, {
  type: 'bar',
  data: {
    labels: ${summary.jsonLabels},
    datasets: [{
      label: "Lines of code",
      backgroundColor: "rgba(2,117,216,1)",
      borderColor: "rgba(2,117,216,1)",
      data: lines,
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
        display: false,
        time: {
          unit: ''
        },
        gridLines: {
          display: false
        },
        ticks: {
          maxTicksLimit: 6
        }
      }],
      yAxes: [{
        ticks: {
          min: 0,
          max: Math.max.apply(this, lines) + 10,
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