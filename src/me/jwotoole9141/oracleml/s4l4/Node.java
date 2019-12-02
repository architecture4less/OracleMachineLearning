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

public class Node<Q, A> {

    private @NotNull Q label;
    private @NotNull WeakReference<Node> parent;
    private @NotNull ObservableMap<@NotNull A, @NotNull Node> children;
    private @Nullable Boolean result;

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

    public @NotNull Q getLabel() {
        return label;
    }

    public @Nullable Node getParent() {
        return parent.get();
    }

    public @NotNull Map<@NotNull A, @NotNull Node> getChildren() {
        return children;
    }

    public boolean getResult() {
        if (!isLeaf()) {
            throw new IllegalStateException(
                    "May only use getResult() on a leaf node. " +
                            "Check with isLeaf() first.");
        }
        //noinspection ConstantConditions
        return result;
    }

    public final boolean isLeaf() {

        // enforce nodes having either a result or children...

        if (!children.isEmpty()) {
            this.result = null;
        }
        return result != null;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
