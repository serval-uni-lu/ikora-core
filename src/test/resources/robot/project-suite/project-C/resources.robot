*** Settings ***
Resource    variables.robot
Resource    indirectDependency.robot

*** Keywords ***
Keyword from resources without arguments
    Log    ${var1}

Keyword from resrouces
    [Arguments]  ${arg1}
    Log    ${arg1}

Clone from project C with spécial character
    ${time}=   Get Time
    ${secs}=    Get Time    epoch
    ${year}=    Get Time    return year
    Log    ${time}
    Log    ${secs}
    Log    ${year}


Duplicated keyword
    Log    I am a duplicated keyword in project B
