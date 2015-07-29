package me.isming.fir.gradle.plugins

import org.gradle.api.NamedDomainObjectContainer

class FirExtension {
    final private NamedDomainObjectContainer<ApkTarget> firApks
    String appId
    String userToken

    public FirExtension(NamedDomainObjectContainer<ApkTarget> apks) {
        firApks = apks
    }

    public apks(Closure closure) {
        firApks.configure(closure)
    }
}