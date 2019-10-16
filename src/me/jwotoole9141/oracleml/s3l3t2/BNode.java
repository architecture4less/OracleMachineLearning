/*
 * AUTH: Jared O'Toole
 * DATE: Tue, Oct 15th, 2019
 * PROJ: OracleMachineLearning
 * FILE: BNode.java
 *
 * BTree class task for Section 3 Lesson 3 of the
 * AI with ML in Java Oracle iLearning Course.
 *
 * Using the BNode class from previous tasks...
 *
 */

package me.jwotoole9141.oracleml.s3l3t2;

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

    /**
     * Create a binary tree node with the given data.
     *
     * @param nodeData the node's non-null data
     */
    public BNode(@NotNull T nodeData) {
        data = nodeData;
    }

    // moved other methods directly into the BTree class...
}
