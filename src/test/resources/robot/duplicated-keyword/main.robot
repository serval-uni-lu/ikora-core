*** Settings ***

Resource            resources1.robot
Resource            resources2.robot

*** Test Cases ***

Call Some simple and duplicated keywords
    Simple Keyword
    Duplicated Keyword