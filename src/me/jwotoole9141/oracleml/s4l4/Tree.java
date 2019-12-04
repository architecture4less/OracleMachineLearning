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
        algorithm.branch(tree, table.toSubTable(col -> col != results), results, toQuestion, toAnswerByCol);
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

                if (table.getNumCells() == 0) {
                    return;
                }

                @SuppressWarnings("unchecked")
                Map<Object, Double> valueDistr = results.getDistribution();

                for (Object value : valueDistr.keySet()) {
                    double distr = valueDistr.get(value);
                    if (distr == 0 || distr == 1) {

                        // node.result = px
                    }
                }
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
         * Gets the entropy of a column of data. Each datum is
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
                    .mapToInt(i -> (int) i)
                    .sum();

            double px = x / (double) results.getRows().size();
            double pk = 1.0 - px;

            // calculate the entropy of the data...

            return (0 - (px * Math.log(px)) - (pk * Math.log(pk))) / LOG_2;
        }

        public static <T> double gain(
                double entropy,
                @NotNull DataTable table,
                int testColIndex,
                int resultsColIndex,
                @NotNull Set<T> successVals) {

            DataTable.Column<?> testColumn = table.getColumns().get(testColIndex);

            @SuppressWarnings("unchecked")
            Map<Object, Integer> outcomeCounts =
                    (Map<Object, Integer>) testColumn.getCounts();

            for (Object value : testColumn.getValues()) {

                DataTable outcomeTable = table.toSubTable(testColumn, e -> e.equals(value));

                @SuppressWarnings("unchecked")
                DataTable.Column<T> outcomeResults =
                        outcomeTable.getColumns().get(resultsColIndex);

                int px = outcomeCounts.get(value) / outcomeResults.getRows().size();

                entropy -= (px * entropy(outcomeResults, successVals));
            }
            return entropy;
        }

        /**
         * The natural logarithm of 2.
         */
        public static final double LOG_2 = Math.log(2);
    }
}
