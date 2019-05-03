*** Settings ***
Resource   ../project-C/resources.robot

*** Test Cases ***
Test from project B
    First Keyword from project B
    Second Keyword from project B
    Duplicated keyword
    Clone from project B
    Clone from project C with spécial character

*** Keywords ***
First Keyword from project B
    Keyword from resources without arguments

Second Keyword from project B
    Keyword from resrouces    ${everwhere}

Unused Keyword from project B
    Log    Never called

Clone from project B
    ${other_time}=   Get Time
    ${other_secs}=    Get Time    epoch
    ${other_year}=    Get Time    return year
    Log    ${other_time}
    Log    ${other_secs}
    Log    ${other_year}

Duplicated keyword
    Log    I am a duplicated keyword in project B