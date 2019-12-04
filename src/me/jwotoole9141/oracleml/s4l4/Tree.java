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

import java.util.*;
import java.util.function.Function;

/**
 * A utility class for building trees using the {@link Node} class
 * and {@link DataTable} class.
 *
 * @author Jared O'Toole
 * @see Algorithm
 * @see #fromTable
 */
public class Tree {

    /**
     * Creates a tree that categorizes a table of data based on the given algorithm.
     * The resulting tree will essentially be a series of <i>questions</i> and
     * <i>answers</i> that lead to the <i>final answer</i> of the table. The
     * algorithm that is used may or may not produce the most <i>efficient
     * ordering</i> of questions.
     *
     * @param table          a table of data
     * @param resultsKey     the label of the column in {@code table} that holds {@code successVals}
     * @param successVals    the data values that should count as a successful <i>final answer</i> to the table
     * @param algorithm      the algorithm to build the tree with
     * @param toQuestionFunc a function that takes the title of the table or the label
     *                       of one of its columns and returns an object of type {@link Q}
     * @param toAnswerFuncs  an array of functions, one per column in the table, that each
     *                       take data from the table and return an object of type {@link A}
     * @param defaultAnswer  the default answer to use for a node if there is no data
     * @param <Q>            the <i>question</i> type of the tree
     * @param <T>            the type of data held by the result column of {@code table}
     * @param <A>            the <i>answer</i> type of the tree
     * @return a tree model that categorizes the given data
     *
     * @throws IllegalArgumentException if {@code table} does not contain a column labled {@code resultsKey}
     * @throws ClassCastException       if data held by the column labeled {@code resultsKey} isn't {@link T}
     */
    public static <Q, A, T> @NotNull Node<Q, A> fromTable(
            @NotNull DataTable table,
            String resultsKey,
            Set<T> successVals,
            @NotNull Algorithm algorithm,
            @NotNull Function<String, Q> toQuestionFunc,
            @NotNull Map<String, Function<Object, A>> toAnswerFuncs,
            A defaultAnswer)
            throws IllegalArgumentException, ClassCastException {

        NodeInner<Q, A> tree = new NodeInner<>(toQuestionFunc.apply(table.getTitle()));
        algorithm.branch(tree, table, resultsKey, successVals, toQuestionFunc, toAnswerFuncs, defaultAnswer);
        return tree;
    }

    /**
     * An enumeration of tree building algorithms that can
     * be used with {@link #fromTable Tree.fromTable()}.
     */
    public enum Algorithm {

        /**
         * An Iterative Dichotomiser 3 implementation.
         */
        ID3 {
            @Override
            public <Q, A, T> void branch(
                    @NotNull NodeInner<Q, A> node,
                    @NotNull DataTable table,
                    @NotNull String resultsKey,
                    Set<T> successVals,
                    @NotNull Function<String, Q> toQuestionFunc,
                    @NotNull Map<String, Function<Object, A>> toAnswerFuncs,
                    @NotNull A defaultAnswer)
                    throws IllegalArgumentException, ClassCastException {

                // TODO 1. testing 2. refactoring

                if (table.getNumCols() == 1 /* just results col */ || table.getNumRows() == 0) {
                    return;
                }

                // calculate system entropy using the results column...

                //noinspection unchecked
                DataTable.Column<T> resultsCol = (DataTable.Column<T>) table.getColumns().stream()
                        .filter(col -> col.getLabel().equals(resultsKey))
                        .findFirst().orElseThrow(() -> new IllegalArgumentException(String.format(
                                "The given table does not contain a column labeled '%s'", resultsKey)));

                double systemEntropy = entropy(resultsCol, successVals);

                System.out.println("system entropy: " + systemEntropy);
                Map<DataTable.Column<?>, Double> gains = new HashMap<>();

                // calculate the gains of every other column...

                for (DataTable.Column<?> column : table.getColumns()) {
                    if (column != resultsCol) {
                        gains.put(column, gain(
                                systemEntropy, column,
                                resultsCol, successVals));
                    }
                }

                // choose the column with the highest gain...

                final DataTable.Column<?> attrCol;
                int attrColIndex;
                {
                    double highestGain = 0;
                    DataTable.Column<?> bestAttr = null;

                    System.out.println("null pointer?");

                    for (DataTable.Column<?> attr : gains.keySet()) {
                        double entropyGain = gains.get(attr);
                        System.out.println("  gain: " + entropyGain);
                        if (entropyGain > highestGain) {
                            highestGain = entropyGain;
                            bestAttr = attr;
                        }
                    }
                    System.out.println(bestAttr);
                    assert bestAttr != null;

                    attrColIndex = table.getColumns().indexOf(bestAttr);
                    attrCol = bestAttr;

                    System.out.println(bestAttr.toDiagram());
                }

                // for each unique value under the chosen column...

                /* gets a sub-table without the attr column-- we dont need it when branching further */
                DataTable slicedTable = table.toSubTable(col -> col != attrCol);

                System.out.println(attrCol);

                for (Object value : attrCol.getValues()) {  // FIXME null pointer

                    /* gets a sub-table with only the rows containing 'value' */
                    DataTable subTable = slicedTable.toSubTable(attrCol, e -> e.equals(value));

                    // TODO duplicated code
                    //noinspection unchecked
                    DataTable.Column<T> subResultsCol = (DataTable.Column<T>) table.getColumns().stream()
                            .filter(col -> col.getLabel().equals(resultsKey))
                            .findFirst().orElseThrow(() -> new IllegalArgumentException(String.format(
                                    "The given table does not contain a column labeled '%s'", resultsKey)));

                    if (subTable.getColumns().size() == 1 /* just results col */) {

                        // if there are no other columns left, end the tree with the most commun result..

                        T mostCommonResult = subResultsCol.getCounts().entrySet().stream()
                                .max(Comparator.comparingInt(Map.Entry::getValue))
                                .map(Map.Entry::getKey).orElse(null);

                        A answer = toAnswerFuncs.get(attrCol.getLabel()).apply(value);
                        A finalAnswer = mostCommonResult == null
                                ? defaultAnswer
                                : toAnswerFuncs.get(subResultsCol.getLabel()).apply(mostCommonResult);

                        // BASE CASE

                        node.getChildren().put(answer, new NodeOuter<>(finalAnswer));
                        return;
                    }
                    else {

                        // else, if there is no entropy left, end the tree with the unanimous result...

                        Map<T, Integer> resultSuccessCounts = resultsCol.getCounts(successVals);
                        for (T successVal : resultSuccessCounts.keySet()) {

                            int count = resultSuccessCounts.get(successVal);
                            if (count == 0 || count == resultsCol.getRows().size()) {

                                A answer = toAnswerFuncs.get(attrCol.getLabel()).apply(value);
                                A finalAnswer = toAnswerFuncs.get(subResultsCol.getLabel()).apply(successVal);

                                // BASE CASE

                                node.getChildren().put(answer, new NodeOuter<>(finalAnswer));
                                return;
                            }
                        }

                        // else, branch on this sub table...

                        A answer = toAnswerFuncs.get(attrCol.getLabel()).apply(value);
                        Q question = toQuestionFunc.apply(attrCol.getLabel());
                        NodeInner<Q, A> child = new NodeInner<>(question);
                        node.getChildren().put(answer, child);

                        // RECURSIVE CASE

                        branch(child, subTable, resultsKey, successVals,
                                toQuestionFunc, toAnswerFuncs, defaultAnswer);
                        return;
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
         * @param resultsKey     the label of the column in {@code table} that holds {@code successVals}
         * @param successVals    the data values that should count as a successful <i>final answer</i> to the table
         * @param toQuestionFunc a function that takes the label of columns in {@code table} and returns a {@link Q}
         * @param toAnswerFuncs  a map of column labels to functions that each take data from their column
         * @param defaultAnswer  the default answer to use for a node if there is no data
         *                       in {@code table} and return an {@link A}
         * @param <Q>            the <i>question</i> type of the given node
         * @param <T>            the type of data held by the result column of {@code table}
         * @param <A>            the <i>answer</i> type of the given node
         * @throws IllegalArgumentException if {@code table} does not contain a column labled {@code resultsKey}
         * @throws ClassCastException       if data held by the column labeled {@code resultsKey} isn't {@link T}
         */
        public abstract <Q, A, T> void branch(
                @NotNull NodeInner<Q, A> node,
                @NotNull DataTable table,
                @NotNull String resultsKey,
                Set<T> successVals,
                @NotNull Function<String, Q> toQuestionFunc,
                @NotNull Map<String, Function<Object, A>> toAnswerFuncs,
                @NotNull A defaultAnswer)
                throws IllegalArgumentException, ClassCastException;

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
                double px = outcomeCounts.get(value) / (double) attrColumn.getRows().size();

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
