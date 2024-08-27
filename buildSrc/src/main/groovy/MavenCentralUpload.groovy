import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

import okhttp3.HttpUrl
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

abstract class MavenCentralUpload extends DefaultTask {

    static final SONATYPE_URL = "https://central.sonatype.com/api/v1/publisher/upload"

    static final APPLICATION_OCTET_STREAM = MediaType.parse("application/octet-stream")

    @Optional
    @Input
    abstract Property<String> getBundleName()

    @Optional
    @Input
    abstract Property<Boolean> getAutoPublish()

    @InputFile
    abstract RegularFileProperty getBundle()

    @Optional
    @Input
    abstract Property<String> getUrl()

    @Input
    abstract Property<String> getToken()

    @TaskAction
    void uploadBundle() throws IOException {
        def file = bundle.get().asFile

        def requestBody = new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("bundle", file.name, RequestBody.create(file, APPLICATION_OCTET_STREAM))
            .build()

        def requestUrl = HttpUrl.get(url.getOrElse(SONATYPE_URL)).newBuilder()
            .addQueryParameter("publishingType", autoPublish.getOrElse(false) ? "AUTOMATIC" : "USER_MANAGED")

        def request = new Request.Builder()
            .header("Authorization", "Bearer " + token.get())
            .url(bundleName.present ? requestUrl.addQueryParameter("name", bundleName.get()).build() : requestUrl.build())
            .post(requestBody)
            .build()

        try (def rsp = new OkHttpClient().newCall(request).execute()) {
            if (!rsp.isSuccessful()) {
                throw new IOException("Request error: " + rsp.code + " " + rsp.body.string())
            }
        }
    }
}
