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

/**
 * A node of the decision tree.
 *
 * <p>
 * Behaves as a question that leads to more questions.
 * </p>
 *
 * @author Jared O'Toole
 */
public class Question {

    private @NotNull Relation relation;
    private @NotNull String subject;

    private @Nullable Question yes;
    private @Nullable Question no;

    /**
     * Creates a new question with the given subject and relation.
     *
     * @param subjectRelation the relation between a subject and X.
     * @param subjectValue    the X being related to.
     */
    public Question(@NotNull Relation subjectRelation, @NotNull String subjectValue) {

        this.relation = subjectRelation;
        this.subject = subjectValue;
    }

    /**
     * Gets the prompt for this question, with subject 'it'.
     *
     * @return an inquisitive statement
     */
    public @NotNull String getPrompt() {
        return relation.getQuestion(subject);
    }

    /**
     * Gets an answer statement for this question, with the given subject.
     *
     * @param yes     whether to answer yes or no
     * @param subject the subject
     * @return a declarative statement
     */
    public @NotNull String getAnswer(@NotNull Boolean yes, @NotNull String subject) {
        return relation.toForm(relation.isTrueForm() == yes /* XNOR */).getStatement(subject, this.subject);
    }

    /**
     * Gets the relation for this question.
     *
     * @return the relation
     */
    public @NotNull Relation getRelation() {
        return relation;
    }

    /**
     * Gets the X value for this question.
     *
     * @return the subject of the question
     */
    public @NotNull String getSubject() {
        return subject;
    }

    /**
     * Tests if this question is a final question or not.
     *
     * @return true if its relation is an ANSWER, else false
     */
    public boolean isLast() {
        return relation == Relation.ANSWER;
    }

    /**
     * Gets the follow-up question for answering yes.
     *
     * <p>
     * Final questions will always show null children.
     * </p>
     *
     * @return the left child node
     */
    public @Nullable Question getYes() {
        return isLast() ? null : yes;
    }

    /**
     * Gets the follow-up question for answering no.
     *
     * <p>
     * Final questions will always show null children.
     * </p>
     *
     * @return the right child node
     */
    public @Nullable Question getNo() {
        return isLast() ? null : no;
    }

    /**
     * Gets either the yes or no follow-up question.
     *
     * <p>
     * Final questions will always show null children.
     * </p>
     *
     * @param answer whether to answer yes or no
     * @return the left or right child node
     */
    public @Nullable Question getYesOrNo(@NotNull Boolean answer) {
        return answer ? getYes() : getNo();
    }

    /**
     * Sets the follow-up question for answering yes.
     *
     * <p>
     * Final questions cannot have children.
     * </p>
     *
     * @param nextQuestion the follow-up question or null
     */
    public void setYes(@Nullable Question nextQuestion) {
        this.yes = isLast() ? null : nextQuestion;
    }

    /**
     * Sets the follow-up question for answering no.
     *
     * <p>
     * Final questions cannot have children.
     * </p>
     *
     * @param nextQuestion the follow-up question or null
     */
    public void setNo(@Nullable Question nextQuestion) {
        this.no = isLast() ? null : nextQuestion;
    }

    /**
     * Sets the follow-up question for answering either yes or no.
     *
     * <p>
     * Final questions cannot have children.
     * </p>
     *
     * @param answer       whether to answer yes or no
     * @param nextQuestion the follow-up question or null
     */
    public void setYesOrNo(@NotNull Boolean answer, @Nullable Question nextQuestion) {
        if (answer) {
            setYes(nextQuestion);
        }
        else {
            setNo(nextQuestion);
        }
    }

    /**
     * Gets a string representation of the question.
     *
     * @return non-recursive representation
     */
    @Override
    public String toString() {
        return String.format("Question[relation=%s, subject='%s', last=%b, yes=%s, no=%s]",
                relation.name(), subject, isLast(), yes == null ? "null" : "...", no == null ? "null" : "...");
    }

    /**
     * Gets a representation of this node and all its children as a json-style mapping.
     *
     * @return a mapping from string to union of string, mapping, null
     */
    public @Nullable Map<String, Object> toMap() {

        Map<String, Object> map = new HashMap<>();
        map.put("relation", getRelation().name());
        map.put("subject", getSubject());
        map.put("yes", (getYes() == null) ? null : getYes().toMap());
        map.put("zno", (getNo() == null) ? null : getNo().toMap());
        return map;
    }

    /**
     * Creates a node from the given json-style mapping.
     *
     * <p><pre>
     * { "relation": String,
     *   "subject": String,
     *   "yes": Map|null,
     *   "zno": Map|null }
     * </pre></p>
     *
     * @param map a mapping of string to union of of string, mapping, null
     * @return the question node that the valid mapping represents, or null
     *
     * @throws IllegalArgumentException invalid string representation of a Relation
     * @throws ClassCastException       wrong type mapped to a key
     */
    public static @Nullable Question fromMap(@Nullable Map<?, ?> map)
            throws IllegalArgumentException, ClassCastException {

        if (map == null) {
            return null;
        }
        Question node = new Question(
                Relation.valueOf((String) map.get("relation")),
                (String) map.get("subject")
        );
        node.setYes(fromMap((Map<?, ?>) map.get("yes")));
        node.setNo(fromMap((Map<?, ?>) map.get("zno")));
        return node;
    }
}
