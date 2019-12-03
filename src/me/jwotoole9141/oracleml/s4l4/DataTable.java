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

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataTable {

    public static class Column<T> {

        protected List<T> outcomes;
        protected List<T> rows;

        public Column(List<T> outcomes) {
            this(outcomes, null);
        }

        protected Column(List<T> outcomes, List<T> rows) {

            this.outcomes = outcomes;
            this.rows = rows == null ? new ArrayList<>() : new ArrayList<>(rows);
        }

        public List<T> getValues() {
            return Collections.unmodifiableList(outcomes);
        }

        public List<T> getRows() {
            return Collections.unmodifiableList(rows);
        }

        public List<Integer> identifySubColumn(Predicate<T> filter) {
            return IntStream.range(0, rows.size())
                    .filter(i -> filter.test(rows.get(i)))
                    .boxed().collect(Collectors.toList());
        }

        public Column<T> toSubColumn(List<Integer> indices) {
            return new Column<>(outcomes, indices.stream()
                    .map(i -> rows.get(i))
                    .collect(Collectors.toList()));
        }
    }

    /*
     * Based on my ID3 pseudocode,
     * this class needs:
     *
     * successes - the number of rows that are 'true' in the result column
     * total - the number of rows
     * attrs - the number of columns other than 'result'
     *
     * without(attr) - gets a subview of the table without the column 'attr'
     * with_only(outcome) - gets a subview of the table with only the rows that have 'outcome' for 'attr'
     */

    private List<Column> attrs;

    public DataTable() {
        this(null);
    }

    public DataTable(List<Column> attrs) {
        this.attrs = attrs == null ? new ArrayList<>() : new ArrayList<>(attrs);
    }

    public List<Column> getColumns() {
        return Collections.unmodifiableList(attrs);
    }

    public DataTable subTable(Predicate<Column> filter) {
        return new DataTable(attrs.stream().filter(filter).collect(Collectors.toList()));
    }

    public DataTable subTable(Column attr, Predicate<Object> filter) {
        for (Column<?> attr : attrs) {
            Column<?> newAttr = new Column<>(attr.outcomes);
            for (Object e : attr.getRows()) {
                if (filter.test(new Pair<>(attr, e))) {
                    newAttr.rows.add(e);
                }
            }
        }
    }

    public static DataTable fromCSV(String csv) {

        return null;  // TODO
    }
}
