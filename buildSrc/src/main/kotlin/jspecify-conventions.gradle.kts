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
    errorprone("com.uber.nullaway:nullaway:0.13.1")
    errorprone("com.google.errorprone:error_prone_core:2.46.0")

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

    outputs.dir(layout.buildDirectory.dir("generated/package-info"))

    doLast {
        val template = file("$rootDir/gradle/package-info").readText()
        sourceSets.main.get().java.sourceDirectories.minus(outputs.files).forEach { baseDir ->
            baseDir.walk().filter { f -> f.isDirectory() }.forEach { dir ->
                val files = dir.list()
                if (files.any { it.endsWith(".java", ignoreCase = true) } && !files.contains("package-info.java")) {
                    val targetDir = dir.relativeTo(baseDir).path
                    File(File(outputs.files.singleFile, targetDir), "package-info.java").apply {
                        parentFile.mkdirs()
                        val packageName = targetDir.replace(File.separatorChar, '.')
                        writeText(template.format(packageName))
                    }
                    logger.lifecycle("package-info.java generated for $targetDir")
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
