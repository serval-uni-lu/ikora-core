<#include "lib/base.ftl">
<#include "lib/table.ftl">

<#macro page_content>
    <@table deadCodePage.table/>
</#macro>

<@display_page sidebar=sidebar title=deadCodePage.name generated_date=generated_date/>