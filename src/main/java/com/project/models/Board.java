package com.project.models;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Board {
    private Map<Integer, Integer> snake = new HashMap<Integer, Integer>();
    private Map<Integer, Integer> ladder = new HashMap<Integer, Integer>();

    public void addSnake(Integer start, Integer end) {
        snake.put(start, end);
    }

    public void addLadder(Integer start, Integer end) {
        ladder.put(start, end);
    }

    public Integer checkSnake(Integer position) {
        if(snake.containsKey(position)) {
            return snake.get(position);
        }
        return 0;
    }

    public Integer checkLadder(Integer position) {
        if(ladder.containsKey(position)) {
            return ladder.get(position);
        }
        return 0;
    }

    public Collection<Map.Entry<Integer, Integer>> viewSnakes() {
        return snake.entrySet();
    }

    public Collection<Map.Entry<Integer, Integer>> viewLadders() {
        return ladder.entrySet();
    }
}
