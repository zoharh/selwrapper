# SelWrapper

A Java wrapper class around Selenium WebDriver that simplifies web automation code and provides standardized logging capabilities.

## Overview

SelWrapper is a utility library that provides a simplified interface for Selenium WebDriver operations. It includes:

- **DriveWrapper**: A comprehensive wrapper around Selenium WebDriver with built-in logging
- **wLogger**: A standardized logging wrapper around Log4j for consistent logging across the project
- Pre-configured WebDriver setup with Firefox driver
- Built-in error handling and timeout management
- Simplified methods for common web automation tasks

## Features

- **Automated WebDriver Management**: Handles driver initialization, configuration, and cleanup
- **Integrated Logging**: Built-in Log4j integration with standardized logging methods
- **Error Handling**: Comprehensive exception handling for WebDriver operations
- **Flexible Configuration**: Support for custom site URLs and driver configurations
- **Maven Integration**: Easy dependency management and build process

## Prerequisites

- Java 7 or higher
- Maven 3.0 or higher
- Firefox browser (for default WebDriver)

## Installation

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd selwrapper
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Add to your project dependencies:
   ```xml
   <dependency>
       <groupId>selwrapper</groupId>
       <artifactId>selwrapper</artifactId>
       <version>1.0.3-SNAPSHOT</version>
   </dependency>
   ```

## Dependencies

The project uses the following main dependencies:
- **Selenium WebDriver 4.18.1**: For web automation (latest stable version)
- **Log4j 2.23.1**: For logging functionality (latest stable version)
- **JUnit 4.12**: For testing (test scope)

## Usage

### Basic Usage

```java
import selwrapper.DriveWrapper;

// Initialize the wrapper
DriveWrapper driver = new DriveWrapper();

// Navigate to a URL
driver.gotoURL("https://example.com");

// Perform web automation tasks
// ... your automation code ...

// Close the driver
driver.close();
```

### Custom Site Configuration

```java
// Initialize with custom site URL
DriveWrapper driver = new DriveWrapper("https://m.example.com");

// The driver will automatically navigate to the specified site
```

### Logging

The wrapper includes built-in logging capabilities:

```java
// Log levels available: DEBUG, INFO, WARN, ERROR, FATAL
driver.log.logIssue(wLogger.INFO, "MyFunction", "Operation completed successfully");
driver.log.logIssue(wLogger.DEBUG, "MyFunction", "Debug information");
driver.log.logIssue(wLogger.ERROR, "MyFunction", "An error occurred");
```

## Project Structure

```
selwrapper/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── selwrapper.java      # Main DriveWrapper class
│   │   │   └── wLogger.java         # Logging wrapper class
│   │   └── resources/
│   │       └── log4j2.xml           # Log4j2 configuration
│   └── test/
│       ├── java/
│       │   ├── wLoggerTest.java     # Unit tests for wLogger class
│       │   ├── DriveWrapperTest.java # Unit tests for DriveWrapper class
│       │   └── SelWrapperTestSuite.java # Test suite runner
│       └── resources/
│           └── log4j2-test.xml      # Test-specific Log4j2 configuration
├── pom.xml                          # Maven configuration
├── README.md                        # This file
└── target/                          # Build output (generated)
    ├── classes/                     # Compiled Java classes
    ├── test-classes/                # Compiled test classes
    └── selwrapper-1.0.3-SNAPSHOT.jar # Generated JAR file
```

## Configuration

### Log4j Configuration

The project includes a `log4j2.xml` file in `src/main/resources/` for logging configuration. You can customize this file to adjust logging levels and output formats. The configuration supports both console and file logging.

### WebDriver Configuration

Default WebDriver settings:
- **Browser**: Firefox
- **Implicit Wait**: 30 seconds
- **Window**: Maximized
- **Timeout**: 30 seconds

## Building and Testing

### Build
```bash
mvn clean compile
```

### Test
```bash
mvn test
```

### Package
```bash
mvn package
```

### Full Build (Compile, Test, Package, Install)
```bash
mvn clean install
```

## Unit Tests

The project includes comprehensive unit tests to ensure code quality and reliability:

### Test Structure
- **wLoggerTest.java**: Tests the logging wrapper functionality
- **DriveWrapperTest.java**: Tests basic DriveWrapper functionality
- **SelWrapperTestSuite.java**: Test suite that runs all tests together

### Test Coverage
The unit tests cover:
- ✅ **wLogger Class**: All logging levels, constructor, error handling
- ✅ **DriveWrapper Class**: Basic functionality and constants
- ✅ **Edge Cases**: Null values, empty strings, invalid inputs
- ✅ **Error Handling**: Exception scenarios and validation

### Running Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=wLoggerTest

# Run with verbose output
mvn test -Dtest=DriveWrapperTest -Dsurefire.useFile=false
```

### Test Dependencies
- **JUnit 4.13.2**: Core testing framework
- **Mockito 5.10.0**: Mocking framework for complex dependencies
- **Mockito Inline 5.2.0**: Support for mocking static methods and constructors

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the terms specified in the LICENSE file.

## Author

**Zohar Hirshfeld** - *Initial work* - August 30, 2013

## Version History

- **1.0.3-SNAPSHOT**: Current development version
- **1.0.0**: Initial release

## Support

For issues and questions, please check the project repository or create an issue.

---

**Note**: This project is designed to work with Selenium WebDriver 4.18.1 and Log4j 2.23.1. The code has been updated to use modern Selenium 4.x APIs.
