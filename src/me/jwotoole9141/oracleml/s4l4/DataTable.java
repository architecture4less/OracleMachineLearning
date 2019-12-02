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

public class DataTable {

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
     *
     * ----------------------------------------
     *
     * Based on these pseudo specs,
     * this system should have:
     *
     * DataAttr - 'question' interface for enum classes { toMap() }
     * DataOutcome - 'answer' interface for enum classes { toMap() }
     * DataTable - boxes a parsed csv file, basically { toCSV(); static fromCSV() }
     *
     * DataTable.Attr - default 'attr' implementation using a string (these could also use enums)
     * DataTable.Outcome - default 'outcome' implementation using a string
     * DataTable.Result - default 'outcome' implementation using a bool
     *
     */
}
