# Run Playwright JavaScript Tests (PowerShell)
Write-Host "==============================================" -ForegroundColor Green
Write-Host " Running Playwright JavaScript Test Suite     " -ForegroundColor Green
Write-Host "==============================================" -ForegroundColor Green

Push-Location "$PSScriptRoot\playwright-javascript"
try {
    npm test
} finally {
    Pop-Location
}

