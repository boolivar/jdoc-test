package org.bool.jdoc.spock;

/**
 * AutoCloseable wrapper for resource
 * that might be [Auto]Closeable.
 * 
 * <pre><code lang="spock">
 * def "closeable resource closed"() {
 *   given:
 *     def closeable = Mock(Closeable)
 *   when:
 *     try (ResourceContainer<?> container = new ResourceContainer<>(closeable)) {
 *       assert container.resource == closeable
 *     }
 *   then:
 *     1 * closeable.close()
 * }
 * 
 * def "arbitrary resource intact"() {
 *   when:
 *     try (ResourceContainer<?> container = new ResourceContainer<>(resource)) {
 *       assert container.resource == resource
 *     }
 *   then:
 *     0 * resource._
 * }
 * </code></pre>
 */
public class ResourceContainer<T> implements AutoCloseable {

    private final T resource;

    public ResourceContainer(T resource) {
        this.resource = resource;
    }

    public T getResource() {
        return resource;
    }

    /**
     * Closes resource if it is [Auto]Closeable.
     */
    @Override
    public void close() throws Exception {
        if (resource instanceof AutoCloseable) {
            ((AutoCloseable) resource).close();
        }
    }
}
