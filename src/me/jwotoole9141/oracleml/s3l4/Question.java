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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        public static Relation getRelation(String sentence) {

            String sentenceLower = sentence.toLowerCase();

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
        }
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
