import io.ktor.client.request.forms.FormBuilder
import io.ktor.client.request.forms.formData
import io.ktor.http.content.PartData
import io.ktor.http.headersOf
import kotlinx.io.streams.asInput
import java.io.File
import java.io.FileInputStream

// 遍历本地 Maven 并组装请求体
suspend fun walkPath(callback:suspend (proj: String, fd: List<PartData>) -> Unit) {
    val home = System.getProperty("user.home")
    val m2Path = "$home/.m2/repository/${group.replace(".", File.separator)}"
    File(m2Path).listFiles { pathname: File -> pathname.absolutePath.contains("$name-") || pathname.absolutePath.endsWith(name) }.forEach { dir ->
        val uploadFiles = File(dir, version)
        if (uploadFiles.exists()) {
            var idx = 1
            val fd = formData {
                uploadFiles.listFiles().forEach { file ->
                    append("asset$idx", "C:\\fakepath\\${file.name}")
                    append("asset$idx.classifier", if (file.name.contains("-sources")) "sources" else "")
                    append("asset$idx.extension", file.extension)
                    appendInput("asset$idx", headersOf("Content-Type", "application/octet-stream")) { FileInputStream(file).asInput() }
                    idx += 1
                }
            }
            callback("$group:${dir.name}:$version", fd)
        }
    }
}