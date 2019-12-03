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

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
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

    @SafeVarargs
    public final void toCsv(Function<Object, String>... serializers)
            throws IOException {

        toCsv(new File(title), serializers);
    }

    @SafeVarargs
    public final void toCsv(File file, Function<Object, String>... serializers)
            throws IOException {

        try (FileWriter fileWriter = new FileWriter(file);
             CSVWriter csvWriter = new CSVWriter(fileWriter)) {

            // write the table header...

            String[] header = new String[cols.size()];
            for (int i = 0; i < cols.size(); i++) {
                header[i] = cols.get(i).label;
            }
            csvWriter.writeNext(header);

            // ensure the number of serializers, if any, match the number of columns...

            if (serializers.length > 0 && (serializers.length != header.length)) {
                throw new IllegalArgumentException("Mismatch in number of columns and serializers.");
            }

            // write the table data...

            for (int r = 0; r < numRows; r++) {
                String[] line = new String[cols.size()];

                for (int c = 0; c < cols.size(); c++) {
                    line[c] = serializers[c].apply(
                            cols.get(c).rows.get(r));
                }
                csvWriter.writeNext(line);
            }
        }
    }

    @SafeVarargs  // 'deserializers' array is only accessed
    public static DataTable fromCsvFile(File file, Function<String, Object>... deserializers)
            throws IOException, CsvValidationException, IllegalArgumentException {

        try (FileReader fileReader = new FileReader(file);
             CSVReader csvReader = new CSVReader(fileReader)) {

            // read the table header...

            String[] header = csvReader.readNext();
            if (header == null) {
                throw new IllegalArgumentException("CSV was empty.");
            }

            // ensure the number of deserializers, if any, match the number of columns...

            if (deserializers.length > 0 && (deserializers.length != header.length)) {
                throw new IllegalArgumentException("Mismatch in number of columns and deserializers.");
            }

            // initialize the data map...

            Map<String, List<Object>> data = new HashMap<>();
            for (String label : header) {
                data.put(label, new ArrayList<>());
            }

            // collect table data...

            String[] line;
            while ((line = csvReader.readNext()) != null) {

                if (line.length != header.length) {
                    throw new IllegalArgumentException("Row sizes are unequal.");
                }
                for (int i = 0; i < line.length; i++) {
                    data.get(header[i]).add(
                            deserializers[i].apply(line[i]));
                }
            }

            // parse columns...

            Set<Column> columns = new HashSet<>();
            for (String label : data.keySet()) {
                columns.add(new Column<>(label, data.get(label)));
            }

            // create and return the data table...

            return new DataTable(file.getName(), columns);
        }
    }
}
