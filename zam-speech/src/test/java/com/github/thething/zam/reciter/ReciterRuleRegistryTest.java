package com.github.thething.zam.reciter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class ReciterRuleRegistryTest {

    private ReciterRuleRegistry underTest;

    @BeforeEach
    void setUp() throws IOException {
        underTest = ReciterRuleRegistry.load("rules/reciter-rules.txt");
    }

    @Test
    void shouldReturnMatchingRule() {
        ReciterRule rule = underTest.findMatchingRule("ing", 0);

        assertThat(rule).isNotNull();
        assertThat(rule.getExpression()).isEqualTo(" (IN)=IHN");
        assertThat(rule.getPattern()).isEqualTo("IN");
        assertThat(rule.getPrefix()).isInstanceOf(WordBreakMatcher.class);
        assertThat(rule.getSuffix()).isInstanceOf(EmptyMatcher.class);
        assertThat(rule.getPhonemes()).isEqualTo("IHN");
    }
}