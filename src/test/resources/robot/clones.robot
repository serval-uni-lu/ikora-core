*** Keywords ***

First keyword
    Open Browser  firefox  google.com
    Title Should be  google.com
    Close All Browsers

Second keyword
    Open Browser  firefox  google.com
    Title Should be  google.com
    Close All Browsers

Third keyword
    Open Browser  chrome  google.com
    Title Should be  google.com
    Close All Browsers

Forth keyword
    Log  Opening browser
    Open Browser  firefox  google.com
    Title Should be  google.com
    Log  Closing browser
    Close All Browsers

Fifth keyword
    Log  Opening browser
    ${result}=  Open Browser  firefox  google.com
    Title Should be  google.com
    Log  Closing browser
    Close All Browsers

Sixth keyword
    :FOR    ${INDEX}    IN RANGE    1    3
    \    Log    ${INDEX}
    \    ${RANDOM_STRING}=    Generate Random String    ${INDEX}
    \    Log    ${RANDOM_STRING}

Seventh keyword
    :FOR    ${INDEX}    IN RANGE    1    10
    \    Log    ${INDEX}
    \    ${RANDOM_STRING}=    Generate Random String    ${INDEX}
    \    Log    ${RANDOM_STRING}

Eighth keyword
    :FOR    ${INDEX}    IN RANGE    1    3
    \    ${RANDOM_STRING}=    Generate Random String    ${INDEX}
    \    Log    ${RANDOM_STRING}