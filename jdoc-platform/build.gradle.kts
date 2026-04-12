plugins {
    `java-platform`
}

javaPlatform {
    allowDependencies()
}

dependencies {
    api(platform(libs.cucumber.bom))
    api(platform(libs.junit.bom))
    constraints {
        api(libs.commons.io)
        api(libs.javaparser.core)
        api(libs.commons.lang3)
        api(libs.spock.core)
        api(libs.jspecify)

        api(libs.assertj.core)
        api(libs.mockito.junitJupiter)

        runtime(libs.jdoc.spock)
        runtime(libs.bytebuddy)
        runtime(libs.objenesis)
    }
}
