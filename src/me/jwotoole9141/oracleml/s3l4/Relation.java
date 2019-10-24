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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public enum Relation {

    IS_A("Is it a type of %s?",
            "%s is a type of %s.",
            "%s isn't a type of %s.",
            new String[] { "is a type of", "is a kind of" },
            new String[] { "isnt a type of", "is not a type of", "isnt a kind of", "is not a kind of" }),

    IS("Is it %s?",
            "%s is %s.",
            "%s isn't %s.",
            new String[] { "is" },
            new String[] { "isnt", "is not" }),

    HAS("Does it have %s?",
            "%s has %s.",
            "%s hasn't %s.",
            new String[] { "has" },
            new String[] { "hasnt", "has not", "doesnt have", "does not have" }),

    DOES("Does it %s?",
            "%s can %s.",
            "%s doesn't %s.",
            new String[] { "can", "does" },
            new String[] { "cant", "cannot", "can not", "doesnt", "does not" });

    private @NotNull String questionFormat;
    private @NotNull String confirmFormat;
    private @NotNull String negConfirmFormat;
    private @NotNull String[] declareKeys;
    private @NotNull String[] negDeclareKeys;

    Relation(@NotNull String q, @NotNull String c, @NotNull String nc, @NotNull String[] d, @NotNull String[] nd) {

        this.questionFormat = q;
        this.confirmFormat = c;
        this.negConfirmFormat = nc;
        this.declareKeys = d;
        this.negDeclareKeys = nd;
    }

    public @NotNull String getQuestionFormat() {
        return questionFormat;
    }

    public @NotNull String getConfirmFormat() {
        return confirmFormat;
    }

    public @NotNull String getNegConfirmFormat() {
        return negConfirmFormat;
    }

    public @NotNull String[] getDeclareKeys() {
        return declareKeys;
    }

    public @NotNull String[] getNegDeclareKeys() {
        return negDeclareKeys;
    }

    public static @Nullable Pair<Pair<Relation, Boolean>, String> parse(@NotNull String sentence) {

        String sentenceLower = sentence.toLowerCase();

        HashMap<Pair<Relation, Boolean>, Pair<List<String>, Integer>> matches = new HashMap<>();
        Pair<Relation, Boolean> bestMatch = null;
        int bestMatchIndex = 0;
        int bestMatchPower = 0;

        // for each subject relation type...
        for (Relation rel : values()) {

            Pair<Relation, Boolean> posRel = new Pair<>(rel, true);
            Pair<Relation, Boolean> negRel = new Pair<>(rel, false);

            // find any declare keys used...
            matches.put(posRel, new Pair<>(new ArrayList<>(), 0));
            for (String key : rel.declareKeys) {
                //noinspection DuplicatedCode
                if (sentenceLower.contains(key)) {
                    matches.get(posRel).getKey().add(key);
                    if (key.length() > bestMatchPower) {
                        bestMatchPower = key.length();
                        bestMatchIndex = matches.get(posRel).getKey().size() - 1;
                        bestMatch = posRel;
                    }
                }
            }
            // find any neg declare keys used...
            matches.put(negRel, new Pair<>(new ArrayList<>(), 0));
            for (String key : rel.negDeclareKeys) {
                //noinspection DuplicatedCode
                if (sentenceLower.contains(key)) {
                    matches.get(negRel).getKey().add(key);
                    if (key.length() > bestMatchPower) {
                        bestMatchPower = key.length();
                        bestMatchIndex = matches.get(negRel).getKey().size() - 1;
                        bestMatch = negRel;
                    }
                }
            }
        }

        // if a match was found, return the relation and subject...
        if (bestMatch != null) {
            String key = matches.get(bestMatch).getKey().get(bestMatchIndex);
            int subjectIndex = sentenceLower.lastIndexOf(key) + key.length();
            return new Pair<>(bestMatch, sentence.substring(subjectIndex));
        }
        // else, return null...
        else {
            return null;
        }
    }
}
