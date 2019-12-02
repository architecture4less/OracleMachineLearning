/*
 * AUTH: Jared O'Toole
 * DATE: 12/1/2019 11:35 PM
 * PROJ: OracleMachineLearning
 * FILE: LeafNode.java
 *
 * "Create an ID3 Tree" task for Section 4 Lesson 4
 * of the AI with ML in Java Oracle iLearning Course.
 *
 * Defines the LeafNode class.
 */

package me.jwotoole9141.oracleml.s4l4;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Represents a leaf node in a non-binary tree. Leaf nodes
 * have a <i>final answer</i> to the premise of its tree.
 *
 * @param <Q> The <i>question</i> type in this node's heirarchy
 * @param <A> The <i>answer</i> type in this node's heirarchy
 * @author Jared O'Toole
 * @see InnerNode
 */
public class OuterNode<Q, A> extends Node<Q, A> {

    protected @NotNull A answer;

    /**
     * Creates a new leaf node with the given <i>answer</i>.
     *
     * @param answer the <i>final answer</i> to the premise of the entire tree
     */
    public OuterNode(@NotNull A answer) {
        super();
        this.answer = answer;
    }

    /**
     * Gets the <i>final answer</i> of this leaf node.
     * This represents the <i>answer</i> to the premise
     * of the entire tree.
     *
     * @return the <i>final answer</i>
     */
    public @NotNull A getAnswer() {
        return answer;
    }

    /**
     * Sets the <i>final answer</i> of this leaf node.
     * This represents the <i>answer</i> to the premise
     * of the entire tree.
     *
     * @param answer the <i>final answer</i>
     */
    public void setAnswer(@NotNull A answer) {
        this.answer = answer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSize() {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("OuterNode[answer=%s]",
                answer.toString()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Map<String, Object> toMap(
            @NotNull Function<Q, Map<String, Object>> questionToMap,
            @NotNull Function<A, String> answerToStr) {

        Map<String, Object> map = new HashMap<>();
        map.put("answer", answerToStr.apply(getAnswer()));
        return map;
    }

    @Override
    protected @NotNull String toDiagram(@NotNull String prefix, @NotNull String branch) {
        return prefix + "\\--> "
                + (getParentAnswer() == null ? " " : "[" + getParentAnswer().toString() + "] ")
                + getAnswer().toString() + "\n";
    }
}
