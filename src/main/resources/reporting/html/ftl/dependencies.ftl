<#include "lib/base.ftl">

<#macro js_imports>
        <script src="${dependencyGraphUrl}"></script>
</#macro>

<#macro page_content>
    <!-- Graph Chart -->
    <div class="card border-0 shadow mb-4">
        <div class="card-header border-0 py-3 d-flex flex-row align-items-center justify-content-between">
            <h6 class="m-0 font-weight-bold text-primary">${dependencies.name}</h6>
        </div>
        <div class="card-body">
            <div class="chart-area" id="${dependencies.id}">
            </div>
        </div>
    </div>
</#macro>

<@display_page sidebar=sidebar title=dependencies.name generated_date=generated_date/>