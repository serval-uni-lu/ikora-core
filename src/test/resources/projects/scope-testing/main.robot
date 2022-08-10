*** Settings ***

Resource            resources1.robot
Resource            resources2.robot

*** Test Cases ***

Check if the resolution of libraries is well defined
    Load keyword from resource1
    Load keyword from resource2

*** Keywords ***

Load keyword from resource1
    resources1.keyword to execute

Load keyword from resource2
    resources2.keyword to execute
