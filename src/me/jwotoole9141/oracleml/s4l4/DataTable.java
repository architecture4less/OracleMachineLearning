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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Represents a table of data, organized by <i>column</i>, and
 * serializable with CSV format. A table will have a label and
 * any number of columns, each with separate data types.
 *
 * @author Jared O'Toole
 * @see DataTable.Column
 */
public class DataTable {

    /**
     * Represents a column of data in a {@link DataTable}. A column
     * will have a label and rows of data, all of the same type.
     *
     * @param <T> the type of data held in this column
     * @author Jared O'Toole
     */
    public static class Column<T> {

        private final @NotNull String label;
        private final @NotNull List<@NotNull T> rows = new ArrayList<>();
        private final @NotNull Set<@NotNull T> values = new HashSet<>();
        private final @NotNull List<@NotNull T> rowsView = Collections.unmodifiableList(rows);
        private final @NotNull Set<@NotNull T> valuesView = Collections.unmodifiableSet(values);

        /**
         * Creates an empty column with the given label.
         *
         * @param label the label of this column
         */
        public Column(@NotNull String label) {
            this(label, null);
        }

        /**
         * Creates a column with the given label and data.
         *
         * @param label the label of this column
         * @param rows  the data held by this column
         */
        public Column(@NotNull String label, @Nullable List<T> rows) {
            this.label = label;
            if (rows != null) {
                this.rows.addAll(rows);
                this.values.addAll(rows);
            }
        }

        /**
         * Gets the label for this column.
         *
         * @return a brief name or phrase describing this column's contents
         */
        public @NotNull String getLabel() {
            return label;
        }

        /**
         * Gets a view of the rows of data under this column.
         *
         * @return the data elements
         */
        public @NotNull List<T> getRows() {
            return rowsView;
        }

        /**
         * Gets the complete set of unique values under this column.
         *
         * @return a unique set of every possible data value
         */
        public @NotNull Set<T> getValues() {
            return valuesView;
        }

        /**
         * Gets the percent distribution of every data value under this column.
         * Some of this column's rows shall have been filtered out.
         *
         * @return a map of the unique data values to their fraction of the whole column
         */
        public @NotNull Map<T, Double> getDistribution() {
            return getDistribution(values);
        }

        /**
         * Gets a percent distribution for the given data values under this column.
         *
         * @param values the set of data values to get a distribution for
         * @return a map of the data values to their fraction of the whole set
         */
        public @NotNull Map<T, Double> getDistribution(@NotNull Set<T> values) {

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

        /**
         * Creates a new column that represents a <i>sub-vew</i> of this column's rows.
         * Some of this column's rows shall have been filtered out.
         *
         * @param filter a function that takes data from this column's rows and
         *               returns true if that row should be included in the <i>sub-vew</i>
         * @return a new column with a subset of the same data
         */
        public @NotNull Column<T> toSubColumn(@NotNull Predicate<T> filter) {
            return new Column<>(label, IntStream.range(0, rows.size())
                    .filter(i -> filter.test(rows.get(i)))
                    .boxed()
                    .map(rows::get)
                    .collect(Collectors.toList()));
        }

        /**
         * Creates a new column that represents a <i>sub-vew</i> of this column's rows.
         * Some of this column's rows shall have been filtered out.
         *
         * @param indices the indices of the rows that should be included in the <i>sub-vew</i>
         * @return a new column with a subset of the same data
         */
        public @NotNull Column<T> toSubColumn(@NotNull List<Integer> indices) {
            return new Column<>(label, indices.stream()
                            .map(rows::get)
                            .collect(Collectors.toList()));
        }

        /**
         * Generates a list of indices that match the given filter.
         *
         * @param filter a function that takes a row and returns true
         *               if its index should be included in the list
         * @return a list of indices for rows under this column
         */
        public @NotNull List<Integer> toSubColumnIndices(@NotNull Predicate<T> filter) {
            return IntStream.range(0, rows.size())
                    .filter(i -> filter.test(rows.get(i)))
                    .boxed()
                    .collect(Collectors.toList());
        }

        /**
         * Creates a string representation of this column.
         *
         * @return an informative, single-line string
         */
        @Override
        public @NotNull String toString() {
            return String.format("Column[label=%s, rows=%d, values={%s}]",
                    label, rows.size(), values.stream()
                            .limit(20)
                            .map(T::toString)
                            .collect(Collectors.joining(", ")));
        }
    }

    private final @NotNull String title;
    private final int numCols;
    private final int numRows;
    private final int size;

    private final @NotNull List<@NotNull Column> cols = new ArrayList<>();
    private final @NotNull List<@NotNull Column> colsView = Collections.unmodifiableList(cols);

    /**
     * Creates an empty data table with the given title.
     *
     * @param title the title of this table
     */
    public DataTable(@NotNull String title) {
        this(title, null);
    }

    /**
     * Creates a data table with the given title and columns.
     * The number of rows of each column <b><i>must</i></b> match.
     *
     * @param title   the title of this table
     * @param columns the columns of this table (with their data)
     * @throws IllegalArgumentException if the number of rows between
     *                                  all columns are not equal
     */
    public DataTable(@NotNull String title, @Nullable List<Column> columns) {

        this.title = title;

        if (columns != null) {
            this.cols.addAll(columns);
        }
        if (this.cols.isEmpty()) {
            this.numCols = 0;
            this.numRows = 0;
            this.size = 0;
        }
        else {
            this.numCols = this.cols.size();
            this.numRows = this.cols.get(0).rows.size();
            this.size = numCols * numRows;

            for (Column col : this.cols) {
                if (col.rows.size() != numRows) {
                    throw new IllegalArgumentException("Row sizes are unequal.");
                }
            }
        }
    }

    /**
     * Gets the title of this table. This will serve as the filename
     * when serializing the table's data with CSV.
     *
     * @return a brief name or phrase describing this table's contents
     */
    public @NotNull String getTitle() {
        return title;
    }

    /**
     * Gets a view of the table's columns.
     *
     * @return an unmodifiable list
     */
    public @NotNull List<Column> getColumns() {
        return colsView;
    }

    /**
     * Gets the number of columns in this table. This has the same
     * effect as {@link #getColumns()}{@link List#size() .size()}
     *
     * @return the number of table columns
     */
    public int getNumCols() {
        return numCols;
    }

    /**
     * Gets the number of rows in this table, across any and all columns.
     * This has the same effect as calling {@link #getColumns()}
     * {@link List#get(int) .get(x)}{@link Column#getRows() .getRows()}
     * {@link List#size() .size()}, where {@code x} is a valid index.
     *
     * @return the number of table rows
     */
    public int getNumRows() {
        return numRows;
    }

    /**
     * Gets the total number of elements, or <i>cells</i>, in this table, across all columns.
     * This has the same effect as {@link #getNumCols()} {@code *}
     * {@link #getNumRows()}
     *
     * @return the number of table cells
     */
    public int getNumCells() {
        return size;
    }

    /**
     * Creates a new table that represents a <i>sub-view</i> of this table.
     * Some of this table's columns shall have been filtered out.
     *
     * @param columnFilter a function that takes a column and returns
     *                     true if it should be included in the <i>sub-view</i>
     * @return a new table with a subset of the same data
     */
    public @NotNull DataTable toSubTable(@NotNull Predicate<Column> columnFilter) {
        return new DataTable(title, cols.stream()
                .filter(columnFilter)
                .collect(Collectors.toList()));
    }

    /**
     * Creates a new table that represents a <i>sub-view</i> of this table.
     * Some of this table's rows shall have been filtered out.
     *
     * @param filterColumn the column whose data will supply the {@code rowFilter}
     * @param rowFilter    a function that takes data from the rows of {@code column} and
     *                     returns true if that row should be included in the <i>sub-view</i>
     * @param <T>          the type of data held by {@code column}
     * @return a new table with a subset of the same data
     */
    public <T> @NotNull DataTable toSubTable(
            @NotNull Column<T> filterColumn, @NotNull Predicate<T> rowFilter) {

        if (cols.contains(filterColumn)) {
            List<Integer> indices = filterColumn.toSubColumnIndices(rowFilter);
            return new DataTable(title, cols.stream()
                    .map(c -> c.toSubColumn(indices))
                    .collect(Collectors.toList()));
        }
        return this;
    }

    /**
     * Creates a new table that represents a <i>sub-view</i> of this table.
     * Some of this table's rows and/or columns shall have been filtered out.
     *
     * @param colIndices the indices of the columns that should be included in the <i>sub-vew</i>
     * @param rowIndices the indices of the rows that should be included in the <i>sub-vew</i>
     * @return a new table with a subset of the same data
     */
    public @NotNull DataTable toSubTable(
            @Nullable List<Integer> colIndices, @Nullable List<Integer> rowIndices) {

        List<Column> columns = new ArrayList<>();

        IntStream colRange = (colIndices == null)
                ? IntStream.range(0, numCols)
                : IntStream.of(colIndices.stream().mapToInt(Integer::intValue).toArray());

        colRange.boxed()
                .map(cols::get)
                .map(c -> (rowIndices == null) ? c : c.toSubColumn(rowIndices))
                .forEach(columns::add);

        return new DataTable(title, columns);
    }

    /**
     * Creates a string representation of this table.
     *
     * @return an informative, single-line string
     */
    @Override
    public @NotNull String toString() {
        return String.format("DataTable[size=%d, cols={%s}]",
                size, cols.stream()
                        .map(Column::toString)
                        .collect(Collectors.joining(", ")));
    }

    /**
     * Serializes this table to a CSV file. The table's title
     * is used as the name of the file being written.
     *
     * @param serializers an optional array of functions, one per column, that each
     *                    take a datum from that column's rows and serialize it into
     *                    a string for the CSV file. If left out, all data is simply
     *                    converted into a string using {@link Objects#toString()}
     * @throws IllegalArgumentException if the number of serializers is not zero but
     *                                  does not match the number of columns
     * @throws IOException              if the file could not be created or modified
     */
    @SafeVarargs
    public final void toCsv(@NotNull Function<Object, String>... serializers)
            throws IllegalArgumentException, IOException {

        toCsv(new File(title), serializers);
    }

    /**
     * Serializes this table to a CSV file.
     *
     * @param file        the file to write
     * @param serializers an optional array of functions, one per column, that each
     *                    take a datum from that column's rows and serialize it into
     *                    a string for the CSV file. If unused, all data is simply
     *                    converted into a string using {@link Objects#toString()}
     * @throws IllegalArgumentException if the number of serializers is not zero but
     *                                  does not match the number of columns
     * @throws IOException              if the file could not be created or modified
     */
    @SafeVarargs // 'serializers' array is only accessed
    public final void toCsv(@NotNull File file, @NotNull Function<Object, String>... serializers)
            throws IllegalArgumentException, IOException {

        try (FileWriter fileWriter = new FileWriter(file);
             CSVWriter csvWriter = new CSVWriter(fileWriter)) {

            // write the table header...

            String[] header = new String[numCols];
            for (int i = 0; i < numCols; i++) {
                header[i] = cols.get(i).label;
            }
            csvWriter.writeNext(header);

            // ensure the number of serializers, if any, match the number of columns...

            if (serializers.length > 0 && (serializers.length != header.length)) {
                throw new IllegalArgumentException(String.format(
                        "Mismatch in number of columns and serializers. (%d and %d)",
                        header.length, serializers.length));
            }

            // write the table data...

            for (int r = 0; r < numRows; r++) {
                String[] line = new String[numCols];

                for (int c = 0; c < numCols; c++) {
                    line[c] = (serializers.length == 0)
                            ? cols.get(c).rows.get(r).toString()
                            : serializers[c].apply(cols.get(c).rows.get(r));
                }
                csvWriter.writeNext(line);
            }
        }
    }

    /**
     * Deserializes a CSV file into a new data table.
     *
     * @param file          the CSV file to read
     * @param deserializers an optional array of functions, one per column, that each
     *                      take a string and deserialize it into the desired data type
     *                      for the column. If unused, all data simply remains a string.
     * @return a new table representing the CSV file's data
     *
     * @throws IOException              if the file could not be found or read
     * @throws CsvValidationException   if the file contained invalid CSV
     * @throws IllegalArgumentException if the number of deserializers is not zero but
     *                                  does not match the number of columns OR if the
     *                                  number of columns per line do not all match
     */
    @SafeVarargs  // 'deserializers' array is only accessed
    public static @NotNull DataTable fromCsvFile(
            @NotNull File file, @NotNull Function<String, Object>... deserializers)
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
                throw new IllegalArgumentException(String.format(
                        "Mismatch in number of columns and deserializers. (%d and %d)",
                        header.length, deserializers.length));
            }

            // initialize the data map...

            Map<String, List<Object>> data = new HashMap<>();
            for (String label : header) {
                data.put(label, new ArrayList<>());
            }

            // collect table data...

            String[] line;
            while ((line = csvReader.readNext()) != null) {

                if (line.length != 0 && line.length != header.length) {
                    throw new IllegalArgumentException(String.format(
                            "Mismatch in number of columns and width of row (%d and %d).",
                            line.length, header.length));
                }
                for (int i = 0; i < line.length; i++) {
                    data.get(header[i]).add(
                            (deserializers.length == 0)
                                    ? line[i]
                                    : deserializers[i].apply(line[i]));
                }
            }

            // parse columns...

            List<Column> columns = new ArrayList<>();
            for (String label : data.keySet()) {
                columns.add(new Column<>(label, data.get(label)));
            }

            // create and return the data table...

            return new DataTable(file.getName(), columns);
        }
    }
}
