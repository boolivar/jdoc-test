import net.ltgt.gradle.errorprone.errorprone
import net.ltgt.gradle.nullaway.nullaway

plugins {
    java
    id("net.ltgt.errorprone")
    id("net.ltgt.nullaway")
}

nullaway {
    onlyNullMarked = true
}

dependencies {
    val libs = project.the<VersionCatalogsExtension>().named("libs")

    errorprone(libs.findBundle("errorprone").get())

    implementation("org.jspecify:jspecify")
}

tasks.withType<JavaCompile>().configureEach {
    options.errorprone.nullaway {
        if (name.lowercase().contains("test")) {
            disable()
        } else {
            error()
        }
    }
}

val generatePackageInfo = tasks.register("generatePackageInfo") {

    inputs.dir("src/main/java")

    outputs.dir(layout.buildDirectory.dir("generated/package-info"))

    doLast {
        val template = file("$rootDir/gradle/package-info").readText()
        inputs.files.asFileTree.visit {
            if (isDirectory) {
                val files = file.list()
                if (files.any { it.endsWith(".java", ignoreCase = true) } && !files.contains("package-info.java")) {
                    val packageName = path.replace('/', '.')
                    File(File(outputs.files.singleFile, path), "package-info.java").apply {
                        parentFile.mkdirs()
                        writeText(template.format(packageName))
                    }
                    logger.lifecycle("package-info.java generated for $path")
                }
            }
        }
    }
}

sourceSets {
    main {
        java {
            srcDir(generatePackageInfo)
        }
    }
}
