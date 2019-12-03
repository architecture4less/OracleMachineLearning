/*
 * AUTH: Jared O'Toole
 * DATE: 12/1/2019 11:34 PM
 * PROJ: OracleMachineLearning
 * FILE: Node.java
 *
 * "Create an ID3 Tree" task for Section 4 Lesson 4
 * of the AI with ML in Java Oracle iLearning Course.
 *
 * Defines the Node class.
 */

package me.jwotoole9141.oracleml.s4l4;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Represents a branch node in a non-binary tree. Branch nodes have
 * both a <i>question</i> and a map of children to various <i>answers</i>.
 *
 * @param <Q> The <i>question</i> type in this node's heirarchy
 * @param <A> The <i>answer</i> type in this node's heirarchy
 * @author Jared O'Toole
 * @see OuterNode
 */
public class InnerNode<Q, A> extends Node<Q, A> {

    protected @NotNull Q question;
    protected @NotNull ObservableMap<@NotNull A, @NotNull Node<Q, A>> children;

    /**
     * Creates a new node with the given <i>question</i>.
     *
     * @param question the premise of the node
     */
    public InnerNode(@NotNull Q question) {
        this(question, null);
    }

    /**
     * Creates a new node with the given <i>question</i> and <i>answers</i>.
     *
     * @param question the premise of the node
     * @param children a map of <i>answers</i> to <i>follow-up questions</i>
     */
    public InnerNode(@NotNull Q question, @Nullable Map<@NotNull A, @NotNull Node<Q, A>> children) {

        super();
        this.question = question;
        this.children = FXCollections.observableHashMap();

        // map listener sets or removes the parent reference of child nodes...

        this.children.addListener((MapChangeListener<A, Node<Q, A>>) change -> {

            if (change.wasAdded()) {
                Node<Q, A> child = change.getValueAdded();
                child.updateParent(this, change.getKey());
            }
            if (change.wasRemoved()) {
                Node<Q, A> child = change.getValueRemoved();
                child.updateParent(null, null);
            }
        });

        if (children != null) {
            this.children.putAll(children);
        }
    }

    /**
     * Gets an object that represents the premise for
     * how to choose an <i>answer</i> from this node.
     *
     * @return the node's premise
     */
    public @NotNull Q getQuestion() {
        return question;
    }

    /**
     * Gets the children of this node. Child nodes are
     * accessed with a key that represents the <i>answer</i>
     * to this node's premise.
     *
     * @return a mapping of <i>answers</i> to child nodes
     */
    public @NotNull Map<@NotNull A, @NotNull Node<Q, A>> getChildren() {
        return children;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSize() {
        return 1 + children.values().stream()
                .map(Node::getSize)
                .mapToInt(Integer::intValue)
                .sum();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String toString() {
        return String.format("InnerNode[question=%s, answers={%s}]",
                question.toString(),
                children.keySet().stream()
                        .map(A::toString)
                        .collect(Collectors.joining(", "))
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Map<String, Object> toMap(
            @NotNull Function<Q, Map<String, Object>> questionToMap,
            @NotNull Function<A, String> answerToStr) {

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> subMap = new HashMap<>();

        for (Map.Entry<A, Node<Q, A>> entry : children.entrySet()) {
            subMap.put(answerToStr.apply(entry.getKey()),
                    entry.getValue().toMap(questionToMap, answerToStr));
        }
        map.put("question", questionToMap.apply(question));
        map.put("children", subMap);

        return map;
    }

    @Override
    protected @NotNull String toDiagram(@NotNull String prefix, @NotNull String branch) {

        StringBuilder result = new StringBuilder(prefix + "\\--> "
                + (getParentAnswer() == null ? " " : "[" + getParentAnswer().toString() + "] ")
                + getQuestion().toString() + "\n");

        int i = children.size();
        String newPrefix = prefix + branch;

        for (Node<Q, A> child : children.values()) {
            result.append(child.toDiagram(newPrefix, --i > 0 ? "|    " : "    "));
        }
        return result.toString();
    }
}
