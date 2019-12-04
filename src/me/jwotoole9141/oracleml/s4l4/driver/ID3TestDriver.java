/*
 * AUTH: Jared O'Toole
 * DATE: 12/1/2019 8:48 PM
 * PROJ: OracleMachineLearning
 * FILE: ID3TestDriver.java
 *
 * "Create an ID3 Tree" task for Section 4 Lesson 4
 * of the AI with ML in Java Oracle iLearning Course.
 *
 * Defines the ID3TestDriver driver class.
 */

package me.jwotoole9141.oracleml.s4l4.driver;

import me.jwotoole9141.oracleml.s4l4.DataTable;
import me.jwotoole9141.oracleml.s4l4.Tree;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

public class ID3TestDriver {

    public static void main(String[] args) {

        // testing the entropy function...

        {
            Set<Boolean> successCriteria = Collections.singleton(true);

            DataTable.Column<Boolean> results = new DataTable.Column<>("test passed",
                    Arrays.asList(true, false, true, true, false, false, true, false, false, false));

            double entropy = Tree.Algorithm.entropy(results, successCriteria);

            System.out.println("\nTesting Tree.Aglorithm.entropy()...");
            System.out.println(results.toDiagram());
            System.out.println("The entropy of the above column is: " + entropy);
        }
        {
            Set<CoinFlip> successCriteria = Collections.singleton(CoinFlip.HEADS);
            DataTable.Column<CoinFlip> results = new DataTable.Column<>("coin flip",
                    Arrays.asList(CoinFlip.HEADS, CoinFlip.TAILS, CoinFlip.TAILS, CoinFlip.TAILS));

            double entropy = Tree.Algorithm.entropy(results, successCriteria);

            System.out.println("\nTesting Tree.Aglorithm.entropy()...");
            System.out.println(results.toDiagram());
            System.out.println("The entropy of the above column is: " + entropy);
        }
    }

    public enum CoinFlip {HEADS, TAILS}
}
