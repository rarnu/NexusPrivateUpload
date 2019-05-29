import io.ktor.util.InternalAPI
import io.ktor.util.encodeBase64
import java.io.File

var group = ""
var version = ""
var name = ""
var nexusUploadUrl = ""
var nexusAccount = ""
var nexusPassword = ""

@UseExperimental(InternalAPI::class)
fun prepareData() = try {
    val work = System.getProperty("user.dir")
    val buildGradle = "$work/build.gradle"
    val settingsGradle = "$work/settings.gradle"
    val gradleProperties = "$work/gradle.properties"
    File(buildGradle).readLines().forEach {
        if (it.trim().startsWith("group ")) {
            group = it.extract("group ")
        }
        if (it.trim().startsWith("version ")) {
            version = it.extract("version ")
        }
    }
    File(settingsGradle).readLines().forEach {
        if (it.trim().startsWith("rootProject.name")) {
            name = it.extract("rootProject.name")
        }
    }
    File(gradleProperties).readLines().forEach {
        if (it.trim().startsWith("nexusUploadUrl")) {
            nexusUploadUrl = it.extract("nexusUploadUrl")
        }
        if (it.trim().startsWith("nexusAccount")) {
            nexusAccount = it.extract("nexusAccount")
        }
        if (it.trim().startsWith("nexusPassword")) {
            nexusPassword = it.extract("nexusPassword")
        }
    }

    nexusAccount = nexusAccount.encodeBase64().replace("=", "%3D")
    nexusPassword = nexusPassword.encodeBase64().replace("=", "%3D")

    println("group => $group")
    println("version => $version")
    println("name => $name")
    println("nexusUploadUrl => $nexusUploadUrl")
    println("nexusAccount => $nexusAccount")
    println("nexusPassword => $nexusPassword")
} catch (e: Exception) {
    println("must run under a project root.")
}

fun String.extract(field: String) = this.trim().replace(field, "").replace("'", "").replace("\"", "").replace("=", "").trim()