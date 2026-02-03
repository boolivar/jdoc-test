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
    implementation("org.jspecify:jspecify:1.0.0")
}

tasks.withType<JavaCompile>() {
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
            baseDir.walk().filter { p -> p.isDirectory() && with(p.list()) { any { it.endsWith(".java", true) } && none { it.equals("package-info.java", true) } } }.forEach { packageDir ->
                val targetDir = packageDir.relativeTo(baseDir).path
                File(File(outputs.files.singleFile, targetDir), "package-info.java").apply {
                    parentFile.mkdirs()
                    writeText(template.format(targetDir.replace(File.separatorChar, '.')))
                }
                println("package-info.java generated for $targetDir")
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
