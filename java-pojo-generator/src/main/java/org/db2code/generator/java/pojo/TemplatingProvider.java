package org.db2code.generator.java.pojo;

public interface TemplatingProvider {
    String merge(Object context, String template);
}
