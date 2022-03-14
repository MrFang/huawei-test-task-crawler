package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

class Trie {
    private final Node root = new Node();

    @Contract(mutates = "this")
    public boolean add(@NotNull String element) {
        var trail = new Stack<Node>();
        trail.add(root);

        for (char c : element.toCharArray()) {
            var current = trail.peek();

            if (!current.children.containsKey(c)) {
                current.children.put(c, new Node());
            }

            trail.add(current.children.get(c));
        }


        if (trail.peek().isTerminal) {
            return false;
        }

        trail.peek().isTerminal = true;

        for (Node n : trail) {
            n.terminalStringsCount += 1;
        }

        return true;
    }

    @Contract(pure = true)
    public boolean contains(@NotNull String element) {
        if (size() == 0) {
            return false;
        }

        Node current = root;

        for (char c : element.toCharArray()) {
            if (!current.children.containsKey(c)) {
                return false;
            }

            current = current.children.get(c);
        }

        return current.isTerminal;
    }

    @Contract(mutates = "this")
    public boolean remove(@NotNull String element) {
        if (size() == 0) {
            return false;
        }

        var trail = new Stack<Node>();
        trail.add(root);

        for (char c : element.toCharArray()) {
            var current = trail.peek();

            if (!current.children.containsKey(c)) {
                return false;
            }

            trail.add(current.children.get(c));
        }

        if (!trail.peek().isTerminal) {
            return false;
        }

        var end = trail.pop();
        end.isTerminal = false;
        end.terminalStringsCount -= 1;

        var cleanup = end.children.size() == 0;

        for (char c : reverseString(element).toCharArray()) {
            var top = trail.pop();
            var children = top.children;

            if (cleanup) {
                children.remove(c);
            }

            top.terminalStringsCount -= 1;

            if (children.size() > 0 || top.isTerminal) {
                cleanup = false;
            }
        }

        return true;
    }

    @Contract(pure = true)
    public int size() {
        return root.terminalStringsCount;
    }

    @NotNull
    @Contract(pure = true)
    public List<String> allWithPrefix(@NotNull String prefix) {
        Node current = root;

        for (char c : prefix.toCharArray()) {
            if (!current.children.containsKey(c)) {
                return new ArrayList<>();
            }

            current = current.children.get(c);
        }

        return getSuffixesFrom(current)
            .stream()
            .map(s -> prefix + s)
            .toList();
    }

    @NotNull
    @Contract(pure = true)
    private List<String> getSuffixesFrom(@NotNull Node start) {
        List<String> result = new ArrayList<>();

        if (start.isTerminal) {
            result.add("");
        }

        for (char c : start.children.keySet()) {
            result.addAll(
                getSuffixesFrom(start.children.get(c))
                    .stream()
                    .map(s -> c + s)
                    .toList()
            );
        }

        return result;
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    private String reverseString(@NotNull String s) {
        return new StringBuilder(s).reverse().toString();
    }

    private static class Node {
        private final Map<Character, Node> children = new HashMap<>();
        private boolean isTerminal = false;
        private int terminalStringsCount = 0;
    }
}
