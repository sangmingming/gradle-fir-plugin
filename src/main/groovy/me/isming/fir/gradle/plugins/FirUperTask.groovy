package me.isming.fir.gradle.plugins

import com.amazonaws.util.json.JSONObject
import com.squareup.okhttp.MediaType
import com.squareup.okhttp.MultipartBuilder
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.RequestBody
import com.squareup.okhttp.Response
import okio.BufferedSink
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Project
import java.util.concurrent.TimeUnit

class FirUperTask extends DefaultTask {
    private final String API_END_POINT = "http://fir.im/"

    void upload(Project project, Apk apk) {
        JSONObject jsonObject = getAppInfo(project)
        if (jsonObject == null) {
            return
        }
        JSONObject pkgJson = jsonObject.getJSONObject("cert").getJSONObject("binary")
        if (pkgJson != null) {
            JSONObject result = httpPost(pkgJson, apk)
            errorHandling(result)
            println "${apk.name} result: ${result.toString()}"
            String version = result.getString("versionOid")
            String token = project.firuper.userToken
            //updateInfo(version, token)
            println "finish"
        }
    }

    private void errorHandling(JSONObject json) {
        println json.toString()
    }

    private JSONObject getAppInfo(Project project) {
        OkHttpClient client = new OkHttpClient()
        client.setConnectTimeout(10, TimeUnit.SECONDS)
        client.setReadTimeout(60, TimeUnit.SECONDS)
        String url = getAppinfoUrl(project)
        MultipartBuilder multipartBuilder = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
        multipartBuilder.addFormDataPart("type", "android")
        multipartBuilder.addFormDataPart("bundle_id", project.firuper.appid)
        multipartBuilder.addFormDataPart("api_token", project.firuper.userToken)
        Request request = new Request.Builder().url(url).post(multipartBuilder.build()).build()
        Response response = client.newCall(request).execute()

        if (response == null || response.body() == null ) return null;
        InputStream is = response.body().byteStream()
        BufferedReader reader = new BufferedReader(new InputStreamReader(is))
        JSONObject json = new JSONObject(reader.readLine())
        println json.toString()
        is.close()
        return json;
    }

    private String getAppinfoUrl(Project project) {
        String appid = project.firuper.appid
        String userToken = project.firuper.userToken
        if (appid == null || userToken == null) {
            throw new GradleException("appid or userToken is missing")
        }
        String endPoint = API_END_POINT + "apps/"+ appid + "/releases"
        return endPoint
    }

    private JSONObject httpPost(JSONObject json, Apk apk) {
        String uploaderUrl = json.getString("upload_url");
        String token = json.getString("token");
        String key = json.getString("key")
        if (uploaderUrl == null || token == null || key == null)
            throw new GradleException("can't get upload need info")
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(30, TimeUnit.SECONDS);
        client.setReadTimeout(60, TimeUnit.SECONDS);
        MultipartBuilder multipartBuilder = new MultipartBuilder()
                .type(MultipartBuilder.FORM)


        multipartBuilder.addFormDataPart("key", key)
        multipartBuilder.addFormDataPart("token", token)


        multipartBuilder.addFormDataPart("file",
                apk.file.name,
                RequestBody.create(
                        MediaType.parse("application/vnd.android.package-archive"),
                        apk.file)
        )
        multipartBuilder.addFormDataPart("x:name", "妹子图")
        multipartBuilder.addFormDataPart("x:version", "1.2.3")
        multipartBuilder.addFormDataPart("x:build", "123")

        Request request = new Request.Builder().url(uploaderUrl).
                post(multipartBuilder.build()).
                build()

        Response response = client.newCall(request).execute();

        if (response == null || response.body() == null ) return null;
        InputStream is = response.body().byteStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is))
        JSONObject jsonResult = new JSONObject(reader.readLine())
        is.close()

        return jsonResult
    }


//    private JSONObject updateInfo(String versionOid, String userToken) {
//        String url = API_END_POINT + "/appVersion/" + versionOid + "/complete?token=" + userToken + "&type=android"
//        OkHttpClient client = new OkHttpClient()
//        client.setConnectTimeout(10, TimeUnit.SECONDS)
//        client.setReadTimeout(60, TimeUnit.SECONDS)
//        Request request = new Request.Builder().url(url).put(null).build()
//        Response response = client.newCall(request).execute()
//
//        if (response == null || response.body() == null ) return null;
//        InputStream is = response.body().byteStream()
//        BufferedReader reader = new BufferedReader(new InputStreamReader(is))
//        JSONObject json = new JSONObject(reader.readLine())
//        println json.toString()
//        is.close()
//        return json;
//    }
}
