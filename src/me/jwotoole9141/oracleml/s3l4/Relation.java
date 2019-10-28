/*
 * AUTH: Jared O'Toole
 * DATE: Wed Oct 23rd, 2019
 * PROJ: OracleMachineLearning
 * FILE: Relation.java
 *
 * "Create a Yes/No Guessing Game" task for Section 4 Lesson 1
 * of the AI with ML in Java Oracle iLearning Course.
 *
 * Defines the Relation enum.
 */

package me.jwotoole9141.oracleml.s3l4;

import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Relation {

    ANSWER("Is the answer '%s'?",
            "%s, '%s', is the answer",
            Pattern.compile("(?<=the answer is ).*",
                    Pattern.CASE_INSENSITIVE),
            true),

    OF("Is it a type of %s?",
            "%s is a type of %s",
            Pattern.compile("(?<=is a (?:type|kind) of ).*",
                    Pattern.CASE_INSENSITIVE),
            true),

    NOT_OF("Is it a type of %s?",
            "%s isn't a type of %s",
            Pattern.compile("(?<=is(?:n'?t| not) a (?:type|kind) of ).*",
                    Pattern.CASE_INSENSITIVE),
            false),

    IS("Is it %s?",
            "%s is %s",
            Pattern.compile("(?<=is ).*",
                    Pattern.CASE_INSENSITIVE),
            true),

    IS_NOT("Is it %s?",  // TODO negatives must come first so that 'not' doesn't get eaten up...
            "%s isn't %s",
            Pattern.compile("(?<=is(?:n'?t| not) ).*",
                    Pattern.CASE_INSENSITIVE),
            false),

    HAS("Does it have %s?",
            "%s has %s",
            Pattern.compile("(?<=(?:has|does have) ).*",
                    Pattern.CASE_INSENSITIVE),
            true),

    HAS_NOT("Does it have %s?",
            "%s hasn't %s",
            Pattern.compile("(?<=(?:has(?:n'?t| not)|does(?:n'?t| not) have) ).*",
                    Pattern.CASE_INSENSITIVE),
            false),

    DOES("Does it %s?",
            "%s can %s",
            Pattern.compile("(?<=can ).*",
                    Pattern.CASE_INSENSITIVE),
            true),

    DOES_NOT("Does it %s?",
            "%s doesn't %s",
            Pattern.compile("(?<=can(?:'?t| ?not) ).*",
                    Pattern.CASE_INSENSITIVE),
            false);

    private @NotNull String questionFormat;
    private @NotNull String statementFormat;
    private @NotNull Pattern statementPattern;
    private boolean positive;

    Relation(@NotNull String question, @NotNull String statement,
            @NotNull Pattern pattern, boolean positive) {

        this.questionFormat = question;
        this.statementFormat = statement;
        this.statementPattern = pattern;
        this.positive = positive;
    }

    public @NotNull String getQuestionFormat() {
        return questionFormat;
    }

    public @NotNull String getStatementFormat() {
        return statementFormat;
    }

    public @NotNull Pattern getStatementPattern() {
        return statementPattern;
    }

    public boolean isPositive() {
        return positive;
    }

    public Relation toPositivity(boolean positivity) {

        // answer is unsigned...
        if (this != ANSWER) {
            // odd enums are positive...
            if ((ordinal() & 1) == 1) {
                if (!positivity) {
                    return values()[ordinal() + 1];
                }
            }
            // even enums are negative...
            else if (positivity) {
                return values()[ordinal() - 1];
            }
        }
        return this;
    }

    public static @Nullable Pair<Relation, String> parse(@NotNull String sentence) {

        for (Relation rel : values()) {
            Matcher matcher = rel.statementPattern.matcher(sentence);
            if (matcher.find()) {
                return new Pair<>(rel, matcher.group());
            }
        }
        return null;
    }
}
