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
import java.util.Map;
import java.util.function.Function;

/**
 * Represents a node in a non-binary tree. It should be cast to
 * either a {@link InnerNode} or a {@link OuterNode} when using.
 *
 * @param <Q> The <i>question</i> type in this node's heirarchy
 * @param <A> The <i>answer</i> type in this node's heirarchy
 * @author Jared O'Toole
 */
public abstract class Node<Q, A> {

    protected @NotNull WeakReference<Node<Q, A>> parent;
    protected @NotNull WeakReference<A> parentAnswer;

    protected Node() {

        this.parent = new WeakReference<>(null);
        this.parentAnswer = new WeakReference<>(null);
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

    /**
     * Gets the <i>answer</i> this node is mapped to in its
     * parent if it exists. Its reference is held weakly.
     *
     * @return the parent <i>answer</i> or null
     */
    public @Nullable A getParentAnswer() {
        return parentAnswer.get();
    }

    /**
     * Gets the size of the tree, recursively, starting at this node.
     *
     * @return the number of nodes in the tree from here down
     */
    public abstract int getSize();

    /**
     * Creates a non-recursive string representation of this node.
     *
     * @return an informative, single-line string
     */
    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * Creates a string diagram representing the tree, recursively, starting at this node.
     *
     * @return a visual, multi-line string
     */
    public @NotNull String toDiagram() {
        return toDiagram("", "    ");
    }

    /**
     * Creates a json map representing the tree, recursively, starting at this node.
     *
     * @return a json-compatible mapping
     */
    public abstract @Nullable Map<String, Object> toMap(
            @NotNull Function<Q, Map<String, Object>> questionToMap,
            @NotNull Function<A, String> answerToStr);

    protected abstract @NotNull String toDiagram(@NotNull String prefix, @NotNull String branch);

    protected void updateParent(Node<Q, A> parent, A answer) {

        this.parent = new WeakReference<>(parent);
        this.parentAnswer = new WeakReference<>(answer);
    }
}
