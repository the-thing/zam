package com.github.thething.zam.reciter;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReciterRuleTest {

    @Test
    void shouldReturnTrueWhenRuleIsMatching() {
        assertThat(ReciterRule.parse("(G)+=J").isMatch("wage", 2)).isTrue();
        assertThat(ReciterRule.parse("L(L)=").isMatch("wall", 3)).isTrue();
        assertThat(ReciterRule.parse(" :(I)^%=AY5").isMatch("white", 2)).isTrue();
        assertThat(ReciterRule.parse(" :(ABLE)=EY4BUL").isMatch("able", 0)).isTrue();
        assertThat(ReciterRule.parse("(E)^%=IY4").isMatch("accelerate", 3)).isTrue();
        assertThat(ReciterRule.parse(" (IN)=IHN").isMatch("ing", 0)).isTrue();
        assertThat(ReciterRule.parse("@(U)=UW").isMatch("insulator", 3)).isTrue();
        assertThat(ReciterRule.parse("(?)=?").isMatch("foo?", 3)).isTrue();
        assertThat(ReciterRule.parse("#:^(L)%=UL").isMatch("trouble", 5)).isTrue();
        assertThat(ReciterRule.parse("#:^(L)%=UL").isMatch("trouble flat", 5)).isTrue();
    }

    @Test
    void shouldReturnFalseWhenRuleIsNotMatching() {
        assertThat(ReciterRule.parse("(A)^%=EY").isMatch("alphabet", 4)).isFalse();
        assertThat(ReciterRule.parse("(A)^+:#=AE").isMatch("alphabet", 4)).isFalse();
    }
}
