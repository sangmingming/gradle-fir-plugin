package me.isming.fir.gradle.plugins

import org.gradle.api.Named
import org.gradle.api.internal.project.ProjectInternal

class ApkTarget implements Named {

    String name
    ProjectInternal target
    File sourceFile
    String version
    String build
    String changelog
    File icon

    public ApkTarget(String name) {
        super()
        this.name = name
        this.target = target
    }

    @Override
    String getName() {
        return this.name
    }
}