/*
 * AUTH: Jared O'Toole
 * DATE: Tue, Oct 15th, 2019
 * PROJ: OracleMachineLearning
 * FILE: Node.java
 *
 * Node traversal task for Section 3 Lesson 3 of the
 * AI with ML in Java Oracle iLearning Course.
 *
 * Add traversal methods to the simple binary tree node class.
 *
 */

package me.jwotoole9141.oracleml.s3l3;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A binary tree node.
 *
 * @param <T> the data type held by the node
 */
public class BNode<T> {

    @NotNull public T data;
    @Nullable public BNode<T> left;
    @Nullable public BNode<T> right;

    public BNode(@NotNull T nodeData) {
        data = nodeData;
    }

    public String toStringPreOrder() {
        String str = data.toString() + " ";
        if (left != null) str += left.toStringPreOrder();
        if (right != null) str += right.toStringPreOrder();
        return str;
    }

    public String toStringPostOrder() {
        String str = "";
        if (left != null) str += left.toStringPostOrder();
        if (right != null) str += right.toStringPostOrder();
        return str + data.toString() + " ";
    }

    public String toStringInOrder() {
        String str = "";
        if (left != null) str += left.toStringInOrder();
        str += data.toString() + " ";
        if (right != null) str += right.toStringInOrder();
        return str;
    }

    public String toStringDiagram() {
        return toStringDiagram("", true);
    }

    private String toStringDiagram(String prefix, boolean isTail) {

        return ((right == null) ? "" : right.toStringDiagram(prefix + (isTail ? "|  " : "   "), false))
                + (prefix + (isTail ? "\\--" : "/--") + data.toString() + "\n")
                + ((left == null) ? "" : left.toStringDiagram(prefix + (isTail ? "   " : "|  "), true));

//        String result = "";
//        if (right != null) result += right.toStringDiagram(prefix + (isTail ? "|  " : "   "), false);
//        result += prefix + (isTail ? "\\--" : "/--") + data.toString() + "\n";
//        if (left != null) result += left.toStringDiagram(prefix + (isTail ? "   " : "|  "), true);
//        return result;
    }
}
