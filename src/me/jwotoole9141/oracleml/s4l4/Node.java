/*
 * AUTH: Jared O'Toole
 * DATE: 12/1/2019 8:46 PM
 * PROJ: OracleMachineLearning
 * FILE: AbsNode.java
 *
 * "Create an ID3 Tree" task for Section 4 Lesson 4
 * of the AI with ML in Java Oracle iLearning Course.
 *
 * Defines the AbsNode class.
 */

package me.jwotoole9141.oracleml.s4l4;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

/**
 * Represents a node in a non-binary tree. It should be cast to
 * either a {@link BranchNode} or a {@link LeafNode} when using.
 *
 * @param <Q> The <i>question</i> type in this node's heirarchy
 * @param <A> The <i>answer</i> type in this node's heirarchy
 * @author Jared O'Toole
 */
public abstract class Node<Q, A> {

    protected @NotNull WeakReference<Node<Q, A>> parent;

    protected Node() {
        this.parent = new WeakReference<>(null);
    }

    /**
     * Gets the parent of this node if it exists.
     * Its reference is held weakly.
     *
     * @return the parent node or null
     */
    public @Nullable Node<Q, A> getParent() {
        return parent.get();
    }
}
