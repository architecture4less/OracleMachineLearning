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

/**
 * Defines relationships between a subject and an object.
 *
 * @author Jared O'Toole
 */
public enum Relation {

    /** The subject, X, is the answer. */
    ANSWER("Is the answer '%s'?",
            "%s, '%s', is the answer",
            Pattern.compile("(?<=the answer is ).*",
                    Pattern.CASE_INSENSITIVE),
            true),

    /** The subject is not a type of X. */
    NOT_OF("Is it NOT a type of %s?",
            "%s isn't a type of %s",
            Pattern.compile("(?<=is(?:n'?t| not) a (?:type|kind) of ).*",
                    Pattern.CASE_INSENSITIVE),
            false),

    /** The subject is a type of X. */
    OF("Is it a type of %s?",
            "%s is a type of %s",
            Pattern.compile("(?<=is a (?:type|kind) of ).*",
                    Pattern.CASE_INSENSITIVE),
            true),

    /** The subject is X. */
    IS_NOT("Is it NOT %s?",
            "%s isn't %s",
            Pattern.compile("(?<=is(?:n'?t| not) ).*",
                    Pattern.CASE_INSENSITIVE),
            false),

    /** The subject is not X. */
    IS("Is it %s?",
            "%s is %s",
            Pattern.compile("(?<=is ).*",
                    Pattern.CASE_INSENSITIVE),
            true),

    /** The subject does not have X. */
    HAS_NOT("Does it NOT have %s?",
            "%s hasn't %s",
            Pattern.compile("(?<=(?:has(?:n'?t| not)|does(?:n'?t| not) have) ).*",
                    Pattern.CASE_INSENSITIVE),
            false),

    /** The subject has X. */
    HAS("Does it have %s?",
            "%s has %s",
            Pattern.compile("(?<=(?:has|does have) ).*",
                    Pattern.CASE_INSENSITIVE),
            true),

    /** The subject does not X. */
    DOES_NOT("Does it NOT %s?",
            "%s doesn't %s",
            Pattern.compile("(?<=(?:can(?:'?t| ?not)|does(?:n'?t| not)) ).*",
                    Pattern.CASE_INSENSITIVE),
            false),

    /** The subject does X. */
    DOES("Does it %s?",
            "%s can %s",
            Pattern.compile("(?<=(?:can|does) ).*",
                    Pattern.CASE_INSENSITIVE),
            true);

    private @NotNull String questionFormat;
    private @NotNull String statementFormat;
    private @NotNull Pattern statementPattern;
    private boolean trueForm;

    Relation(@NotNull String question, @NotNull String statement, @NotNull Pattern pattern, boolean trueForm) {

        this.questionFormat = question;
        this.statementFormat = statement;
        this.statementPattern = pattern;
        this.trueForm = trueForm;
    }

    /**
     * Gets an inquisitive statement regarding the subject 'it' and X.
     *
     * @param subject the X of the sentence clause
     * @return an inquisitive statement
     */
    public @NotNull String getQuestion(@NotNull String subject) {
        return String.format(questionFormat, subject);
    }

    /**
     * Gets a declarative statement regarding a subject and X.
     *
     * @param subject the subject of the sentence clause
     * @param object  the X of the sentence clause
     * @return a declarative statement
     */
    public @NotNull String getStatement(@NotNull String subject, @NotNull String object) {
        return String.format(statementFormat, subject, object);
    }

    /**
     * Tests if this relation is a true form.
     *
     * <p>
     * "subject is X" is an example of true form.
     * "subject is NOT X" is an example of false form.
     * </p>
     *
     * @return whether the relation is a true form or not
     */
    public boolean isTrueForm() {
        return trueForm;
    }

    /**
     * Gets the alternate form of this relation.
     *
     * <p>
     * ANSWER has only a true form.
     * All other enum constants have a true and false form.
     * </p>
     *
     * @param trueForm the form to change to
     * @return the true or false form of this relation
     */
    public Relation toForm(boolean trueForm) {

        // answer is only positive...
        if (this != ANSWER) {
            // odd enums are negative...
            if ((ordinal() & 1) == 1) {
                if (trueForm) {
                    return values()[ordinal() + 1];
                }
            }
            // even enums are positive...
            else if (!trueForm) {
                return values()[ordinal() - 1];
            }
        }
        return this;
    }

    /**
     * Parses a relation and subject from the given sentence.
     *
     * <p>
     * The subject of the sentence clause may be omitted.
     * </p>
     *
     * @param sentence a declarative sentence
     * @return A pair of the matched relation and the X of the sentence clause
     */
    public static @Nullable Pair<@NotNull Relation, @NotNull String> parse(@NotNull String sentence) {

        for (Relation rel : values()) {
            Matcher matcher = rel.statementPattern.matcher(sentence);

            if (matcher.find()) {
                return new Pair<>(rel, matcher.group());
            }
        }
        return null;
    }
}
