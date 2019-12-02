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

/**
 * Represents a leaf node in a binary tree.
 *
 * @param <Q> The 'question' type for the label of any parent nodes
 * @param <A> The 'answer' type for the node's result
 * @author Jared O'Toole
 */
public class LeafNode<Q, A> extends Node<Q, A> {

    private boolean result;

    /**
     * Creates a new leaf node with the given result.
     *
     * @param result the 'answer' for the entire tree
     */
    public LeafNode(boolean result) {
        super();
        this.result = result;
    }

    /**
     * Gets the success or failure of this leaf node. This
     * represents the answer to the premise of the entire tree.
     *
     * @return true if this leaf node represents a success, else false
     */
    public boolean getResult() {
        return result;
    }

    /**
     * Sets the result of this leaf node to be
     * either success or failure.
     *
     * @param result true if success, else false
     */
    public void setResult(boolean result) {
        this.result = result;
    }
}
