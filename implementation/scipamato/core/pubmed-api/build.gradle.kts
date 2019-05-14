description = "SciPaMaTo-Core :: Pubmed API Project"

dependencies {
    implementation(project(Module.scipamatoCommon("utils")))

    // Cloud access
    api(Lib.springCloud("openfeign").id) {
        exclude("com.netflix.archaius", "archaius-core")
    }
    implementation(Lib.openfeign("jaxb"))
    implementation(Lib.openfeign("okhttp"))
    implementation(Lib.openfeign("slf4j"))

    // Object/XML mapping
    implementation(Lib.spring("oxm"))
    implementation(Lib.jaxbApi())
    implementation(Lib.jaxbRuntime())

    implementation(Lib.commonsLang3())

    testLibCompile(project(Module.scipamatoCommon("test")))

    integrationTestAnnotationProcessor(Lib.lombok())
    integrationTestRuntimeOnly(Lib.lombok())
}

