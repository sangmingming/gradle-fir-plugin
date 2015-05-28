package me.isming.fir.gradle.plugins

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test
import me.isming.fir.gradle.plugins.FirUperTask

import static org.junit.Assert.assertTrue

class ApplyPluginTest {
    @Test
    public void checkTask() {
        Project target = ProjectBuilder.builder().build()
        target.apply plugin: 'deploygate'

        assertTrue(target.tasks.uploadDeployGate instanceof FirUperTask)
    }
}
