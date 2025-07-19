package org.example.repositories.repos_impl;

import org.example.entities.User;
import org.example.repositories.BaseRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class UserRepository extends BaseRepository<User, Long> {

    public UserRepository(SessionFactory factory) {
        super(factory, User.class);
    }

    public Optional<User> findByLogin(String login) {
        Session session = sessionFactory.getCurrentSession();
        Optional<User> optional = Optional.ofNullable(session.createQuery("from User where login = :login", clazz)
                .setParameter("login", login)
                .uniqueResult());

        return optional;
    }
}
