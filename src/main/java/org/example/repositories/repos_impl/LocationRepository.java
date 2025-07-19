package org.example.repositories.repos_impl;

import org.example.entities.Location;
import org.example.repositories.BaseRepository;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class LocationRepository extends BaseRepository<Location, Long> {

    public LocationRepository(SessionFactory factory) {
        super(factory, LocationRepository.class);
    }
}
