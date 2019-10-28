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

import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Relation {

    OF("Is it a type of %s?",
            "%s is a type of %s.",
            Pattern.compile("(?<=is a (?:type|kind) of ).*",
                    Pattern.CASE_INSENSITIVE)),

    NOT_OF("Is it NOT a type of %s?",
            "%s isn't a type of %s.",
            Pattern.compile("(?<=is(?:n'?t| not) a (?:type|kind) of ).*",
                    Pattern.CASE_INSENSITIVE)),

    IS("Is it %s?",
            "%s is %s.",
            Pattern.compile("(?<=is ).*",
                    Pattern.CASE_INSENSITIVE)),

    IS_NOT("Is it NOT %s?",
            "%s isn't %s.",
            Pattern.compile("(?<=is(?:n'?t| not) ).*",
                    Pattern.CASE_INSENSITIVE)),

    HAS("Does it have %s?",
            "%s has %s.",
            Pattern.compile("(?<=(?:has|does have) ).*",
                    Pattern.CASE_INSENSITIVE)),

    HAS_NOT("Does it NOT have %s?",
            "%s hasn't %s.",
            Pattern.compile("(?<=(?:has(?:n'?t| not)|does(?:n'?t| not) have) ).*",
                    Pattern.CASE_INSENSITIVE)),

    DOES("Does it %s?",
            "%s can %s.",
            Pattern.compile("(?<=(?:can|does) ).*",
                    Pattern.CASE_INSENSITIVE)),

    DOES_NOT("Does it NOT %s?",
            "%s doesn't %s.",
            Pattern.compile("(?<=(?:can(?:'?t| ?not))|does(?:n'?t| not) ).*",
                    Pattern.CASE_INSENSITIVE));

    private @NotNull String questionFormat;
    private @NotNull String statementFormat;
    private @NotNull Pattern statementPattern;

    Relation(@NotNull String question, @NotNull String statement, @NotNull Pattern pattern) {

        this.questionFormat = question;
        this.statementFormat = statement;
        this.statementPattern = pattern;
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
