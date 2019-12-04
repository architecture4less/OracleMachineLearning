/*
 * AUTH: Jared O'Toole
 * DATE: 12/2/2019 12:40 AM
 * PROJ: OracleMachineLearning
 * FILE: TableTestDriver.java
 *
 * "Create an ID3 Tree" task for Section 4 Lesson 4
 * of the AI with ML in Java Oracle iLearning Course.
 *
 * Defines the TableTestDriver driver class.
 */

package me.jwotoole9141.oracleml.s4l4.driver;

import com.opencsv.exceptions.CsvValidationException;
import me.jwotoole9141.oracleml.s4l4.DataTable;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Tests the {@link DataTable} and {@link DataTable.Column} classes.
 *
 * @author Jared O'Toole
 */
public class TableTestDriver {

    /**
     * Runs the test driver.
     *
     * @param args unused command-line args
     */
    public static void main(String[] args)
            throws IOException, CsvValidationException {

        // test loading a table from a CSV file...

        File csv = new File(new File("").getParent(), "res/play_sport.csv");
        DataTable table = DataTable.fromCsvFile(csv);

        System.out.println("\nTesting empty diagram...");
        System.out.println(new DataTable("nill").toDiagram());

        System.out.println("\nTesting toString()...");
        System.out.println(table);

        System.out.println("\nTesting toDiagram()...");
        System.out.println(table.toDiagram());

        // test creating a table programmatically...

        DataTable myTable = new DataTable("res/planets.csv", Arrays.asList(

                new DataTable.Column<>("planet", Arrays.asList(
                        Planet.MERCURY, Planet.VENUS, Planet.EARTH, Planet.MARS,
                        Planet.JUPITER, Planet.SATURN, Planet.URANUS, Planet.NEPTUNE)),

                new DataTable.Column<>("hockey", Arrays.asList(
                        SkillLevel.POOR, SkillLevel.MEDIUM, SkillLevel.HIGH, SkillLevel.MEDIUM,
                        SkillLevel.HIGH, SkillLevel.HIGH, SkillLevel.MEDIUM, SkillLevel.POOR)),

                new DataTable.Column<>("sculpting", Arrays.asList(
                        SkillLevel.NONE, SkillLevel.MEDIUM, SkillLevel.POOR, SkillLevel.POOR,
                        SkillLevel.HIGH, SkillLevel.MEDIUM, SkillLevel.NONE, SkillLevel.EXCELLENT)),

                new DataTable.Column<>("trivia", Arrays.asList(
                        SkillLevel.HIGH, SkillLevel.NONE, SkillLevel.EXCELLENT, SkillLevel.NONE,
                        SkillLevel.POOR, SkillLevel.MEDIUM, SkillLevel.EXCELLENT, SkillLevel.NONE))

        ));
        System.out.println("\nTesting creating a table...");
        System.out.println(myTable.toDiagram());

        @SuppressWarnings("unchecked")
        DataTable.Column<SkillLevel> planetsCol = (DataTable.Column<SkillLevel>) myTable.getColumns().get(0);

        System.out.println("\nTesting col.toDiagram()");
        System.out.println(planetsCol.toDiagram());

        // test saving a table to a CSV file...

        myTable.toCsv();

        myTable.toCsv(
                Object::toString,
                Object::toString,
                Object::toString,
                Object::toString);

        File myCsv = new File(new File("").getParent(), myTable.getTitle());
        DataTable reloadedTable = DataTable.fromCsvFile(myCsv,
                Planet::valueOf,
                SkillLevel::valueOf,
                SkillLevel::valueOf,
                SkillLevel::valueOf);

        System.out.println("\nTesting saving to file and reloading... ");
        System.out.println(reloadedTable.toDiagram());

        // test various ways of getting sub-views...

        DataTable excellence = myTable.toSubTable(c -> c.equals(planetsCol)
                || c.getValues().contains(SkillLevel.EXCELLENT));

        System.out.println("\nTesting toSubTable(columnFilter)...");
        System.out.println(excellence.toDiagram());

        @SuppressWarnings({ "unchecked", "OptionalGetWithoutIsPresent" })
        DataTable.Column<SkillLevel> triviaCol = (DataTable.Column<SkillLevel>) myTable.getColumns().stream()
                .filter(c -> c.getLabel().equals("trivia")).findFirst().get();

        DataTable poorAtTrivia = myTable.toSubTable(triviaCol, e -> e.equals(SkillLevel.NONE));

        System.out.println("\nTesting toSubTable(filterColumn, rowFilter)...");
        System.out.println(poorAtTrivia.toDiagram());

        DataTable subView = myTable.toSubTable(Arrays.asList(0, 1, 2), Arrays.asList(0, 1, 2, 3, 6, 7));

        System.out.println("\nTesting toSubTable(colIndices, rowIndices)...");
        System.out.println(subView.toDiagram());
    }

    /**
     * An enumeration of discrete labels for
     * different levels of skill.
     */
    enum SkillLevel {

        NONE, POOR, MEDIUM, HIGH, EXCELLENT
    }

    /**
     * An enumeration of the eight planets.
     */
    enum Planet {

        MERCURY(0.39, 0.378, 10e-15),
        VENUS(0.723, 0.91, 90.73),
        EARTH(1, 1.00, 1.0),
        MARS(1.524, 0.379, 0.006),
        JUPITER(5.203, 2.53, Double.NaN),
        SATURN(9.539, 1.07, Double.NaN),
        URANUS(19.18, 0.91, Double.NaN),
        NEPTUNE(30.06, 1.14, Double.NaN);

        private double distFromSun;
        private double surfGravity;
        private double surfPressure;

        Planet(double d, double g, double p) {
            this.distFromSun = d;
            this.surfGravity = g;
            this.surfPressure = p;
        }

        /** Gets this planet's average distance from The Sun in AUs. */
        public double getDistFromSun() {
            return distFromSun;
        }

        /** Gets this planet's surface gravity in Gs. */
        public double getSurfGravity() {
            return surfGravity;
        }

        /** Roughly gets this planets surface pressure in Atms. */
        public double getSurfPressure() {
            return surfPressure;
        }
    }
}
