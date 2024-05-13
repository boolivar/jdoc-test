package org.bool.jdoc.spock;

public class ResourceContainer<T> implements AutoCloseable {

    private final T resource;

    public ResourceContainer(T resource) {
        this.resource = resource;
    }

    /**
     * Returns resource that potentially might be [Auto]Closeable.
     * 
     * <pre><code lang="spock">
     * def "closeable resource closed"() {
     *   given:
     *     def closeable = Mock(Closeable)
     *   when:
     *     try (ResourceContainer<?> container = new ResourceContainer(closeable)) {
     *       assert container.resource == closeable
     *     }
     *   then:
     *     1 * closeable.close()
     * }
     * 
     * def "arbitrary resource intact"() {
     *   when:
     *     try (ResourceContainer<?> container = new ResourceContainer(resource)) {
     *       assert container.resource == resource
     *     }
     *   then:
     *     0 * resource._
     * }
     * </code></pre>
     */
    public T getResource() {
        return resource;
    }

    @Override
    public void close() throws Exception {
        if (resource instanceof AutoCloseable) {
            ((AutoCloseable) resource).close();
        }
    }
}
