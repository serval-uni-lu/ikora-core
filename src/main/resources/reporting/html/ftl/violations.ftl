<#include "lib/base.ftl">
<#include "lib/table.ftl">

<#macro page_title>

</#macro>

<#macro page_content>
    <@table violations.table/>
</#macro>

<@display_page sidebar=sidebar title=violations.name generated_date=generated_date/>