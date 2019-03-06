*** Settings ***
Resource    variables.robot

*** Keywords ***
Keyword from resources without arguments
    Log    ${var1}

Keyword from resrouces
    [Arguments]  ${arg1}
    Log    ${arg1}

Clone from project C
    ${time}=   Get Time
    ${secs}=    Get Time    epoch
    ${year}=    Get Time    return year
    Log    ${time}
    Log    ${secs}
    Log    ${year}
