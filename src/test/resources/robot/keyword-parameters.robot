*** Test Cases ***
Test keyword with keyword as argument

*** Keywords ***
Test keyword with keyword as argument
    Run Keyword If    'two' == 'two'    Log    Execute


Test keyword with keyword as argument with collapsed arguments
    Run Keyword If    @{params}


*** Variables ***
@{params}    Run Keyword If    'two' == 'two'    Log    Execute