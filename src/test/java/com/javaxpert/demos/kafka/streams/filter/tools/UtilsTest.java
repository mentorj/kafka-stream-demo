package com.javaxpert.demos.kafka.streams.filter.tools;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import java.util.Map;

class UtilsTest {
    private static Rules rules;
    private static Rule r1, r2;
    private static RulesEngine engine;
    private static Facts facts;

    @BeforeAll
    public static void setupTests() {
        engine = new DefaultRulesEngine();
        facts = new Facts();
        facts.put("nothing", "nothing");
        rules = new Rules();
        r1 = new Rule() {
            @Override
            public String getName() {
                return "r1";
            }

            @Override
            public String getDescription() {
                return "stupid r1 is always ok";
            }

            @Override
            public int getPriority() {
                return 0;
            }

            @Override
            public boolean evaluate(Facts facts) {
                return true;
            }

            @Override
            public void execute(Facts facts) throws Exception {

            }

            @Override
            public int compareTo(Rule rule) {
                return getName().compareTo(rule.getName());
            }
        };
        r2 = new Rule() {
            @Override
            public String getName() {
                return "r2";
            }

            @Override
            public String getDescription() {
                return "always false";
            }

            @Override
            public int getPriority() {
                return 1;
            }

            @Override
            public boolean evaluate(Facts facts) {
                return false;
            }

            @Override
            public void execute(Facts facts) throws Exception {

            }

            @Override
            public int compareTo(Rule rule) {
                return getName().compareTo(rule.getName());
            }
        };
    }

    @org.junit.jupiter.api.Test
    void allRulesOk() {
        rules.register(r1);
        //rules.register(r2);
        Map<Rule, Boolean> checks = engine.check(rules, facts);
        Assertions.assertEquals(Utils.allRulesOk(checks), true);

        rules.unregister(r1);
        rules.register(r2);
        checks = engine.check(rules, facts);
        Assertions.assertEquals(Utils.allRulesOk(checks), false);


        rules.unregister(r2);
        rules.register(r1);
        rules.register(r2);
        checks = engine.check(rules, facts);
        Assertions.assertEquals(Utils.allRulesOk(checks), false);

    }
}