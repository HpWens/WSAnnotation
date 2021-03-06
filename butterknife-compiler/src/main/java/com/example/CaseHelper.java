package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * desc:
 * author: wens
 * date: 2017/8/9.
 */

public  class CaseHelper {

    private static final Pattern PATTERN = Pattern.compile("([A-Z]|[a-z])[a-z0-9]*");

    private CaseHelper() {
    }

    public static String camelCaseToSnakeCase(String camelCase) {

        List<String> tokens = new ArrayList<>();
        Matcher matcher = PATTERN.matcher(camelCase);
        String acronym = "";
        while (matcher.find()) {
            String found = matcher.group();
            if (found.matches("^[A-Z]$")) {
                acronym += found;
            } else {
                if (acronym.length() > 0) {
                    // we have an acronym to add before we continue
                    tokens.add(acronym);
                    acronym = "";
                }
                tokens.add(found.toLowerCase());
            }
        }
        if (acronym.length() > 0) {
            tokens.add(acronym);
        }
        if (tokens.size() > 0) {
            StringBuilder sb = new StringBuilder(tokens.remove(0));
            for (String s : tokens) {
                sb.append("_").append(s);
            }
            return sb.toString();
        } else {
            return camelCase;
        }
    }

    public static String camelCaseToUpperSnakeCase(String camelCase) {
        return camelCaseToSnakeCase(camelCase).toUpperCase();
    }

    public static String lowerCaseFirst(String string) {
        if (string.length() < 2) {
            return string.toLowerCase();
        }
        String first = string.substring(0, 1).toLowerCase();
        String end = string.substring(1, string.length());
        return first + end;
    }

    public static String camelCaseToUpperSnakeCase(String prefix, String camelCase, String suffix) {
        if (prefix != null && !camelCase.startsWith(prefix)) {
            camelCase = prefix + "_" + camelCase;
        }
        if (suffix != null && !camelCase.toLowerCase().endsWith(suffix.toLowerCase())) {
            camelCase = camelCase + "_" + suffix;
        }
        return camelCaseToUpperSnakeCase(camelCase);
    }

}
