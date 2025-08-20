package org.example.repositories.repos_impl;

import org.example.entities.Location;
import org.example.exception_handling.exceptions.repository.DBException;
import org.example.repositories.BaseRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Page;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;


@Repository
public class LocationRepository extends BaseRepository<Location, Long> {

    private static final int PAGE_SIZE = 4;

    public LocationRepository(SessionFactory factory) {
        super(factory, Location.class);
    }

    public Integer deleteUserLocation(BigDecimal latitude, BigDecimal longitude, Long userId) {
        try {
            Session session = sessionFactory.getCurrentSession();
            return session.createQuery("DELETE FROM Location l " +
                            "WHERE l.latitude = :latitude" +
                            " AND l.longitude = :longitude" +
                            " AND l.user.id = :userId")
                    .setParameter("latitude", latitude)
                    .setParameter("longitude", longitude)
                    .setParameter("userId", userId)
                    .executeUpdate();
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    public List<Location> getPage(int pageNumber, Long userId) {
        try {
            Page page = Page.page(PAGE_SIZE, --pageNumber);
            Session session = sessionFactory.getCurrentSession();
            return session.createQuery("from Location l WHERE l.user.id = :userId", Location.class)
                    .setParameter("userId", userId)
                    .setPage(page)
                    .list();
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    public Long getPageCount(Long userId) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Long locationNumber = session.createQuery("SELECT COUNT(*) from Location l WHERE l.user.id = :userId", Long.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
            Long allPagesCount = (locationNumber % PAGE_SIZE) != 0 ? locationNumber / PAGE_SIZE + 1 : locationNumber / PAGE_SIZE;
            allPagesCount = allPagesCount == 0 ? 1 : allPagesCount;
            return allPagesCount;
        }catch (Exception e) {
            throw new DBException(e);
        }
    }
}
