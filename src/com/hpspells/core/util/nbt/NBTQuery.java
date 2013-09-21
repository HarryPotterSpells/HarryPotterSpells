package com.hpspells.core.util.nbt;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// HPS modifications: removed string parser calls, removed runtime exception messages
public class NBTQuery {

    private List<Object> values = new ArrayList<Object>();

    public List<Object> getValues() {
        return new ArrayList<Object>(values);
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    public NBTQuery getParent() {
        if (values.isEmpty()) return null;
        List<Object> v = getValues();
        v.remove(v.size() - 1);
        return new NBTQuery(v);
    }

    public String getQuery() {
        String s = "";
        for (Object node : values) {
            if (node instanceof Integer) s += "[" + node + "]";
            else s += "." + node;
        }
        if (s.startsWith(".")) s = s.substring(1);
        if (s.isEmpty()) s = ".";
        return s;
    }

    public static NBTQuery fromString(String string) {
        if (string == null || string.isEmpty()) return new NBTQuery();
        LinkedList<Object> v = new LinkedList<Object>();
        Queue<Character> chars = new LinkedList<Character>();
        StringBuilder buffer = new StringBuilder();
        for (char c : string.toCharArray()) chars.add(c);
        byte mode = 0; // 0 = default text;  1 = text in ""; 2 = text in []
        tokenizer:
        while (true) {
            Character c = chars.poll();
            switch (mode) {
                case 0: {
                    if (c == null) {
                        if (buffer.length() != 0) v.add(buffer.toString());
                        break tokenizer;
                    } else if (c == '.') {
                        if (buffer.length() != 0) {
                            v.add(buffer.toString());
                            buffer = new StringBuilder();
                        }
                    } else if (c == '\"') {
                        mode = 1;
                    } else if (c == '[') {
                        if (buffer.length() != 0) {
                            v.add(buffer.toString());
                            buffer = new StringBuilder();
                        }
                        mode = 2;
                    } else if (c == ']') {
                        throw new RuntimeException();
                    } else {
                        buffer.append(c);
                    }
                    break;
                }
                case 1: {
                    if (c == null) {
                        throw new RuntimeException();
                    }
                    if (c == '\\') {
                        buffer.append(c);
                        Character t = chars.poll();
                        if (t == null) throw new RuntimeException();
                        buffer.append(t);
                    } else if (c == '"') {
                        if (buffer.length() != 0) {
                            v.add(buffer.toString());
                            buffer = new StringBuilder();
                        }
                        mode = 0;
                    } else {
                        buffer.append(c);
                    }
                    break;
                }
                case 2: {
                    if (c == null) {
                        throw new RuntimeException();
                    } else if (c == ']') {
                        String t = buffer.toString();
                        int r = -1;
                        if (!t.isEmpty()) r = Integer.parseInt(t);
                        v.add(r);
                        buffer = new StringBuilder();
                        mode = 0;
                    } else if (c.toString().matches("[0-9]")) {
                        buffer.append(c);
                    } else {
                        throw new RuntimeException();
                    }
                    break;
                }
            }
        }
        NBTQuery q = new NBTQuery();
        q.values = v;
        return q;
    }

    public NBTQuery(Object... nodes) {
        for (Object node : nodes) {
            if (node instanceof String || node instanceof Integer) {
                values.add(node);
            } else throw new RuntimeException("invalid node: " + node);
        }
    }

    public NBTQuery(List<Object> nodes) {
        for (Object node : nodes) {
            if (node instanceof String || node instanceof Integer) {
                values.add(node);
            } else throw new RuntimeException("invalid node: " + node);
        }
    }

    public Queue<Object> getQueue() {
        return new LinkedList<Object>(values);
    }
}
