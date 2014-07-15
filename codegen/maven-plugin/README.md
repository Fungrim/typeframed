# Maven Plugin #

This is the Typeframed Maven plugin. You will need a protoc plugin to compile your protocol files. After that you need a Typeframed dependency:

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