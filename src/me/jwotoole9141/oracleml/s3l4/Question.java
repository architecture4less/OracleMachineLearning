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

import java.util.HashMap;
import java.util.Map;

public class Question {

    public static enum Relation {
        IS_A("Is it a type of %s?",
                "%s is a type of %s.",
                new String[] {"type of", "kind of", "is a"}),

        ISNT_A("Is it NOT a type of %s?",
                "%s isn't a type of %s.",
                new String[] {"type of", "kind of", "isnt a", "is not a"}),

        IS("Is it %s?",
                "%s is %s.",
                new String[] {"is"}),

        ISNT("Is it not %s?",
                "%s isn't %s.",
                new String[] {"isnt", "is not"}),

        HAS("Does it have %s?",
                "%s has %s.",
                new String[] {"has"}),

        HASNT("Does it NOT have %s?",
                "%s hasn't %s",
                new String[] {"hasnt", "has not", "doesnt have", "does not have"}),

        DOES("Does it %s?",
                "%s can %s.",
                new String[] {"can", "can do"}),

        DOESNT("Does it NOT %s?",
                "%s doesn't %s",
                new String[] {});

        private String questionFmt;
        private String confirmFmt;
        private String[] declareKeys;

        private Relation(String q, String c, String[] d) {
            this.questionFmt = q;
            this.confirmFmt = c;
            this.declareKeys = d;
        }

        public String getQuestionFmt() {
            return questionFmt;
        }

        public String getConfirmFmt() {
            return confirmFmt;
        }

        public String[] getDeclareKeys() {
            return declareKeys;
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

    public Relation getRelation() {
        return relation;
    }

    public String getSubject() {
        return subject;
    }

    public boolean isLast() {
        return last;
    }

    public @NotNull String getPrompt() {
        return ;
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
        map.put("prompt", prompt);
        map.put("last", last);
        map.put("yes", yes);
        map.put("no", no);
        return map;
    }

    public static @Nullable Question fromMap(@Nullable Map<?, ?> map) throws ClassCastException {
        if (map == null) {
            return null;
        }
        Question node = new Question((Boolean) map.get("last"), (String) map.get("prompt"));
        node.setYes(fromMap((Map<?, ?>) map.get("yes")));
        node.setNo(fromMap((Map<?, ?>) map.get("no")));
        return node;
    }
}
