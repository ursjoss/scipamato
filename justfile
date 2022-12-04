@_list:
    just --list --unsorted

# with app := [core,public] Starts SciPaMaTo-Core or Public (requires docker and postgresql to run)
run app='core':
    ./gradlew :{{app}}-web:bootRun

