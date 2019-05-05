<#include "lib/base.ftl">

<#macro js_imports>
  <#list summaryPage.scripts as url>
    <script src="${url}"></script>
  </#list>
</#macro>

<#macro page_content>
  <div class="row mb-4">
    <div class="col-sm">
      <div class="card border-0 bg-primary text-white shadow">
        <div class="card-body">
          ${summaryPage.linesOfCode} Lines of Code
        </div>
      </div>
    </div>
    <div class="col-sm">
      <div class="card border-0 bg-primary text-white shadow">
        <div class="card-body">
          ${summaryPage.numberKeywords} Keywords
        </div>
      </div>
    </div>
    <div class="col-sm">
      <div class="card border-0 bg-primary text-white shadow">
        <div class="card-body">
          ${summaryPage.numberTestCases} Test Cases
        </div>
      </div>
    </div>
  </div>

  <div class="row">
    <div class="col-sm-12">
      <!-- Area Chart -->
      <div class="card border-0 shadow mb-4">
        <div class="card-header border-0 py-3 d-flex flex-row align-items-center justify-content-between">
          <h6 class="m-0 font-weight-bold text-primary">${summaryPage.linesChart.name}</h6>
        </div>
        <div class="card-body">
          <div class="chart-area">
            <canvas id="${summaryPage.linesChart.id}"></canvas>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div class="row">
    <div class="col-sm-12">
      <!-- Area Chart -->
      <div class="card border-0 shadow mb-4">
        <div class="card-header border-0 py-3 d-flex flex-row align-items-center justify-content-between">
          <h6 class="m-0 font-weight-bold text-primary">${summaryPage.testCasesChart.name}</h6>
        </div>
        <div class="card-body">
          <div class="chart-area">
            <canvas id="${summaryPage.testCasesChart.id}"></canvas>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div class="row">
    <div class="col-sm-12">
      <!-- Area Chart -->
      <div class="card border-0 shadow mb-4">
        <div class="card-header border-0 py-3 d-flex flex-row align-items-center justify-content-between">
          <h6 class="m-0 font-weight-bold text-primary">${summaryPage.userKeywordsChart.name}</h6>
        </div>
        <div class="card-body">
          <div class="chart-area">
            <canvas id="${summaryPage.userKeywordsChart.id}"></canvas>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div class="row">
    <div class="col-sm-6">
      <!-- Area Chart -->
      <div class="card border-0 shadow mb-4">
        <div class="card-header border-0 py-3 d-flex flex-row align-items-center justify-content-between">
          <h6 class="m-0 font-weight-bold text-primary">${summaryPage.cloneChart.name}</h6>
        </div>
        <div class="card-body">
          <div class="chart-area">
            <canvas id="${summaryPage.cloneChart.id}"></canvas>
          </div>
        </div>
      </div>
    </div>
  </div>
</#macro>

<@display_page sidebar=sidebar title=summaryPage.name generated_date=generated_date/>