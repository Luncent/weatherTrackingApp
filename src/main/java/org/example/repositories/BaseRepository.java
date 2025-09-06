package org.example.repositories;

import jakarta.persistence.Id;
import org.example.exception_handling.exceptions.repository.DBException;
import org.example.exception_handling.exceptions.repository.EntityExistsException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Transactional
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public abstract class BaseRepository<T, K extends Serializable> implements CRUDRepository<T, K> {
    protected final Class<T> clazz;
    protected final SessionFactory sessionFactory;
    protected final String idFieldName;

    public BaseRepository(SessionFactory sessionFactory, Class<T> clazz) {
        this.sessionFactory = sessionFactory;
        this.clazz = clazz;
        this.idFieldName = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst().orElseThrow(() -> new RuntimeException("Id field not found for class" + clazz.getName()))
                .getName();
    }

    @Override
    public T save(T entity) {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.persist(entity);
        } catch (ConstraintViolationException ex) {
            throw new EntityExistsException(entity.getClass().getSimpleName(), ex);
        } catch (Exception ex) {
            throw new DBException(ex);
        }
        return entity;
    }

    @Transactional(readOnly = true)
    @Override
    public List<T> getAll() {
        try {
            return sessionFactory.getCurrentSession()
                    .createQuery("FROM " + clazz.getName(), clazz)
                    .list();
        } catch (Exception ex) {
            throw new DBException(ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<T> getById(K id) {
        try {
            return Optional.ofNullable(
                    sessionFactory.getCurrentSession().find(clazz, id)
            );
        } catch (Exception ex) {
            throw new DBException(ex);
        }
    }

    @Override
    public T update(T entity) {
        try {
            sessionFactory.getCurrentSession().merge(entity);
            return entity;
        } catch (ConstraintViolationException ex) {
            throw new EntityExistsException(entity.getClass().getName(), ex);
        } catch (Exception ex) {
            throw new DBException(ex);
        }
    }

    @Override
    public void deleteById(K id) {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.createQuery("DELETE FROM " + clazz.getName() + " WHERE " + idFieldName + " = :id")
                    .setParameter("id", id)
                    .executeUpdate();
        } catch (Exception ex) {
            throw new DBException(ex);
        }
    }
}
