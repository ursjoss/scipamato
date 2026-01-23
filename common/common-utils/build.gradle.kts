description = "SciPaMaTo-Common :: Utilities Project"

plugins {
    `java-library`
    `java-test-fixtures`
    `maven-publish`
}

dependencies {
    testFixturesApi(libs.equalsverifier)
}
