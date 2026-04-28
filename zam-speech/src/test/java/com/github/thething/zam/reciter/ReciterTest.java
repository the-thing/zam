package com.github.thething.zam.reciter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class ReciterTest {

    private Reciter reciter;

    @BeforeEach
    void setUp() throws IOException {
        ReciterRuleRegistry reciterRuleRegistry = ReciterRuleRegistry.load("rules/reciter-rules.txt");
        reciter = new Reciter(reciterRuleRegistry);
    }

    @Test
    void shouldReciteSpecificWords() {
        assertThat(reciter.recite("-ing")).isEqualTo("IHNG");
        assertThat(reciter.recite("insulator")).isEqualTo("IHNSUWLAETER");
        assertThat(reciter.recite("  \tinsulator")).isEqualTo("IHNSUWLAETER");
        assertThat(reciter.recite("S.A.M.")).isEqualTo("EH4S.EH4Y. EH4M.");
        assertThat(reciter.recite("O.K.")).isEqualTo("OH4W.KEY4.");
        assertThat(reciter.recite(". what is your name?")).isEqualTo(". WHAHT IHZ YOHR NEYM?");
        assertThat(reciter.recite("What is your name ?")).isEqualTo("WHAHT IHZ YOHR NEYM ?");
        assertThat(reciter.recite("What is your name     ?")).isEqualTo("WHAHT IHZ YOHR NEYM ?");
        assertThat(reciter.recite("Lolo, popo. Koko")).isEqualTo("LAALOW, PAAPOW. KAAKOW");
        assertThat(reciter.recite("away eight")).isEqualTo("AXWEY5 EY4T");
        assertThat(reciter.recite("new . cow")).isEqualTo("NUW . KOW");
        assertThat(reciter.recite("careful")).isEqualTo("KEH4RFUHL");
        assertThat(reciter.recite("careful flat")).isEqualTo("KEH4RFUHL FLAET");
        assertThat(reciter.recite("trouble")).isEqualTo("TRAH5BUL");
        assertThat(reciter.recite("trouble flat")).isEqualTo("TRAH5BUL FLAET");
        assertThat(reciter.recite("get...over")).isEqualTo("GEH5T...OW5VER");
        assertThat(reciter.recite("  get...over  ")).isEqualTo("GEH5T...OW5VER");
    }

    @Test
    void shouldTrimWhitespaces() {
        assertThat(reciter.recite("  \ninsu \r \n lator  ")).isEqualTo("IHNSUW LAETER");
    }
}