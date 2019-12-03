/*
 * AUTH: Jared O'Toole
 * DATE: 12/1/2019 10:49 PM
 * PROJ: OracleMachineLearning
 * FILE: DataTable.java
 *
 * "Create an ID3 Tree" task for Section 4 Lesson 4
 * of the AI with ML in Java Oracle iLearning Course.
 *
 * Defines the DataTable class.
 */

package me.jwotoole9141.oracleml.s4l4;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataTable {

    /*
     * Based on my ID3 pseudocode,
     * this class needs:
     *
     * - [X] successes - the number of rows that are 'true' in the result column ( col.getDistr )
     * - [X] total - the number of rows
     * - [X] attrs - the number of columns other than 'result'
     *
     * - [X] without(attr) - gets a subview of the table without the column 'attr'
     * - [X] with_only(outcome) - gets a subview of the table with only the rows that have 'outcome' for 'attr'
     */

    public static class Column<T> {

        private final List<T> rows = new ArrayList<>();
        private final Set<T> values = new HashSet<>();
        private List<T> rowsView = Collections.unmodifiableList(rows);
        private Set<T> valuesView = Collections.unmodifiableSet(values);

        public Column() {
            this(null);
        }

        public Column(List<T> rows) {
            if (rows != null) {
                this.rows.addAll(rows);
                this.values.addAll(rows);
            }
        }

        public List<T> getRows() {
            return rowsView;
        }

        public Set<T> getValues() {
            return valuesView;
        }

        public Map<T, Double> getDistribution() {
            return getDistribution(values);
        }

        public Map<T, Double> getDistribution(Set<T> values) {

            Map<T, Double> distr = new HashMap<>();
            for (T value : rows) {
                if (values.contains(value)) {
                    distr.put(value, 1 + distr.getOrDefault(value, 0d));
                }
            }
            for (T value : distr.keySet()) {
                distr.put(value, distr.get(value) / distr.size());
            }
            return distr;
        }

        public List<Integer> subColumnIndices(Predicate<T> filter) {
            return IntStream.range(0, rows.size())
                    .filter(i -> filter.test(rows.get(i)))
                    .boxed().collect(Collectors.toList());
        }

        public Column<T> toSubColumn(List<Integer> indices) {
            return new Column<>(indices.stream()
                    .map(rows::get)
                    .collect(Collectors.toList()));
        }

        public Column<T> toSubColumn(Predicate<T> filter) {
            return new Column<>(IntStream.range(0, rows.size())
                    .filter(i -> filter.test(rows.get(i)))
                    .boxed()
                    .map(rows::get)
                    .collect(Collectors.toList()));
        }
    }

    private final List<Column> cols = new ArrayList<>();
    private final List<Column> colsView = Collections.unmodifiableList(cols);

    private int numRows = 0;
    private int size = 0;

    public DataTable() {
        this(null);
    }

    public DataTable(Set<Column> columns) {

        if (columns != null) {
            this.cols.addAll(columns);
        }
        if (!this.cols.isEmpty()) {

            this.numRows = this.cols.get(0).rows.size();
            this.size = numRows * this.cols.size();

            for (Column attr : this.cols) {
                if (attr.rows.size() != numRows) {
                    throw new IllegalArgumentException("Row sizes are unequal.");
                }
            }
        }
    }

    public List<Column> getColumns() {
        return colsView;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getSize() {
        return size;
    }

    public DataTable toSubTable(Predicate<Column> columnFilter) {
        return new DataTable(cols.stream().filter(columnFilter).collect(Collectors.toSet()));
    }

    public <T> DataTable toSubTable(Column<T> filterColumn, Predicate<T> rowFilter) {
        if (cols.contains(filterColumn)) {
            List<Integer> indices = filterColumn.subColumnIndices(rowFilter);
            return new DataTable(cols.stream().map(c -> c.toSubColumn(indices)).collect(Collectors.toSet()));
        }
        return this;
    }

    @Override
    public String toString() {
        return super.toString();  // TODO
    }

    public String toCSV() {

        return null;  // TODO
    }

    public static DataTable fromCSV(String csv) {

        return null;  // TODO
    }
}
