/*
 * AUTH: Jared O'Toole
 * DATE: 12/2/2019 12:38 AM
 * PROJ: OracleMachineLearning
 * FILE: TreeTestDriver.java
 *
 * "Create an ID3 Tree" task for Section 4 Lesson 4
 * of the AI with ML in Java Oracle iLearning Course.
 *
 * Defines the TreeTestDriver driver class.
 */

package me.jwotoole9141.oracleml.s4l4.driver;

import me.jwotoole9141.oracleml.s4l4.NodeInner;
import me.jwotoole9141.oracleml.s4l4.Node;
import me.jwotoole9141.oracleml.s4l4.NodeOuter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Tests the {@link Node}, {@link NodeInner}, and {@link NodeOuter} classes.
 *
 * @author Jared O'Toole
 */
public class TreeTestDriver {

    /**
     * Runs the test driver.
     *
     * @param args unused command-line args
     */
    public static void main(String[] args) throws ParseException {

        // functions for converting between a tree and a json map...

        Function<String, Map<String, Object>> questionToMap = q -> {
            Map<String, Object> map = new HashMap<>();
            map.put("value", q);
            return map;
        };

        Function<Map<?, ?>, String> questionFromMap = m -> (String) m.get("value");

        // build the tree...

        String treeQuestion = "Play?";

        NodeInner<String, String> tree = new NodeInner<>("Outlook?");
        tree.getChildren().put("Sunny", new NodeInner<>("Humidity?"));
        tree.getChildren().put("Overcast", new NodeOuter<>("Yes"));
        tree.getChildren().put("Rainy", new NodeInner<>("Wind?"));

        NodeInner<String, String> sunny =
                (NodeInner<String, String>) tree.getChildren().get("Sunny");
        sunny.getChildren().put("High", new NodeOuter<>("No"));
        sunny.getChildren().put("Normal", new NodeOuter<>("Yes"));

        NodeInner<String, String> rainy =
                (NodeInner<String, String>) tree.getChildren().get("Rainy");
        rainy.getChildren().put("Strong", new NodeOuter<>("No"));
        rainy.getChildren().put("Weak", new NodeOuter<>("Yes"));

        // test functionality of the tree...

        System.out.println("\nTesting tree.toString()...");
        System.out.println(tree.toString());

        System.out.println("\nTesting tree.toDiagram()...");
        System.out.println("Play?\n" + tree.toDiagram());

        System.out.println("\nTesting tree.toMap()...");
        System.out.println(new JSONObject(tree
                .toMap(questionToMap, Objects::toString))
                .toJSONString());

        String newTreeJson = "{\"question\":{\"value\":\"Outlook?\"},\"children\":{"
                + "\"Rainy\":{\"question\":{\"value\":\"Wind?\"},\"children\":{"
                + "\"Weak\":{\"answer\":\"Yes\"},"
                + "\"Strong\":{\"answer\":\"No\"}}},"
                + "\"Overcast\":{\"answer\":\"Yes\"},"
                + "\"Sunny\":{\"question\":{\"value\":\"Humidity?\"},\"children\":{"
                + "\"High\":{\"answer\":\"No\"},"
                + "\"Normal\":{\"answer\":\"Yes\"}}}}}";

        Object newTreeMap = new JSONParser().parse(newTreeJson);

        NodeInner<String, String> newTree =
                (NodeInner<String, String>) Node.fromMap(newTreeMap, questionFromMap, Object::toString);

        System.out.println("\nTesting Node.fromMap()...");
        System.out.println(new JSONObject(tree
                .toMap(questionToMap, Objects::toString))
                .toJSONString());

        System.out.println("\nTesting getParent()...");
        System.out.println("sunny's parent: "
                + sunny.getParent());

        System.out.println("\nTesting getParentAnswer()...");
        System.out.println("sunny's parent answer: "
                + sunny.getParentAnswer());

        // System.out.println("\nTesting getQuestion()...");
        // System.out.println("sunny's question: "
        //         + sunny.getQuestion());
        //
        // System.out.println("\nTesting getChild()...");
        // System.out.println("sunny's 'high' answer: "
        //         + sunny.getChildren().get("High"));
        //
        // System.out.println("\nTesting getAnswer()...");
        // System.out.println("sunny & high's answer: "
        //         + ((OuterNode) sunny.getChildren().get("High")).getAnswer());
    }
}
