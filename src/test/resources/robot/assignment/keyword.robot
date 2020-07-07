*** Keywords ***

Test with a simple test case to see how assignment works
    [Arguments]  ${user}    ${pwd}
    ${EtatRun}=    Run Keyword And Return Status    Connexion to the service    ${user}    ${pwd}
    Log    ${EtatRun}

Connexion to the service
    [Arguments]  ${user}    ${pwd}
    Log    Connecting to service with ${user} and ${pwd}
