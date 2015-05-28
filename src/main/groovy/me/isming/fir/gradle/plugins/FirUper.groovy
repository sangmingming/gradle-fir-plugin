package me.isming.fir.gradle.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.apache.commons.lang.WordUtils

class FirUper implements Plugin<Project> {
    void apply(Project project) {
        def apks = project.container(ApkTarget) {
            String apkName = WordUtils.capitalize(it.toString())
            def userTask = project.task("uploadFir${apkName}", type: FirUperUserUploadTask)
            userTask.group = 'FirUper'
            userTask.description = "Upload an APK file of ${apkName}" 
            userTask.apkName = apkName

            project.extensions.create(it, ApkTarget, apkName)
        }

        def firuper = new FirUperExtension(apks)
        project.convention.plugins.firuper = firuper
        project.extensions.firuper = firuper

        def apkUpload = project.task('uploadFir', type: FirUpAllUploadTask)
        apkUpload.group = 'FirUper'
        apkUpload.description = 'Uploads the APK file. Also updates the distribution specified by distributionKey if configured'
    }
}
