package com.space.repository;

import com.space.model.Ship;
import com.space.model.ShipRequest;
import com.space.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.*;
import java.util.*;

@Repository
public class ShipRepository implements org.springframework.data.repository.Repository<Ship, Long>
{
    private static final Logger log = Logger.getLogger(ShipRepository.class);

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public List<Ship> getShips(ShipRequest shipRequest)
    {
        log.debug("Getting all ships");
        Transaction transaction = session().beginTransaction();

        CriteriaBuilder builder = session().getCriteriaBuilder();
        CriteriaQuery<Ship> query = builder.createQuery(Ship.class);
        Root<Ship> root = query.from(Ship.class);

        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isNotEmpty(shipRequest.getName()))
            predicates.add(builder.like(root.get("name"), new StringBuilder("%").append(shipRequest.getName()).append("%").toString()));
        if (StringUtils.isNotEmpty(shipRequest.getPlanet()))
            predicates.add(builder.like(root.get("planet"), new StringBuilder("%").append(shipRequest.getPlanet()).append("%").toString()));
        if (shipRequest.getShipType() != null)
            predicates.add(builder.equal(root.get("shipType"), shipRequest.getShipType()));
        if (shipRequest.getAfter() != null)
            predicates.add(builder.greaterThanOrEqualTo(root.get("prodDate"), new Date(shipRequest.getAfter())));
        if (shipRequest.getBefore() != null)
            predicates.add(builder.lessThanOrEqualTo(root.get("prodDate"), new Date(shipRequest.getBefore())));
        if (shipRequest.isUsed() != null)
            predicates.add(builder.equal(root.get("isUsed"), shipRequest.isUsed()));
        if (shipRequest.getMinSpeed() != null)
            predicates.add(builder.greaterThanOrEqualTo(root.get("speed"), shipRequest.getMinSpeed()));
        if (shipRequest.getMaxSpeed() != null)
            predicates.add(builder.lessThanOrEqualTo(root.get("speed"), shipRequest.getMaxSpeed()));
        if (shipRequest.getMinCrewSize() != null)
            predicates.add(builder.greaterThanOrEqualTo(root.get("crewSize"), shipRequest.getMinCrewSize()));
        if (shipRequest.getMaxCrewSize() != null)
            predicates.add(builder.lessThanOrEqualTo(root.get("crewSize"), shipRequest.getMaxCrewSize()));
        if (shipRequest.getMinRating() != null)
            predicates.add(builder.greaterThanOrEqualTo(root.get("rating"), shipRequest.getMinRating()));
        if (shipRequest.getMaxRating() != null)
            predicates.add(builder.lessThanOrEqualTo(root.get("rating"), shipRequest.getMaxRating()));

        Predicate[] filterPredicates = new Predicate[predicates.size()];
        predicates.toArray(filterPredicates);
        query.select(root).where(filterPredicates);

        if (shipRequest.getOrder() != null)
            query.orderBy(builder.asc(root.get(shipRequest.getOrder().getFieldName())));

        Query<Ship> q = session().createQuery(query);
        List<Ship> allShips = q.getResultList();

        transaction.commit();

        log.debug("Got ships: " + allShips.size());

        return allShips;
    }

    public Ship createShip(Ship ship)
    {
        log.debug("Create ship " + ship.toString());
        Transaction tx = session().beginTransaction();
        Long id = (Long) session().save(ship);
        Ship savedShip = session().get(Ship.class, id);
        tx.commit();

        log.debug("Created ship with id: " + id);

        return savedShip;
    }

    public Ship getShip(String id)
    {
        log.debug("Get ship by id: " + id);
        Transaction tx = session().beginTransaction();
        CriteriaBuilder builder = session().getCriteriaBuilder();
        CriteriaQuery<Ship> query = builder.createQuery(Ship.class);
        Root<Ship> root = query.from(Ship.class);

        query.select(root).where(builder.equal(root.get("id"), id));

        Query<Ship> q = session().createQuery(query);
        Ship ship = q.uniqueResult();

        tx.commit();

        log.debug("Got ship: " + ship.toString());

        return ship;
    }

    public Ship updateShip(Ship ship)
    {
        log.debug("Update ship: " + ship);
        Transaction transaction = session().beginTransaction();

        Ship updateShip = session().get(Ship.class, ship.getId());
        if (updateShip == null)
            return null;
        if (StringUtils.isNotEmpty(ship.getName()))
            updateShip.setName(ship.getName());
        if (StringUtils.isNotEmpty(ship.getPlanet()))
            updateShip.setPlanet(ship.getPlanet());
        if (ship.getShipType() != null)
            updateShip.setShipType(ship.getShipType());
        if (ship.getProdDate() != null)
            updateShip.setProdDate(ship.getProdDate());
        if (ship.getUsed() != null)
            updateShip.setUsed(ship.getUsed());
        if (ship.getSpeed() != null)
            updateShip.setSpeed(ship.getSpeed());
        if (ship.getCrewSize() != null)
            updateShip.setCrewSize(ship.getCrewSize());
        Utils.countRating(updateShip);

        Ship updatedShip = (Ship) session().merge(updateShip);

        transaction.commit();

        log.debug("Ship is updated");

        return updatedShip;
    }

    public Ship deleteShip(Long id)
    {
        log.debug("Delete ship by id: " + id);
        Transaction tx = session().beginTransaction();
        Ship deleteShip = session().get(Ship.class, id);
        if (deleteShip == null)
            return null;

        session().delete(deleteShip);
        tx.commit();

        log.debug("Ship is deleted");
        return deleteShip;
    }

    private Session session()
    {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        return sessionFactory.getCurrentSession();
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory)
    {
        this.entityManagerFactory = entityManagerFactory;

    }
}
