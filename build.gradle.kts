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
    // Add the JTransforms library version 1.3.10
    implementation("org.openimaj:JTransforms:1.3.10")
    // Add the Jackson Databind library version 2.14.2
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    // Add the Jackson Annotations library version 2.13.0
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.13.0")
    // Add the Guava library version 30.1.1-jre
    implementation("com.google.guava:guava:30.1.1-jre")
    // Add the Gson library version 2.10.1
    implementation("com.google.code.gson:gson:2.10.1")
    // Add the Apache HttpClient library version 4.5.14
    implementation("org.apache.httpcomponents:httpclient:4.5.14")
    // Add the Apache HttpMime library version 4.5.13
    implementation("org.apache.httpcomponents:httpmime:4.5.13")
    // Add the SLF4J API library version 1.7.32
    implementation("org.slf4j:slf4j-api:1.7.32")
    // Add the SLF4J Simple library version 1.7.32
    implementation("org.slf4j:slf4j-simple:1.7.32")
    // Add the Commons IO library version 2.11.0
    implementation("commons-io:commons-io:2.11.0")
    // Add the Kotlin Standard Library
    implementation(kotlin("stdlib"))
    // Add the Kotlin Script Runtime library
    testImplementation(kotlin("script-runtime"))
    // Add the JUnit Jupiter API library version 5.8.1
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
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
