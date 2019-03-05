<#include "lib/base.ftl">
<#include "lib/table.ftl">

<#macro page_title>
    ${clones.name}
</#macro>

<#macro page_content>
    <@table clones.table/>
</#macro>

<@display_page sidebar=sidebar/>