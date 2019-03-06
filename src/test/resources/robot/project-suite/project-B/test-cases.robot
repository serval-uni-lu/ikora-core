*** Settings ***
Resource   ../project-C/resources.robot

*** Test Cases ***
Test from project B
    First Keyword from project B
    Second Keyword from project B

*** Keywords ***
First Keyword from project B
    Keyword from resources without arguments

Second Keyword from project B
    Keyword from resrouces    ${everwhere}