import org.gradle.api.Project

private fun Project.getBuildToolsVersion(): String? =
    if (hasProperty("buildToolsVersion")) {
        properties["buildToolsVersion"].toString()
    } else {
        System.getProperty("buildToolsVersion", null)
    }

private fun Project.getVersionName(): String =
    if (hasProperty("versionName")) properties["versionName"].toString() else "offline"

private fun Project.getVersionCode(): Int =
    if (hasProperty("versionCode")) properties["versionCode"].toString().toInt() else 1

object Versions {

    val Project.VERSION_NAME get() = getVersionName()
    val Project.VERSION_CODE get() = getVersionCode()
    val Project.BUILD_TOOLS get() = getBuildToolsVersion()
}