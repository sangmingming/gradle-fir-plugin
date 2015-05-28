package me.isming.fir.gradle.plugins

import org.gradle.api.NamedDomainObjectContainer

public class FirUperExtension {
    final private NamedDomainObjectContainer<ApkTarget> deploygateApks
    String appid
    String userToken

    public FirUperExtension(NamedDomainObjectContainer<ApkTarget> apks) {
        deploygateApks = apks
    }

    public apks(Closure closure) {
        deploygateApks.configure(closure)
    }
}
