package com.github.simiacryptus.openai.proxy

import java.io.File
import java.util.concurrent.Callable
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.atomic.AtomicInteger
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

interface BasicSoftwareProjectAI {
    fun newProject(description: String): Project

    data class Project(
        val name: String = "",
        val description: String = "",
        val language: String = "",
        val features: List<String> = listOf(),
        val libraries: List<String> = listOf(),
        val buildTools: List<String> = listOf(),
    )

    fun getProjectStatements(description: String, project: Project): ProjectStatements

    data class ProjectStatements(
        val assumptions: List<String> = listOf(),
        val designPatterns: List<String> = listOf(),
        val requirements: List<String> = listOf(),
        val risks: List<String> = listOf(),
    )

    fun buildProjectDesign(project: Project, requirements: ProjectStatements): ProjectDesign

    data class ProjectDesign(
        val components: List<ComponentDetails> = listOf(),
        val documents: List<DocumentationDetails> = listOf(),
        val tests: List<TestDetails> = listOf(),
    )

    data class ComponentDetails(
        val name: String = "",
        val description: String = "",
        val features: List<String> = listOf(),
    )

    data class TestDetails(
        val name: String = "",
        val steps: List<String> = listOf(),
        val expectations: List<String> = listOf(),
    )

    data class DocumentationDetails(
        val name: String = "",
        val description: String = "",
        val sections: List<String> = listOf(),
    )

    fun buildProjectFileSpecifications(
        project: Project,
        requirements: ProjectStatements,
        design: ProjectDesign,
        recursive: Boolean = true
    ): FileList

    fun buildComponentFileSpecifications(
        project: Project,
        requirements: ProjectStatements,
        design: ComponentDetails,
        recursive: Boolean = true
    ): FileList

    fun buildTestFileSpecifications(
        project: Project,
        requirements: ProjectStatements,
        design: TestDetails,
        recursive: Boolean = true
    ): FileList

    fun buildDocumentationFileSpecifications(
        project: Project,
        requirements: ProjectStatements,
        design: DocumentationDetails,
        recursive: Boolean = true
    ): FileList

    data class FileList(
        val files: List<FileSpecification> = listOf(),
    )

    data class FileSpecification(
        @Notes("either code, test, or doc") val type: String = "",
        val description: String = "",
        val requires: List<FilePath> = listOf(),
        val publicProperties: List<String> = listOf(),
        val publicMethodSignatures: List<String> = listOf(),
        val language: String = "",
        val location: FilePath = FilePath(),
    )

    data class FilePath(
        @Notes("e.g. projectRoot/README.md") val fileName: String = "",
    ) {
        override fun toString(): String {
            return fileName
        }
    }

    fun implementSpecification(
        project: Project,
        imports: List<FileSpecification>,
        specification: FileSpecification
    ): SourceCode


    fun implementComponentSpecification(
        project: Project,
        component: ComponentDetails,
        imports: List<FileSpecification>,
        specification: FileSpecification
    ): SourceCode


    fun implementTestSpecification(
        project: Project,
        test: TestDetails,
        imports: List<FileSpecification>,
        specification: FileSpecification
    ): SourceCode


    fun implementDocumentationSpecification(
        project: Project,
        documentation: DocumentationDetails,
        imports: List<FileSpecification>,
        specification: FileSpecification
    ): SourceCode

    data class SourceCode(
        val language: String = "",
        val code: String = "",
    )

    companion object {
        val log = org.slf4j.LoggerFactory.getLogger(BasicSoftwareProjectAI::class.java)
        fun parallelImplement(
            api: BasicSoftwareProjectAI,
            project: Project,
            components: Map<ComponentDetails, FileList>,
            documents: Map<DocumentationDetails, FileList>,
            tests: Map<TestDetails, FileList>,
            drafts: Int,
            threads: Int
        ): Map<FileSpecification, SourceCode?> = parallelImplementWithAlternates(
            api,
            project,
            components,
            documents,
            tests,
            drafts,
            threads
        ).mapValues { it.value.maxByOrNull { it.code.length } }

        fun parallelImplementWithAlternates(
            api: BasicSoftwareProjectAI,
            project: Project,
            components: Map<ComponentDetails, FileList>,
            documents: Map<DocumentationDetails, FileList>,
            tests: Map<TestDetails, FileList>,
            drafts: Int,
            threads: Int,
            progress: (Double) -> Unit = {}
        ): Map<FileSpecification, List<SourceCode>> {
            val threadPool = Executors.newFixedThreadPool(threads)
            try {
                val totalDrafts = (components + tests + documents).values.sumOf { it.files.size } * drafts
                val currentDraft = AtomicInteger(0)
                val fileImplCache = ConcurrentHashMap<String, List<Future<Pair<FileSpecification, SourceCode>>>>()
                val normalizeFileName : (String) -> String = {
                    it.trimStart('/','.')
                }
                val componentImpl = components.flatMap { (component, files) ->
                    files.files.flatMap { file ->
                        fileImplCache.getOrPut(normalizeFileName(file.location.fileName)) {
                            (0 until drafts).map { _ ->
                                threadPool.submit(Callable {
                                    val implement = api.implementComponentSpecification(
                                        project,
                                        component,
                                        files.files.filter { file.requires.contains(it.location) }.toList(),
                                        file.copy(requires = listOf())
                                    )
                                    (currentDraft.incrementAndGet().toDouble() / totalDrafts)
                                        .also { progress(it) }
                                        .also { log.info("Progress: $it") }
                                    file to implement
                                })
                            }
                        }
                    }
                }.toTypedArray().map {
                    try {
                        it.get()
                    } catch (e: Exception) {
                        null
                    }
                }.filterNotNull().groupBy { it.first }
                    .mapValues { it.value.map { it.second }.sortedBy { it.code.length } }
                val testImpl = tests.flatMap { (testDetails, files) ->
                    files.files.flatMap { file ->
                        fileImplCache.getOrPut(normalizeFileName(file.location.fileName)) {
                            (0 until drafts).map { _ ->
                                threadPool.submit(Callable {
                                    val implement = api.implementTestSpecification(
                                        project,
                                        testDetails,
                                        files.files.filter { file.requires.contains(it.location) }.toList(),
                                        file.copy(requires = listOf())
                                    )
                                    (currentDraft.incrementAndGet().toDouble() / totalDrafts)
                                        .also { progress(it) }
                                        .also { log.info("Progress: $it") }
                                    file to implement
                                })
                            }
                        }
                    }
                }.toTypedArray().map {
                    try {
                        it.get()
                    } catch (e: Exception) {
                        null
                    }
                }.filterNotNull().groupBy { it.first }
                    .mapValues { it.value.map { it.second }.sortedBy { it.code.length } }
                val docImpl = documents.flatMap { (documentationDetails, files) ->
                    files.files.flatMap { file ->
                        fileImplCache.getOrPut(normalizeFileName(file.location.fileName)) {
                            (0 until drafts).map { _ ->
                                threadPool.submit(Callable {
                                    val implement = api.implementDocumentationSpecification(
                                        project,
                                        documentationDetails,
                                        files.files.filter { file.requires.contains(it.location) }.toList(),
                                        file
                                    )
                                    (currentDraft.incrementAndGet().toDouble() / totalDrafts)
                                        .also { progress(it) }
                                        .also { log.info("Progress: $it") }
                                    file to implement
                                })
                            }
                        }
                    }
                }.toTypedArray().map {
                    try {
                        it.get()
                    } catch (e: Exception) {
                        null
                    }
                }.filterNotNull().groupBy { it.first }
                    .mapValues { it.value.map { it.second }.sortedBy { it.code.length } }
                return componentImpl + docImpl + testImpl
            } finally {
                threadPool.shutdown()
            }
        }

        fun write(
            zipArchiveFile: File,
            implementations: Map<FileSpecification, SourceCode?>
        ) {
            ZipOutputStream(zipArchiveFile.outputStream()).use { zip ->
                implementations.forEach { (file, sourceCodes) ->
                    zip.putNextEntry(ZipEntry(file.location.toString()))
                    zip.write(sourceCodes!!.code.toByteArray())
                    zip.closeEntry()
                }
            }
        }
    }
}

