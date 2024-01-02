# DB2Code Project
![db2code](https://github.com/alberlau/DB2Code/blob/master/db2code.png)
## Overview

DB2Code is a tool designed to facilitate the generation of files from JDBC Metadata. This project streamlines the process of loading metadata into objects and then utilizing these objects within Mustache templates to generate the desired output. It's an ideal solution for developers looking to automate and simplify their database related code generation processes.

### Prerequisites

Before you begin, ensure you have the following installed:
- Java Development Kit (JDK) 11, or above
- Maven 3

### Installation

Modify your pom.xml as bellow and adjust necessary parameters:
```xml
    <build>
        <plugins>
            <plugin>
                <groupId>org.db2code</groupId>
                <artifactId>java-pojo-generator-mojo</artifactId>
                <version>1.1.1</version>
                <configuration>
                    <jdbcUrl>jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM '${project.basedir}/init.sql'</jdbcUrl>
                    <jdbcClassName>org.h2.Driver</jdbcClassName>
                    <extractionParameters>
                        <item>
                            <schemaPattern>TEST_SCHEMA</schemaPattern>
                            <catalog>TEST</catalog>
                        </item>
                    </extractionParameters>
                    <baseDir>${project.basedir}</baseDir>
                    <targetFolder>target/generated-sources</targetFolder>
                    <targetPackage>testpkg</targetPackage>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>generatePojo</goal>
                        </goals>
                    </execution>
                </executions>
                <dependencies>
                    <dependency>
                        <groupId>com.h2database</groupId>
                        <artifactId>h2</artifactId>
                        <version>2.2.224</version>
                    </dependency>
                </dependencies>
            </plugin>
        </plugins>
    </build>
```

Optionally if you need to attach generated code to your source code add this:
```xml
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>build-helper-maven-plugin</artifactId>
                <version>3.4.0</version>
                <executions>
                    <execution>
                        <id>add-source</id>
                        <phase>generate-sources</phase>
                        <goals>
                            <goal>add-source</goal>
                        </goals>
                        <configuration>
                            <sources>
                                <source>${project.build.directory}/generated-sources</source>
                            </sources>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
```

### Configuration params

- __jdbcUrl__
- __jdbcClassName__
- __jdbcUser__
- __jdbcPassword__
- __extractionParameters__ exactly as in DatabaseMetadata.getTables
  - __schemaPattern__ supports %_, can be blank
  - __catalog__ can be blank, mostly it's database
  - __tableNamePattern__ supports %_, can be blank, selects all tables
  - __types__ one of TABLE, VIEW, SYSTEM_TABLE, GLOBAL_TEMPORARY, LOCAL_TEMPORARY, ALIAS, SYNONYM, can be blank
  - __exportFile__ file to export metadata, which later can be imported from that file, instead of fetching from DB
  - __importFile__ metadata file previously exported with exportFile, which can be used as source of metadata
- __baseDir__ where to output generated source, can be ${project.baseDir}
- __targetFolder__ where to put sources under baseDir, can be target/generated-sources
- __targetPackage__ what package should be used for generated classes
- __ext__ extension for generated files, defaults to .java
- __dateImpl__ what java date implementation should be used: UTIL_DATE or LOCAL_DATE
- __includeGenerationInfo__ should info about generation be included? Defaults to false

You can customize generation template, by providing __templates__ list:
```xml
  <templates>
      <template>some-template.mustache</template>
  </templates>
```

Currently built-in templates are: __pojo.mustache__, __spring-data.mustache__.
You can provide your own template:

```xml
  <templates>
      <template>${project.baseUri}/my-custom.mustache</template>
  </templates>
```


You can provide multiple executions with different id's to select from different schemas, providing different templates or some other config options.

Check https://github.com/alberlau/DB2Code/tree/master/java-pojo-generator-mojo-example and see example usage.

To see what is exposed into model, check: https://github.com/alberlau/DB2Code/tree/master/core/src/main/java/org/db2code/rawmodel classes
along with adapter class: https://github.com/alberlau/DB2Code/blob/master/java-pojo-generator/src/main/java/org/db2code/generator/java/pojo/adapter/JavaClassAdapter.java

### Use case: export/import from metadata file

To exportMetadata, add to configuration/extractionParameters exportFile

```xml
  <execution>
      <id>exportMetadata</id>
      <goals>
          <goal>generatePojo</goal>
      </goals>
      <configuration>
          <includeGenerationInfo>true</includeGenerationInfo>
          <jdbcUrl>jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM '${project.basedir}/init.sql'</jdbcUrl>
          <jdbcClassName>org.h2.Driver</jdbcClassName>
          <extractionParameters>
              <item>
                  <schemaPattern>TEST_SCHEMA</schemaPattern>
                  <catalog>TEST</catalog>
                  <exportFile>${basedir}/target/classes/TEST_SCHEMA.json</exportFile>
              </item>
          </extractionParameters>
      </configuration>
  </execution>
```
Above can be added under separate maven profile.

Then metadata file can be imported with such execution:
```xml
  <execution>
      <id>importFromExportedMetadata</id>
      <goals>
          <goal>generatePojo</goal>
      </goals>
      <configuration>
          <includeGenerationInfo>true</includeGenerationInfo>
          <extractionParameters>
              <item>
                  <importFile>${basedir}/target/classes/TEST_SCHEMA.json</importFile>
              </item>
          </extractionParameters>
          <baseDir>${project.basedir}</baseDir>
          <targetFolder>target/generated-sources</targetFolder>
          <targetPackage>com.mypkg</targetPackage>
      </configuration>
  </execution>
```
