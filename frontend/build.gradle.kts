
import com.moowork.gradle.node.NodeExtension
import com.moowork.gradle.node.npm.NpmTask
import com.moowork.gradle.node.npm.NpxTask

plugins {
    base
    id("com.github.node-gradle.node")
}

val node = "12.16.1"
val npm = "6.13.4"
val nodeInstallDir = file("${project.buildDir}/node")
val npmInstallDir = file("${project.buildDir}/npm")


val angularDist by configurations.creating {
    description = "Transpiled static resources"
}

val buildDir = "${project.buildDir}/dist/frontend"

configure<NodeExtension> {
    version = node
    npmVersion = npm
    download = true
    workDir = nodeInstallDir
    npmWorkDir = npmInstallDir
}

tasks {
    val npmInstall = named<NpmTask>("npmInstall") {
        setArgs(listOf("--prefer-offline", "--no-audit"))
    }

    val npmSetup by existing
    val nodeSetup by existing
    val prepareWrapper by registering(Task::class) {
        dependsOn(npmSetup, nodeSetup)

        inputs.dir(nodeInstallDir)
        inputs.dir(npmInstallDir)
        outputs.files(file("npmw"), file("npxw"))

        doFirst {
            val npmDir = File(npmInstallDir, npmInstallDir.list { _, name -> name.startsWith("npm-v${npm}") }?.first()!!)
            val nodeDir = File(nodeInstallDir, nodeInstallDir.list { _, name -> name.startsWith("node-v${node}") }?.first()!!)

            logger.lifecycle("Generating wrapper to access ${npmDir}...")
            listOf("npm", "npx").forEach {
                val wrapper = file(it + "w")
                java.nio.file.Files.writeString(wrapper.toPath(), wrapperScript(npmDir, nodeDir, it))
                wrapper.setExecutable(true)
            }
        }
    }

    npmSetup.configure {
        finalizedBy(prepareWrapper)
    }

    val ngBuild = register<NpxTask>("ngBuild") {
        inputs.dir("src")
        inputs.files("angular.json", "tsconfig.json")
        outputs.dir(buildDir)

        command = "ng"
        setArgs(listOf("build", "--output-path=${buildDir}", "--prod"))
        dependsOn(npmInstall)
        outputs.cacheIf { true }
    }

    val ngLint by registering(NpxTask::class) {
        class LintReport(val report: File) : StandardOutputListener {
            override fun onOutput(output: CharSequence?) {
                report.appendText(output.toString())
            }
        }

        val reportDir = File("${project.buildDir}/reports/lint")
        val outLint = LintReport(File(reportDir, "ng.out"))
        val errLint = LintReport(File(reportDir, "ng.err"))

        doFirst {
            reportDir.deleteRecursively()
            reportDir.mkdirs()
            listOf(outLint, errLint).forEach { it.report.createNewFile() }
            logging.addStandardOutputListener(outLint)
            logging.addStandardErrorListener(errLint)
        }

        doLast {
            logging.removeStandardOutputListener(outLint)
            logging.removeStandardErrorListener(errLint)
        }

        inputs.dir("src")
        outputs.files(outLint.report, errLint.report)

        command = "ng"
        setArgs(listOf("lint"))
        dependsOn(npmInstall)
        outputs.cacheIf { true }
    }

    val ngTest by registering(NpxTask::class) {
        inputs.dir("src")
        inputs.files("jest.config.js")

        outputs.dir("${project.buildDir}/reports/jest")
        outputs.dir("${project.buildDir}/reports/coverage")
        command = "ng"
        setArgs(listOf("test"))
        dependsOn(npmInstall)
        outputs.cacheIf { true }
    }

    check {
        dependsOn(ngLint, ngTest)
    }

    val zip by registering(Zip::class) {
        archiveBaseName.set("frontend-dist")
        archiveExtension.set("jar")
        from(file(buildDir))

        dependsOn(ngBuild)
    }

    artifacts.add(angularDist.name, zip)
}

fun wrapperScript(npmFullDir: File, nodeFullDir: File, command: String): String {
    return """
            #!/bin/sh

            export PATH=$npmFullDir/bin:$nodeFullDir/bin:${'$'}PATH
            $command "${'$'}@"
          """.trimIndent()
}


val coverageExclusions = listOf(
    "src/app/**/*.spec.ts",
    "src/app/**/*.module.ts"
)