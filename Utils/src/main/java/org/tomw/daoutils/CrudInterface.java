package org.tomw.daoutils;

public interface CrudInterface<E> {
    void add(E entity);
    void delete(E entity);
    E get(String id, Class<?> claz);
}
