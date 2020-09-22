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
            if (certObj.has("binary") && apk.file != null) {
                JSONObject binaryObj = certObj.getJSONObject("binary")
                String up_url = binaryObj.getString("upload_url")
                String up_token = binaryObj.getString("token")
                String up_key = binaryObj.getString("key")
                JSONObject reusltobj = uploadApk(up_url, up_key, up_token, apk)
                println("apk上传结果:" + reusltobj)
            }

            if (certObj.has("icon") && apk.icon != null) {
                JSONObject iconObj = certObj.getJSONObject("icon")
                String up_url = iconObj.getString("upload_url")
                String up_token = iconObj.getString("token")
                String up_key = iconObj.getString("key")
                JSONObject reusltobj = uploadIcon(up_url, up_key, up_token, apk)
                println("icon上传结果:" + reusltobj)

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
        String is = response.body().string()
        println("getAppinfo result:" + is)
        JSONObject json = new JSONObject(is)
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
            build.addFormDataPart(k, params.get(k))
        }
        Request request = new Request.Builder().url(url).post(build.build()).build()
        Response response = client.newCall(request).execute()
        if (response == null || response.body() == null) return null
        String is = response.body().string()
        println("upload result:" + is)
        JSONObject json = new JSONObject(is)
        return json
    }

    private JSONObject uploadIcon(String url, String key, String token, Apk apk) {
        OkHttpClient client = new OkHttpClient()
        client.setConnectTimeout(10, TimeUnit.SECONDS);
        client.setReadTimeout(60, TimeUnit.SECONDS);
        MultipartBuilder build = new MultipartBuilder().type(MultipartBuilder.FORM)
        build.addFormDataPart("key", key)
        build.addFormDataPart("token", token)
        build.addFormDataPart("file",
                apk.icon.name,
                RequestBody.create(
                        MediaType.parse("application/vnd.android.package-archive"),
                        apk.icon)
        )
        Request request = new Request.Builder().url(url).post(build.build()).build()
        Response response = client.newCall(request).execute()
        if (response == null || response.body() == null) return null
        String is = response.body().string()
        println("upload result:" + is)
        JSONObject json = new JSONObject(is)
        return json
    }
}