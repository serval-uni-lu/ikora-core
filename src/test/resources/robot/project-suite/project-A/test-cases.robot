*** Settings ***
Resource   ../project-C/resources.robot

*** Test Cases ***
Test from project A
    First Keyword from project A
    Second Keyword from project A

*** Keywords ***
First Keyword from project A
    Keyword from resources without arguments

Second Keyword from project A
    Keyword from resrouces    ${everwhere}
