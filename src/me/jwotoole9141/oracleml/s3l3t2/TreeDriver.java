/*
 * AUTH: Jared O'Toole
 * DATE: Wed, Oct 16th, 2019
 * PROJ: OracleMachineLearning
 * FILE: TreeDriver.java
 *
 * BTree class task for Section 3 Lesson 3 of the
 * AI with ML in Java Oracle iLearning Course.
 *
 * Create a tree driver to test the BTree class.
 *
 */

package me.jwotoole9141.oracleml.s3l3t2;

/**
 * Tests the binary tree class.
 */
public class TreeDriver {

    public static void main(String[] args) {

        System.out.println("Creating an empty integer binary tree...");
        BTree<Integer> tree = new BTree<>(null);

        System.out.println("Counting nodes in the tree: ");
        System.out.println(tree.size());

        System.out.println("Creating a node with data 1 as the root...");
        tree.root = new BNode<>(1);

        System.out.println("Counting nodes in the tree: ");
        System.out.println(tree.size());

        System.out.println("Adding six more nodes...");
        tree.root.left = new BNode<>(2);
        tree.root.right = new BNode<>(3);
        tree.root.left.left = new BNode<>(4);
        tree.root.left.right = new BNode<>(5);
        tree.root.right.right = new BNode<>(6);
        tree.root.left.right.left = new BNode<>(7);

        System.out.println("Counting nodes in the tree: ");
        System.out.println(tree.size());

        System.out.println("Setting the seeker node to be the root's right node...");
        tree.seeker = tree.root.right;

        System.out.println("Displaying the seeker node's data: ");
        System.out.println(tree.seeker.data);

        System.out.println("Printing the tree's data in-order: ");
        System.out.println(tree.getInOrder());
        /*
        System.out.println(tree.getInOrder()
                .stream().map(Object::toString)
                .collect(Collectors.joining(",", "[", "]")));
         */

        System.out.println("Printing the tree's data pre-order: ");
        System.out.println(tree.getPreOrder());
        /*
        System.out.println(tree.getPreOrder()
                .stream().map(Object::toString)
                .collect(Collectors.joining(",", "[", "]")));
         */

        System.out.println("Printing the tree's data post-order: ");
        System.out.println(tree.getPostOrder());
        /*
        System.out.println(tree.getPostOrder()
                .stream().map(Object::toString)
                .collect(Collectors.joining(",", "[", "]")));
        */

        System.out.println("Printing the tree's diagram: ");
        System.out.println(tree.getDiagram());
    }

    /*
    // it turns out that java turns lists into strings
    // quite nicely on its own... oh well
    private static <T> String listToString(List<T> list) {
        return list.stream().map(Object::toString).collect(Collectors.joining(", ", "[", "]"));
    }
    */
}
