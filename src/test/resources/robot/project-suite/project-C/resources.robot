*** Settings ***
Resource    variables.robot

*** Keywords ***
Keyword from resources without arguments
    Log    ${var1}

Keyword from resrouces
    [Arguments]  ${arg1}
    Log    ${arg1}