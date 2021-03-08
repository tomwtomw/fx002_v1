package org.tomw.hibernate;

import org.hibernate.SessionFactory;

/**
 * Interface for creating hibernate SessionFactory object.
 * The actual way the session factory is created will depend on the application instance and machine
 */
public interface SessionFactoryFactory {
    public SessionFactory buildSessionFactory();
}
