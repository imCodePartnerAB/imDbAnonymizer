write-output https://www.freeformatter.com/xsd-generator.html for new xsd

..\..\work\jaxb-ri\bin\xjc.bat .\example.xsd

$configFiles = Get-ChildItem .\generated\ -rec
foreach ($file in $configFiles)
{
    (Get-Content $file.PSPath) |
    Foreach-Object { $_ -replace "package generated;", "package com.imcode.internal.imAnonymizer.parser.xml;" } |
    Set-Content $file.PSPath
}

robocopy.exe /MIR .\generated\ ..\main\java\com\imcode\internal\imAnonymizer\parser\xml\
Remove-Item .\generated\ -Recurse


