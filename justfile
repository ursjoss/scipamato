@_list:
    just --list --unsorted

# with app := [core,public] Starts SciPaMaTo-Core or Public (requires docker and postgresql to run)
run app='core':
    ./gradlew :{{app}}-web:bootRun


debug app='core':
    ./gradlew -Dorg.gradle.jvmargs="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005" :{{app}}-web:bootRun
