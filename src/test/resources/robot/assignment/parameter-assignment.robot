*** Test Cases ***
Simple call
    From parameter assignment    the value

*** Keywords ***
From parameter assignment
    [Arguments]    ${local}
    Log    ${local}

*** Variables ***
 ${variable}  Some variable