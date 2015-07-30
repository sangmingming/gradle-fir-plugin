package me.isming.fir.gradle.plugins

import com.squareup.okhttp.MediaType
import com.squareup.okhttp.MultipartBuilder
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.RequestBody
import com.squareup.okhttp.Response
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.json.JSONObject

import java.util.concurrent.TimeUnit

class FirTask extends DefaultTask {
    private final String API_END_POINT = "http://api.fir.im/"
    private void upload(Project project, Apk apk) {
        String token = project.fir.userToken
        JSONObject infoObj = getAppInfo(getGetInfoEndPoint(project), token)
        if (infoObj != null && infoObj.has("cert")) {
            JSONObject certObj = infoObj.getJSONObject("cert")
            if (certObj.has("binary")) {
                JSONObject binaryObj = certObj.getJSONObject("binary")
                String up_url = binaryObj.getString("upload_url")
                String up_token = binaryObj.getString("token")
                String up_key = binaryObj.getString("key")
                JSONObject reusltobj = uploadApk(up_url, up_key, up_token, apk)
                println(reusltobj)
            }
        }
    }

    private String getGetInfoEndPoint(Project project) {
        String appId = project.fir.appId
        if (appId == null || appId == '') {
            throw new GradleException('appId is missing. Please enter the userName.')
        }
        String endPoint = API_END_POINT + "apps/" + appId + "/releases"
        return endPoint
    }

    private JSONObject getAppInfo(String endpoint, String userToken) {
        OkHttpClient client = new OkHttpClient()
        client.setConnectTimeout(10, TimeUnit.SECONDS);
        client.setReadTimeout(60, TimeUnit.SECONDS);
        MultipartBuilder build = new MultipartBuilder().type(MultipartBuilder.FORM)
        build.addFormDataPart("api_token", userToken)
        build.addFormDataPart("type", "android")
        Request request = new Request.Builder().url(endpoint).post(build.build()).build()
        Response response = client.newCall(request).execute()
        if (response == null || response.body() == null) return null
        InputStream is = response.body().byteStream()
        BufferedReader reader = new BufferedInputStream(new InputStreamReader(is))
        JSONObject json = new JSONObject(reader)
        is.close()
        return json
    }

    private JSONObject uploadApk(String url, String key, String token, Apk apk) {
        OkHttpClient client = new OkHttpClient()
        client.setConnectTimeout(10, TimeUnit.SECONDS);
        client.setReadTimeout(60, TimeUnit.SECONDS);
        MultipartBuilder build = new MultipartBuilder().type(MultipartBuilder.FORM)
        build.addFormDataPart("key", key)
        build.addFormDataPart("token", token)
        build.addFormDataPart("file",
                apk.file.name,
                RequestBody.create(
                        MediaType.parse("application/vnd.android.package-archive"),
                        apk.file)
        )
        HashMap<String, String> params = apk.getParams()
        for (String k : params.keySet()) {
            println("add part key: " + k + " value: " + params.get(k))
            build.addFormDataPart(k, params.get(key))
        }
        Request request = new Request.Builder().url(url).post(build.build()).build()
        Response response = client.newCall(request).execute()
        if (response == null || response.body() == null) return null
        InputStream is = response.body().byteStream()
        BufferedReader reader = new BufferedInputStream(new InputStreamReader(is))
        JSONObject json = new JSONObject(reader)
        is.close()
        return json
    }
}