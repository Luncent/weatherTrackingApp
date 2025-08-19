package org.example.repositories;

import jakarta.persistence.Id;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.transaction.annotation.Transactional;


import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Transactional
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public abstract class BaseRepository<T,K extends Serializable> implements CRUDRepository<T,K> {
    protected Class<T> clazz;
    protected SessionFactory sessionFactory;
    protected String idFieldName;

    public BaseRepository(SessionFactory sessionFactory, Class<T> clazz){
        this.sessionFactory = sessionFactory;
        this.clazz=clazz;
        this.idFieldName = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst().orElseThrow(()->new RuntimeException("Id field not found for class"+clazz.getName()))
                .getName();
    }

    @Override
    public T save(T entity) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(entity);
        return entity;
    }

    @Transactional(readOnly=true)
    @Override
    public List<T> getAll() {
            return sessionFactory.getCurrentSession()
                    .createQuery("FROM "+ clazz.getName(), clazz)
                    .list();
    }

    @Transactional(readOnly=true)
    @Override
    public Optional<T> getById(K id) {
        return Optional.ofNullable(
                sessionFactory.getCurrentSession().find(clazz, id)
        );
    }

    @Override
    public T update(T entity) {
        sessionFactory.getCurrentSession().merge(entity);
        return entity;
    }

    @Override
    public void delete(K id) {
        Session session = sessionFactory.getCurrentSession();
        session.createQuery("DELETE FROM "+clazz.getName()+" WHERE "+idFieldName+" = :id", clazz)
                .setParameter("id", id)
                .executeUpdate();
    }
}
