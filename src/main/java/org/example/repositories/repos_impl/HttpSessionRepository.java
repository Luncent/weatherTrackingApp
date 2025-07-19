package org.example.repositories.repos_impl;

import org.example.entities.HttpSession;
import org.example.repositories.BaseRepository;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public class HttpSessionRepository extends BaseRepository<HttpSession, UUID> {

    public HttpSessionRepository(SessionFactory factory) {
        super(factory, HttpSession.class);
    }
}