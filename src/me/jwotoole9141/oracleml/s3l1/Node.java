/*
 * AUTH: Jared O'Toole
 * DATE: Tue, Oct 15th, 2019
 * PROJ: OracleMachineLearning
 * FILE: Node.java
 *
 * Node class task for Section 3 Lesson 1 of the
 * AI with ML in Java Oracle iLearning Course.
 *
 * Create a simple binary tree node class.
 *
 */

package me.jwotoole9141.oracleml.s3l1;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A binary tree node.
 *
 * @param <T> the data type held by the node
 */
public class Node<T> {

    @NotNull public T data;
    @Nullable public Node<T> left;
    @Nullable public Node<T> right;

    public Node(@NotNull T data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }
}
