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
     * - [ ] successes - the number of rows that are 'true' in the result column
     * - [ ] total - the number of rows
     * - [X] attrs - the number of columns other than 'result'
     *
     * - [X] without(attr) - gets a subview of the table without the column 'attr'
     * - [X] with_only(outcome) - gets a subview of the table with only the rows that have 'outcome' for 'attr'
     */

    public static class Column<T> {

        protected List<T> rows;
        protected Set<T> values;

        public Column() {
            this(null);
        }

        public Column(List<T> rows) {

            this.rows = rows == null ? new ArrayList<>() : new ArrayList<>(rows);
            this.values = new HashSet<>(this.rows);
        }

        public List<T> getRows() {
            return Collections.unmodifiableList(rows);
        }

        public Set<T> getValues() {
            return Collections.unmodifiableSet(values);
        }

        public List<Integer> identifySubColumn(Predicate<T> filter) {
            return IntStream.range(0, rows.size())
                    .filter(i -> filter.test(rows.get(i)))
                    .boxed().collect(Collectors.toList());
        }

        public Column<T> toSubColumn(List<Integer> indices) {
            return new Column<>(indices.stream()
                    .map(i -> rows.get(i))
                    .collect(Collectors.toList()));
        }
    }

    private List<Column> attrs;
    protected int size;

    public DataTable() {
        this(null);
    }

    public DataTable(Set<Column> attrs) {

        this.attrs = attrs == null ? new ArrayList<>() : new ArrayList<>(attrs);

        // another way of saying "all columns should be of equal size" is "the
        // number of unique column sizes should be equal to the number of columns"...

        if (this.attrs.stream().map(c -> c.getRows().size()).distinct().count() != this.attrs.size()) {
            throw new IllegalArgumentException("Row sizes are unequal.");
        }

    }

    public List<Column> getColumns() {
        return Collections.unmodifiableList(attrs);
    }

    public DataTable toSubTable(Predicate<Column> columnFilter) {
        return new DataTable(attrs.stream().filter(columnFilter).collect(Collectors.toSet()));
    }

    public <T> DataTable toSubTable(Column<T> filterColumn, Predicate<T> rowFilter) {
        if (attrs.contains(filterColumn)) {
            List<Integer> indices = filterColumn.identifySubColumn(rowFilter);
            return new DataTable(attrs.stream().map(c -> c.toSubColumn(indices)).collect(Collectors.toSet()));
        }
        return this;
    }

    public static DataTable fromCSV(String csv) {

        // DataTable d = new DataTable();
        // Column<String> myCol = new Column<>();
        // d.subTableByRow(myCol, s -> s.equals("hello!"));

        return null;  // TODO
    }
}
