*** Settings ***
Resource    ../project-c/resources.robot

*** Test Cases ***
Test from project A
    First Keyword from project A
    Second Keyword from project A
    Clone from project A
    Clone from project C with spécial character
    Indirect dependency
    Duplicated keyword

*** Keywords ***
First Keyword from project A
    [Documentation]  Keyword calling another one. Note that the second step is not implemented.
    Keyword from resources without arguments
    This does not exists

Second Keyword from project A
    [Documentation]  Typical keyword calling another one with arguments
    Keyword from resrouces    ${everwhere}  2   3   4   5


Clone from project A
    [Documentation]  This is the definition of a keyword which is duplicated
    ${time}=   Get Time
    ${secs}=    Get Time    epoch
    ${year}=    Get Time    return year
    Log    ${time}
    Log    ${secs}
    Log    ${year}


Clone Type 3 from project A
    [Documentation]  This is the definition of a keyword which has a very similar keyword somewhere else, just without the logs.
    ${time}=   Get Time
    ${secs}=    Get Time    epoch
    ${year}=    Get Time    return year