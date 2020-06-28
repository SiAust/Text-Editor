package io.github.siaust;

import javax.swing.*;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchWorker extends SwingWorker<Deque<int[]>, Object> {
    private final String content;
    private final String searchPattern;
    private final boolean regex;

    public SearchWorker(String content, String searchPattern, boolean regex) {
        this.content = content;
        this.searchPattern = searchPattern;
        this.regex = regex;
    }

    @Override
    protected Deque<int[]> doInBackground() throws Exception {
//        System.out.println("regex: " + regex);
        Deque<int[]> deque = new ArrayDeque<>();

        if (!regex) {
            if (content.contains(searchPattern)) {
                int index = content.indexOf(searchPattern);
                do {
                    int[] subsetItem = new int[2];
                    subsetItem[0] = index;
                    subsetItem[1] = index + searchPattern.length();
                    deque.offer(subsetItem);
                    index = content.indexOf(searchPattern, index + 1);
                } while (index > -1);
            }

        } else {
            Pattern pattern = Pattern.compile(searchPattern); // fixme: remove case insensitive?
            Matcher matcher = pattern.matcher(content);
            while (matcher.find()) {
                int[] subsetItem = new int[2];
                subsetItem[0] = matcher.start();
                subsetItem[1] = matcher.end();
                deque.offer(subsetItem);
            }
        }

//        System.out.println("searchPattern: " + searchPattern);
        deque.forEach(o -> System.out.print(Arrays.toString(o) + " "));
//        System.out.println();
        return deque;
    }

    @Override
    protected void done() {
        try {
            EditorGUI.updateFocus(get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        super.done();
    }
}
