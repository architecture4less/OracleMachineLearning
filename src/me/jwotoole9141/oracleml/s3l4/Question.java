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

import java.util.*;

public class Question {

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
        return last ? null : yes;
    }

    public @Nullable Question getNo() {
        return last ? null : no;
    }

    public void setYes(@Nullable Question yesAnswer) {
        this.yes = last ? null : yesAnswer;
    }

    public void setNo(@Nullable Question noAnswer) {
        this.no = last ? null : noAnswer;
    }

    public @Nullable Map<String, Object> toMap() {

        Map<String, Object> map = new HashMap<>();
        map.put("relation", getRelation().name());
        map.put("subject", getSubject());
        map.put("last", isLast());
        map.put("yes", (getYes() == null) ? null : getYes().toMap());
        map.put("no", (getNo() == null) ? null : getNo().toMap());
        return map;
    }

    public static @Nullable Question fromMap(@Nullable Map<?, ?> map)
            throws IllegalArgumentException, ClassCastException {

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

    @Override
    public String toString() {
        return String.format("Question[relation=%s, subject='%s', last=%b, yes=%s, no=%s]",
                relation.name(), subject, last, yes == null ? "null" : "...", no == null ? "null" : "...");
    }
}
