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

class FirUperTask extends DefaultTask {
    private final String API_END_POINT = "http://fir.im/api/v2"

    void upload(Project project, Apk apk) {
        JSONObject jsonObject = getAppInfo(project)
        if (jsonObject == null) {
            return
        }
        JSONObject pkgJson = jsonObject.getJSONObject("pkg")
        if (pkgJson != null) {
            JSONObject result = httpPost(pkgJson, apk)
            errorHandling(result)
            println "${apk.name} result: ${result.toString()}"
        }
    }

    private void errorHandling(JSONObject json) {
        print(json)
    }

    private JSONObject getAppInfo(Project project) {
        OkHttpClient client = new OkHttpClient()
        client.setConnectTimeout(10, TimeUnit.SECONDS)
        client.setReadTimeout(60, TimeUnit.SECONDS)
        String url = getAppinfoUrl(project)
        Request request = new Request.Builder().url(url).get().build()
        Response response = client.newCall(request).execute()

        if (response == null || response.body() == null ) return null;
        InputStream is = response.body().byteStream()
        BufferedReader reader = new BufferedReader(new InputStreamReader(is))
        JSONObject json = new JSONObject(reader.readLine())
        is.close()
        return json;
    }

    private String getAppinfoUrl(Project project) {
        String appid = project.firuper.appid
        String userToken = project.firuper.userToken
        if (appid == null || userToken == null) {
            throw new GradleException("appid or userToken is missing")
        }
        String endPoint = API_END_POINT + "/app/info/"+ appid + "?token=" + userToken + "&type=android"
        return endPoint
    }

    private JSONObject httpPost(JSONObject json, Apk apk) {
        String uploaderUrl = json.getString("url");
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
}
