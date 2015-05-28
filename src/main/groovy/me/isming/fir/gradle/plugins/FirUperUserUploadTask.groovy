package me.isming.fir.gradle.plugins

import org.gradle.api.tasks.TaskAction

class FirUperUserUploadTask extends FirUperTask {
    String apkName
    @TaskAction
    def userUploadTasks() {
        List<Apk> apks = Apk.getApks(project, apkName)
        super.upload(project, apks)
    }
}
