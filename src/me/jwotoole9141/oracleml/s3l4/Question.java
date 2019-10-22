/*
 * AUTH: Jared O'Toole
 * DATE: Sat Oct 19th, 2019
 * PROJ: OracleMachineLearning
 * FILE: Question.java
 *
 * "Create a Yes/No Guessing Game" task for Section 4 Lesson 1
 * of the AI with ML in Java Oracle iLearning Course.
 *
 * Defines the question tree node class.
 */

package me.jwotoole9141.oracleml.s3l4;

import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Question {

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

        public static @Nullable Pair<Relation, String> parse(@NotNull String sentence) {

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
                    if (sentenceLower.contains(key)) {
                        matches.get(negRel).getKey().add(key);
                        if (key.length() > bestMatchPower) {
                            bestMatchPower = key.length();
                            bestMatchIndex = matches.get(posRel).getKey().size() - 1;
                            bestMatch = negRel;
                        }
                    }
                }
            }

            // if a match was found, return the relation and subject...
            if (bestMatch != null) {
                String key = matches.get(bestMatch).getKey().get(bestMatchIndex);
                int subjectIndex = sentenceLower.lastIndexOf(key) + key.length();
                return new Pair<>(bestMatch.getKey(), sentence.substring(subjectIndex));
            }
            // else, return null...
            else {
                return null;
            }
        }

            /*
            HashMap<Relation, List<String>> matches = new HashMap<>();
            HashMap<Relation, List<String>> negMatches = new HashMap<>();

            // for each subject relation type...
            for (Relation rel : values()) {

                // find any declare keys used...
                matches.put(rel, new ArrayList<>());
                for (String key : rel.declareKeys) {
                    if (sentenceLower.contains(key)) {
                        matches.get(rel).add(key);
                    }
                }
                // find any neg declare keys used...
                negMatches.put(rel, new ArrayList<>());
                for (String key : rel.negDeclareKeys) {
                    if (sentenceLower.contains(key)) {
                        negMatches.get(rel).add(key);
                    }
                }
            }
            // determine the match power of each relation type...
            HashMap<Relation, Integer> matchPower = determineMatchPower(matches);
            HashMap<Relation, Integer> negMatchPower = determineMatchPower(negMatches);

            // get the best match for the declare and neg declare keys...
            Map.Entry<Relation, Integer> bestMatchPower = null;
            Map.Entry<Relation, Integer> bestNegMatchPower = null;

            for (Map.Entry<Relation, Integer> entry : matchPower.entrySet()) {
                if (bestMatchPower == null || entry.getValue() > bestMatchPower.getValue()) {
                    bestMatchPower = entry;
                }
            }
            for (Map.Entry<Relation, Integer> entry : negMatchPower.entrySet()) {
                if (bestNegMatchPower == null || entry.getValue() > bestNegMatchPower.getValue()) {
                    bestNegMatchPower = entry;
                }
            }
            // if a declare key matched best...
            if (((bestMatchPower == null) ? 0 : bestMatchPower.getValue())
                    > ((bestNegMatchPower == null) ? 0 : bestNegMatchPower.getValue())) {

                bestMatchPower.getKey()
            }
            // else, a neg declare key matched best...
            else {

            }

            // return (bestMatch == null) ? null : bestMatch.getKey();
        }

        private static HashMap<Relation, Integer> determineMatchPower(HashMap<Relation, List<String>> matches) {

            HashMap<Relation, Integer> matchPowers = new HashMap<>();
            for (Relation rel : matches.keySet()) {
                int maxSize = 0;
                for (String key : matches.get(rel)) {
                    if (key.length() > maxSize) {
                        maxSize = key.length();
                    }
                }
                matchPowers.put(rel, maxSize);
            }
            return matchPowers;
        }
        */
    }

    private @NotNull Relation relation;
    private @NotNull String subject;
    private boolean last;

    private @Nullable Question yes;
    private @Nullable Question no;

    public Question(@NotNull Relation subjectRelation, @NotNull String subjectValue, boolean isLastQuestion) {

        this.relation = subjectRelation;
        this.subject = subjectValue;
        this.last = isLastQuestion;
    }

    public @NotNull String getPrompt() {
        return String.format(relation.getQuestionFormat(), subject);
    }

    public @NotNull Relation getRelation() {
        return relation;
    }

    public @NotNull String getSubject() {
        return subject;
    }

    public boolean isLast() {
        return last;
    }

    public @Nullable Question getYes() {
        return yes;
    }

    public @Nullable Question getNo() {
        return no;
    }

    public void setYes(@Nullable Question yesAnswer) {
        this.yes = yesAnswer;
    }

    public void setNo(@Nullable Question noAnswer) {
        this.no = noAnswer;
    }

    public @Nullable Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("relation", relation.name());
        map.put("subject", subject);
        map.put("last", last);
        map.put("yes", yes);
        map.put("no", no);
        return map;
    }

    public static @Nullable Question fromMap(@Nullable Map<?, ?> map) throws IllegalArgumentException, ClassCastException {
        if (map == null) {
            return null;
        }
        Question node = new Question(
                Relation.valueOf((String) map.get("relation")),
                (String) map.get("subject"),
                (Boolean) map.get("last")
        );
        node.setYes(fromMap((Map<?, ?>) map.get("yes")));
        node.setNo(fromMap((Map<?, ?>) map.get("no")));
        return node;
    }
}
