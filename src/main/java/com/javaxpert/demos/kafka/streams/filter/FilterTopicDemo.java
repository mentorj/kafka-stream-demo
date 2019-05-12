package com.javaxpert.demos.kafka.streams.filter;

import com.javaxpert.demos.kafka.streams.filter.rules.MessageStartingWithTestRule;
import com.javaxpert.demos.kafka.streams.filter.tools.Utils;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class FilterTopicDemo {

    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger("FilterTopicDemo");

        // setup rules engine
        Rules rules = new Rules();
        rules.register(new MessageStartingWithTestRule());
        Facts facts = new Facts();


        RulesEngine rulesEngine = new DefaultRulesEngine();
        logger.debug("Rulesengine is ready to be fired and rules reguistered");

        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "filter-file-app");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.ByteArray().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.ByteArray().getClass());

        StreamsBuilder builder = new StreamsBuilder();

        // Set up serializers and deserializers, which we will use for overriding the default serdes
        // specified above.
        final Serde<String> stringSerde = Serdes.String();
        final Serde<byte[]> byteArraySerde = Serdes.ByteArray();

        // defines a stream based on connect-test Connector (source)
        KStream<String, String> textLines = builder.stream("connect-demo-test", Consumed.with(stringSerde, stringSerde));
        // write to another topic : connect-demo-filtered
        textLines.to("connect-demo-pre-" +
                "filtered", Produced.with(stringSerde, stringSerde));

        KStream<String, String> filteredLines = builder.stream("connect-demo-pre-filtered", Consumed.with(stringSerde, stringSerde));
        filteredLines
                .filter((key, value) -> {
                    facts.put("msg", value);
                    return Utils.allRulesOk(rulesEngine.check(rules, facts));
                })
                .to("connect-demo-filtered", Produced.with(stringSerde, stringSerde));
        // start the app & handle shutdown
        KafkaStreams streams = new KafkaStreams(builder.build(), config);
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}
