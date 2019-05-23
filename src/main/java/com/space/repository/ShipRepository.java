package com.space.repository;

import com.space.model.Ship;
import com.space.model.ShipRequest;
import com.space.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.*;
import java.util.*;

@Repository
public class ShipRepository implements org.springframework.data.repository.Repository<Ship, Long>
{
    private static final Logger log = Logger.getLogger(ShipRepository.class);

    @Autowired
    private EntityManager entityManager;

    public List<Ship> getShips(ShipRequest shipRequest)
    {
        log.info("Getting all ships");

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
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

        List<Ship> allShips = entityManager.createQuery(query).getResultList();

        log.info("Got ships: " + allShips.size());

        return allShips;
    }

    public Ship createShip(Ship ship)
    {
        log.info("Create ship " + ship);

        entityManager.persist(ship);
        entityManager.flush();

        log.info("Created ship with id: " + ship.getId());
        return ship;
    }

    public Ship getShip(String id)
    {
        log.info("Get ship by id: " + id);

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Ship> query = builder.createQuery(Ship.class);
        Root<Ship> root = query.from(Ship.class);

        query.select(root).where(builder.equal(root.get("id"), id));

        Ship ship = entityManager.createQuery(query).getResultStream().findFirst().orElse(null);

        log.info("Got ship: " + ship);
        return ship;
    }

    public Ship updateShip(Ship ship)
    {
        log.info("Update ship: " + ship);

        Ship updateShip = entityManager.find(Ship.class, ship.getId());
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

        Ship updatedShip = entityManager.merge(updateShip);

        log.info("Ship is updated");
        return updatedShip;
    }

    public Ship deleteShip(Long id)
    {
        log.info("Delete ship by id: " + id);

        Ship deleteShip = entityManager.find(Ship.class, id);
        if (deleteShip == null)
            return null;

        entityManager.remove(deleteShip);

        log.info("Ship is deleted");
        return deleteShip;
    }

    public void setEntityManager(EntityManagerFactory entityManagerFactory)
    {
        this.entityManager = entityManagerFactory.createEntityManager();

    }
}