package org.bool.jdoc.core;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@AllArgsConstructor
public class RegexParser implements JdocParser {

    @AllArgsConstructor
    private static class Tag {

        final String content;

        final int start;

        final int end;

        boolean isOpen() {
            return content.charAt(1) != '/';
        }
    }

    @AllArgsConstructor
    private static class Element {

        final Tag openTag;

        final Tag closeTag;
    }

    private static final Pattern CODE_TAGS = Pattern.compile("<code(?: .*)?>|<\\/code>");

    private final Pattern regex;

    private final BiPredicate<String, String> filter;

    public RegexParser(String lang) {
        this(Pattern.compile(String.format(" lang *= *\"%s\"", lang)).asPredicate());
    }

    public RegexParser(Predicate<String> openTagFilter) {
        this((openTag, closeTag) -> openTagFilter.test(openTag));
    }

    public RegexParser(BiPredicate<String, String> tagFilter) {
        this(CODE_TAGS, tagFilter);
    }

    @Override
    public List<String> parse(String content) {
        return topLevelElements(regex.matcher(content)).stream()
                .filter(e -> filter.test(e.openTag.content, e.closeTag.content))
                .map(e -> content.substring(e.openTag.end, e.closeTag.start))
                .map(this::removeLeadingAsterisks)
                .collect(Collectors.toList());
    }

    private List<Element> topLevelElements(Matcher matcher) {
        ArrayList<Element> elements = new ArrayList<>();
        ArrayDeque<Tag> tags = new ArrayDeque<>();
        while (matcher.find()) {
            Tag tag = new Tag(matcher.group(), matcher.start(), matcher.end());
            if (tag.isOpen()) {
                tags.push(tag);
            } else {
                Tag openTag = tags.pop();
                if (tags.isEmpty()) {
                    elements.add(new Element(openTag, tag));
                }
            }
        }
        return elements;
    }

    @SneakyThrows
    private String removeLeadingAsterisks(String content) {
        try (BufferedReader reader = new BufferedReader(new StringReader(content))) {
            return reader.lines()
                .map(line -> line.replaceFirst("^ *\\*", ""))
                .collect(Collectors.joining("\n"));
        }
    }
}
