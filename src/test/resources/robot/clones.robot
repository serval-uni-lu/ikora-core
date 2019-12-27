*** Keywords ***

First keyword
    Open Browser  firefox  google.com
    Title Should be  google.com
    Close All Browser

Second keyword
    Open Browser  firefox  google.com
    Title Should be  google.com
    Close All Browser

Third keyword
    Open Browser  chrome  google.com
    Title Should be  google.com
    Close All Browser


Forth keyword
    Log  Opening browser
    Open Browser  firefox  google.com
    Title Should be  google.com
    Log  Closing browser
    Close All Browser