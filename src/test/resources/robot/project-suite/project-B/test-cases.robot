*** Settings ***
Resource   ../project-C/resources.robot

*** Test Cases ***
Test from project B
    First Keyword from project B
    Second Keyword from project B
    Clone from project B
    Clone from project C

*** Keywords ***
First Keyword from project B
    Keyword from resources without arguments

Second Keyword from project B
    Keyword from resrouces    ${everwhere}

Unused Keyword from project B
    Log    Never called

Clone from project B
    ${time}=   Get Time
    ${secs}=    Get Time    epoch
    ${year}=    Get Time    return year
    Log    ${time}
    Log    ${secs}
    Log    ${year}
