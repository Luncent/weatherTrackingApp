package org.example.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.transaction.annotation.Transactional;


import java.io.Serializable;
import java.util.List;
import java.util.Optional;

//using transactional manager so this code doesn't expected to be
//used with rollbacks and commits handling in service layer.
// Also each thread has own transaction (Datasource config)
//so getCurrentTransaction wont lead to Race Conditions

@Transactional
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public abstract class BaseRepository<T,K extends Serializable> implements CRUDRepository<T,K> {
    protected Class<T> clazz;
    protected SessionFactory sessionFactory;

    public BaseRepository(SessionFactory sessionFactory, Class clazz){
        this.sessionFactory = sessionFactory;
        this.clazz=clazz;
    }

    @Override
    public T save(T entity) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(entity);
        session.flush();
        return entity;
    }

    @Override
    public List<T> getAll() {
            return sessionFactory.getCurrentSession()
                    .createQuery("FROM "+ clazz.getName(), clazz)
                    .list();
    }

    @Override
    public Optional<T> getById(K id) {
        return Optional.ofNullable(
                sessionFactory.getCurrentSession().find(clazz, id)
        );
    }

    @Override
    public T update(T entity) {
        sessionFactory.getCurrentSession().merge(entity);
        sessionFactory.getCurrentSession().flush();
        return entity;
    }

    @Override
    public void delete(K id) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(session.get(clazz, id));
        session.flush();
    }
}
