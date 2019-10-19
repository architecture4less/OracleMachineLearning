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

public class DecisionTree {

    private @Nullable Question root;

    public DecisionTree(@Nullable Question treeRoot) {
        root = treeRoot;
    }

    public @Nullable Question getRoot() {
        return root;
    }

    public void setRoot(@Nullable Question root) {
        this.root = root;
    }

    public int getSize() {
        return getSize(root);
    }

    private int getSize(Question node) {
        return (node == null) ? 0 : (1 + getSize(node.getYes()) + getSize(node.getNo()));
    }

    public @NotNull String getDiagram() {
        return (root == null) ? "" : getDiagram(root, "", "   ", "---");
    }

    private @NotNull String getDiagram(
            @NotNull Question node, @NotNull String prefix,
            @NotNull String extra, @NotNull String symbol
    ) {
        return (prefix + "\\-" + symbol + "-" + node.getPrompt() + "\n")
                + ((node.getYes() == null) ? "" : getDiagram(node.getYes(), prefix + extra, "|  ", "(Y)"))
                + ((node.getNo() == null) ? "" : getDiagram(node.getNo(), prefix + extra, "   ", "(N)"));
    }
}
