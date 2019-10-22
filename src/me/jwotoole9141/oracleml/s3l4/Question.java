/*
 * AUTH: Jared O'Toole
 * DATE: Sat Oct 19th, 2019
 * PROJ: OracleMachineLearning
 * FILE: Question.java
 *
 * "Create a Yes/No Guessing Game" task for Section 4 Lesson 1
 * of the AI with ML in Java Oracle iLearning Course.
 *
 * Defines the question tree node class.
 */

package me.jwotoole9141.oracleml.s3l4;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class Question {

    private boolean last;
    private @NotNull String prompt;
    private @Nullable Question yes;
    private @Nullable Question no;

    public Question(boolean isLastQuestion, @NotNull String promptText) {
        last = isLastQuestion;
        prompt = promptText;
    }

    public boolean isLast() {
        return last;
    }

    public @NotNull String getPrompt() {
        return prompt;
    }

    public @Nullable Question getYes() {
        return yes;
    }

    public @Nullable Question getNo() {
        return no;
    }

    public void setYes(@Nullable Question yesAnswer) {
        this.yes = yesAnswer;
    }

    public void setNo(@Nullable Question noAnswer) {
        this.no = noAnswer;
    }

    public @Nullable Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("prompt", prompt);
        map.put("last", last);
        map.put("yes", yes);
        map.put("no", no);
        return map;
    }

    public static @Nullable Question fromMap(@Nullable Map<?, ?> map) throws ClassCastException {
        if (map == null) {
            return null;
        }
        Question node = new Question((Boolean) map.get("last"), (String) map.get("prompt"));
        node.setYes(fromMap((Map<?, ?>) map.get("yes")));
        node.setNo(fromMap((Map<?, ?>) map.get("no")));
        return node;
    }
}
