// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#292b2c';

var ctx = document.getElementById("${chart.id}");
ctx.height = ${chart.height};

var chart = new Chart(ctx, {
  type: 'bar',
  data: {
    labels: ${chart.jsonLabels},
    datasets: ${chart.jsonDatasets}
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
        stacked: true,
        time: {
          unit: ''
        },
        gridLines: {
          display: false
        },
        scaleLabel: {
          display: true,
          labelString: "${chart.XLabel}"
        },
        ticks: {
          autoSkip: false,
          maxRotation: 90,
          minRotation: 0
        }
      }],
      yAxes: [{
        stacked: true,
        ticks: {
          min: 0,
          maxTicksLimit: 5
        },
        scaleLabel: {
          display: true,
          labelString: "${chart.YLabel}"
        },
        gridLines: {
          display: true
        }
      }],
    },
    legend: {
      display: <#if chart.isDisplayLegend()>true<#else>false</#if>
    }
  }
});