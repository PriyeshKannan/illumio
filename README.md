# Flow Record Analysis

The scope of the project is to analysis flow records and aggregate data for analysis based on port/tag.

Due to time limitation only `FlowLog` version `2` is implemented.

## Design

The program execution is a written from perspective of a like a single batch process which reads a file, processes the data and generates a the necessary report.


## Development

The following are tools and libraries used for development.

1. `gradle` - The build tool for the project. 
2. `junit5` and `junit5-params` - Unit testing third party libraries.
3. `slf4j` and `logback` - Logging libraries.


Source code repository `https://github.com/PriyeshKannan/illumio.git` 

```bash

git clone https://github.com/PriyeshKannan/illumio.git
cd illumio
tree -d -x
.
├── app
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── illumio
│       │   │           ├── aggregator
│       │   │           ├── data
│       │   │           ├── flowlog
│       │   │           │   ├── parser
│       │   │           │   └── record
│       │   │           └── utils
│       │   └── resources
│       └── test
│           ├── java
│           │   └── com
│           │       └── illumio
│           │           ├── aggregator
│           │           ├── flowlog
│           │           │   └── parser
│           │           └── utils
│           └── resources
├── config
├── gradle
│   └── wrapper
├── log
└── reports
```


* `app` -  The java files of the application
* `config` - configuration file - `tags_config.txt` which contains port , protocol tags mappings
* `log` - Directory containing a sample flow log with version 2 samples . Note version 3 and version 5 processing are not supported yet.
* `gradle` - gradle wrapper folder.
* `reports` - The final output of the exection of application ie file `tag.txt` and `protocol.txt`

```bash
reports
├── protocol.txt
└── tag.txt
```

1. Log Parsing - `com.illumio.flowlog.parser`
2. Data Aggregation and Persistence to report file - `com.illumio.aggregator`
3. The Application starter - `com.illumio.FlowLogAnalyzerApplication`


Data Aggregration is overloaded to keep scope limited due to time limiation.

By default the application reads a sample from `log/flow_record.log` and generates report to `report` folder.

The following environment variable can be override to specify custom locations

1. `tag.config.file` - The file which contain tag, port and protocol definition in csv format. Default value is `<projectroot>/config/tag_config.txt`
2. `flowlog.file` - The flow log record file that will be used to parse. Default `<projectroot>/reports/log/flow_record.log`
3. `tag.report.file` - The tag report file location. Default `<projectroot>/reports/tag.txt`
4. `protocol.report.file` - The protocol report file.  Default `<projectroot>/reports/protocol.txt`


To generate using the sample file provide, execute

```bash
make run
```

To note, the command downloads docker  image`gradle:8.9.0-jdk11` to execute in an insolated environment.

### Testing

#### Unit Testing

1. `com.illumio.flowlog.parser` -  Test case for parsing flow log 
2. `com.illumio.utils` - Test cases for util classes.
3. `com.illumio.aggregate` - Does basic test for data aggregation.

To execute test and codecoverage

```bash
make test codecoverage
```

Note: As of now the code coverage is around `57%` 


## License

This code is under `illimno` license and cannot be reproduce without explicit permission.

### License of Third party Libraries/Dependencies

All third party used are opensource and no commerical libraries are used to implement the product.

1. `slf4j` - `MIT License`.
2. `logback` -  `Eclipse Public License - v 1.0` and `GNU Lesser General Public License`.
3. `logback-stash` - `Apache License, Version 2.0` and `MIT License`.
4. `Junit` - `Eclipse Public License v2.0`.
5. `Junit-params` - `Eclipse Public License v2.0`.


## Reference

### FlowLog - Requirement Analysis

1. [Flow log records](https://docs.aws.amazon.com/vpc/latest/userguide/flow-log-records.html)
2. [IANA Protocol Numbers](https://www.iana.org/assignments/protocol-numbers/protocol-numbers.xml)

### Tools and Library

1. [Gradle]()
2. [Parametrized JUnit 5](https://www.baeldung.com/parameterized-tests-junit-5)
