package org.example.repositories.repos_impl;

import org.example.entities.Location;
import org.example.repositories.BaseRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class LocationRepository extends BaseRepository<Location, Long> {

    private static final int PAGE_SIZE = 4;

    public LocationRepository(SessionFactory factory) {
        super(factory, Location.class);
    }

    public List<Location> getPage(int pageNumber) {
        Page page = Page.page(PAGE_SIZE, --pageNumber);
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Location", Location.class)
                .setPage(page)
                .list();
    }

    public Long getPageCount() {
        Session session = sessionFactory.getCurrentSession();
        Long locationNumber = session.createQuery("SELECT COUNT(*) from Location", Long.class)
                .getSingleResult();
        return (locationNumber % PAGE_SIZE) != 0 ? locationNumber / PAGE_SIZE + 1 : locationNumber / PAGE_SIZE;
    }
}
