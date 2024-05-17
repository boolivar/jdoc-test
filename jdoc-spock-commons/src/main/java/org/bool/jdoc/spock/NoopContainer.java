package org.bool.jdoc.spock;

public class NoopContainer<T> extends ResourceContainer<T> {

    public NoopContainer(T resource) {
        super(resource);
    }

    /**
     * Does nothing with the resource.
     * 
     * <pre><code lang="spock">
     * def "closeable resource"() {
     *   given:
     *     def closeable = Mock(Closeable)
     *   when:
     *     try (ResourceContainer<?> container = new NoopContainer<>(closeable)) {
     *       assert container.resource == closeable
     *     }
     *   then:
     *     0 * closeable.close()
     * }
     * 
     * def "arbitrary resource"() {
     *   when:
     *     try (ResourceContainer<?> container = new NoopContainer<>(resource)) {
     *       assert container.resource == resource
     *     }
     *   then:
     *     0 * resource._
     * }
     * </code></pre>
     */
    @Override
    public void close() {
    }
}
