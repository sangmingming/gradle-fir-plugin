package me.isming.fir.gradle.plugins

import org.apache.commons.lang.WordUtils
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

class FirUper implements Plugin<Project> {
    @Override
    void apply(Project project) {
        def apks = project.container(ApkTarget) {
            String apkName = WordUtils.capitalize(it.toString())
            def userTask = project.task("uploadFir${apkName}", type: FirUpAllTask)
            userTask.group = 'fir'
            userTask.description = "Upload an APK file of ${apkName}"
            userTask.apkName = apkName

            project.extensions.create(it, ApkTarget, apkName)
        }
        def fir = new FirExtension(apks)
        project.convention.plugins.fir = fir
        project.extensions.fir = fir

        def apkUpload = project.task('uploadFir', type:FirUpAllTask)
        apkUpload.group = 'fir'
        apkUpload.description = "Upload all apk to fir"
    }
}