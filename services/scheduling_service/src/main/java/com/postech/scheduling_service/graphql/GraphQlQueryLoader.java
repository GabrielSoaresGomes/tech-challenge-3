package com.postech.scheduling_service.graphql;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GraphQlQueryLoader {

    private final Map<String, String> docs = new HashMap<>();

    private final Map<String, String> fragmentByName = new HashMap<>();

    private static final Pattern FRAGMENT_USE = Pattern.compile("\\.\\.\\.\\s*([A-Za-z_][A-Za-z0-9_]*)");

    public GraphQlQueryLoader() {
        try {
            var resolver = new PathMatchingResourcePatternResolver();

            for (Resource r : resolver.getResources("classpath*:graphql-client/**/fragments*.graphql")) {
                String content = readAll(r);
                for (String fragmentDef : splitFragments(content)) {
                    String name = extractFragmentName(fragmentDef);
                    if (name != null) {
                        fragmentByName.put(name, fragmentDef.trim());
                    }
                }
            }

            for (Resource r : resolver.getResources("classpath*:graphql-client/**/*.graphql")) {
                String filename = r.getFilename();
                if (filename == null || filename.toLowerCase().contains("fragments")) continue;

                String key = filename.replaceFirst("\\.graphql$", "");
                String operation = readAll(r);

                String fullDoc = concatRequiredFragments(operation);
                docs.put(key, fullDoc);
            }

            if (docs.isEmpty()) {
                throw new IllegalStateException("Nenhuma operação .graphql encontrada em graphql-client/");
            }
        } catch (Exception e) {
            throw new IllegalStateException("Erro carregando documentos GraphQL do classpath", e);
        }
    }

    public String get(String name) {
        String doc = docs.get(name);
        if (doc == null) {
            throw new IllegalArgumentException("Documento GraphQL não encontrado: " + name);
        }
        return doc;
    }

    private static String readAll(Resource r) throws Exception {
        return new String(r.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    private static List<String> splitFragments(String content) {
        List<String> list = new ArrayList<>();
        String[] parts = content.split("(?=\\bfragment\\s+[A-Za-z_][A-Za-z0-9_]*)");
        for (String p : parts) {
            String trimmed = p.trim();
            if (trimmed.startsWith("fragment ")) list.add(trimmed);
        }
        return list;
    }

    private static String extractFragmentName(String fragmentDef) {
        int start = fragmentDef.indexOf("fragment ") + "fragment ".length();
        int end = fragmentDef.indexOf(" ", start);
        if (start >= 0 && end > start) {
            return fragmentDef.substring(start, end).trim();
        }
        return null;
    }

    private String concatRequiredFragments(String operation) {
        Set<String> needed = new LinkedHashSet<>();
        Deque<String> toProcess = new ArrayDeque<>(findFragmentUses(operation));

        while (!toProcess.isEmpty()) {
            String name = toProcess.pop();
            if (!needed.add(name)) continue;

            String fragDef = fragmentByName.get(name);
            if (fragDef != null) {
                for (String nested : findFragmentUses(fragDef)) {
                    if (!needed.contains(nested)) {
                        toProcess.push(nested);
                    }
                }
            }
        }

        if (needed.isEmpty()) {
            return operation;
        }

        StringBuilder sb = new StringBuilder();
        for (String name : needed) {
            String fragDef = fragmentByName.get(name);
            if (fragDef != null) {
                sb.append(fragDef).append("\n\n");
            }
        }
        sb.append(operation);
        return sb.toString();
    }

    private static List<String> findFragmentUses(String doc) {
        List<String> names = new ArrayList<>();
        Matcher m = FRAGMENT_USE.matcher(doc);
        while (m.find()) {
            names.add(m.group(1));
        }
        return names;
    }
}
