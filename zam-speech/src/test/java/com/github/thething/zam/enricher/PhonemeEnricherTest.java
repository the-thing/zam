package com.github.thething.zam.enricher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PhonemeEnricherTest {

    private PhonemeEnricher underTest;

    @BeforeEach
    void setUp() {
        underTest = new PhonemeEnricher();
    }

    @Test
    void shouldReturnRewrittenTokens() {
        PhonemeToken[] tokens;

        tokens = underTest.enrich("", PhonemeEnricher.Stage.REWRITE);
        assertThat(tokens.length).isEqualTo(2);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0);

        tokens = underTest.enrich(" ", PhonemeEnricher.Stage.REWRITE);
        assertThat(tokens.length).isEqualTo(3);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", " *", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0);

        // cow
        tokens = underTest.enrich("KOW", PhonemeEnricher.Stage.REWRITE);
        assertThat(tokens.length).isEqualTo(5);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "KX", "OW", "WX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 75, 52, 20, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0);

        // oil
        tokens = underTest.enrich("OY5L", PhonemeEnricher.Stage.REWRITE);
        assertThat(tokens.length).isEqualTo(5);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "OY", "YX", "LX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 50, 21, 19, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 5, 5, 0, 0);

        // all
        tokens = underTest.enrich("AOL", PhonemeEnricher.Stage.REWRITE);
        assertThat(tokens.length).isEqualTo(4);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "AO", "LX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 11, 19, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0);

        // meddle
        tokens = underTest.enrich("MEHDDUL", PhonemeEnricher.Stage.REWRITE);
        assertThat(tokens.length).isEqualTo(8);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "M*", "EH", "D*", "D*", "AX", "LX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 27, 7, 57, 57, 13, 19, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0, 0, 0);

        // astronomy
        tokens = underTest.enrich("AESTRUNAHMIY", PhonemeEnricher.Stage.REWRITE);
        assertThat(tokens.length).isEqualTo(11);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "AE", "S*", "J*", "R*", "AX", "N*", "AH", "M*", "IY", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 8, 32, 44, 23, 13, 28, 10, 27, 5, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

        // function
        tokens = underTest.enrich("FAH5NKSHUN", PhonemeEnricher.Stage.REWRITE);
        assertThat(tokens.length).isEqualTo(9);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "F*", "AH", "N*", "KX", "SH", "AX", "N*", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 34, 10, 28, 75, 33, 13, 28, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 5, 0, 0, 0, 0, 0, 0);

        // await eight
        tokens = underTest.enrich("AXWEY5 EY4T", PhonemeEnricher.Stage.REWRITE);
        assertThat(tokens.length).isEqualTo(11);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "AX", "W*", "EY", "YX", " *", "Q*", "EY", "YX", "T*", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 13, 25, 48, 21, 0, 31, 48, 21, 69, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 5, 5, 0, 0, 4, 4, 0, 0);

        // track
        tokens = underTest.enrich("TRAEK", PhonemeEnricher.Stage.REWRITE);
        assertThat(tokens.length).isEqualTo(6);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "CH", "R*", "AE", "KX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 42, 23, 8, 75, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0);

        // dry
        tokens = underTest.enrich("DRAY", PhonemeEnricher.Stage.REWRITE);
        assertThat(tokens.length).isEqualTo(6);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "J*", "R*", "AY", "YX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 44, 23, 49, 21, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0);

        // odd
        tokens = underTest.enrich("AADD", PhonemeEnricher.Stage.REWRITE);
        assertThat(tokens.length).isEqualTo(5);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "AA", "D*", "D*", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 9, 57, 57, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0);

        // art
        tokens = underTest.enrich("AA5RT", PhonemeEnricher.Stage.REWRITE);
        assertThat(tokens.length).isEqualTo(5);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "AA", "RX", "T*", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 9, 18, 69, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 5, 0, 0, 0);

        // all
        tokens = underTest.enrich("AOL", PhonemeEnricher.Stage.REWRITE);
        assertThat(tokens.length).isEqualTo(4);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "AO", "LX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 11, 19, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0);

        // go
        tokens = underTest.enrich("GOW", PhonemeEnricher.Stage.REWRITE);
        assertThat(tokens.length).isEqualTo(5);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "GX", "OW", "WX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 63, 52, 20, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0);

        // spy
        tokens = underTest.enrich("SPAY", PhonemeEnricher.Stage.REWRITE);
        assertThat(tokens.length).isEqualTo(6);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "S*", "B*", "AY", "YX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 32, 54, 49, 21, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0);

        // sty
        tokens = underTest.enrich("STAY", PhonemeEnricher.Stage.REWRITE);
        assertThat(tokens.length).isEqualTo(6);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "S*", "D*", "AY", "YX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 32, 57, 49, 21, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0);

        // sky
        tokens = underTest.enrich("SKAY", PhonemeEnricher.Stage.REWRITE);
        assertThat(tokens.length).isEqualTo(6);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "S*", "G*", "AY", "YX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 32, 60, 49, 21, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0);

        // scowl
        tokens = underTest.enrich("SKOWL", PhonemeEnricher.Stage.REWRITE);
        assertThat(tokens.length).isEqualTo(7);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "S*", "GX", "OW", "WX", "LX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 32, 63, 52, 20, 19, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0, 0);

        // new
        tokens = underTest.enrich("NUW", PhonemeEnricher.Stage.REWRITE);
        assertThat(tokens.length).isEqualTo(5);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "N*", "UX", "WX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 28, 16, 20, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0);

        // zoo
        tokens = underTest.enrich("ZUW5", PhonemeEnricher.Stage.REWRITE);
        assertThat(tokens.length).isEqualTo(5);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "Z*", "UX", "WX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 38, 16, 20, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 5, 5, 0);

        // chew
        tokens = underTest.enrich("CHYUW", PhonemeEnricher.Stage.REWRITE);
        assertThat(tokens.length).isEqualTo(7);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "CH", "**", "Y*", "UW", "WX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 42, 43, 26, 53, 20, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0, 0);

        // jay
        tokens = underTest.enrich("JEY5", PhonemeEnricher.Stage.REWRITE);
        assertThat(tokens.length).isEqualTo(6);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "J*", "**", "EY", "YX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 44, 45, 48, 21, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 5, 5, 0);

        // party
        tokens = underTest.enrich("PAA5RTIY", PhonemeEnricher.Stage.REWRITE);
        assertThat(tokens.length).isEqualTo(7);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "P*", "AA", "RX", "DX", "IY", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 66, 9, 18, 30, 5, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 5, 0, 0, 0, 0);

        // tardy
        tokens = underTest.enrich("TAA5RDIY", PhonemeEnricher.Stage.REWRITE);
        assertThat(tokens.length).isEqualTo(7);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "T*", "AA", "RX", "DX", "IY", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 69, 9, 18, 30, 5, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 5, 0, 0, 0, 0);
    }

    @Test
    public void shouldReturnStressedTokens() {
        PhonemeToken[] tokens;

        tokens = underTest.enrich("", PhonemeEnricher.Stage.ADD_STRESS);
        assertThat(tokens.length).isEqualTo(2);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0);

        tokens = underTest.enrich(" ", PhonemeEnricher.Stage.ADD_STRESS);
        assertThat(tokens.length).isEqualTo(3);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", " *", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0);

        // cow
        tokens = underTest.enrich("KOW", PhonemeEnricher.Stage.ADD_STRESS);
        assertThat(tokens.length).isEqualTo(5);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "KX", "OW", "WX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 75, 52, 20, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0);

        // oil
        tokens = underTest.enrich("OY5L", PhonemeEnricher.Stage.ADD_STRESS);
        assertThat(tokens.length).isEqualTo(5);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "OY", "YX", "LX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 50, 21, 19, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 5, 5, 0, 0);

        // all
        tokens = underTest.enrich("AOL", PhonemeEnricher.Stage.ADD_STRESS);
        assertThat(tokens.length).isEqualTo(4);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "AO", "LX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 11, 19, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0);

        // meddle
        tokens = underTest.enrich("MEHDDUL", PhonemeEnricher.Stage.ADD_STRESS);
        assertThat(tokens.length).isEqualTo(8);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "M*", "EH", "D*", "D*", "AX", "LX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 27, 7, 57, 57, 13, 19, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0, 0, 0);

        // astronomy
        tokens = underTest.enrich("AESTRUNAHMIY", PhonemeEnricher.Stage.ADD_STRESS);
        assertThat(tokens.length).isEqualTo(11);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "AE", "S*", "J*", "R*", "AX", "N*", "AH", "M*", "IY", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 8, 32, 44, 23, 13, 28, 10, 27, 5, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

        // function
        tokens = underTest.enrich("FAH5NKSHUN", PhonemeEnricher.Stage.ADD_STRESS);
        assertThat(tokens.length).isEqualTo(9);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "F*", "AH", "N*", "KX", "SH", "AX", "N*", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 34, 10, 28, 75, 33, 13, 28, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 6, 5, 0, 0, 0, 0, 0, 0);

        // away eight
        tokens = underTest.enrich("AXWEY5 EY4T", PhonemeEnricher.Stage.ADD_STRESS);
        assertThat(tokens.length).isEqualTo(11);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "AX", "W*", "EY", "YX", " *", "Q*", "EY", "YX", "T*", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 13, 25, 48, 21, 0, 31, 48, 21, 69, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 6, 5, 5, 0, 5, 4, 4, 0, 0);

        // track
        tokens = underTest.enrich("TRAEK", PhonemeEnricher.Stage.ADD_STRESS);
        assertThat(tokens.length).isEqualTo(6);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "CH", "R*", "AE", "KX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 42, 23, 8, 75, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0);

        // dry
        tokens = underTest.enrich("DRAY", PhonemeEnricher.Stage.ADD_STRESS);
        assertThat(tokens.length).isEqualTo(6);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "J*", "R*", "AY", "YX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 44, 23, 49, 21, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0);

        // odd
        tokens = underTest.enrich("AADD", PhonemeEnricher.Stage.ADD_STRESS);
        assertThat(tokens.length).isEqualTo(5);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "AA", "D*", "D*", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 9, 57, 57, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0);

        // art
        tokens = underTest.enrich("AA5RT", PhonemeEnricher.Stage.ADD_STRESS);
        assertThat(tokens.length).isEqualTo(5);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "AA", "RX", "T*", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 9, 18, 69, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 5, 0, 0, 0);

        // all
        tokens = underTest.enrich("AOL", PhonemeEnricher.Stage.ADD_STRESS);
        assertThat(tokens.length).isEqualTo(4);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "AO", "LX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 11, 19, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0);

        // go
        tokens = underTest.enrich("GOW", PhonemeEnricher.Stage.ADD_STRESS);
        assertThat(tokens.length).isEqualTo(5);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "GX", "OW", "WX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 63, 52, 20, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0);

        // spy
        tokens = underTest.enrich("SPAY", PhonemeEnricher.Stage.ADD_STRESS);
        assertThat(tokens.length).isEqualTo(6);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "S*", "B*", "AY", "YX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 32, 54, 49, 21, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0);

        // sty
        tokens = underTest.enrich("STAY", PhonemeEnricher.Stage.ADD_STRESS);
        assertThat(tokens.length).isEqualTo(6);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "S*", "D*", "AY", "YX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 32, 57, 49, 21, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0);

        // sky
        tokens = underTest.enrich("SKAY", PhonemeEnricher.Stage.ADD_STRESS);
        assertThat(tokens.length).isEqualTo(6);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "S*", "G*", "AY", "YX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 32, 60, 49, 21, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0);

        // scowl
        tokens = underTest.enrich("SKOWL", PhonemeEnricher.Stage.ADD_STRESS);
        assertThat(tokens.length).isEqualTo(7);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "S*", "GX", "OW", "WX", "LX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 32, 63, 52, 20, 19, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0, 0);

        // new
        tokens = underTest.enrich("NUW", PhonemeEnricher.Stage.ADD_STRESS);
        assertThat(tokens.length).isEqualTo(5);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "N*", "UX", "WX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 28, 16, 20, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0);

        // zoo
        tokens = underTest.enrich("ZUW5", PhonemeEnricher.Stage.ADD_STRESS);
        assertThat(tokens.length).isEqualTo(5);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "Z*", "UX", "WX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 38, 16, 20, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 6, 5, 5, 0);

        // chew
        tokens = underTest.enrich("CHYUW", PhonemeEnricher.Stage.ADD_STRESS);
        assertThat(tokens.length).isEqualTo(7);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "CH", "**", "Y*", "UW", "WX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 42, 43, 26, 53, 20, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0, 0);

        // jay
        tokens = underTest.enrich("JEY5", PhonemeEnricher.Stage.ADD_STRESS);
        assertThat(tokens.length).isEqualTo(6);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "J*", "**", "EY", "YX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 44, 45, 48, 21, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 6, 5, 5, 0);

        // party
        tokens = underTest.enrich("PAA5RTIY", PhonemeEnricher.Stage.ADD_STRESS);
        assertThat(tokens.length).isEqualTo(7);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "P*", "AA", "RX", "DX", "IY", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 66, 9, 18, 30, 5, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 6, 5, 0, 0, 0, 0);

        // tardy
        tokens = underTest.enrich("TAA5RDIY", PhonemeEnricher.Stage.ADD_STRESS);
        assertThat(tokens.length).isEqualTo(7);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "T*", "AA", "RX", "DX", "IY", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 69, 9, 18, 30, 5, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0, 0, 0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 6, 5, 0, 0, 0, 0);
    }

    @Test
    public void shouldReturnTokensWithLength() {
        PhonemeToken[] tokens;

        tokens = underTest.enrich("", PhonemeEnricher.Stage.SET_LENGTH);
        assertThat(tokens.length).isEqualTo(2);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0);

        tokens = underTest.enrich(" ", PhonemeEnricher.Stage.SET_LENGTH);
        assertThat(tokens.length).isEqualTo(3);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", " *", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0);

        // cow
        tokens = underTest.enrich("KOW", PhonemeEnricher.Stage.SET_LENGTH);
        assertThat(tokens.length).isEqualTo(5);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "KX", "OW", "WX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 75, 52, 20, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 6, 14, 8, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0);

        // oil
        tokens = underTest.enrich("OY5L", PhonemeEnricher.Stage.SET_LENGTH);
        assertThat(tokens.length).isEqualTo(5);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "OY", "YX", "LX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 50, 21, 19, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 15, 8, 9, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 5, 5, 0, 0);

        // all
        tokens = underTest.enrich("AOL", PhonemeEnricher.Stage.SET_LENGTH);
        assertThat(tokens.length).isEqualTo(4);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "AO", "LX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 11, 19, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 12, 9, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0);

        // meddle
        tokens = underTest.enrich("MEHDDUL", PhonemeEnricher.Stage.SET_LENGTH);
        assertThat(tokens.length).isEqualTo(8);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "M*", "EH", "D*", "D*", "AX", "LX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 27, 7, 57, 57, 13, 19, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 7, 8, 5, 5, 5, 9, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0, 0, 0);

        // astronomy
        tokens = underTest.enrich("AESTRUNAHMIY", PhonemeEnricher.Stage.SET_LENGTH);
        assertThat(tokens.length).isEqualTo(11);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "AE", "S*", "J*", "R*", "AX", "N*", "AH", "M*", "IY", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 8, 32, 44, 23, 13, 28, 10, 27, 5, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 8, 2, 8, 7, 5, 7, 6, 7, 8, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

        // function
        tokens = underTest.enrich("FAH5NKSHUN", PhonemeEnricher.Stage.SET_LENGTH);
        assertThat(tokens.length).isEqualTo(9);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "F*", "AH", "N*", "KX", "SH", "AX", "N*", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 34, 10, 28, 75, 33, 13, 28, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 2, 11, 7, 6, 2, 5, 7, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 6, 5, 0, 0, 0, 0, 0, 0);

        // away eight
        tokens = underTest.enrich("AXWEY5 EY4T", PhonemeEnricher.Stage.SET_LENGTH);
        assertThat(tokens.length).isEqualTo(11);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "AX", "W*", "EY", "YX", " *", "Q*", "EY", "YX", "T*", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 13, 25, 48, 21, 0, 31, 48, 21, 69, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 5, 8, 14, 8, 0, 5, 14, 8, 4, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 6, 5, 5, 0, 5, 4, 4, 0, 0);

        // track
        tokens = underTest.enrich("TRAEK", PhonemeEnricher.Stage.SET_LENGTH);
        assertThat(tokens.length).isEqualTo(6);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "CH", "R*", "AE", "KX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 42, 23, 8, 75, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 6, 7, 8, 6, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0);

        // dry
        tokens = underTest.enrich("DRAY", PhonemeEnricher.Stage.SET_LENGTH);
        assertThat(tokens.length).isEqualTo(6);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "J*", "R*", "AY", "YX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 44, 23, 49, 21, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 8, 7, 12, 7, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0);

        // odd
        tokens = underTest.enrich("AADD", PhonemeEnricher.Stage.SET_LENGTH);
        assertThat(tokens.length).isEqualTo(5);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "AA", "D*", "D*", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 9, 57, 57, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 11, 5, 5, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0);

        // art
        tokens = underTest.enrich("AA5RT", PhonemeEnricher.Stage.SET_LENGTH);
        assertThat(tokens.length).isEqualTo(5);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "AA", "RX", "T*", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 9, 18, 69, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 15, 10, 4, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 5, 0, 0, 0);

        // all
        tokens = underTest.enrich("AOL", PhonemeEnricher.Stage.SET_LENGTH);
        assertThat(tokens.length).isEqualTo(4);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "AO", "LX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 11, 19, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 12, 9, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0);

        // go
        tokens = underTest.enrich("GOW", PhonemeEnricher.Stage.SET_LENGTH);
        assertThat(tokens.length).isEqualTo(5);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "GX", "OW", "WX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 63, 52, 20, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 6, 14, 8, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0);

        // spy
        tokens = underTest.enrich("SPAY", PhonemeEnricher.Stage.SET_LENGTH);
        assertThat(tokens.length).isEqualTo(6);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "S*", "B*", "AY", "YX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 32, 54, 49, 21, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 2, 6, 12, 7, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0);

        // sty
        tokens = underTest.enrich("STAY", PhonemeEnricher.Stage.SET_LENGTH);
        assertThat(tokens.length).isEqualTo(6);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "S*", "D*", "AY", "YX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 32, 57, 49, 21, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 2, 5, 12, 7, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0);

        // sky
        tokens = underTest.enrich("SKAY", PhonemeEnricher.Stage.SET_LENGTH);
        assertThat(tokens.length).isEqualTo(6);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "S*", "G*", "AY", "YX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 32, 60, 49, 21, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 2, 6, 12, 7, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0);

        // scowl
        tokens = underTest.enrich("SKOWL", PhonemeEnricher.Stage.SET_LENGTH);
        assertThat(tokens.length).isEqualTo(7);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "S*", "GX", "OW", "WX", "LX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 32, 63, 52, 20, 19, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 2, 6, 14, 8, 9, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0, 0);

        // new
        tokens = underTest.enrich("NUW", PhonemeEnricher.Stage.SET_LENGTH);
        assertThat(tokens.length).isEqualTo(5);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "N*", "UX", "WX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 28, 16, 20, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 7, 10, 8, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0);

        // zoo
        tokens = underTest.enrich("ZUW5", PhonemeEnricher.Stage.SET_LENGTH);
        assertThat(tokens.length).isEqualTo(5);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "Z*", "UX", "WX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 38, 16, 20, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 6, 12, 8, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 6, 5, 5, 0);

        // chew
        tokens = underTest.enrich("CHYUW", PhonemeEnricher.Stage.SET_LENGTH);
        assertThat(tokens.length).isEqualTo(7);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "CH", "**", "Y*", "UW", "WX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 42, 43, 26, 53, 20, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 6, 2, 6, 9, 8, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0, 0);

        // jay
        tokens = underTest.enrich("JEY5", PhonemeEnricher.Stage.SET_LENGTH);
        assertThat(tokens.length).isEqualTo(6);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "J*", "**", "EY", "YX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 44, 45, 48, 21, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 8, 4, 14, 8, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 6, 5, 5, 0);

        // party
        tokens = underTest.enrich("PAA5RTIY", PhonemeEnricher.Stage.SET_LENGTH);
        assertThat(tokens.length).isEqualTo(7);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "P*", "AA", "RX", "DX", "IY", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 66, 9, 18, 30, 5, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 8, 15, 10, 2, 8, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 6, 5, 0, 0, 0, 0);

        // tardy
        tokens = underTest.enrich("TAA5RDIY", PhonemeEnricher.Stage.SET_LENGTH);
        assertThat(tokens.length).isEqualTo(7);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "T*", "AA", "RX", "DX", "IY", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 69, 9, 18, 30, 5, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 6, 15, 10, 2, 8, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 6, 5, 0, 0, 0, 0);
    }

    @Test
    public void shouldReturnTokensWithAdjustedLength() {
        PhonemeToken[] tokens;

        tokens = underTest.enrich("", PhonemeEnricher.Stage.ADJUST_LENGTH);
        assertThat(tokens.length).isEqualTo(2);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0);

        tokens = underTest.enrich(" ", PhonemeEnricher.Stage.ADJUST_LENGTH);
        assertThat(tokens.length).isEqualTo(3);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", " *", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0);

        // new. cow
        tokens = underTest.enrich("NUW. KOW", PhonemeEnricher.Stage.ADJUST_LENGTH);
        assertThat(tokens.length).isEqualTo(10);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "N*", "UX", "WX", ".*", " *", "KX", "OW", "WX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 28, 16, 20, 1, 0, 75, 52, 20, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 7, 10, 13, 18, 0, 6, 14, 8, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

        // tardy
        tokens = underTest.enrich("TAA5RDIY", PhonemeEnricher.Stage.ADJUST_LENGTH);
        assertThat(tokens.length).isEqualTo(7);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "T*", "AA", "RX", "DX", "IY", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 69, 9, 18, 30, 5, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 6, 14, 10, 2, 8, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 6, 5, 0, 0, 0, 0);

        // away eight
        tokens = underTest.enrich("AXWEY5 EY4T", PhonemeEnricher.Stage.ADJUST_LENGTH);
        assertThat(tokens.length).isEqualTo(11);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "AX", "W*", "EY", "YX", " *", "Q*", "EY", "YX", "T*", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 13, 25, 48, 21, 0, 31, 48, 21, 69, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 7, 8, 14, 8, 0, 5, 14, 7, 4, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 6, 5, 5, 0, 5, 4, 4, 0, 0);

        // and
        tokens = underTest.enrich("AEND", PhonemeEnricher.Stage.ADJUST_LENGTH);
        assertThat(tokens.length).isEqualTo(5);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "AE", "N*", "D*", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 8, 28, 57, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 11, 5, 6, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0);

        // meddle
        tokens = underTest.enrich("MEHDDUL", PhonemeEnricher.Stage.ADJUST_LENGTH);
        assertThat(tokens.length).isEqualTo(8);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "M*", "EH", "D*", "D*", "AX", "LX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 27, 7, 57, 57, 13, 19, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 7, 11, 3, 3, 5, 9, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0, 0, 0);

        // empty
        tokens = underTest.enrich("EHMPTIY", PhonemeEnricher.Stage.ADJUST_LENGTH);
        assertThat(tokens.length).isEqualTo(7);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "EH", "M*", "P*", "T*", "IY", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 7, 27, 66, 69, 5, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 11, 5, 4, 3, 8, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0, 0);

        // play
        tokens = underTest.enrich("PLEY5", PhonemeEnricher.Stage.ADJUST_LENGTH);
        assertThat(tokens.length).isEqualTo(6);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "P*", "L*", "EY", "YX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 66, 24, 48, 21, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 8, 7, 14, 8, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 6, 5, 5, 0);
    }

    @Test
    public void shouldReturnExpandedTokens() {
        PhonemeToken[] tokens;

        tokens = underTest.enrich("", PhonemeEnricher.Stage.EXPAND);
        assertThat(tokens.length).isEqualTo(2);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0);

        tokens = underTest.enrich(" ", PhonemeEnricher.Stage.EXPAND);
        assertThat(tokens.length).isEqualTo(3);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", " *", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0);

        // new. cow
        tokens = underTest.enrich("NUW. KOW", PhonemeEnricher.Stage.EXPAND);
        assertThat(tokens.length).isEqualTo(12);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "N*", "UX", "WX", ".*", " *", "KX", "**", "**", "OW", "WX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 28, 16, 20, 1, 0, 75, 76, 77, 52, 20, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 7, 10, 13, 18, 0, 6, 1, 4, 14, 8, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

        // tardy
        tokens = underTest.enrich("TAA5RDIY", PhonemeEnricher.Stage.EXPAND);
        assertThat(tokens.length).isEqualTo(9);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "T*", "**", "**", "AA", "RX", "DX", "IY", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 69, 70, 71, 9, 18, 30, 5, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 6, 2, 2, 14, 10, 2, 8, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 6, 6, 6, 5, 0, 0, 0, 0);

        // away eight
        tokens = underTest.enrich("AXWEY5 EY4T", PhonemeEnricher.Stage.EXPAND);
        assertThat(tokens.length).isEqualTo(13);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "AX", "W*", "EY", "YX", " *", "Q*", "EY", "YX", "T*", "**", "**", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 13, 25, 48, 21, 0, 31, 48, 21, 69, 70, 71, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 7, 8, 14, 8, 0, 5, 14, 7, 4, 2, 2, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 6, 5, 5, 0, 5, 4, 4, 0, 0, 0, 0);

        // and
        tokens = underTest.enrich("AEND", PhonemeEnricher.Stage.EXPAND);
        assertThat(tokens.length).isEqualTo(7);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "AE", "N*", "D*", "**", "**", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 8, 28, 57, 58, 59, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 11, 5, 6, 1, 1, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0, 0);

        // meddle
        tokens = underTest.enrich("MEHDDUL", PhonemeEnricher.Stage.EXPAND);
        assertThat(tokens.length).isEqualTo(12);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "M*", "EH", "D*", "**", "**", "D*", "**", "**", "AX", "LX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 27, 7, 57, 58, 59, 57, 58, 59, 13, 19, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 7, 11, 3, 1, 1, 3, 1, 1, 5, 9, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

        // empty
        tokens = underTest.enrich("EHMPTIY", PhonemeEnricher.Stage.EXPAND);
        assertThat(tokens.length).isEqualTo(9);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "EH", "M*", "P*", "T*", "**", "**", "IY", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 7, 27, 66, 69, 70, 71, 5, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 11, 5, 4, 3, 2, 2, 8, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0, 0, 0, 0);

        // play
        tokens = underTest.enrich("PLEY5", PhonemeEnricher.Stage.EXPAND);
        assertThat(tokens.length).isEqualTo(8);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "P*", "**", "**", "L*", "EY", "YX", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 66, 67, 68, 24, 48, 21, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 8, 2, 2, 7, 14, 8, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 6, 5, 5, 0);
    }

    @Test
    public void shouldReturnTokensWithBreathInserted() {
        PhonemeToken[] tokens;

        tokens = underTest.enrich("", PhonemeEnricher.Stage.INSERT_BREATH);
        assertThat(tokens.length).isEqualTo(2);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0);

        tokens = underTest.enrich(" ", PhonemeEnricher.Stage.INSERT_BREATH);
        assertThat(tokens.length).isEqualTo(3);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", " *", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 0, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0);

        // until was done rule live silver dollar track nation often
        tokens = underTest.enrich("AHNTIHL WAHZ DAH5N RUWL LAY5V SIHLVER DAALAA5R TRAEK NEY5SHUN AO4FTEHN", PhonemeEnricher.Stage.INSERT_BREATH);
        assertThat(tokens.length).isEqualTo(64);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "AH", "N*", "T*", "**", "**", "IH", "LX", " *", "W*", "AH", "Z*", " *", "D*", "**", "**", "AH", "N*", " *", "R*", "UW", "WX", "LX", " *", "L*", "AY", "YX", "V*", " *", "S*", "IH", "LX", "V*", "ER", "Q*", "??", "D*", "**", "**", "AA", "LX", "AA", "RX", " *", "CH", "R*", "AE", "KX", " *", "N*", "EY", "YX", "SH", "AX", "N*", " *", "AO", "F*", "T*", "**", "**", "EH", "N*", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 10, 28, 69, 70, 71, 6, 19, 0, 25, 10, 38, 0, 57, 58, 59, 10, 28, 0, 23, 53, 20, 19, 0, 24, 49, 21, 40, 0, 32, 6, 19, 40, 15, 31, 254, 57, 58, 59, 9, 19, 9, 18, 0, 42, 23, 8, 75, 0, 28, 48, 21, 33, 13, 28, 0, 11, 34, 69, 70, 71, 7, 28, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 8, 5, 6, 2, 2, 8, 9, 0, 8, 8, 6, 0, 7, 1, 1, 14, 7, 0, 7, 9, 8, 9, 0, 9, 15, 11, 7, 0, 2, 7, 12, 7, 11, 4, 0, 5, 1, 1, 11, 9, 15, 10, 0, 6, 7, 7, 6, 0, 8, 14, 8, 2, 7, 7, 0, 16, 2, 4, 2, 2, 11, 7, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 6, 6, 5, 0, 0, 0, 0, 0, 0, 0, 6, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 6, 5, 5, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0);

        // any ten shell ground trouble flat plant subtract
        tokens = underTest.enrich("EH4NIY TEHN SHEHL GRAWND TRAH5BUL FLAET PLAENT SAH5BTRAEKT", PhonemeEnricher.Stage.INSERT_BREATH);
        assertThat(tokens.length).isEqualTo(64);
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::pattern)).containsExactly(" *", "EH", "N*", "IY", " *", "T*", "**", "**", "EH", "N*", " *", "SH", "EH", "LX", " *", "GX", "**", "**", "R*", "AW", "WX", "N*", "D*", "**", "**", " *", "CH", "R*", "AH", "B*", "**", "**", "AX", "LX", " *", "F*", "L*", "AE", "T*", "Q*", "??", "P*", "**", "**", "L*", "AE", "N*", "T*", "**", "**", " *", "S*", "AH", "B*", "**", "**", "CH", "R*", "AE", "KX", "T*", "**", "**", " *");
        assertThat(Stream.of(tokens).map(PhonemeToken::phoneme).map(Phoneme::index)).containsExactly(0, 7, 28, 5, 0, 69, 70, 71, 7, 28, 0, 33, 7, 19, 0, 63, 64, 65, 23, 51, 20, 28, 57, 58, 59, 0, 42, 23, 10, 54, 55, 56, 13, 19, 0, 34, 24, 8, 69, 31, 254, 66, 67, 68, 24, 8, 28, 69, 70, 71, 0, 32, 10, 54, 55, 56, 42, 23, 8, 75, 69, 70, 71, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::length)).containsExactly(0, 14, 7, 8, 0, 4, 2, 2, 11, 7, 0, 2, 8, 9, 0, 6, 1, 2, 5, 12, 11, 5, 6, 1, 1, 0, 6, 10, 14, 6, 1, 2, 5, 9, 0, 2, 6, 7, 3, 4, 0, 5, 2, 2, 4, 11, 5, 6, 2, 2, 0, 2, 14, 6, 1, 2, 6, 7, 7, 4, 3, 2, 2, 0);
        assertThat(Stream.of(tokens).map(PhonemeToken::stress)).containsExactly(0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }
}
