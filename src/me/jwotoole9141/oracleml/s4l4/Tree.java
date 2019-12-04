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

import java.util.List;
import java.util.Map;
import java.util.Set;
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

        NodeInner<Q, A> tree = new NodeInner<>(toQuestion.apply(table.getTitle()));
        DataTable.Column results = table.getColumns().get(resultColIndex);
//        algorithm.branch(tree, table.toSubTable(col -> col != results), results, toQuestion, toAnswerByCol);
        return tree;  // FIXME
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
                    int resultColIndex,
                    @NotNull Function<String, Q> toQuestion,
                    @NotNull Function<Object, A>[] toAnswerByCol)
                    throws IllegalArgumentException {

                // TODO

//                if (table.getNumCells() == 0) {
//                    return;
//                }
//
//                @SuppressWarnings("unchecked")
//                Map<Object, Double> valueDistr = results.getDistribution();
//
//                for (Object value : valueDistr.keySet()) {
//                    double distr = valueDistr.get(value);
//                    if (distr == 0 || distr == 1) {
//
//                        // node.result = px
//                    }
//                }
            }
        };

        /**
         * Mutates the given node into a tree that
         * dichotomises the given table of data.
         *
         * @param node           the node to branch
         * @param table          the table to branch with
         * @param resultColIndex the column index of the table's <i>final answers</i>
         * @param toQuestion     a function that takes the label of columns in
         *                       the table and returns an object of type {@link Q}
         * @param toAnswerByCol  an array of functions, one per column in the table, that each
         *                       take data from the table and return an object of type {@link A}
         * @param <Q>            the <i>question</i> type of the given node
         * @param <A>            the <i>answer</i> type of the given node
         */
        public abstract <Q, A> void branch(
                @NotNull Node<Q, A> node,
                @NotNull DataTable table,
                int resultColIndex,
                @NotNull Function<String, Q> toQuestion,
                @NotNull Function<Object, A>[] toAnswerByCol)
                throws IllegalArgumentException;

        /**
         * Calculates the entropy of a column of data. Each datum is
         * classified as either a success or not, first.
         *
         * @param results     the column of a table that holds {@code successVals}
         * @param successVals the data values that should count as a success
         * @param <T>         the data type held by the given column
         * @return a number between 0 (<i>no entropy</i>) and 1 (<i>maximum entropy</i>)
         */
        public static <T> double entropy(
                @NotNull DataTable.Column<T> results,
                @NotNull Set<T> successVals) {

            // determine the probability of data being a success...

            int x = results.getCounts(successVals)
                    .values().stream()
                    .mapToInt(Integer::intValue)
                    .sum();

            double px = x / (double) results.getRows().size();
            double pk = 1.0 - px;

            // calculate the entropy of the data...

            double result = (0 - (px * Math.log(px)) - (pk * Math.log(pk))) / LOG_2;

            return Double.isNaN(result) ? 0 : result;  // logarithm domain
        }

        /**
         * Calculates the gain in entropy of a column of data. Makes a call
         * to {@link #entropy(DataTable.Column, Set) entropy()} for each
         * <i>outcome</i> of {@code attrColumn}.
         *
         * @param systemEntropy the total entropy of the {@code resultColumn}
         * @param attrColumn    the column whose gain in entropy is being evaluated
         * @param resultColumn  the column of a table that holds {@code successVals}
         * @param successVals   the data values that should count as a success
         * @param <A>           the data type held by the {@code attrColumn}
         * @param <B>           the data type held by the {@code resultColumn}
         * @return a number between 0 (<i>no entropy</i>) and 1 (<i>maximum entropy</i>)
         */
        public static <A, B> double gain(
                double systemEntropy,
                @NotNull DataTable.Column<A> attrColumn,
                @NotNull DataTable.Column<B> resultColumn,
                @NotNull Set<B> successVals) {

            // make sure both columns have the same number of rows...

            if (attrColumn.getRows().size() != resultColumn.getRows().size()) {
                throw new IllegalArgumentException(String.format(
                        "testColumn and resultColumn row mismatch. (%d and %d)",
                        attrColumn.getRows().size(), resultColumn.getRows().size()));
            }

            // for each outcome in the attribute column...

            Map<A, Integer> outcomeCounts = attrColumn.getCounts();
            for (A value : outcomeCounts.keySet()) {

                // subtract the proportional entropy of that outcome from 'entropy'...

                List<Integer> outcomeIndices = attrColumn.toSubColumnIndices(e -> e.equals(value));
                DataTable.Column<B> outcomeResultColumn = resultColumn.toSubColumn(outcomeIndices);
                double px = outcomeCounts.get(value) / (double) outcomeIndices.size();

                systemEntropy -= (px * entropy(outcomeResultColumn, successVals));
            }
            return systemEntropy;
        }

        /**
         * The natural logarithm of 2.
         */
        public static final double LOG_2 = Math.log(2);
    }
}
