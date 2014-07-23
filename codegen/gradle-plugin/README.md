# Gradle Plugin #

## Usage ##

This is the Typeframed Gradle plugin. As with the maven plugin you will also need a protoc plugin to compile your protocol classes. Below is an extract from a gradle build file containing only the Typeframed sections:

```
apply plugin: 'typeframed'

buildscript {
  repositories {
    mavenCentral()
  }
  dependencies {
    classpath 'org.typeframed:typeframed-codegen-generator:1.0.0'
    classpath 'org.typeframed:typeframed-codegen-gradle-plugin:1.0.0'
  }
}

typeframed {
  codegenPackage = "org.typeframed.protobuf"
}

dependencies {
  compile 'com.google.protobuf:protobuf-java:2.5.0'
  compile 'org.typeframed:typeframed-api:1.0.0'
}
```