# Run Selenium Java Tests (PowerShell)
Write-Host "==============================================" -ForegroundColor Cyan
Write-Host " Running Selenium WebDriver Java Test Suite   " -ForegroundColor Cyan
Write-Host "==============================================" -ForegroundColor Cyan

# Fallback Java locations if current JAVA_HOME is invalid
$validJdks = @(
    "C:\Users\Chaitanya\.cursor\extensions\redhat.java-1.50.0-win32-x64\jre\21.0.9-win32-x86_64",
    "C:\Program Files\Eclipse Adoptium\jdk-17.0.20.101-hotspot",
    "C:\Program Files\Java\jdk-17",
    "C:\Program Files\Java\jdk-21"
)

if (-not $env:JAVA_HOME -or -not (Test-Path "$env:JAVA_HOME\bin\javac.exe")) {
    foreach ($jdk in $validJdks) {
        if (Test-Path "$jdk\bin\javac.exe") {
            $env:JAVA_HOME = $jdk
            break
        }
    }
}

# Ensure Maven is on PATH
$mavenPaths = @(
    "C:\Users\Chaitanya\tools\apache-maven-3.9.9\bin",
    "C:\Program Files\apache-maven-3.9.9\bin"
)

foreach ($mvnPath in $mavenPaths) {
    if (Test-Path $mvnPath) {
        $env:PATH = "$mvnPath;$env:JAVA_HOME\bin;$env:PATH"
        break
    }
}

Write-Host "Using JAVA_HOME: $env:JAVA_HOME" -ForegroundColor DarkGray

Push-Location "$PSScriptRoot\selenium-java"
try {
    mvn test
} finally {
    Pop-Location
}
