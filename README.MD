* Introduction
This project shows how to use Kafka Connectors & Kafka Stream APi together to :
  - read data from input file with a file connector
  - stream this data
  - filter this data using a pattern
  - write back the stream to a  file using a sink connector
  
* The demo

** Starting components
You need to have :
  - zookeeper
  - kafka server
  - connect (standalone mode)
  - filter running
  
*** Starting zookper 
Use the standard way to start Zookeeper

   
*** Starting kafka
Kafka is started normally once Zookeeper is up...
``` bash
 ./bin/kafka-server-start.sh config/server.properties
   
```

*** Starting connect
Connect should be started with 2 properties used to configure input connector
and  sink connector. Please refer to sections below  for further details.

```bash
./bin/standalone.properties ~/work/customers/BNP/file-source-demo.properties
 ~/work/customers/BNP/file-sink-demo.properties
```

*** Starting filter 
Use the uberjar provided and the filter will run forever using:
```shell
java -jar <filter-uberjar.jar>
```   

** Using the demo
Once the demo started:
- fire up a terminal ,
- cd <demo_text_dir>, this is where input & output text files should live
- add contents to the test.txt file
- fire up another terminal in the same directory: tail -f test.sink.txt

You should see new contents from the test.txt appearing in the second terminal.


* Configuring Kafka connectors
** File connector
This connector watches for file  changes in the specified file and streams these
changes to the specified topic. 
```
name=local-file-source                                                                          
connector.class=FileStreamSource                                                                
tasks.max=1                                                                                     
file=/home/deadbrain/work/customers/BNP/test.txt                                                
topic=connect-demo-test  

```  
  
**  File sink
This connector outpts the given topic contents to a given file.
```
name=local-file-sink                                                                            
connector.class=FileStreamSink                                                                  
tasks.max=1                                                                                     
file=/home/deadbrain/work/customers/BNP/test.sink.txt                                           
topics=connect-demo-filtered                                                                    
key.converter=org.apache.kafka.connect.storage.StringConverter                                  
value.converter=org.apache.kafka.connect.storage.StringConverter  
``` 


* Technical details
This section gives some details about technical implementation of the demo.

** Filtering

Filtering done using Kafka Stream APi:
-  receiving input data from a first topic (fed by Kafka Connector )
- filtering (using a regex as a business ruke inside an engine) output to a second topic
- output the final topic consumed by the sink connector

** Serialization

Bewware of some serialization problems once the message reachess Kafka, please refer to
serializer key config property inside the properties file.


**  Business rules engine

Using a business rule engine is the perfect way to keep most of the demo structure 
and use new rules to drive the transformation. Just change the rules and setup and keeo the code
in place and you have other kind of filtering.

The only trickery here is how to map the context (key,value) pair as facts  required by
the business rule engine. Inn this case the only fact managed is the value of the payload transported
through Kafka topics.



* Troubleshooting

** Check ports 

Check that kafka & zookpeer ar e upo & running using :
  - netstat
  - ps
  
** Check log files

They may contain useful information.

** Check topics contents

Use the kafka console consumer to check a given topic.