package me.isming.fir.gradle.plugins

import org.gradle.api.Project

class Apk {
    String name
    File file
    String version
    String build
    String changelog


    Apk(String name, File file, String version=null, String build=null, String changelog=null) {
        this.name = name
        this.file = file
        this.version = version
        this.build = build
        this.changelog = changelog
    }

    public HashMap<String, String> getParams() {
        HashMap<String, String> params = new HashMap<String, String>()
        if(version != null) {
            params.put("version", version)
        }
        if(build != null) {
            params.put("build", build)
        }
        if(changelog != null) {
            params.put("changelog", changelog)
        }
        return params
    }


    public static List<Apk> getApks(Project project, String searchApkName = "") {
        List<Apk> apks = []
        for (_apk in project.firApks) {
            String name = _apk.name
            if(searchApkName != "" && searchApkName != name) continue

            File file = null
            String version = null
            String build = null
            String changelog = null
            if (_apk.hasProperty("version") && _apk.version != null) {
                version = _apk.version
            }
            if(_apk.hasProperty("sourceFile") && _apk.sourceFile != null) {
                file = _apk.sourceFile
            }
            if(_apk.hasProperty("build") && _apk.build != null) {
                build = _apk.build
            }
            if(_apk.hasProperty("changelog") && _apk.changelog != null) {
                changelog = _apk.changelog
            }
            Apk apk = new Apk(name, file, version, build, changelog);
            apks.add(apk)
        }
        return apks
    }
}
