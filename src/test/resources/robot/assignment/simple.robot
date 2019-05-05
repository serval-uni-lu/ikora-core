*** Keywords ***

Test with a simple test case to see how assignment works
    [Arguments]  ${user}    ${pwd}
    ${EtatRun}=    Run Keyword And Return Status    Connexion to the service    ${user}    ${pwd}
    Run Keyword If    '''${EtatRun}'''=='''False'''    Send error message
    Take Screenshot

Connexion to the service
    [Arguments]  ${user}    ${pwd}
    Log    Connecting to service with ${user} and ${pwd}

Send error message
    Log    There was an error