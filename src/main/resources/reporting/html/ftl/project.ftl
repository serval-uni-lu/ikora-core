<#include "lib/base.ftl">

<#macro page_title>
    ${project.link.text}
</#macro>

<#macro js_imports>

</#macro>

<#macro page_content>
<div class="row">
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
</#macro>

<@display_page sidebar=sidebar/>