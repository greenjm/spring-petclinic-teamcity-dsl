import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.Swabra
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.swabra
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2019.2"

project {
    vcsRoot(MyVcs)
    buildType(Build)
    subProject()
}

object Build : BuildType({
    name = "Build"
    artifactRules = "target/*jar"

    vcs {
        root(MyVcs)
    }
    steps {
        powerShell {
            name = "Test PowerShell Build Step"
            scriptMode = script {
                content = "echo 'Hello World'"
            }
        }
    }
    triggers {
        vcs {
            groupCheckinsByCommitter = true
        }
    }
})

object MyVcs : GitVcsRoot({
    name = "MyVcs"
    url = "https://github.com/spring-projects/spring-petclinic.git"
    branch = "main"
})


fun wrapWithFeature(buildType: BuildType, featureBlock: BuildFeatures.() -> Unit): BuildType {
    buildType.features {
        featureBlock()
    }
    return buildType
}