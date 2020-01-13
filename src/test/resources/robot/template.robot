*** Test Cases ***

Test Case using template with explicit arguments
    [Template]  This is a template
    Just a little
    A little more
    Are you crazy

*** Keywords ***
This is a template
    [Arguments]  ${value}
    Log  ${value}