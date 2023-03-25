import java.net.URI

/**
 * An OpenAI API client for Java
 */
plugins {
    `java-library`
    `maven-publish`
    signing
    kotlin("jvm") version "1.7.21"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.openimaj:JTransforms:1.3.10")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.14.2")
    implementation("com.google.guava:guava:31.1-jre")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.apache.httpcomponents:httpclient:4.5.14")
    implementation("org.apache.httpcomponents:httpmime:4.5.14")
    implementation("org.slf4j:slf4j-api:2.0.5")
    implementation("org.slf4j:slf4j-simple:2.0.5")
    implementation("commons-io:commons-io:2.11.0")
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.10")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.2")
    testImplementation(kotlin("script-runtime"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
}
fun properties(key: String) = project.findProperty(key).toString()
group = properties("libraryGroup")
version = properties("libraryVersion")

java {
    withJavadocJar()
    withSourcesJar()
}

kotlin {
    jvmToolchain(11)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "joe-penai"
            //from(components["java"])
            from(components["kotlin"])
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
            pom {
                name.set("Joe Penai")
                description.set("A Java client for OpenAI's API")
                url.set("https://github.com/SimiaCryptus/JoePenai")
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
                    connection.set("scm:git:git://git@github.com/SimiaCryptus/JoePenai.git")
                    developerConnection.set("scm:git:ssh://git@github.com/SimiaCryptus/JoePenai.git")
                    url.set("https://github.com/SimiaCryptus/JoePenai")
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
                username = System.getenv("OSSRH_USERNAME") ?: System.getProperty("ossrhUsername") ?: properties("ossrhUsername")
                password = System.getenv("OSSRH_PASSWORD") ?: System.getProperty("ossrhPassword") ?: properties("ossrhPassword")
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(System.getenv("GPG_PRIVATE_KEY"), System.getenv("GPG_PASSPHRASE"))
    val archives = project.configurations["archives"]
    sign(archives)
}
