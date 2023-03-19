package com.github.simiacryptus.aicoder.proxy

import com.github.simiacryptus.openai.proxy.BasicSoftwareProjectAI
import com.github.simiacryptus.openai.proxy.BasicSoftwareProjectAI.Companion.parallelImplement
import com.github.simiacryptus.openai.proxy.BasicSoftwareProjectAI.Companion.write
import org.junit.jupiter.api.Test
import java.util.*

/**
 * AutoDevelop takes a software project description and generates a software project with all the necessary files.
 */
class AutoDevelop : GenerationReportBase() {

    @Test
    fun softwareProject() {
        runReport("SoftwareProject", BasicSoftwareProjectAI::class) { api, logJson, out ->
            report(api, logJson, out)
        }
    }

    private fun report(
        api: BasicSoftwareProjectAI,
        logJson: (Any?) -> Unit,
        out: (Any?) -> Unit
    ) {
        var description: String
        description = """
                |
                |Slack bot to monitor a support alias
                |All requests tagging an alias are recorded in a database
                |When requests are tagged with a specific label, the bot will send a message to a slack channel
                |Fully implement all functions
                |Do not comment code
                |Include documentation and build scripts
                |
                |Language: Kotlin
                |Frameworks: Gradle, Spring
                |
                """.trimMargin()
        description = """
                |
                |Create a website where users can upload stories, share them, and rate them
                |
                |Fully implement all functions
                |Do not comment code
                |Include documentation and build scripts
                |
                |Language: Kotlin
                |Frameworks: Gradle, Spring
                |
                """.trimMargin()


        var project: BasicSoftwareProjectAI.Project? = null
        var requirements: BasicSoftwareProjectAI.ProjectStatements? = null
        var projectDesign: BasicSoftwareProjectAI.ProjectDesign? = null
        var components: Map<BasicSoftwareProjectAI.ComponentDetails, BasicSoftwareProjectAI.FileList>? = null
        var documents: Map<BasicSoftwareProjectAI.DocumentationDetails, BasicSoftwareProjectAI.FileList>? = null
        var tests: Map<BasicSoftwareProjectAI.TestDetails, BasicSoftwareProjectAI.FileList>? = null
        var implementations: Map<BasicSoftwareProjectAI.FileSpecification, BasicSoftwareProjectAI.SourceCode?>? = null

        try {
            project = api.newProject(description.trim())
            logJson(project)
            requirements = api.getProjectStatements(description.trim(), project)
            logJson(requirements)
            projectDesign = api.buildProjectDesign(project, requirements)
            logJson(projectDesign)
            components =
                projectDesign.components.map { it to api.buildComponentFileSpecifications(project, requirements, it) }
                    .toMap()
            logJson(components)
            documents =
                projectDesign.documents.map {
                    it to api.buildDocumentationFileSpecifications(
                        project,
                        requirements,
                        it
                    )
                }.toMap()
            logJson(documents)
            tests = projectDesign.tests.map { it to api.buildTestFileSpecifications(project, requirements, it) }.toMap()
            logJson(tests)
            implementations = parallelImplement(api, project, components, documents, tests, 1, 7)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //logJson(files)


        if (project != null) {
            out(
                """
                |
                |# ${project.name}
                |
                |${project.description}
                |
                |Language: ${project.language}
                |
                |Libraries: ${project.libraries.joinToString(", ")}
                |
                |Build Tools: ${project.buildTools.joinToString(", ")}
                |
                |""".trimMargin()
            )
        }


        if (implementations != null) {
            val relative = "projects/${project?.name ?: UUID.randomUUID()}.zip"
            val zipArchiveFile = outputDir.resolve(relative)
            zipArchiveFile.parentFile.mkdirs()
            write(zipArchiveFile, implementations)
            out(
                """
                |
                |## Project Files
                |
                |[Download]($relative)
                |
                |""".trimMargin()
            )
            implementations.forEach { (file, sourceCodes) ->
                out(
                    """
                    |
                    |### ${file.location.fileName}
                    |
                    |${file.description}
                    |
                    |```${sourceCodes!!.language.lowercase()}
                    |${sourceCodes.code}
                    |```
                    |
                    |""".trimMargin()
                )
            }
        }

    }
}