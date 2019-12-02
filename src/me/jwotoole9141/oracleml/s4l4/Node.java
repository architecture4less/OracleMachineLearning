/*
 * AUTH: Jared O'Toole
 * DATE: 12/1/2019 8:46 PM
 * PROJ: OracleMachineLearning
 * FILE: Node.java
 *
 * "Create an ID3 Tree" task for Section 4 Lesson 4
 * of the AI with ML in Java Oracle iLearning Course.
 *
 * Defines the Node class.
 */

package me.jwotoole9141.oracleml.s4l4;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * Represents a node in a non-binary tree.
 *
 * @param <Q> The 'question' type for the node's label
 * @param <A> The 'answer' type for the map-keys of the node's children
 * @author Jared O'Toole
 */
public class Node<Q, A> {

    /*
     * FIXME node should really be broken into
     *  two different classes extending a base node.
     *  - AbsNode { parent, key }
     *  - Node extends AbsNode { label, children }
     *  - LeafNode extends AbsNode { result }
     */

    private @NotNull Q label;
    private @NotNull WeakReference<Node> parent;
    private @NotNull ObservableMap<@NotNull A, @NotNull Node> children;
    private @Nullable Boolean result;

    /**
     * Creates a new node with the given label.
     *
     * @param label the node's 'question'
     */
    public Node(@NotNull Q label) {
        this(label, null, null, null);
    }

    private Node(
            @NotNull Q label,
            @Nullable Node parent,
            @Nullable Map<@NotNull A, @NotNull Node> children,
            @Nullable Boolean result) {

        this.label = label;
        this.parent = new WeakReference<>(parent);
        this.children = FXCollections.emptyObservableMap();
        this.result = result;

        // map listener sets or removes the parent reference of child nodes...

        this.children.addListener((MapChangeListener<A, Node>) change -> {

            if (change.wasAdded()) {
                Node child = change.getValueAdded();
                child.parent = new WeakReference<Node>(this);
            }
            if (change.wasRemoved()) {
                Node child = change.getValueRemoved();
                child.parent.clear();
            }
        });

        if (children != null) {
            this.children.putAll(children);
        }
    }

    /**
     * Gets an object that represents the premise for
     * how to choose an 'answer' from this node.
     *
     * @return the node's 'question'
     */
    public @NotNull Q getLabel() {
        return label;
    }

    /**
     * Gets the parent of this node if it exists.
     * Its reference is held weakly.
     *
     * @return the parent node or null
     */
    public @Nullable Node getParent() {
        return parent.get();
    }

    /**
     * Gets the children of this node. Child nodes are
     * accessed by a mapped key that represents the answer
     * to this node's 'question'.
     *
     * @return a mapping of 'answers' to sub nodes
     */
    public @NotNull Map<@NotNull A, @NotNull Node> getChildren() {
        return children;
    }

    /**
     * Gets the success or failure of this leaf node. This
     * represents the answer to the premise of the entire tree.
     * <br />
     * <br />
     * This method should <b><i>only</i></b> be called on a leaf
     * node. Use {@link #isLeaf()} to check before calling.
     *
     * @return true if this leaf node represents a success, else false
     * @throws IllegalStateException called on a non-leaf node
     */
    public boolean getResult() {
        if (!isLeaf()) {
            throw new IllegalStateException(
                    "May only use getResult() on a leaf node. " +
                            "Check with isLeaf() first.");
        }
        //noinspection ConstantConditions
        return result;
    }

    /**
     * Tests if this is a leaf node or not. A leaf node has
     * a result but does not have a label or children.
     *
     * @return true if this is a leaf node, else false
     */
    public final boolean isLeaf() {

        // enforce nodes having either a result or children...

        if (!children.isEmpty()) {
            this.result = null;
        }
        return result != null;
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
