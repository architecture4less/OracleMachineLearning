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

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataTable {

    public static class Column<T> {

        private final String label;
        private final List<T> rows = new ArrayList<>();
        private final Set<T> values = new HashSet<>();
        private final List<T> rowsView = Collections.unmodifiableList(rows);
        private final Set<T> valuesView = Collections.unmodifiableSet(values);

        public Column(String label) {
            this(label, null);
        }

        public Column(String label, List<T> rows) {
            this.label = label;
            if (rows != null) {
                this.rows.addAll(rows);
                this.values.addAll(rows);
            }
        }

        public String getLabel() {
            return label;
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

        public Column<T> toSubColumn(Predicate<T> filter) {
            return new Column<>(label, IntStream.range(0, rows.size())
                    .filter(i -> filter.test(rows.get(i)))
                    .boxed()
                    .map(rows::get)
                    .collect(Collectors.toList()));
        }

        public Column<T> toSubColumn(List<Integer> indices) {
            return new Column<>(label, indices.stream()
                    .map(rows::get)
                    .collect(Collectors.toList()));
        }

        public List<Integer> toSubColumnIndices(Predicate<T> filter) {
            return IntStream.range(0, rows.size())
                    .filter(i -> filter.test(rows.get(i)))
                    .boxed()
                    .collect(Collectors.toList());
        }
    }

    private final String title;
    private final int numRows;
    private final int size;

    private final List<Column> cols = new ArrayList<>();
    private final List<Column> colsView = Collections.unmodifiableList(cols);

    public DataTable(String title) {
        this(title, null);
    }

    public DataTable(String title, Set<Column> columns) {

        this.title = title;

        if (columns != null) {
            this.cols.addAll(columns);
        }
        if (this.cols.isEmpty()) {
            this.numRows = 0;
            this.size = 0;
        }
        else {
            this.numRows = this.cols.get(0).rows.size();
            this.size = numRows * this.cols.size();

            for (Column col : this.cols) {
                if (col.rows.size() != numRows) {
                    throw new IllegalArgumentException("Row sizes are unequal.");
                }
            }
        }
    }

    public String getTitle() {
        return title;
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
        return new DataTable(title, cols.stream().filter(columnFilter).collect(Collectors.toSet()));
    }

    public <T> DataTable toSubTable(Column<T> filterColumn, Predicate<T> rowFilter) {
        if (cols.contains(filterColumn)) {
            List<Integer> indices = filterColumn.toSubColumnIndices(rowFilter);
            return new DataTable(title, cols.stream().map(c -> c.toSubColumn(indices)).collect(Collectors.toSet()));
        }
        return this;
    }

    @Override
    public String toString() {
        return String.format("DataTable[size=%d, cols={%s}]",
                size, cols.stream()
                        .map(Column::toString)
                        .collect(Collectors.joining(", ")));
    }

    public String toCSV() {

        StringWriter strWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(strWriter);

        return null;  // TODO
    }

    public static DataTable fromCSV(String csv) {

        try {
            StringReader strReader = new StringReader(csv);
            CSVReader csvReader = new CSVReader(strReader);

            String[] line;
            while ((line = csvReader.readNext()) != null) {

            }
        }
        catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return null;  // TODO
    }
}
