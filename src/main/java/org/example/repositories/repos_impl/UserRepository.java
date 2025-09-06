package org.example.repositories.repos_impl;

import org.example.entities.User;
import org.example.exception_handling.exceptions.repository.DBException;
import org.example.repositories.BaseRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository extends BaseRepository<User, Long> {

    public UserRepository(SessionFactory factory) {
        super(factory, User.class);
    }

    public Optional<User> findByLogin(String login) {
        try {
            Session session = sessionFactory.getCurrentSession();
            return Optional.ofNullable(session.createQuery("from User where login = :login", clazz)
                    .setParameter("login", login)
                    .uniqueResult());
        }catch (Exception e) {
            throw new DBException(e);
        }
    }
}
