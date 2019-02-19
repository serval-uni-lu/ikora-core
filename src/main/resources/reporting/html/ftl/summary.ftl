<#include "lib/base.ftl">

<#macro page_title>
  Ukwikora - Dashboard
</#macro>

<#macro js_imports>
  <#list summary.scripts as url>
    <script src="${url}"></script>
  </#list>
</#macro>

<#macro page_content>
  <!-- Area Chart -->
  <div class="card border-0 shadow mb-4">
    <div class="card-header border-0 py-3 d-flex flex-row align-items-center justify-content-between">
      <h6 class="m-0 font-weight-bold text-primary">${summary.linesChart.name}</h6>
    </div>
    <div class="card-body">
      <div class="chart-area">
        <canvas id="${summary.linesChart.id}"></canvas>
      </div>
    </div>
  </div>

  <!-- Area Chart -->
  <div class="card border-0 shadow mb-4">
    <div class="card-header border-0 py-3 d-flex flex-row align-items-center justify-content-between">
      <h6 class="m-0 font-weight-bold text-primary">${summary.testCasesChart.name}</h6>
    </div>
    <div class="card-body">
      <div class="chart-area">
        <canvas id="${summary.testCasesChart.id}"></canvas>
      </div>
    </div>
  </div>

  <!-- Area Chart -->
  <div class="card border-0 shadow mb-4">
    <div class="card-header border-0 py-3 d-flex flex-row align-items-center justify-content-between">
      <h6 class="m-0 font-weight-bold text-primary">${summary.userKeywordsChart.name}</h6>
    </div>
    <div class="card-body">
      <div class="chart-area">
        <canvas id="${summary.userKeywordsChart.id}"></canvas>
      </div>
    </div>
  </div>
</#macro>

<@display_page sidebar=sidebar/>