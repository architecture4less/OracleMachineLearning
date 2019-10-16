/*
 * AUTH: Jared O'Toole
 * DATE: Tue, Oct 15th, 2019
 * PROJ: OracleMachineLearning
 * FILE: BTree.java
 *
 * BTree class task for Section 3 Lesson 3 of the
 * AI with ML in Java Oracle iLearning Course.
 *
 * Create a binary tree with various specified methods.
 *
 */

package me.jwotoole9141.oracleml.s3l3t2;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * A binary tree.
 *
 * @param <T> the data type held by the tree's nodes
 */
public class BTree<T> {

    @Nullable public BNode<T> root;
    @Nullable public BNode<T> seeker;

    /**
     * Create a binary tree with an initial root node.
     *
     * @param rootNode the tree's root node
     */
    public BTree(@Nullable BNode<T> rootNode) {
        this.root = rootNode;
    }

    /**
     * Determine if the binary tree is empty.
     *
     * @return true if there are no nodes
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Get the size of the binary tree.
     *
     * @return the number of nodes
     */
    public int size() {
        return size(root);
    }

    private int size(@Nullable BNode<T> node) {
        return (node == null) ? 0 : (1 + size(node.left) + size(node.right));
    }

    /**
     * Search for a node with the given data using the equals method.
     *
     * @param data the data to search for
     * @return a node containing the given data
     * if it exists, else null
     */
    public @Nullable BNode<T> search(@NotNull T data) {
        return search(root, data);
    }

    private @Nullable BNode<T> search(@Nullable BNode<T> node, @NotNull T data) {
        if (node == null) {
            return null;
        }
        if (node.data.equals(data)) {
            return node;
        }
        if (node.left != null) {
            BNode<T> result = search(node.left, data);
            if (result != null) {
                return result;
            }
        }
        if (node.right != null) {
            return search(node.right, data);
        }
        return null;
    }

    /**
     * Get an in-order list of all data in the tree.
     *
     * @return the list of data
     */
    public @NotNull List<T> getInOrder() {
        return (root == null) ? new ArrayList<>() : getInOrder(root);
    }

    private List<T> getInOrder(@NotNull BNode<T> node) {

        List<T> result = new ArrayList<>();
        if (node.left != null) result.addAll(getInOrder(node.left));
        result.add(node.data);
        if (node.right != null) result.addAll(getInOrder(node.right));
        return result;
    }

    /**
     * Get a pre-order list of all data in the tree.
     *
     * @return the list of data
     */
    public @NotNull List<T> getPreOrder() {
        return (root == null) ? new ArrayList<>() : getPreOrder(root);
    }

    private List<T> getPreOrder(@NotNull BNode<T> node) {

        List<T> result = new ArrayList<>();
        result.add(node.data);
        if (node.left != null) result.addAll(getPreOrder(node.left));
        if (node.right != null) result.addAll(getPreOrder(node.right));
        return result;
    }

    /**
     * Get a post-order list of all data in the tree.
     *
     * @return the list of data
     */
    public @NotNull List<T> getPostOrder() {
        return (root == null) ? new ArrayList<>() : getPostOrder(root);
    }

    private List<T> getPostOrder(@NotNull BNode<T> node) {

        List<T> result = new ArrayList<>();
        if (node.left != null) result.addAll(getPostOrder(node.left));
        if (node.right != null) result.addAll(getPostOrder(node.right));
        result.add(node.data);
        return result;
    }

    /**
     * Get a multi-line string representing a
     * horizontal, ascii-art diagram of the tree.
     *
     * @return the string diagram
     */
    public @NotNull String getDiagram() {
        return (root == null) ? "" : getDiagram(root, "", true);
    }

    private String getDiagram(@NotNull BNode<T> node, String prefix, boolean isTail) {
        return ((node.right == null) ? "" : getDiagram(node.right, prefix + (isTail ? "|  " : "   "), false))
                + (prefix + (isTail ? "\\--" : "/--") + node.data.toString() + "\n")
                + ((node.left == null) ? "" : getDiagram(node.left, prefix + (isTail ? "   " : "|  "), true));
    }
}
