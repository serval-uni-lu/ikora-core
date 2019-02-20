<#include "lib/base.ftl">

<#macro page_title>
    ${dependencies.name}
</#macro>

<#macro js_imports>
        <script src="${dependency-graph-url}"></script>
</#macro>

<#macro page_content>
    <!-- Graph Chart -->
    <div class="card border-0 shadow mb-4">
        <div class="card-header border-0 py-3 d-flex flex-row align-items-center justify-content-between">
            <h6 class="m-0 font-weight-bold text-primary">${dependencies.name}</h6>
        </div>
        <div class="card-body">
            <div class="chart-area">
                <canvas id="${dependencies.id}"></canvas>
            </div>
        </div>
    </div>
</#macro>

<@display_page sidebar=sidebar/>