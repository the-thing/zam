package com.github.thething.zam.common;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RequirementsTest {

    @Test
    void shouldReturnValueWhenInRange() {
        assertThat(Requirements.requireInRange(10, 0, 20)).isEqualTo(10);
        assertThat(Requirements.requireInRange(0, 0, 0)).isEqualTo(0);
        assertThat(Requirements.requireInRange(-10, -11, 10)).isEqualTo(-10);
    }

    @Test
    void shouldThrowExceptionWhenNotInRange() {
        assertThatThrownBy(() -> Requirements.requireInRange(0, 10, 20))
                .isInstanceOf(IllegalArgumentException.class)
                .hasNoCause()
                .hasMessage("Value must be in range [10,20]: 0");

        assertThatThrownBy(() -> Requirements.requireInRange(-1, 0, 100000))
                .isInstanceOf(IllegalArgumentException.class)
                .hasNoCause()
                .hasMessage("Value must be in range [0,100000]: -1");
    }

    @Test
    void shouldThrowExceptionWhenRangeIsInvalid() {
        assertThatThrownBy(() -> Requirements.requireInRange(10, 20, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasNoCause()
                .hasMessage("Invalid range [20,10]");
    }
}