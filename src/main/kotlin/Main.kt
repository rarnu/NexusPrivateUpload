import io.ktor.client.HttpClient
import io.ktor.client.features.cookies.AcceptAllCookiesStorage
import io.ktor.client.features.cookies.HttpCookies
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.content.TextContent
import io.ktor.http.*
import kotlinx.io.streams.asInput
import java.io.File
import java.io.FileInputStream
import kotlin.system.exitProcess

val client = HttpClient() {
    install(HttpCookies) {
        // 设置接受所有 cookie
        storage = AcceptAllCookiesStorage()
    }
}

suspend fun main(args: Array<String>) {
    // 准备数据
    prepareData()

    // 申请 session，得到的 NXSESSIONID 由 ktor-client 自动管理
    client.post<String>("$nexusUploadUrl/service/rapture/session") {
        body = TextContent("username=$nexusAccount&password=$nexusPassword", ContentType.parse("application/x-www-form-urlencoded; charset=UTF-8"))
    }

    // 遍历要上传的文件，并且上传
    walkPath { proj, data ->
        // 发起上传到 nexus 的请求
        val uploadResult = client.post<String>("$nexusUploadUrl/service/rest/internal/ui/upload/maven-releases") {
            header("Cache-Control", "max-age=0")
            header("Upgrade-Insecure-Requests", "1")
            header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
            body = MultiPartFormDataContent(data)
        }
        println("$proj => ${if (uploadResult.contains("success")) "success" else "fail"}")
    }

    exitProcess(0)
}