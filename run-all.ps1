# Run All Test Suites (Selenium Java + Playwright JavaScript)
Write-Host "==========================================================" -ForegroundColor Yellow
Write-Host " Techdome QA Automation Portfolio - Full Test Suite Run   " -ForegroundColor Yellow
Write-Host "==========================================================" -ForegroundColor Yellow

$overallSuccess = $true

Write-Host "`n>>> [1/2] Executing Selenium Java TestNG Suite..." -ForegroundColor Cyan
& "$PSScriptRoot\run-selenium.ps1"
if ($LASTEXITCODE -ne 0) {
    Write-Host "[FAIL] Selenium Java Suite encountered failures." -ForegroundColor Red
    $overallSuccess = $false
} else {
    Write-Host "[PASS] Selenium Java Suite passed successfully!" -ForegroundColor Green
}

Write-Host "`n>>> [2/2] Executing Playwright JavaScript Suite..." -ForegroundColor Cyan
& "$PSScriptRoot\run-playwright.ps1"
if ($LASTEXITCODE -ne 0) {
    Write-Host "[FAIL] Playwright Suite encountered failures." -ForegroundColor Red
    $overallSuccess = $false
} else {
    Write-Host "[PASS] Playwright Suite passed successfully!" -ForegroundColor Green
}

Write-Host "`n==========================================================" -ForegroundColor Yellow
if ($overallSuccess) {
    Write-Host " ALL TEST SUITES PASSED (100% SUCCESS) " -ForegroundColor Green
} else {
    Write-Host " SOME TESTS FAILED. PLEASE CHECK LOGS AND REPORTS. " -ForegroundColor Red
}
Write-Host "==========================================================" -ForegroundColor Yellow

