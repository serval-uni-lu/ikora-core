*** Settings ***
Resource   ../project-c/resources.robot

*** Test Cases ***
Test from project B
    First Keyword from project B
    Second Keyword from project B
    Duplicated keyword
    Clone from project B
    Clone from project C with spécial character
    ${illegal}=  Try to perform an assignment in the test

*** Keywords ***
First Keyword from project B
    [Documentation]  This is the first keyword
    Keyword from resources without arguments

Second Keyword from project B
    [Documentation]  This is the second keyword
    Keyword from resrouces    ${everwhere}

Unused Keyword from project B
    Log    Never called

Clone from project B
    [Documentation]  Clone from project B
    ${other_time}=   Get Time
    ${other_secs}=    Get Time    epoch
    ${other_year}=    Get Time    return year
    Log    ${other_time}
    Log    ${other_secs}
    Log    ${other_year}

Duplicated keyword
    [Documentation]  This is the definition of a keyword which is duplicated
    Log    I am a duplicated keyword in project B

Keyword without steps
    [Documentation]  This keyword will not do anything :(

Keyowrd doing nothing
    [Documentation]  This keyword will not perform any action
    No operation