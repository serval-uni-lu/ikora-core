*** Settings ***
Resource    variables.robot
Resource    indirectDependency.robot

*** Keywords ***
Keyword from resources without arguments
    Log    ${var1}

Keyword from resrouces
    [Documentation]  Keyword with too many arguments
    [Arguments]  ${arg1}  ${arg2}  ${arg3}  ${arg4}  ${arg5}
    Log    ${arg1}
    Log    ${arg2}
    Log    ${arg3}
    Log    ${arg4}
    Log    ${arg5}

Clone from project C with spécial character
    [Documentation]  Keyword having a clone without the logs. Note the presence of special characters
    ${time}=   Get Time
    ${secs}=    Get Time    epoch
    ${year}=    Get Time    return year
    Log    ${time}
    Log    ${secs}
    Log    ${year}


Duplicated keyword
    [Documentation]  Keyword with a clone in project B
    Log    I am a duplicated keyword in project B
