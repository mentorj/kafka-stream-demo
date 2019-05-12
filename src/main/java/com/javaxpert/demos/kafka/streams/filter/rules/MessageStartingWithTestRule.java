package com.javaxpert.demos.kafka.streams.filter.rules;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Rule(name = "message retained should start with test-", description = "This rule ensures that messages retained start with test-")
public class MessageStartingWithTestRule {
    private static Logger logger = LoggerFactory.getLogger("MessageStartingWithTestRule");

    @Condition
    public boolean startWithTest(@Fact("msg") String msg) {
        return msg.contains("test-");
    }

    @Action
    public void retainMsg() {
        logger.debug("Messqge retained with this rules");
    }
}
