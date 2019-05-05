*** Settings ***
Resource    ../project-C/resources.robot

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
    Keyword from resources without arguments

Second Keyword from project A
    Keyword from resrouces    ${everwhere}


Clone from project A
    ${time}=   Get Time
    ${secs}=    Get Time    epoch
    ${year}=    Get Time    return year
    Log    ${time}
    Log    ${secs}
    Log    ${year}


Clone Type 3 from project A
    ${time}=   Get Time
    ${secs}=    Get Time    epoch
    ${year}=    Get Time    return year