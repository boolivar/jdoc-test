plugins {
    alias(libs.plugins.versioning)
    alias(libs.plugins.sonarqube)
}

scmVersion {
    tag.prefix = ""
    useHighestVersion = true
    versionIncrementer("incrementMinorIfNotOnRelease", mapOf("releaseBranchPattern" to "release\\/.+"))
    branchVersionCreator = mapOf(
        "master" to "simple",
        "release/.*" to "simple",
        ".*" to "versionWithBranch"
    )
}

sonar {
    properties {
        property("sonar.projectKey", "boolivar_jdoc-test")
        property("sonar.organization", "boolivar")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}

allprojects {
    group = "io.github.boolivar.jdoctest"
    version = rootProject.scmVersion.version
}
