/*
 * AUTH: Jared O'Toole
 * DATE: 12/3/2019 10:46 PM
 * PROJ: OracleMachineLearning
 * FILE: Tree.java
 *
 * "Create an ID3 Tree" task for Section 4 Lesson 4
 * of the AI with ML in Java Oracle iLearning Course.
 *
 * Defines the Tree class.
 */

package me.jwotoole9141.oracleml.s4l4;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class Tree {

    /**
     * Creates a tree that categorizes a table of data based on the given algorithm.
     * The resulting tree will essentially be a series of <i>questions</i> and
     * <i>answers</i> that lead to the <i>final answer</i> of the table. The
     * algorithm that is used may or may not produce the most <i>efficient
     * ordering</i> of questions.
     *
     * @param table          a table of data
     * @param resultColIndex the column index of the table's <i>final answers</i>
     * @param algorithm      the algorithm to build the tree with
     * @param toQuestion     a function that takes the title of the table or the label
     *                       of one of its columns and returns an object of type {@link Q}
     * @param toAnswerByCol  an array of functions, one per column in the table, that each
     *                       take data from the table and return an object of type {@link A}
     * @param <Q>            the <i>question</i> type of the tree
     * @param <A>            the <i>answer</i> type of the tree
     * @return a tree model that categorizes the given data
     */
    public static <Q, A> @NotNull Node<Q, A> fromTable(
            @NotNull DataTable table,
            int resultColIndex,
            @NotNull Algorithm algorithm,
            @NotNull Function<String, Q> toQuestion,
            @NotNull Function<Object, A>[] toAnswerByCol)
            throws IllegalArgumentException {

        DataTable.Column results = table.getColumns().get(resultColIndex);
        NodeInner<Q, A> tree = new NodeInner<>(toQuestion.apply(table.getTitle()));
        algorithm.branch(tree, table, results, toQuestion, toAnswerByCol);
        return tree;
    }

    /**
     * An enumeration of tree building algorithms that can
     * be used with {@link #fromTable(DataTable, int, Algorithm,
     * Function, Function[]) Tree.fromTable()}.
     */
    public enum Algorithm {

        /**
         * An Iterative Dichotomiser 3 implementation.
         */
        ID3 {
            @Override
            public <Q, A> void branch(
                    @NotNull Node<Q, A> node,
                    @NotNull DataTable table,
                    @NotNull DataTable.Column results,
                    @NotNull Function<String, Q> toQuestion,
                    @NotNull Function<Object, A>[] toAnswerByCol)
                    throws IllegalArgumentException {

                // TODO ID3 ALGORITHM
            }
        };

        /**
         * Mutates the given node into a tree that
         * dichotomises the given table of data.
         *
         * @param node          the node to branch
         * @param table         the table to branch with
         * @param results       the key column of the given table
         * @param toQuestion    a function that takes the label of columns in
         *                      the table and returns an object of type {@link Q}
         * @param toAnswerByCol an array of functions, one per column in the table, that each
         *                      take data from the table and return an object of type {@link A}
         * @param <Q>           the <i>question</i> type of the given node
         * @param <A>           the <i>answer</i> type of the given node
         */
        public abstract <Q, A> void branch(
                @NotNull Node<Q, A> node,
                @NotNull DataTable table,
                @NotNull DataTable.Column results,
                @NotNull Function<String, Q> toQuestion,
                @NotNull Function<Object, A>[] toAnswerByCol)
                throws IllegalArgumentException;
    }
}
