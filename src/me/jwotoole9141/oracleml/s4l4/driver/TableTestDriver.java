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

public class TableTestDriver {

    public static void main(String[] args)
            throws IOException, CsvValidationException {

        // test loading a table from a CSV file...

        File csv = new File(new File("").getParent(), "res/play_sport.csv");
        DataTable table = DataTable.fromCsvFile(csv);

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
        System.out.println("Testing creating a table...");
        System.out.println(myTable.toDiagram());

        // test saving a table to a CSV file...

        // test various ways of getting sub-views...

    }

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
