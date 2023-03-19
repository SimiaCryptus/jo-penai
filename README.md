This is an OpenAI API client for Java, built with Kotlin. It provides a simple interface for interacting with OpenAI's API, allowing you to easily generate natural language text, perform advanced calculations, and more.

# Dependencies
This project has the following dependencies:

* `org.openimaj:JTransforms:1.3.10`
* `com.fasterxml.jackson.core:jackson-databind:2.13.0`
* `com.fasterxml.jackson.core:jackson-annotations:2.13.0`
* `com.google.guava:guava:30.1.1-jre`
* `com.google.code.gson:gson:2.8.8`
* `org.apache.httpcomponents:httpclient:4.5.13`
* `org.apache.httpcomponents:httpmime:4.5.13`
* `org.slf4j:slf4j-api:1.7.32`
* `org.slf4j:slf4j-simple:1.7.32`
* `commons-io:commons-io:2.11.0`
* `kotlin:stdlib`
* `org.junit.jupiter:junit-jupiter-api:5.8.1`

# Building
To build this project, you will need to have Java 11 or later installed on your system. You can build the project using the following command:


```shell
./gradlew build
```

This will compile the project and generate the necessary JAR files. You can find the compiled JAR files in the build/libs directory.

# Publishing
This project uses the maven-publish plugin to publish artifacts to a Maven repository. You can publish the project using the following command:

``` bash
./gradlew publishToMavenLocal
```

This will publish the artifacts to your local Maven repository. You can also publish the artifacts to a remote repository by configuring the publishing block in the build.gradle.kts file.

# Contributing
If you would like to contribute to this project, please feel free to submit a pull request. Before submitting a pull request, please make sure that your changes are consistent with the project's style and that all tests pass.
