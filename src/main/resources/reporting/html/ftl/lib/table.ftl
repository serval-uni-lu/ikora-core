<#macro table data>
    <div class="table-responsive">
        <table class="dataTable table table-bordered" id="${data.id}" width="100%" cellpadding="0">
            <thead>
                <tr>
                    <#list data.labels as label>
                        <th>${label}</th>
                    </#list>
                </tr>
            </thead>
            <tfoot>
            <tr>
                <#list data.labels as label>
                    <th>${label}</th>
                </#list>
            </tr>
            </tfoot>
            <tbody>
            <#list data.rows as row>
                <tr>
                    <#list row as column>
                        <td>${column}</td>
                    </#list>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
</#macro>