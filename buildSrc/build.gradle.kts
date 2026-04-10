plugins {
    `kotlin-dsl`
    `groovy-gradle-plugin`
}

repositories {
    gradlePluginPortal()
}

dependencies {
	implementation(libs.checkstyle)
    implementation(libs.commons.io)
    implementation(libs.pmd)

    implementation(plugin(libs.plugins.errorprone))
    implementation(plugin(libs.plugins.gradleDevelopment))
    implementation(plugin(libs.plugins.lombok))
    implementation(plugin(libs.plugins.nullaway))
    implementation(plugin(libs.plugins.pluginPublish))
    implementation(plugin(libs.plugins.shadow))
    implementation(plugin(libs.plugins.sonatypePublish))
    implementation(plugin(libs.plugins.spotbugs))
}

fun DependencyHandlerScope.plugin(plugin: Provider<PluginDependency>) =
    plugin.map { "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}" }
