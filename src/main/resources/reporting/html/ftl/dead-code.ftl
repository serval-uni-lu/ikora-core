<#include "lib/base.ftl">
<#include "lib/table.ftl">

<#macro page_title>
    ${deadCodePage.name}
</#macro>

<#macro page_content>
    <@table deadCodePage.table/>
</#macro>

<@display_page sidebar=sidebar/>