package me.isming.fir.gradle.plugins

import org.gradle.api.Project

class Apk {
    String name
    File file
    String version
    String build
    String changelog
    File icon


    Apk(String name, File file, String version=null, String build=null, String changelog=null, File icon = null) {
        this.name = name
        this.file = file
        this.version = version
        this.build = build
        this.changelog = changelog
        this.icon = icon
    }

    public HashMap<String, String> getParams() {
        HashMap<String, String> params = new HashMap<String, String>()
        if(version != null) {
            params.put("x:version", version)
        }
        if(build != null) {
            params.put("x:build", build)
        }
        if(changelog != null) {
            params.put("x:changelog", changelog)
        }
        if (name != null) {
            params.put("x:name", name)
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
            File icon = null
            if (_apk.hasProperty("version") && _apk.version != null) {
                version = _apk.version
            }
            if(_apk.hasProperty("sourceFile") && _apk.sourceFile != null) {
                println("sourceFile is note null" + _apk.sourceFile.name)
                file = _apk.sourceFile
            } else {
                println("sourceFile is null")
            }
            if(_apk.hasProperty("build") && _apk.build != null) {
                build = _apk.build
            }
            if(_apk.hasProperty("changelog") && _apk.changelog != null) {
                changelog = _apk.changelog
            }

            if (_apk.hasProperty("icon") && _apk.icon != null) {
                icon = _apk.icon
            }
            Apk apk = new Apk(name, file, version, build, changelog, icon);
            apks.add(apk)
        }
        return apks
    }
}
