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

import com.opencsv.exceptions.CsvValidationException;
import me.jwotoole9141.oracleml.s4l4.DataTable;
import me.jwotoole9141.oracleml.s4l4.Node;
import me.jwotoole9141.oracleml.s4l4.Tree;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ID3TestDriver {

    public static void main(String[] args) throws IOException, CsvValidationException {

        // create sample data...

        Set<Integer> studyTrueCriteria = IntStream.range(1, 24).boxed().collect(Collectors.toSet());
        DataTable.Column<Integer> studyHoursCol = new DataTable.Column<>("hours studied",
                Arrays.asList(0, 0, 0, 0));

        Set<CoinFlip> coinTrueCriteria = Collections.singleton(CoinFlip.HEADS);
        DataTable.Column<CoinFlip> coinFlipCol = new DataTable.Column<>("coin flip",
                Arrays.asList(CoinFlip.HEADS, CoinFlip.TAILS, CoinFlip.TAILS, CoinFlip.TAILS));

        Set<Character> testTrueCriteria = new HashSet<>(Arrays.asList('A', 'B', 'C'));
        DataTable.Column<Character> testGradeCol = new DataTable.Column<>("test grade",
                Arrays.asList('C', 'B', 'F', 'D'));

        DataTable table = new DataTable("Study Hours vs Coin Toss vs Test Grade",
                Arrays.asList(studyHoursCol, coinFlipCol, testGradeCol));

        System.out.println(table.toDiagram());

        // testing the entropy function...

        System.out.println("Testing Tree.Aglorithm.entropy()...");

        double studyHoursEntropy = Tree.Algorithm.entropy(studyHoursCol, studyTrueCriteria);
        System.out.printf("The entropy of '%s' is: %f\n", studyHoursCol.getLabel(), studyHoursEntropy);

        double coinFlipEntropy = Tree.Algorithm.entropy(coinFlipCol, coinTrueCriteria);
        System.out.printf("The entropy of '%s' is: %f\n", coinFlipCol.getLabel(), coinFlipEntropy);

        double testGradeEntropy = Tree.Algorithm.entropy(testGradeCol, testTrueCriteria);
        System.out.printf("The entropy of '%s' is: %f\n", testGradeCol.getLabel(), testGradeEntropy);

        // testing the gain function...

        System.out.println("\nTesting Tree.Algorithm.gain()...");

        double hoursGainOnGrade = Tree.Algorithm.gain(testGradeEntropy, studyHoursCol, testGradeCol, testTrueCriteria);
        System.out.printf("The gain of '%s' on '%s' is: %f\n",
                studyHoursCol.getLabel(), testGradeCol.getLabel(), hoursGainOnGrade);

        double flipGainOnGrade = Tree.Algorithm.gain(testGradeEntropy, coinFlipCol, testGradeCol, testTrueCriteria);
        System.out.printf("The gain of '%s' on '%s' is: %f\n",
                coinFlipCol.getLabel(), testGradeCol.getLabel(), flipGainOnGrade);

        // testing the id3 algorithm...

        File csv = new File(new File("").getParent(), "res/play_sport.csv");
        DataTable playSportTable = DataTable.fromCsvFile(csv);

        Function[] toAnswerByCol = new Function[playSportTable.getNumCols()];
        for (int i = 0; i < toAnswerByCol.length; i++) {
            toAnswerByCol[i] = Object::toString;
        }

        //noinspection unchecked
        Node<String, String> playSportTree = Tree.fromTable(
                playSportTable,
                playSportTable.getNumCols() - 1,  // FIXME index out of bounds exception
                Collections.singleton("No"),
                Tree.Algorithm.ID3,
                Object::toString,
                toAnswerByCol);

        System.out.println("\nTesting Tree.fromTable()...");
        System.out.println(playSportTable.toDiagram());
    }

    public enum CoinFlip {HEADS, TAILS}
}
