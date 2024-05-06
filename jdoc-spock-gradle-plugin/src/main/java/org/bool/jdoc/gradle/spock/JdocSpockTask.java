package org.bool.jdoc.gradle.spock;

import org.bool.jdoc.gradle.JdocTask;

import org.gradle.api.model.ObjectFactory;

import javax.inject.Inject;

public class JdocSpockTask extends JdocTask {

    @Inject
    public JdocSpockTask(ObjectFactory objectFactory) {
        super(objectFactory);
    }
}
