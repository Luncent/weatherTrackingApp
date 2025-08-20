package org.example.repositories.repos_impl;

import org.example.entities.HttpSession;
import org.example.exception_handling.exceptions.repository.DBException;
import org.example.repositories.BaseRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class HttpSessionRepository extends BaseRepository<HttpSession, UUID> {

    public HttpSessionRepository(SessionFactory factory) {
        super(factory, HttpSession.class);
    }

    public int deleteAllUserSessions(Long userId){
        try{
            Session session = sessionFactory.getCurrentSession();
            return session.createQuery("DELETE FROM HttpSession s WHERE s.user.id = :userId")
                    .setParameter("userId", userId)
                    .executeUpdate();
        }catch (Exception e){
            throw new DBException(e);
        }
    }
}