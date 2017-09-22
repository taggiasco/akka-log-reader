[![Build Status](https://travis-ci.org/taggiasco/akka-log-reader.svg?branch=master)](https://travis-ci.org/taggiasco/akka-log-reader)

# akka-log-reader

The goal of this project is to provide a web interface in order to extract some simple statistics about logs.

The http server is running with Akka HTTP and Akka Streams is used to parse the log files.



## Usage

$ sbt run

Example : http://localhost:8080/stat?source=timing-log.log&reducer=http&statistic=sum

which will return something like : 

```
OPTIONS : 6
GET : 698387
HEAD : 348
POST : 66898
```



## Technologies

* Scala
* Akka HTTP
* Akka Streams
