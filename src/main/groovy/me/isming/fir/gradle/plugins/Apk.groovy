package me.isming.fir.gradle.plugins

import org.gradle.api.Project

class Apk {
    String name
    File file


    Apk(String name, File file) {
        this.name = name
        this.file = file
    }


    public static List<Apk> getApks(Project project, String searchApkName = "") {
        List<Apk> apks = []
        for (_apk in project.deploygateApks) {
            String name = _apk.name
            if(searchApkName != "" && searchApkName != name) continue

            File file = null

            Apk apk = new Apk(name, file);

            if(_apk.hasProperty("sourceFile") && _apk.sourceFile != null) {
                apk.file = _apk.sourceFile
            }
            apks.add(apk)
        }
        return apks
    }
}
