/*
 * AUTH: Jared O'Toole
 * DATE: Tue, Oct 15th, 2019
 * PROJ: OracleMachineLearning
 * FILE: Node.java
 *
 * BTree class task for Section 3 Lesson 3 of the
 * AI with ML in Java Oracle iLearning Course.
 *
 * Create a binary tree with various specified methods.
 *
 */

package me.jwotoole9141.oracleml.s3l3;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A binary tree.
 *
 * @param <T> the data type held by the tree's nodes
 */
public class BTree<T> {

    @Nullable public BNode<T> root;
    @Nullable public BNode<T> current;

    /**
     * Create a binary tree with an initial root node.
     *
     * @param rootNode the tree's root node
     */
    public BTree(@Nullable BNode<T> rootNode) {
        this.root = rootNode;
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
     * Determine if the binary tree is empty.
     *
     * @return true if there are no nodes
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Search for a node with the given data using the equals method.
     *
     * @param data the data to search for
     * @return a node containing the given data
     *     if it exists, else null
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

    public @NotNull String getInOrder() {
        return (root == null) ? "" : root.toStringInOrder();
    }

    public @NotNull String getPreOrder() {
        return (root == null) ? "" : root.toStringPreOrder();
    }

    public @NotNull String getPostOrder() {
        return (root == null) ? "" : root.toStringPostOrder();
    }

    public @NotNull String getDiagram() {
        return (root == null) ? "" : root.toStringDiagram();
    }
}
