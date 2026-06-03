# Web Crawler

This project implements a simple web crawler in Java.

The crawler supports:
- recursive crawling with configurable depth
- domain filterig
- detection of broken links
- structured output as markdown file
- automated tests via JUnit
- concurrent crawling using a fixed thread pool
- each webpage is processed as a separate crawl task
- thread-safe tracking of already visited URLs
- clean error handling with errors included in the Markdown report
- clean boundary to jsoup through the PageVisitor interface

The crawler uses a fixed-size thread pool internally for concurrent crawling.

Used Technology:
- Java 25
- Maven
- jsoup
- JUnit 5

## Error Handling

Errors during crawling are caught and stored in the crawler result structure.
Broken pages are marked in the generated Markdown report and include error details such as the exception type and message.
The application is designed to continue crawling even if individual pages cannot be processed.

## Build

To build the project run: 
```bash
mvn clean install
```
This will compile the project, execute the unit tests and build a .jar file.

## Executing the crawler

The crawler is executed from the command line.

```bash
java -jar target/web-crawler-2.0.0-SNAPSHOT.jar <URL> <depth> <domains>
```

### Parameters
| Param | Description |
|----------|----------|
| URL  | starting homepage   | 
| depth  | recursion depth for crawling   | 
| domains  | allowed domains (comma separated)   | 

### Example
```bash
java -jar target/web-crawler-2.0.0-SNAPSHOT.jar https://ilogs.systems 1 ilogs.systems
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

GitHub Copilot was used as a development aid for small inline code-completion suggestions only.
It was not used to generate complete source code blocks or complete solution parts automatically.
All suggestions were reviewed, adapted, and integrated manually by the authors.

Copilot was also used to assist in drafting parts of the README documentation.