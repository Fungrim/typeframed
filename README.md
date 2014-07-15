# Typeframed #
Welcome to Typeframed, a protocol, API, plugins and code generators for polomorphic protobuf streaming. That's quite a mouthful, let's break it down. This project...

* ... introduces a wire protocol for determining type and message length of protobuf messages. It also adds an optional header and checksum per message.  
* ... have an API for handling protobuf messages in runtime, including utilities for writing them off/on streams and byte buffers.
* ... contains a build time code generator that will parse a protobuf file to determine message ID's and generate code to help with polymorphism.
* ... contains plugins for various build systems to run the above code generation.

Currently the code generation and API is Java only, but we're looking at Python, Go and JavaScript down the road.

# Wire Protocol #
The wire protocol wraps each protobuf message in an envelope containing an optional header field and an equally optional checksum. 

```
-------------------------------------------------------------------
| type | hdr len [| header] | msg len | msg | chsum len [| chsum] |
-------------------------------------------------------------------
```

In order of appearance:

* *type* - Type identifier; varint.
* *header length* - Length of header; varint (0 == no header)
* *header* - Header data, custom encoded.
* *message length* - Message length; varint.
* *message* - Protobuf message
* *checksum length* - Length of checksum; varint (0 == no checksum)
* *checksum* - Checksum data, custom encoded

All length fields and the type field are 32 bit raw [varints](https://developers.google.com/protocol-buffers/docs/encoding). From this follows that a minimal Typeframed messages is 4 bytes excluding protobuf message length.  

## Header ##
The header field is optional and will be customized per installation. It could for example carry a correlation ID for request/response messaging, transaction ID for idempotent messaging etc. 

## Checksum ##
Likewise the checksum is optional. If included it should contain a fast checksum (such as the default CRC32) on the protobuf message in its binary form. 

# Embedding ID in .proto files #
Typeframed uses an option field in protobuf to embed ID information for each message. An example protofile for a *hello world* example might look like this:

```
package test;

// #1 - import descriptor
import "descriptor.proto";

// #2 - declare type_id option
extend google.protobuf.MessageOptions {
  optional int32 type_id = 50666;
}

message HelloRequest {
  // #3 - set ID for message
  option (type_id) = 100; 
  required string name = 1;
}

message HelloResponse {
  // #3 - set ID for message
  option (type_id) = 101; 
  required string text = 1;
}
``` 
The protofile above declares two messages with type ID 100 and 101. These type ID's are set as option fields - hence the import and separate declaration - which will be read by Typeframed to generate polymorphic helper code.

# Code Generation #
Typeframed contains a parser for proto files to extract ID information and can generate helper code for dealing with the different types. 

## Java ##
There are two plugins available for Gradle and Maven. Both will generate the same code: 

* *JavaTypeDictionary* - A concrete instance of a *type dictionary* that maps the Java version of the protofile messages to their type ID's. 
* *JavaTypeSwitchTarget* - An abstract message handler class with one *handle* method for each message type. 
* *JavaTypeSwitch* - A router implementation that given a *java switch target* and a *java type dictionary* knows how to cast generic message instances to the switch target handler. 

Here's a simple example of our *hello world* protocol. Notice that it extends the *JavaTypeSwitchTarget*  which have been generated - that way you will never forget protocol changes as your code will stop compiling until you repair it. 

```
#!java
public class Handler extends JavaTypeSwitchTarget {

    @Override
    public void handle(HelloRequest msg) { }

    @Override
    public void handle(HelloResponse msg) { }

}
```

Given this, we can now read and handle messages using the Typeframed API:

```
#!java
public void readMsg() throws IOException {
    // get input stream
    InputStream in = getInputStream();
    // create generated dictionary
    JavaTypeDictionary dictionary = new JavaTypeDictionary();
    // create stream reader (API class)
    StreamReader<Void> reader = new StreamReader<Void>(dictionary, in);
    // read an envolope (message plus a void header)
    Envelope<Void> envelope = reader.read();
    // create handler and generated switch
    Handler handler = new Handler();
    JavaTypeSwitch typeSwitch = new JavaTypeSwitch(handler);
    // forward message to handler
    typeSwitch.forward(envelope.getMessage());
}
```

### Maven Plugin ###
You will need a protoc plugin to compile your protocol files. After that you need a Typeframed dependency:

```
<dependency>
  <groupId>org.typeframed</groupId>
  <artifactId>typeframed-api</artifactId>
  <version>1.0.0</version>
</dependency>
```

Then add the Typeframed plugin (after the protoc plugin):

```
<plugin>
  <artifactId>typeframed-codegen-maven-plugin</artifactId>
  <groupId>org.typeframed</groupId>
  <version>1.0.0</version>
  <configuration>
    <protocolFile>src/main/proto/test.proto</protocolFile>
    <codegenPackage>org.typeframed.test</codegenPackage>
  </configuration>
  <executions>
    <execution>
      <goals>
        <goal>generate</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```
Most probably you will need the "build helper plugin" to add your generated sources to the main artifact build:

```
<plugin>
  <groupId>org.codehaus.mojo</groupId>
  <artifactId>build-helper-maven-plugin</artifactId>
  <executions>
    <execution>
      <phase>generate-sources</phase>
      <goals>
        <goal>add-source</goal>
      </goals>
      <configuration>
        <sources>
          <source>${project.build.directory}/generated-sources/typeframed/java</source>
          <source>${project.build.directory}/generated-sources/protobuf/java</source>
        </sources>
      </configuration>
    </execution>
  </executions>
</plugin>
```

### Gradle Plugin ###
As with the maven plugin you will also need a protoc plugin to compile your protocol classes. Below is an extract from a gradle build file containing only the Typeframed sections:

```
apply plugin: 'typeframed'

buildscript {
  repositories {
    mavenCentral()
  }
  dependencies {
    classpath 'org.typeframed:typeframed-codegen-generator:0.1-SNAPSHOT'
    classpath 'org.typeframed:typeframed-codegen-gradle-plugin:0.1-SNAPSHOT'
  }
}

typeframed {
  codegenPackage = "org.typeframed.protobuf"
}

dependencies {
  compile 'com.google.protobuf:protobuf-java:2.5.0'
  compile 'org.typeframed:typeframed-api:0.1-SNAPSHOT'
}
```

# Release Notes #

## 1.0.0 #

 * First release. 