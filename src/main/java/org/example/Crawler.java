package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Crawler {
    private final Map<String, Integer> pageRegistry = new HashMap<>();
    private final Map<String, Set<String>> links = new HashMap<>();
    private final Trie trie = new Trie();

    // add newly found page url
    @Contract(mutates = "this")
    public void addUrl(@NotNull String rootUrl) throws IllegalArgumentException {
        if (pageRegistry.containsKey(rootUrl)) {
            throw new IllegalArgumentException("Url already presented");
        }

        pageRegistry.put(rootUrl, 0);
        links.put(rootUrl, new HashSet<>());
        trie.add(rootUrl);
    }

    // add the content of newly found page url
    @Contract(mutates = "this")
    public void addHyperLink(
        @NotNull String rootUrl,
        @NotNull String childUrl
    ) throws IllegalArgumentException {
        if (!pageRegistry.containsKey(rootUrl)) {
            throw new IllegalArgumentException("Root url not presented");
        }

        if (!pageRegistry.containsKey(childUrl)) {
            addUrl(childUrl);
        }

        pageRegistry.merge(childUrl, 1, Integer::sum);
        links.get(rootUrl).add(childUrl);
    }

    // return top 5 most important pages
    @Contract(pure = true)
    public List<String> queryTopFive(String rootUrl) {
        List<String> top = new ArrayList<>();

        for (String url : links.get(rootUrl)) {
            for (int i  = 0; i < top.size(); i++) {
                if (pageRegistry.get(top.get(i)) < pageRegistry.get(url)) {
                    top.add(i, url);
                    break;
                }
            }

            if (top.size() < 5) {
                top.add(url);
            }

            if (top.size() > 5) {
                top.remove(5);
            }
        }

        return top;
    }

    // return a list of pages which urls have same prefix
    @Contract(pure = true)
    public List<String> queryListOfPagesWithGivenPrefix(String prefix) {
        return trie.allWithPrefix(prefix);
    }
}
