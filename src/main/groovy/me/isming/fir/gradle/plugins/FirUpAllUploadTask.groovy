package me.isming.fir.gradle.plugins

import org.gradle.api.tasks.TaskAction

class FirUpAllUploadTask extends FirUperTask {
    @TaskAction
    def uploadDeployGate() {
        List<Apk> apks = Apk.getApks(project)
        super.upload(project, apks.get(0))
    }
}
