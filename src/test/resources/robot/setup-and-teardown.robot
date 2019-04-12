*** Test Cases ***

Test with setup and teardown
    [Setup]  Setup the test case
    Do Something Cool
    [Teardown]  Clean the environment

*** Keywords ***

Setup the test case
    Log    Setup environment

Do Something Cool
    Log    Perform some cool action

Clean the environment
    Log    Make sure nothing is polluting the environment