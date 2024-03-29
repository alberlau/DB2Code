# DB2Code Project
![db2code](https://github.com/alberlau/DB2Code/blob/master/db2code.png)
## Overview

DB2Code is a tool designed to facilitate the generation of files from JDBC Metadata. This project streamlines the process of loading metadata into objects and then utilizing these objects within Mustache templates to generate the desired output. It's an ideal solution for developers looking to automate and simplify their database related code generation processes.
The default use case as in below diagram:

![db2code](https://github.com/alberlau/DB2Code/blob/master/BasicToolWorkflow.png)

### Prerequisites

Before you begin, ensure you have the following installed:
- Java Development Kit (JDK) 11, or above
- Maven 3

### Installation

You can use DB2Code in two ways at the moment.
1. As command line tool
2. As Maven plugin

#### Using as command line tool
Look into distr directory.
Follow README.md in it
Configuration params is same as for Maven plugin, scroll down to find more details

#### Using as Maven plugin
Modify your pom.xml as bellow and adjust necessary parameters:
```xml
    <build>
        <plugins>
            <plugin>
                <groupId>org.db2code</groupId>
                <artifactId>java-pojo-generator-mojo</artifactId>
                <version>1.3.6</version>
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
- __generatorStrategy__ how generator manages outputs, into single file or multiple per table files. Options: SINGLE_FILE or CLASS_PER_TABLE, default to CLASS_PER_TABLE.
- __singleResultName__ the name of the output generated file if generatorStrategy is SINGLE_FILE. Otherwise ignored.
- __targetFolder__ where to put sources under baseDir, can be target/generated-sources
- __targetPackage__ what package should be used for generated classes
- __ext__ extension for generated files, defaults to .java
- __dateImpl__ what java date implementation should be used: UTIL_DATE or LOCAL_DATE
- __typeMapFile__ type mapping file to use. Default is /type-mappings/java-type-map.properties. There is available dbml-type-map.properties file on same location, or you can define your own.
- __includeGenerationInfo__ should info about generation be included? Defaults to false
- __doNotGenerateTables__ list of tables to be not generated. Can be regexp.
 

You can customize generation template, by providing __templates__ list:
```xml
  <templates>
      <template>some-template.mustache</template>
  </templates>
```

Currently built-in templates are: __pojo.mustache__, __spring-data.mustache__, __dbml.mustache__ .
You can provide your own template:

```xml
  <templates>
      <template>${project.baseUri}/my-custom.mustache</template>
  </templates>
```

Do not generate example, tables ending 3, will not be generated:
```xml
  <doNotGenerateTables>
      <doNotGenerateTable>.+3</doNotGenerateTable>
  </doNotGenerateTables>
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

## Reference of attributes available in model
Bellow is reference of attributes which can be used in mustache templates.
Most of them is converted directly from JDBC DatabaseMetadata available result sets.
For example TABLE_SCHEM in metadata result set is available as tableSchem attribute to be used in mustache template.

### Model reference, common properties
  - Boolean isLast common property for objects, which can be contained in list, to indicate if it is last 
  - String tableCat - common for raw jdbc objects
  - String tableSchem - common for raw jdbc objects
  - String tableName - common for raw jdbc objects
#### For CLASS_PER_TABLE strategy
- package
- className
- properties
  - rawColumn - Data grabbed from ResultSetMetadata.getColumns Consult JDBC javadoc: https://docs.oracle.com/javase/8/docs/api/java/sql/DatabaseMetaData.html#getColumns-java.lang.String-java.lang.String-java.lang.String-java.lang.String-
        - tableCat, tableSchem, tableName
        - String columnName
        - Integer dataType
        - String typeName
        - Integer columnSize
        - Integer decimalDigits
        - Integer numPrecRadix
        - Integer nullable
        - String remarks
        - String columnDef
        - Integer sqlDataType
        - Integer sqlDatetimeSub
        - Integer charOctetLength
        - Integer ordinalPosition
        - String isNullable
  - propertyType as resolved using typeMapFile configuration
  - propertyName
  - methodName
  - isId
  - isNullable
  - size
- generationInfo
- rawTable
  - tableCat, tableSchem, tableName, isLast
  - String tableType
  - String remarks
  - String typeCat
  - String typeSchem
  - String typeName
  - String selfReferencingColName
  - String refGeneration
  - Collection<RawColumn> columns
    - see properties.rawColumn above
  - Collection<RawPrimaryKey> primaryKey
    - tableCat, tableSchem, tableName, isLast
      - columnName
      - keySeq
      - pkName
  - Collection<RawForeignKey> foreignKeys
    - String pktableCat
    - String pktableSchem
    - String pktableName
    - String pkcolumnName
    - String fktableCat
    - String fktableSchem
    - String fktableName
    - String fkcolumnName
    - int keySeq
    - Integer updateRule
    - Integer deleteRule
    - String fkName
    - String pkName
    - Integer deferrability
    - Boolean isLast
- rawProcedure in case of procedure
  - procedureCat
  - procedureSchem
  - procedureName
  - remarks
  - procedureType
  - specificName
  - isLast
  - parameters
    - rawParameter
      - procedureCat
      - procedureSchem
      - procedureName
      - isLast
      - columnName
      - columnType
      - dataType
      - typeName
      - precision
      - length
      - scale
      - radix
      - nullable
      - remarks
      - columnDef
      - charOctetLength
      - ordinalPosition
      - isNullable
      - specificName
      - sqlDataType
      - sqlDatetimeSub
    - position
    - name , adapted, camelcase
    - type , as resolved by type mapper
    - isInput as per jdbc COLUMN_TYPE 1 OR 2
    - isOutput as per jdbc COLUMN_TYPE 2 OR 3
    - isInputOutput  as per jdbc COLUMN_TYPE 3
    - isReturn as per jdbc COLUMN_TYPE 4
    - isResult as per jdbc COLUMN_TYPE 5
    - isLast
- parameters in case of procedure
- singleParameterReturn , parameter  in case of procedure
- inputParameters  in case of procedure
- 
#### For SINGLE_FILE strategy
- targetPackage
- classes - see above all CLASS_PER_TABLE object is exposed
- rawDatabaseMetadata
  - tables , list, see rawTable
  - procedures , list of procedures extracted, see above what is marked: "in case of procedure" 
  - databaseProductName
  - databaseProductVersion
- params  - config params passed, see ExecutorParams class for all
