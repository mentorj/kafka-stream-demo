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
   
*** Starting kafka
``` bash
 ./bin/kafka-server-start.sh config/server.properties
   
```

*** Starting connect
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
- cd <demo_text_dir>
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

