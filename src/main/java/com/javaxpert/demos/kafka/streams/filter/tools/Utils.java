package com.javaxpert.demos.kafka.streams.filter.tools;

import org.jeasy.rules.api.Rule;

import java.util.Map;

public class Utils {
    public static boolean allRulesOk(Map<Rule, Boolean> inputMap) {
        return !inputMap.containsValue(Boolean.FALSE);
    }
}
