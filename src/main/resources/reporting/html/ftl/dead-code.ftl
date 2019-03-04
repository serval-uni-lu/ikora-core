<#include "lib/base.ftl">
<#include "lib/table.ftl">

<#macro page_title>
    ${deadCode.name}
</#macro>

<#macro page_content>
    <@table deadCode.table/>
</#macro>

<@display_page sidebar=sidebar/>