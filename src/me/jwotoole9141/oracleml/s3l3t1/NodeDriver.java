/*
 * AUTH: Jared O'Toole
 * DATE: Tue, Oct 15th, 2019
 * PROJ: OracleMachineLearning
 * FILE: NodeDriver.java
 *
 * Node traversal task for Section 3 Lesson 3 of the
 * AI with ML in Java Oracle iLearning Course.
 *
 * Create a driver to test the node class.
 *
 */

package me.jwotoole9141.oracleml.s3l3t1;

/**
 * Test the node class.
 */
public class NodeDriver {

    public static void main(String[] args) {

        BNode<Integer> tree = new BNode<>(1);
        tree.left = new BNode<>(2);
        tree.right = new BNode<>(3);
        tree.left.left = new BNode<>(4);
        tree.left.right = new BNode<>(5);
        tree.right.right = new BNode<>(6);
        tree.left.right.left = new BNode<>(7);

        System.out.println("Pre-Order Traversal:  " + tree.toStringPreOrder());
        System.out.println("Post-Order Traversal: " + tree.toStringPostOrder());
        System.out.println("In-Order Traversal:   " + tree.toStringInOrder());
        System.out.println("Tree Diagram:");
        System.out.println(tree.toStringDiagram());
    }
}
