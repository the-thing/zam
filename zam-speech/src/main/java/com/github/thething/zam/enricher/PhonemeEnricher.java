package com.github.thething.zam.enricher;

import com.github.thething.zam.common.VisibleForTesting;
import com.github.thething.zam.reciter.Characters;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts a phonetic string (SAM-notation) into an array of {@link PhonemeToken}s ready for audio rendering.
 */
public final class PhonemeEnricher {

    private static final int DEFAULT_LENGTH_LIMIT = 232;

    private final int lengthLimit;

    public PhonemeEnricher() {
        this(DEFAULT_LENGTH_LIMIT);
    }

    public PhonemeEnricher(int lengthLimit) {
        if (lengthLimit <= 0) {
            throw new IllegalArgumentException("Length limit must be > 0: " + lengthLimit);
        }

        this.lengthLimit = lengthLimit;
    }

    /**
     * Converts a phonetic string in SAM notation into an array of {@link PhonemeToken}s ready for audio rendering.
     *
     * @param phonetic the phonetic input string in SAM notation (e.g. {@code "HEHLOW"})
     * @return an array of {@link PhonemeToken}s representing the enriched phoneme sequence
     * @throws RuntimeException if the input contains an unrecognized phoneme pattern
     */
    public PhonemeToken[] enrich(String phonetic) {
        return enrich(phonetic, Stage.INSERT_BREATH);
    }

    /**
     * Converts a phonetic string in SAM notation into an array of {@link PhonemeToken}s, stopping at the specified
     * processing {@link Stage}. This allows partial pipeline execution for testing purposes.
     *
     * @param phonetic the phonetic input string in SAM notation (e.g. {@code "HEHLOW"})
     * @param stage    the pipeline stage at which processing should stop (inclusive)
     * @return an array of {@link PhonemeToken}s representing the enriched phoneme sequence up to the given stage
     * @throws RuntimeException if the input contains an unrecognized phoneme pattern
     */
    @VisibleForTesting
    PhonemeToken[] enrich(String phonetic, Stage stage) {
        switch (stage) {

            case MAP -> {
                return map(phonetic).toArray(PhonemeToken[]::new);
            }

            case REWRITE -> {
                List<PhonemeToken> tokens = map(phonetic);
                rewrite(tokens);
                return tokens.toArray(PhonemeToken[]::new);
            }

            case ADD_STRESS -> {
                List<PhonemeToken> tokens = map(phonetic);
                rewrite(tokens);
                addStress(tokens);
                return tokens.toArray(PhonemeToken[]::new);
            }

            case SET_LENGTH -> {
                List<PhonemeToken> tokens = map(phonetic);
                rewrite(tokens);
                addStress(tokens);
                setLength(tokens);
                return tokens.toArray(PhonemeToken[]::new);
            }

            case ADJUST_LENGTH -> {
                List<PhonemeToken> tokens = map(phonetic);
                rewrite(tokens);
                addStress(tokens);
                setLength(tokens);
                adjustLength(tokens);
                return tokens.toArray(PhonemeToken[]::new);
            }

            case EXPAND -> {
                List<PhonemeToken> tokens = map(phonetic);
                rewrite(tokens);
                addStress(tokens);
                setLength(tokens);
                adjustLength(tokens);
                expand(tokens);
                return tokens.toArray(PhonemeToken[]::new);
            }

            case INSERT_BREATH -> {
                List<PhonemeToken> tokens = map(phonetic);
                rewrite(tokens);
                addStress(tokens);
                setLength(tokens);
                adjustLength(tokens);
                expand(tokens);
                insertBreath(tokens);
                return tokens.toArray(PhonemeToken[]::new);
            }

            default -> throw new IllegalArgumentException("Unknown stage: " + stage);
        }
    }

    /**
     * Maps a phonetic string in SAM notation to a list of {@link PhonemeToken}s. The resulting list is wrapped with a
     * leading and trailing pause token.
     *
     * @param phonetic the phonetic input string in SAM notation (e.g. {@code "HEHLOW"})
     * @return a mutable list of {@link PhonemeToken}s with head and tail pause tokens
     * @throws RuntimeException if the input contains an unrecognized phoneme pattern
     */
    private List<PhonemeToken> map(String phonetic) {
        List<PhonemeToken> tokens = new ArrayList<>();
        int index = 0;

        Phoneme pausePhoneme = PhonemeTables.getPhoneme(' ', '*');

        // head pause
        tokens.add(new PhonemeToken(pausePhoneme, 0, 0));

        while (index < phonetic.length()) {
            char c1 = phonetic.charAt(index);
            c1 = Characters.toUpperCase(c1);

            if (index < phonetic.length() - 1) {
                char c2 = phonetic.charAt(index + 1);
                c2 = Characters.toUpperCase(c2);

                // try matching both characters
                if (PhonemeTables.isDefined(c1, c2)) {
                    Phoneme phoneme = PhonemeTables.getPhoneme(c1, c2);
                    tokens.add(new PhonemeToken(phoneme, 0, 0));
                    index += 2;
                    continue;
                }
            }

            // try matching single character + wildcard
            if (PhonemeTables.isDefined(c1)) {
                Phoneme phoneme = PhonemeTables.getPhoneme(c1);
                tokens.add(new PhonemeToken(phoneme, 0, 0));
                index++;
                continue;
            }

            // try matching stress digit and add stress to previous pattern
            if (c1 >= '1' && c1 <= '9' && index > 0) {
                PhonemeToken previous = tokens.remove(tokens.size() - 1);

                int stress = c1 - '0';
                tokens.add(new PhonemeToken(previous.phoneme(), previous.length(), stress));

                index++;
                continue;
            }

            throw new RuntimeException("Invalid pattern pattern at index " + index + ": '" + c1 + "'");
        }

        // tail pause
        tokens.add(new PhonemeToken(pausePhoneme, 0, 0));

        return tokens;
    }

    /**
     * Rewrites the phonemes using the following rules:
     *
     * <pre>
     * () - pivot
     *
     * ({DIPHTHONG ENDING WITH WX}) -> ({DIPHTHONG ENDING WITH WX}) WX
     * ({DIPHTHONG NOT ENDING WITH WX}) -> ({DIPHTHONG NOT ENDING WITH WX}) YX
     * (UL) -> (AX) L*
     * (UM) -> (AX) M*
     * (UN) -> (AX) N*
     * ({STRESSED VOWEL}) {SILENCE} {STRESSED VOWEL} -> ({STRESSED VOWEL}) {SILENCE} Q* {VOWEL}
     * T* (R*) -> CH (R*)
     * (D*) R* -> (J*) R*
     * {VOWEL} (R*) -> {VOWEL} (RX)
     * {VOWEL} (L*) -> {VOWEL} (LX)
     * G* (S*) -> G* (Z*)
     * (K*) {NOT A DIPHTHONG YX} -> (KX) {NOT A DIPHTHONG YX}
     * (G*) {NOT A DIPHTHONG YX} -> (GX) {NOT A DIPHTHONG YX}
     * (S*) P* -> (S*) B*
     * (S*) T* -> (S*) D*
     * (S*) K* -> (S*) G*
     * (S*) KX -> (S*) GX
     * {ALVEOLAR} (UW) -> {ALVEOLAR} (UX)
     * (CH) -> (CH) CH+1
     * (J*) -> (J*) J+1
     * {VOWEL} (D* or T*) {OPTIONAL PAUSE} {VOWEL} -> {VOWEL} (DX) {OPTIONAL PAUSE} {VOWEL}
     * {VOWEL} (D* or T*) {UNSTRESSED VOWEL} -> {VOWEL} (DX) {UNSTRESSED VOWEL}
     * </pre>
     */
    private void rewrite(List<PhonemeToken> tokens) {
        // buffer all required phonemes
        Phoneme qStar = PhonemeTables.getPhoneme('Q');
        Phoneme lStar = PhonemeTables.getPhoneme('L');
        Phoneme rStar = PhonemeTables.getPhoneme('R');
        Phoneme tStar = PhonemeTables.getPhoneme('T');
        Phoneme dStar = PhonemeTables.getPhoneme('D');
        Phoneme jStar = PhonemeTables.getPhoneme('J');
        Phoneme mStar = PhonemeTables.getPhoneme('M');
        Phoneme nStar = PhonemeTables.getPhoneme('N');
        Phoneme gStar = PhonemeTables.getPhoneme('G');
        Phoneme zStar = PhonemeTables.getPhoneme('Z');
        Phoneme sStar = PhonemeTables.getPhoneme('S');
        Phoneme kStar = PhonemeTables.getPhoneme('K');
        Phoneme pStar = PhonemeTables.getPhoneme('P');
        Phoneme bStar = PhonemeTables.getPhoneme('B');

        Phoneme ax = PhonemeTables.getPhoneme('A', 'X');
        Phoneme ch = PhonemeTables.getPhoneme('C', 'H');
        Phoneme rx = PhonemeTables.getPhoneme('R', 'X');
        Phoneme wx = PhonemeTables.getPhoneme('W', 'X');
        Phoneme yx = PhonemeTables.getPhoneme('Y', 'X');
        Phoneme ul = PhonemeTables.getPhoneme('U', 'L');
        Phoneme um = PhonemeTables.getPhoneme('U', 'M');
        Phoneme un = PhonemeTables.getPhoneme('U', 'N');
        Phoneme lx = PhonemeTables.getPhoneme('L', 'X');
        Phoneme kx = PhonemeTables.getPhoneme('K', 'X');
        Phoneme gx = PhonemeTables.getPhoneme('G', 'X');
        Phoneme uw = PhonemeTables.getPhoneme('U', 'W');
        Phoneme ux = PhonemeTables.getPhoneme('U', 'X');
        Phoneme dx = PhonemeTables.getPhoneme('D', 'X');

        Phoneme chNext = PhonemeTables.getPhoneme(ch.index() + 1);
        Phoneme jStarNext = PhonemeTables.getPhoneme(jStar.index() + 1);

        int index = 0;

        while (index < tokens.size()) {
            final PhonemeToken token = tokens.get(index);
            final Phoneme phoneme = token.phoneme();

            if (phoneme.isDiphthong()) {
                if (phoneme.pattern().charAt(1) == 'W') {
                    // ({DIPHTHONG ENDING WITH WX}) -> ({DIPHTHONG ENDING WITH WX}) WX
                    tokens.add(index + 1, new PhonemeToken(wx, 0, token.stress()));
                } else {
                    // ({DIPHTHONG NOT ENDING WITH WX}) -> ({DIPHTHONG NOT ENDING WITH WX}) YX
                    tokens.add(index + 1, new PhonemeToken(yx, 0, token.stress()));
                }
            }

            // (UL) -> (AX) L*
            if (phoneme.index() == ul.index()) {
                tokens.set(index, new PhonemeToken(ax, 0, token.stress()));
                tokens.add(index + 1, new PhonemeToken(lStar, 0, token.stress()));
                index++;
                continue;
            }

            // (UM) -> (AX) M*
            if (phoneme.index() == um.index()) {
                tokens.set(index, new PhonemeToken(ax, 0, token.stress()));
                tokens.add(index + 1, new PhonemeToken(mStar, 0, token.stress()));
                index++;
                continue;
            }

            // (UN) -> (AX) N*
            if (phoneme.index() == un.index()) {
                tokens.set(index, new PhonemeToken(ax, 0, token.stress()));
                tokens.add(index + 1, new PhonemeToken(nStar, 0, token.stress()));
                index++;
                continue;
            }

            // ({STRESSED VOWEL}) {SILENCE} {STRESSED VOWEL} -> ({STRESSED VOWEL}) {SILENCE} Q* {VOWEL}
            if (phoneme.isVowel() && token.stress() > 0 && index < tokens.size() - 2) {
                PhonemeToken next1 = tokens.get(index + 1);
                PhonemeToken next2 = tokens.get(index + 2);

                if (next1.phoneme().isPause() && next2.phoneme().isVowel() && next2.stress() > 0) {
                    tokens.add(index + 2, new PhonemeToken(qStar, 0, 0));
                    index++;
                    continue;
                }
            }

            // IDE complains about the duplicate checks
            boolean rStarMatch = token.phoneme().index() == rStar.index();

            // T* (R*) -> CH (R*)
            if (rStarMatch && index > 0) {
                PhonemeToken previous = tokens.get(index - 1);

                if (previous.phoneme().index() == tStar.index()) {
                    tokens.set(index - 1, new PhonemeToken(ch, 0, previous.stress()));
                    index++;
                    continue;
                }
            }

            // (D*) R* -> (J*) R*
            if (token.phoneme().index() == dStar.index() && index < tokens.size() - 1) {
                PhonemeToken next = tokens.get(index + 1);

                if (next.phoneme().index() == rStar.index()) {
                    tokens.set(index, new PhonemeToken(jStar, 0, token.stress()));
                    index++;
                    continue;
                }
            }

            // {VOWEL} (R*) -> {VOWEL} (RX)
            if (rStarMatch && index > 0) {
                PhonemeToken previous = tokens.get(index - 1);

                if (previous.phoneme().isVowel()) {
                    tokens.set(index, new PhonemeToken(rx, 0, token.stress()));
                    index++;
                    continue;
                }
            }

            // {VOWEL} (L*) -> {VOWEL} (LX)
            if (phoneme.index() == lStar.index() && index > 0) {
                PhonemeToken previous = tokens.get(index - 1);

                if (previous.phoneme().isVowel()) {
                    tokens.set(index, new PhonemeToken(lx, 0, token.stress()));
                    index++;
                    continue;
                }
            }

            // G* (S*) -> G* (Z*)
            if (phoneme.index() == sStar.index() && index > 0) {
                PhonemeToken previous = tokens.get(index - 1);

                if (previous.phoneme() == gStar) {
                    tokens.set(index, new PhonemeToken(zStar, 0, previous.stress()));
                    index++;
                    continue;
                }
            }

            // (K*) {NOT A DIPHTHONG YX} -> (KX) {NOT A DIPHTHONG YX}
            if (phoneme.index() == kStar.index() && index < tokens.size() - 1) {
                PhonemeToken next = tokens.get(index + 1);

                if (!next.phoneme().isDiphthongYX()) {
                    tokens.set(index, new PhonemeToken(kx, 0, token.stress()));
                    index++;
                    continue;
                }
            }

            // (G*) {NOT A DIPHTHONG YX} -> (GX) {NOT A DIPHTHONG YX}
            if (phoneme.index() == gStar.index() && index < tokens.size() - 1) {
                PhonemeToken next = tokens.get(index + 1);

                if (!next.phoneme().isDiphthongYX()) {
                    tokens.set(index, new PhonemeToken(gx, 0, token.stress()));
                    index++;
                    continue;
                }
            }

            if (phoneme == sStar && index < tokens.size() - 1) {
                PhonemeToken next = tokens.get(index + 1);

                // (S*) P* -> (S*) B*
                if (next.phoneme() == pStar) {
                    tokens.set(index + 1, new PhonemeToken(bStar, 0, next.stress()));
                    index++;
                    continue;
                }

                // (S*) T* -> (S*) D*
                if (next.phoneme() == tStar) {
                    tokens.set(index + 1, new PhonemeToken(dStar, 0, next.stress()));
                    index++;
                    continue;
                }

                // (S*) K* -> (S*) G*
                if (next.phoneme() == kStar) {
                    tokens.set(index + 1, new PhonemeToken(gStar, 0, next.stress()));
                    index++;
                    continue;
                }

                // (S*) KX -> (S*) GX
                if (next.phoneme().index() == kx.index()) {
                    tokens.set(index + 1, new PhonemeToken(gStar, 0, next.stress()));
                    index++;
                    continue;
                }
            }

            // {ALVEOLAR} (UW) -> {ALVEOLAR} (UX)
            if (phoneme.index() == uw.index() && index > 0) {
                PhonemeToken previous = tokens.get(index - 1);

                if (previous.phoneme().isAlveolar()) {
                    tokens.set(index, new PhonemeToken(ux, 0, token.stress()));
                    index++;
                    continue;
                }
            }

            // (CH) -> (CH) CH+1
            if (phoneme == ch) {
                tokens.add(index + 1, new PhonemeToken(chNext, 0, token.stress()));
                index++;
                continue;
            }

            // (J*) -> (J*) J+1
            if (phoneme == jStar) {
                tokens.add(index + 1, new PhonemeToken(jStarNext, 0, token.stress()));
                index++;
                continue;
            }

            // the rule is strange - either implementation or comments in the original are wrong

            // {VOWEL} (D* or T*) {OPTIONAL PAUSE} {VOWEL} -> {VOWEL} (DX) {OPTIONAL PAUSE} {VOWEL}
            // {VOWEL} (D* or T*) {UNSTRESSED VOWEL} -> {VOWEL} (DX) {UNSTRESSED VOWEL}
            if ((phoneme.index() == tStar.index() || phoneme.index() == dStar.index()) && index > 0) {
                PhonemeToken previous = tokens.get(index - 1);

                if (previous.phoneme().isVowel() && index < tokens.size() - 1) {
                    PhonemeToken next = tokens.get(index + 1);
                    PhonemeToken nextNext = next;

                    if (next.phoneme().isPause() && index < tokens.size() - 2) {
                        nextNext = tokens.get(index + 2);
                    }

                    if (nextNext.phoneme().isVowel() && next.stress() == 0) {
                        tokens.set(index, new PhonemeToken(dx, 0, token.stress()));
                        index++;
                        continue;
                    }
                }
            }

            index++;
        }
    }

    /**
     * Propagates stress values from vowels back to their preceding consonants. For each consonant immediately followed
     * by a stressed vowel, the consonant's stress is set to the vowel's stress level incremented by one.
     *
     * <p>Only consonant–vowel pairs where the vowel carries a non-zero stress are affected:</p>
     *
     * <pre>
     * {CONSONANT} ({STRESSED VOWEL}) -> ({CONSONANT with stress = vowel stress + 1}) {STRESSED VOWEL}
     * </pre>
     */
    private void addStress(List<PhonemeToken> tokens) {
        for (int i = 0; i < tokens.size() - 1; i++) {
            PhonemeToken token = tokens.get(i);

            // skip non-consonant phonemes
            if (!token.phoneme().isConsonant()) {
                continue;
            }

            PhonemeToken next = tokens.get(i + 1);

            // skip non-vowel phonemes
            if (!next.phoneme().isVowel()) {
                continue;
            }

            int nextStress = next.stress();

            // skip phonemes without stress
            if (nextStress == 0) {
                continue;
            }

            // set current phoneme's stress to the next phoneme's stress + 1
            tokens.set(i, new PhonemeToken(token.phoneme(), token.length(), nextStress + 1));
        }
    }

    /**
     * Sets the length of each phoneme token based on its stress value.
     */
    private void setLength(List<PhonemeToken> tokens) {
        for (int i = 0; i < tokens.size(); i++) {
            PhonemeToken token = tokens.get(i);
            Phoneme phoneme = token.phoneme();
            int stress = token.stress();

            if (stress == 0 || stress > 127) {
                int unstressedLength = phoneme.unstressedLength();
                tokens.set(i, new PhonemeToken(token.phoneme(), unstressedLength, token.stress()));
            } else {
                int stressedLength = phoneme.stressedLength();
                tokens.set(i, new PhonemeToken(token.phoneme(), stressedLength, token.stress()));
            }
        }
    }

    /**
     * Applies various rules that adjust the lengths of phonemes.
     *
     * <pre>
     * {NOT FRICATIVE} or {VOICED} between {VOWEL} and {PUNCTUATION}    - multiply by 1.5
     * {VOWEL} {RX or LX} <CONSONANT>                                   - decrease {VOWEL} by 1
     * {VOWEL} {UNVOICED STOP CONSONANT}                                - decrease {VOWEL} by 1/8
     * {VOWEL} {VOICED CONSONANT}                                       - increase {VOWEL} by 1/4 + 1
     * {NASAL} {STOP CONSONANT}                                         - set {NASAL} to 5 and {STOP CONSONANT} 6
     * {STOP CONSONANT} {OPTIONAL SILENCE} {STOP CONSONANT}             - shorten both to 1/2 + 1
     * {LIQUID CONSONANT} {DIPHTHONG}                                   - decrease {DIPHTHONG} by 2
     * </pre>
     */
    private void adjustLength(List<PhonemeToken> tokens) {
        // {NOT FRICATIVE} or {VOICED} between {VOWEL} and {PUNCTUATION} - multiply by 1.5
        for (int i = 0; i < tokens.size(); i++) {
            PhonemeToken token = tokens.get(i);
            Phoneme phoneme = token.phoneme();

            if (phoneme.isPunctuation()) {
                int startIndex = i - 1;

                while (startIndex >= 0 && !tokens.get(startIndex).phoneme().isVowel()) {
                    startIndex--;
                }

                if (startIndex >= 0) {
                    // do not include starting vowel
                    for (int j = startIndex; j < i; j++) {
                        PhonemeToken ruleToken = tokens.get(j);
                        Phoneme rulePhoneme = ruleToken.phoneme();

                        if (!rulePhoneme.isFricative() || rulePhoneme.isVoiced()) {
                            int newLength = (ruleToken.length() >> 1) + ruleToken.length() + 1;
                            tokens.set(j, new PhonemeToken(rulePhoneme, newLength, ruleToken.stress()));
                        }
                    }
                }
            }
        }

        Phoneme lx = PhonemeTables.getPhoneme('L', 'X');
        Phoneme rx = PhonemeTables.getPhoneme('R', 'X');

        for (int i = 0; i < tokens.size(); i++) {
            PhonemeToken token = tokens.get(i);
            Phoneme phoneme = token.phoneme();

            // {VOWEL} {RX or LX} <CONSONANT> - decrease {VOWEL} by 1
            if (phoneme.isVowel() && i < tokens.size() - 2) {
                Phoneme nextPhoneme = tokens.get(i + 1).phoneme();
                Phoneme nextNextPhoneme = tokens.get(i + 2).phoneme();

                if ((nextPhoneme == lx || nextPhoneme == rx) && nextNextPhoneme.isConsonant()) {
                    tokens.set(i, new PhonemeToken(token.phoneme(), token.length() - 1, token.stress()));
                    continue;
                }
            }

            // {VOWEL} {UNVOICED STOP CONSONANT} - decrease vowel by 1/8th
            if (phoneme.isVowel() && i < tokens.size() - 1) {
                Phoneme nextPhoneme = tokens.get(i + 1).phoneme();

                if (nextPhoneme.isUnvoicedStopConsonant()) {
                    int newLength = token.length() - (token.length() >> 3);
                    tokens.set(i, new PhonemeToken(phoneme, newLength, token.stress()));
                    continue;
                }
            }

            // {VOWEL} {VOICED CONSONANT} - increase vowel by 1/4 + 1
            if (phoneme.isVowel() && i < tokens.size() - 1) {
                Phoneme nextPhoneme = tokens.get(i + 1).phoneme();

                if (nextPhoneme.isConsonant() && nextPhoneme.isVoiced()) {
                    int newLength = (token.length() >> 2) + token.length() + 1;
                    tokens.set(i, new PhonemeToken(phoneme, newLength, token.stress()));
                    continue;
                }
            }

            // {NASAL} {STOP CONSONANT} - set {NASAL} to 5 and {STOP CONSONANT} 6
            if (phoneme.isNasal() && i < tokens.size() - 1) {
                PhonemeToken nextToken = tokens.get(i + 1);
                Phoneme nextPhoneme = nextToken.phoneme();

                if (nextPhoneme.isStopConsonant()) {
                    tokens.set(i, new PhonemeToken(phoneme, 5, token.stress()));
                    tokens.set(i + 1, new PhonemeToken(nextPhoneme, 6, nextToken.stress()));
                    continue;
                }
            }

            // {STOP CONSONANT} {OPTIONAL SILENCE} {STOP CONSONANT} - shorten both to 1/2 + 1
            if (phoneme.isStopConsonant() && i < tokens.size() - 1) {
                PhonemeToken nextToken;

                int k = i;

                do {
                    k++;
                    nextToken = tokens.get(k);
                } while (nextToken.phoneme().isPause() && k < tokens.size() - 1);

                Phoneme nextPhoneme = nextToken.phoneme();

                if (nextPhoneme.isStopConsonant()) {
                    int newLength = (token.length() >> 1) + 1;
                    tokens.set(i, new PhonemeToken(phoneme, newLength, token.stress()));
                    newLength = (nextToken.length() >> 1) + 1;
                    tokens.set(k, new PhonemeToken(nextPhoneme, newLength, nextToken.stress()));
                    continue;
                }
            }

            // {LIQUID CONSONANT} {DIPHTHONG} - decrease {DIPHTHONG} by 2
            if (phoneme.isStopConsonant() && i < tokens.size() - 1) {
                PhonemeToken nextToken = tokens.get(i + 1);
                Phoneme nextPhoneme = nextToken.phoneme();

                if (nextPhoneme.isLiquid()) {
                    tokens.set(i + 1, new PhonemeToken(nextPhoneme, nextToken.length() - 2, nextToken.stress()));
                }
            }
        }
    }

    /**
     * Expands certain consonant phonemes into a two-part representation by inserting their "prime" variants (index + 1,
     * index + 2) into the phoneme stream. This is consistent with how SAM represents phonemes like CH → CH + CH' and J
     * → J + J', which require two phoneme slots each.
     */
    private void expand(List<PhonemeToken> tokens) {
        Phoneme slashH = PhonemeTables.getPhoneme('/', 'H');
        Phoneme slashX = PhonemeTables.getPhoneme('/', 'X');

        int index = 0;

        while (index < tokens.size()) {
            PhonemeToken token = tokens.get(index);
            Phoneme phoneme = token.phoneme();

            if (!phoneme.isStopConsonant()) {
                index++;
                continue;
            }

            if (!phoneme.isUnvoicedStopConsonant()) {
                Phoneme add1 = PhonemeTables.getPhoneme(phoneme.index() + 1);
                tokens.add(index + 1, new PhonemeToken(add1, add1.unstressedLength(), token.stress()));

                Phoneme add2 = PhonemeTables.getPhoneme(phoneme.index() + 2);
                tokens.add(index + 2, new PhonemeToken(add2, add2.unstressedLength(), token.stress()));

                index += 3;

                continue;
            }

            if (index >= tokens.size() - 1) {
                continue;
            }

            int k = index;
            PhonemeToken nextToken;

            do {
                k++;
                nextToken = tokens.get(k);
            } while (nextToken.phoneme().isPause() && k < tokens.size() - 1);

            Phoneme nextPhoneme = nextToken.phoneme();

            if (nextPhoneme.isVcSonorant()) {
                index++;
                continue;
            }

            if (nextPhoneme == slashX || nextPhoneme == slashH) {
                index++;
                continue;
            }

            Phoneme add1 = PhonemeTables.getPhoneme(phoneme.index() + 1);
            tokens.add(index + 1, new PhonemeToken(add1, add1.unstressedLength(), token.stress()));

            Phoneme add2 = PhonemeTables.getPhoneme(phoneme.index() + 2);
            tokens.add(index + 2, new PhonemeToken(add2, add2.unstressedLength(), token.stress()));

            index += 3;
        }
    }

    /**
     * Ensures the synthesized speech is broken into manageable breath groups. It prefers inserting breaths at natural
     * stop-consonant boundaries, but if the utterance runs too long, it forces a glottal stop at the last available
     * pause position.
     */
    private void insertBreath(List<PhonemeToken> tokens) {
        int i = 0;
        int currentLength = 0;
        int lastSilenceIndex = -1;
        Phoneme qStar = PhonemeTables.getPhoneme('Q');

        while (i < tokens.size()) {
            PhonemeToken token = tokens.get(i);
            Phoneme phoneme = token.phoneme();

            currentLength += token.length();

            if (currentLength < lengthLimit) {
                if (phoneme.isPunctuation()) {
                    // insert breath after punctuation
                    currentLength = 0;
                    tokens.add(i + 1, PhonemeToken.BREATH);
                    i += 2;
                    continue;
                }

                // mark last seen pause
                if (phoneme.isPause()) {
                    lastSilenceIndex = i;
                }

                i++;
                continue;
            }

            // limit is exceeded

            if (lastSilenceIndex != -1) {
                tokens.set(lastSilenceIndex, new PhonemeToken(qStar, 4, 0));
                tokens.add(lastSilenceIndex + 1, PhonemeToken.BREATH);

                currentLength = 0;
                i = lastSilenceIndex + 1;
                lastSilenceIndex = -1;

                continue;
            }

            i++;
        }
    }

    enum Stage {

        MAP,
        REWRITE,
        ADD_STRESS,
        SET_LENGTH,
        ADJUST_LENGTH,
        EXPAND,
        INSERT_BREATH
    }
}
