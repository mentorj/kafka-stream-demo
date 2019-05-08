package com.javaxpert.demos.kafka.streams.filter;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class FilterTopicDemo {

    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger("FilterTopicDemo");

        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "filter-file-app");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();

        // defines a stream based on connect-test Connector (source)
        KStream<String, String> textLines = builder.stream("connect-test" +
                "" +
                "");
        // remove non test- based patterns
        textLines.filter(new Predicate<String, String>() {
            @Override
            public boolean test(String s, String s2) {
                logger.info("testing string :" + s2);
                boolean takeLine = s2.contains("test-");
                logger.info("line taken ? = " + takeLine);
                return takeLine;
            }
        });//((key,value) -> {logger.info("filtering value =" + value );return value.contains("test-");} );
        // write to another topic : connect-filtered
        textLines.to("connect-filtered");

        // start the app & handle shutdown
        KafkaStreams streams = new KafkaStreams(builder.build(), config);
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}
