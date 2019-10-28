/*
 * AUTH: Jared O'Toole
 * DATE: Sat Oct 19th, 2019
 * PROJ: OracleMachineLearning
 * FILE: Question.java
 *
 * "Create a Yes/No Guessing Game" task for Section 4 Lesson 1
 * of the AI with ML in Java Oracle iLearning Course.
 *
 * Defines the decision tree class.
 */

package me.jwotoole9141.oracleml.s3l4;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * A binary decision tree of Question nodes.
 *
 * @author Jared O'Toole
 */
public class DecisionTree {

    private @Nullable Question root;

    /**
     * Creates a new decision tree with the specified root node.
     *
     * @param treeRoot an initial question or null
     */
    public DecisionTree(@Nullable Question treeRoot) {
        root = treeRoot;
    }

    /**
     * Gets the initial question of the decision tree.
     *
     * @return the root node
     */
    public @Nullable Question getRoot() {
        return root;
    }

    /**
     * Recursively gets the size of the decision tree.
     *
     * @return the number of nodes in the tree
     */
    public int getSize() {
        return getSize(root);
    }

    private int getSize(Question node) {
        return (node == null) ? 0 : (1 + getSize(node.getYes()) + getSize(node.getNo()));
    }

    /**
     * Gets a string diagram representing the decision tree.
     *
     * @return a likely multi-line string
     */
    public @NotNull String getDiagram() {
        return (root == null) ? "" : getDiagram(root, "", "    ", "-->");
    }

    private @NotNull String getDiagram(
            @NotNull Question node, @NotNull String prefix,
            @NotNull String extra, @NotNull String symbol) {

        return (prefix + "\\" + symbol + " " + node.getPrompt() + "\n")
                + ((node.getYes() == null) ? "" : getDiagram(node.getYes(), prefix + extra, "|   ", "Y->"))
                + ((node.getNo() == null) ? "" : getDiagram(node.getNo(), prefix + extra, "    ", "N->"));
    }

    /**
     * Sets the initial question of this decision tree.
     *
     * @param root the new root node, or null
     */
    public void setRoot(@Nullable Question root) {
        this.root = root;
    }

    /**
     * Gets a representation of this decision tree as a json-style mapping.
     *
     * @return a mapping of string to union of string, mapping, null
     */
    public @NotNull Map<@NotNull String, @Nullable Object> toMap() {

        Map<String, Object> map = new HashMap<>();
        map.put("tree", (root != null) ? (root.toMap()) : null);
        return map;
    }

    /**
     * Creates a tree from the given json-style mapping.
     *
     * <p><pre>
     * { "tree": Map|null }
     * </pre></p>
     *
     * @param map a mapping of string to union of of string, mapping, null
     * @return the decision tree that the valid mapping represents, or null
     *
     * @throws IllegalArgumentException invalid string representation of a Relation
     * @throws ClassCastException       wrong type mapped to a key
     */
    public static @NotNull DecisionTree fromMap(@Nullable Map<?, ?> map)
            throws IllegalArgumentException, ClassCastException {

        if (map == null) {
            return new DecisionTree(null);
        }
        return new DecisionTree(Question.fromMap((Map<?, ?>) map.get("tree")));
    }
}
