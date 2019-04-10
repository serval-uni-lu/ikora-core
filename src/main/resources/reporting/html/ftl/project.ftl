<#include "lib/base.ftl">

<#macro js_imports>
    <#list project.scripts as url>
        <script src="${url}"></script>
    </#list>
</#macro>

<#macro page_content>
    <div class="row mb-4">
        <div class="col-sm">
            <div class="card border-0 bg-primary text-white shadow">
                <div class="card-body">
                    ${project.linesOfCode} Lines of Code
                </div>
            </div>
        </div>
        <div class="col-sm">
            <div class="card border-0 bg-primary text-white shadow">
                <div class="card-body">
                    ${project.numberKeywords} Keywords
                </div>
            </div>
        </div>
        <div class="col-sm">
            <div class="card border-0 bg-primary text-white shadow">
                <div class="card-body">
                    ${project.numberTestCases} Test Cases
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-6">
            <!-- Area Chart -->
            <div class="card border-0 shadow mb-4">
                <div class="card-header border-0 py-3 d-flex flex-row align-items-center justify-content-between">
                    <h6 class="m-0 font-weight-bold text-primary">${project.connectivityChart.name}</h6>
                </div>
                <div class="card-body">
                    <div class="chart-area">
                        <canvas id="${project.connectivityChart.id}"></canvas>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-lg-6">
            <!-- Area Chart -->
            <div class="card border-0 shadow mb-4">
                <div class="card-header border-0 py-3 d-flex flex-row align-items-center justify-content-between">
                    <h6 class="m-0 font-weight-bold text-primary">${project.depthChart.name}</h6>
                </div>
                <div class="card-body">
                    <div class="chart-area">
                        <canvas id="${project.depthChart.id}"></canvas>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-6">
            <!-- Area Chart -->
            <div class="card border-0 shadow mb-4">
                <div class="card-header border-0 py-3 d-flex flex-row align-items-center justify-content-between">
                    <h6 class="m-0 font-weight-bold text-primary">${project.sizeChart.name}</h6>
                </div>
                <div class="card-body">
                    <div class="chart-area">
                        <canvas id="${project.sizeChart.id}"></canvas>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-lg-6">
            <!-- Area Chart -->
            <div class="card border-0 shadow mb-4">
                <div class="card-header border-0 py-3 d-flex flex-row align-items-center justify-content-between">
                    <h6 class="m-0 font-weight-bold text-primary">${project.sequenceChart.name}</h6>
                </div>
                <div class="card-body">
                    <div class="chart-area">
                        <canvas id="${project.sequenceChart.id}"></canvas>
                    </div>
                </div>
            </div>
        </div>
    </div>
</#macro>

<@display_page sidebar=sidebar title=project.link.text generated_date=generated_date/>