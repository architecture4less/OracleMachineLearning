/*
 * AUTH: Jared O'Toole
 * DATE: Wed Oct 28th, 2019
 * PROJ: OracleMachineLearning
 * FILE: TestQuestions.java
 *
 * "Create a Yes/No Guessing Game" task for Section 4 Lesson 1
 * of the AI with ML in Java Oracle iLearning Course.
 *
 * Defines the TestQuestions test driver class
 */

package me.jwotoole9141.oracleml.s3l4;

import org.javatuples.Pair;

import java.util.Objects;

/**
 * Tests the Questions class.
 *
 * @author Jared O'Toole
 */
public class TestQuestions {

    /**
     * Displays the behavior of the Question class.
     *
     * @param args unused command-line args
     */
    public static void main(String[] args) {

        Pair<Relation, String> qaPair = Relation.parse("It doesn't involve other people");
        Objects.requireNonNull(qaPair);

        Relation relation = qaPair.getValue0();
        String subject = qaPair.getValue1();

        Question question = new Question(relation, subject);

        System.out.println(question.getAnswer(true, "it"));
    }
}
