# Ikora Core: Robot Framework engine

![build](https://github.com/UL-SnT-Serval/ikora-core/workflows/build/badge.svg)
![codecov](.github/badges/jacoco.svg)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/lu.uni.serval/ikora-core/badge.svg)](https://search.maven.org/search?q=g:lu.uni.serval%20ikora-core)

## Description

Ikora Core is a parse for a Robot Framework that builds an AST and a call graph of the language to allow static analysis. In future versions, Ikora Core will implement a Robot Framework Runner.

## Build requirements

* Java Development Kit (JDK) 8 or higher
* Maven 3.6.0 or higher

## Build from source

1. Clone the project on your machine using ```git clone https://github.com/UL-SnT-Serval/ikora-core.git``` .
2. Move to the directory.
3. run the Maven command ```mvn clean install```.

License
-------
Code is under the [Apache Licence v2](https://www.apache.org/licenses/LICENSE-2.0.txt).