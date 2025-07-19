package org.example.repositories;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface CRUDRepository<T, K extends Serializable> {
    T save(T entity);
    Optional<T> getById(K id);
    T update(T entity);
    void delete(K id);
    List<T> getAll();
}
