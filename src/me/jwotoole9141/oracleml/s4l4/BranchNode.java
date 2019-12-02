/*
 * AUTH: Jared O'Toole
 * DATE: 12/1/2019 11:34 PM
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
 * Represents a branch node in a binary tree.
 *
 * @param <Q> The 'question' type for the node's label
 * @param <A> The 'answer' type for the map-keys of the node's children
 * @author Jared O'Toole
 */
public class BranchNode<Q, A> extends Node<Q, A> {

    private @NotNull Q label;
    private @NotNull ObservableMap<@NotNull A, @NotNull Node> children;

    /**
     * Creates a new node with the given label.
     *
     * @param label the node's 'question'
     */
    public BranchNode(@NotNull Q label) {
        this(label, null);
    }

    /**
     * Creates a new node with the given label and children.
     *
     * @param label    the node's 'question'
     * @param children the node's map of 'answers' to 'follow-up questions'
     */
    public BranchNode(@NotNull Q label, @Nullable Map<@NotNull A, @NotNull Node> children) {

        super();
        this.label = label;
        this.children = FXCollections.emptyObservableMap();

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
     * Gets the children of this node. Child nodes are
     * accessed by a mapped key that represents the answer
     * to this node's 'question'.
     *
     * @return a mapping of 'answers' to sub nodes
     */
    public @NotNull Map<@NotNull A, @NotNull Node> getChildren() {
        return children;
    }
}
