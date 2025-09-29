package com.postech.scheduling_service.graphql;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class GraphQlQueryLoader {

    private final Map<String, String> docs = new HashMap<>();
    private final String fragmentsBlock;

    public GraphQlQueryLoader() {
        try {
            var resolver = new PathMatchingResourcePatternResolver();

            StringBuilder fb = new StringBuilder();
            for (Resource r : resolver.getResources("classpath*:graphql-client/**/fragments*.graphql")) {
                fb.append(new String(r.getInputStream().readAllBytes(), StandardCharsets.UTF_8))
                        .append('\n');
            }
            fragmentsBlock = fb.toString();

            for (Resource r : resolver.getResources("classpath*:graphql-client/**/*.graphql")) {
                String filename = r.getFilename();
                if (filename == null || filename.toLowerCase().contains("fragments")) continue;

                String key = filename.replaceFirst("\\.graphql$", ""); // chave = nome do arquivo sem extensão
                String content = new String(r.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

                String fullDoc = fragmentsBlock.isBlank() ? content : fragmentsBlock + "\n" + content;
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
}
