/*
 * AUTH: Jared O'Toole
 * DATE: 12/1/2019 8:46 PM
 * PROJ: OracleMachineLearning
 * FILE: AbsNode.java
 *
 * "Create an ID3 Tree" task for Section 4 Lesson 4
 * of the AI with ML in Java Oracle iLearning Course.
 *
 * Defines the AbsNode class.
 */

package me.jwotoole9141.oracleml.s4l4;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Represents a node in a non-binary tree. It should be cast to
 * either a {@link InnerNode} or a {@link OuterNode} when using.
 *
 * @param <Q> The <i>question</i> type in this node's heirarchy
 * @param <A> The <i>answer</i> type in this node's heirarchy
 * @author Jared O'Toole
 */
public abstract class Node<Q, A> {

    protected @NotNull WeakReference<Node<Q, A>> parent;
    protected @NotNull WeakReference<A> parentAnswer;

    protected Node() {

        this.parent = new WeakReference<>(null);
        this.parentAnswer = new WeakReference<>(null);
    }

    /**
     * Gets the parent of this node if it exists.
     * Its reference is held weakly.
     *
     * @return the parent node or null
     */
    public @Nullable Node<Q, A> getParent() {
        return parent.get();
    }

    /**
     * Gets the <i>answer</i> this node is mapped to in its
     * parent if it exists. Its reference is held weakly.
     *
     * @return the parent <i>answer</i> or null
     */
    public @Nullable A getParentAnswer() {
        return parentAnswer.get();
    }

    /**
     * Gets the size of the tree, recursively, starting at this node.
     *
     * @return the number of nodes in the tree from here down
     */
    public abstract int getSize();

    /**
     * Creates a non-recursive string representation of this node.
     *
     * @return an informative, single-line string
     */
    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * Creates a string diagram representing the tree, recursively, starting at this node.
     *
     * @return a visual, multi-line string
     */
    public @NotNull String toDiagram() {
        return toDiagram("", "    ");
    }

    /**
     * Creates a json map representing the tree, recursively, starting at this node.
     *
     * @param questionToMap a function that converts a(n) {@link Q} in the tree to a {@link Map}
     * @param answerToStr   a function that converts a(n) {@link A} in the tree to a {@link String}
     * @return a json-compatible mapping
     */
    public abstract @NotNull Map<String, Object> toMap(
            @NotNull Function<Q, Map<String, Object>> questionToMap,
            @NotNull Function<A, String> answerToStr);

    protected abstract @NotNull String toDiagram(@NotNull String prefix, @NotNull String branch);

    protected void updateParent(Node<Q, A> parent, A answer) {

        this.parent = new WeakReference<>(parent);
        this.parentAnswer = new WeakReference<>(answer);
    }

    /**
     * Creates a node from the given json map.
     *
     * @param mapObj          a json map
     * @param questionFromMap a function that converts a {@link Map} to a(n) {@link Q}
     * @param answerFromStr   a function that converts a {@link String} to a(n) {@link A}
     * @return the node represented by the given json map
     *
     * @throws ClassCastException       unexpected type mapped to a key
     * @throws IllegalArgumentException invalid or missing value in json map
     */
    public static <Q, A> @NotNull Node<Q, A> fromMap(
            @Nullable Object mapObj,
            @NotNull Function<Map<?, ?>, Q> questionFromMap,
            @NotNull Function<String, A> answerFromStr)
            throws IllegalArgumentException, ClassCastException {

        if ((mapObj instanceof Map<?, ?>)) {

            Map<?, ?> map = (Map<?, ?>) mapObj;

            if (map.containsKey("question") && map.containsKey("children")) {

                Q question = questionFromMap.apply((Map<?, ?>) map.get("question"));
                Map<A, Node<Q, A>> children = new HashMap<>();

                for (Map.Entry<?, ?> entry : ((Map<?, ?>) map.get("children")).entrySet()) {
                    A answer = answerFromStr.apply(((String) entry.getKey()));
                    Node<Q, A> child = Node.fromMap(
                            entry.getValue(), questionFromMap, answerFromStr);

                    children.put(answer, child);
                }
                return new InnerNode<>(question, children);
            }
            else if (map.containsKey("answer")) {

                A answer = answerFromStr.apply((String) map.get("answer"));
                return new OuterNode<>(answer);
            }
        }
        throw new IllegalArgumentException("Not enough data to build a node.");
    }
}
