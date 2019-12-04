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

public class TableTestDriver {

    public static void main(String[] args)
            throws IOException, CsvValidationException {

        File csv = new File(new File("").getParent(), "res/play_sport.csv");
        DataTable table = DataTable.fromCsvFile(csv);

        System.out.println("\nTesting toString()...");
        System.out.println(table);

        System.out.println("\nTesting toDiagram()...");
        System.out.println(table.toDiagram());
    }
}
