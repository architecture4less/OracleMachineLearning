/*
 * AUTH: Jared O'Toole
 * DATE: 12/2/2019 6:12 PM
 * PROJ: OracleMachineLearning
 * FILE: DataAttr.java
 *
 * "Create an ID3 Tree" task for Section 4 Lesson 4
 * of the AI with ML in Java Oracle iLearning Course.
 *
 * Defines the DataAttr interface.
 */

package me.jwotoole9141.oracleml.s4l4;

import java.util.List;

public interface DataAttr<O extends DataOutcome> {

    List<O> getOutcomes();
}
