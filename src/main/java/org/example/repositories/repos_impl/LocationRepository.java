package org.example.repositories.repos_impl;

import org.example.entities.Location;
import org.example.repositories.BaseRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Repository
public class LocationRepository extends BaseRepository<Location, Long> {

    private static final int PAGE_SIZE = 4;

    public LocationRepository(SessionFactory factory) {
        super(factory, Location.class);
    }

    public Optional<Location> getByCoordinatesAndUserId(BigDecimal latitude, BigDecimal longitude, Long userId) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT l FROM Location l " +
                        "WHERE l.latitude = :latitude AND l.longitude = :longitude AND l.user.id = :userId",
                        Location.class)
                .setParameter("latitude", latitude)
                .setParameter("longitude", longitude)
                .setParameter("userId", userId)
                .uniqueResultOptional();
    }

    public List<Location> getPage(int pageNumber, Long userId) {
        Page page = Page.page(PAGE_SIZE, --pageNumber);
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Location l WHERE l.user.id = :userId", Location.class)
                .setParameter("userId", userId)
                .setPage(page)
                .list();
    }

    public Integer getPageCount(Long userId) {
        Session session = sessionFactory.getCurrentSession();
        Integer locationNumber = session.createQuery("SELECT COUNT(*) from Location l WHERE l.user.id = :userId", Integer.class)
                .setParameter("userId", userId)
                .getSingleResult();
        return (locationNumber % PAGE_SIZE) != 0 ? locationNumber / PAGE_SIZE + 1 : locationNumber / PAGE_SIZE;
    }
}
