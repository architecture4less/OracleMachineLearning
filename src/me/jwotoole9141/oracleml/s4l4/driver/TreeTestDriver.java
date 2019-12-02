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

import me.jwotoole9141.oracleml.s4l4.InnerNode;
import me.jwotoole9141.oracleml.s4l4.Node;
import me.jwotoole9141.oracleml.s4l4.OuterNode;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Tests the {@link Node}, {@link InnerNode}, and {@link OuterNode} classes.
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

        InnerNode<String, String> tree = new InnerNode<>("Outlook?");
        tree.getChildren().put("Sunny", new InnerNode<>("Humidity?"));
        tree.getChildren().put("Overcast", new OuterNode<>("Yes"));
        tree.getChildren().put("Rainy", new InnerNode<>("Wind?"));

        InnerNode<String, String> sunny =
                (InnerNode<String, String>) tree.getChildren().get("Sunny");
        sunny.getChildren().put("High", new OuterNode<>("No"));
        sunny.getChildren().put("Normal", new OuterNode<>("Yes"));

        InnerNode<String, String> rainy =
                (InnerNode<String, String>) tree.getChildren().get("Rainy");
        rainy.getChildren().put("Strong", new OuterNode<>("No"));
        rainy.getChildren().put("Weak", new OuterNode<>("Yes"));

        // test functionality of the tree...

        System.out.println("\nTesting tree.toString()...");
        System.out.println(tree.toString());

        System.out.println("\nTesting tree.toDiagram()...");
        System.out.println(tree.toDiagram());

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

        InnerNode<String, String> newTree =
                (InnerNode<String, String>) Node.fromMap(newTreeMap, questionFromMap, Object::toString);

        System.out.println("\nTesting Node.fromMap()...");
        System.out.println(new JSONObject(tree
                .toMap(questionToMap, Objects::toString))
                .toJSONString());
    }
}
