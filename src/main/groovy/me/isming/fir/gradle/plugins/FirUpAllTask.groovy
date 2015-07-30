package me.isming.fir.gradle.plugins


import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import org.gradle.api.Project
import java.util.HashMap
import org.json.JSONObject

class FirUpAllTask extends FirTask {
    @TaskAction
    def uploadFir() {
        List<Apk> apks = Apk.getApks(project)
        if (apks != null && apks.size() > 0) {
            super.upload(project, apks.get(0))
        }
    }
}