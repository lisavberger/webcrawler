# Web Crawler

This project implements a simple web crawler in Java.

The crawler supports:
- recursive crawling with configurable depth
- domain filterig
- detection of broken links
- structured output as markdown file
- automated tests via JUnit

Used Technology:
- Java 25
- Maven
- jsoup
- JUnit 5

## Build

To build the project run: 
```bash
mvn clean install
```
This will compile the project, execute the unit tests and build a .jar file.

## Executing the crawler

The crawler is executed from the command line.

```bash
java -jar target/web-crawler-1.0.0-SNAPSHOT.jar <URL> <depth> <domains>
```

### Parameters
| Param | Description |
|----------|----------|
| URL  | starting homepage   | 
| depth  | recursion depth for crawling   | 
| domains  | allowed domains (comma separated)   | 

### Example
```bash
java -jar target/web-crawler-1.0.0-SNAPSHOT.jar https://ilogs.systems 1 ilogs.systems
```

## Output 

The crawler generates a Markdown report with visited page, headers and linked pages

## Running tests

The project contains automated testing via JUnit tests

Execute following command to run all tests:

```bash
mvn test
```

## AI Assistance Disclosure

GitHub Copilot was used as a development aid for code completion during the implementation of this project.  
It was also used to assist in generating portions of the README documentation.  
All suggestions were reviewed, validated, and modified where necessary by the authors.