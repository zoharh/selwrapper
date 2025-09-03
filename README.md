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
- **Selenium WebDriver 2.53.0**: For web automation
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
│   └── main/
│       ├── java/
│       │   ├── selwrapper.java      # Main DriveWrapper class
│       │   └── wLogger.java         # Logging wrapper class
│       └── resources/
│           └── log4j.properties     # Log4j configuration
├── pom.xml                          # Maven configuration
└── README.md                        # This file
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

**Note**: This project is designed to work with Selenium WebDriver 2.53.0 and Log4j 2.23.1. For newer versions of Selenium, some modifications may be required due to API changes.
