#!/usr/bin/env bash
# ==============================================================================
# Techdome QA Automation Portfolio - Full Test Suite Runner (Linux / macOS)
# Executes both Selenium Java and Playwright JavaScript test suites.
# ==============================================================================

set -e

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" >/dev/null 2>&1 && pwd)"

echo "=========================================================="
echo " Techdome QA Automation Portfolio - Full Test Suite Run   "
echo "=========================================================="

echo ""
echo ">>> [1/2] Executing Selenium Java TestNG Suite..."
cd "$DIR/selenium-java"
mvn clean test -Dheadless=true

echo ""
echo ">>> [2/2] Executing Playwright JavaScript Suite..."
cd "$DIR/playwright-javascript"
npm test

echo ""
echo "=========================================================="
echo " ALL TEST SUITES PASSED (100% SUCCESS)                    "
echo "=========================================================="
