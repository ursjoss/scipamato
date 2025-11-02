@_list:
    just --list --unsorted

# with app := [core,public] Starts SciPaMaTo-Core or Public (requires docker and postgresql to run)
run app='core':
    ./gradlew :{{app}}-web:bootRun


# with app := [core,public] STarts SciPaMaTo-Core or Public with configuration to attach debugger from IntelliJ
debug app='core':
    ./gradlew -Dorg.gradle.jvmargs="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005" :{{app}}-web:bootRun

# Generates the license report
licenseReport:
    ./gradlew generateLicenseReport --no-parallel --rerun-tasks --no-build-cache
    chromium build/reports/dependency-license/report.html
