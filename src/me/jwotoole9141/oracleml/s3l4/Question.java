/*
 * AUTH: Jared O'Toole
 * DATE: Sat Oct 19th, 2019
 * PROJ: OracleMachineLearning
 * FILE: Question.java
 *
 * "Create a Yes/No Guessing Game" task for Section 4 Lesson 1
 * of the AI with ML in Java Oracle iLearning Course.
 *
 * Define the tree node class.
 */

package me.jwotoole9141.oracleml.s3l4;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Question {

    @NotNull private String prompt;
    private boolean last;

    @Nullable private Question yes;
    @Nullable private Question no;

    public Question(@NotNull String promptText, boolean isBaseCase) {
        prompt = promptText;
        last = isBaseCase;
    }

    public boolean isLast() {
        return last;
    }

    @NotNull
    public String getPrompt() {
        return prompt;
    }

    @Nullable
    public Question getYes() {
        return yes;
    }

    @Nullable
    public Question getNo() {
        return no;
    }

    public void setYes(@Nullable Question yesAnswer) {
        this.yes = yesAnswer;
    }

    public void setNo(@Nullable Question noAnswer) {
        this.no = noAnswer;
    }
}
