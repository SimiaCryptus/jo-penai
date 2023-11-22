import org.gradle.api.tasks.testing.logging.TestLogEvent
import java.net.URI

fun properties(key: String) = project.findProperty(key).toString()
group = properties("libraryGroup")
version = properties("libraryVersion")

plugins {
    java
    `java-library`
    `maven-publish`
    id("org.jetbrains.kotlin.jvm") version "1.9.20"
    id("signing")
}

repositories {
    mavenCentral()
}

tasks {

    compileKotlin {
        kotlinOptions {
            javaParameters = true
        }
    }
    compileTestKotlin {
        kotlinOptions {
            javaParameters = true
        }
    }

    wrapper {
        gradleVersion = properties("gradleVersion")
    }

    test {
        useJUnitPlatform()
        systemProperty("surefire.useManifestOnlyJar", "false")
        testLogging {
            events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

kotlin {
    jvmToolchain(11)
}

val logback_version = "1.4.11"
val jupiter_version = "5.10.1"
val jackson_version = "2.15.3"
dependencies {

    implementation(group = "org.slf4j", name = "slf4j-api", version = "2.0.9")
    implementation(group = "org.apache.httpcomponents.client5", name = "httpclient5", version = "5.2.1")
    implementation(group = "com.fasterxml.jackson.core", name = "jackson-databind", version = jackson_version)
    implementation(group = "com.fasterxml.jackson.core", name = "jackson-annotations", version = jackson_version)
    implementation(group = "com.fasterxml.jackson.module", name = "jackson-module-kotlin", version = jackson_version)
    implementation(group = "com.google.guava", name = "guava", version = "32.1.3-jre")
    implementation(group = "com.google.code.gson", name = "gson", version = "2.10.1")
    implementation(group = "org.openimaj", name = "JTransforms", version = "1.3.10")
    implementation(group = "commons-io", name = "commons-io", version = "2.15.0")

    compileOnlyApi(kotlin("stdlib"))
    compileOnlyApi(kotlin("reflect"))
    testImplementation(kotlin("stdlib"))
    testImplementation(kotlin("reflect"))
    testImplementation(kotlin("script-runtime"))

    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-api", version = jupiter_version)
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-params", version = jupiter_version)
    testRuntimeOnly(group = "org.junit.jupiter", name = "junit-jupiter-engine", version = jupiter_version)
    testImplementation(group = "ch.qos.logback", name = "logback-classic", version = logback_version)
    testImplementation(group = "ch.qos.logback", name = "logback-core", version = logback_version)
    testImplementation(group = "com.squareup.okhttp3", name = "mockwebserver", version = "4.12.0")

}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "jo-penai"
            from(components["java"])
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
            pom {
                name.set("Jo Penai")
                description.set("A Java client for OpenAI's API")
                url.set("https://github.com/SimiaCryptus/JoPenai")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("acharneski")
                        name.set("Andrew Charneski")
                        email.set("acharneski@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://git@github.com/SimiaCryptus/JoPenai.git")
                    developerConnection.set("scm:git:ssh://git@github.com/SimiaCryptus/JoPenai.git")
                    url.set("https://github.com/SimiaCryptus/JoPenai")
                }
            }
        }
    }
    repositories {
        maven {
            val releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotsRepoUrl = "https://oss.sonatype.org/mask/repositories/snapshots"
            url = URI(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
            credentials {
                username = System.getenv("OSSRH_USERNAME") ?: System.getProperty("ossrhUsername")
                        ?: properties("ossrhUsername")
                password = System.getenv("OSSRH_PASSWORD") ?: System.getProperty("ossrhPassword")
                        ?: properties("ossrhPassword")
            }
        }
    }
    if (System.getenv("GPG_PRIVATE_KEY") != null && System.getenv("GPG_PASSPHRASE") != null) afterEvaluate {
        signing {
            sign(publications["mavenJava"])
        }
    }
}

if (System.getenv("GPG_PRIVATE_KEY") != null && System.getenv("GPG_PASSPHRASE") != null) {
    apply<SigningPlugin>()

    configure<SigningExtension> {
        useInMemoryPgpKeys(System.getenv("GPG_PRIVATE_KEY"), System.getenv("GPG_PASSPHRASE"))
        sign(configurations.archives.get())
    }
}
