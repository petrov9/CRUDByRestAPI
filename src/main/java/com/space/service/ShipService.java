package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipRequest;
import com.space.repository.ShipRepository;
import com.space.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ShipService
{
    @Autowired
    private ShipRepository shipRepository;

    public List<Ship> getShips(ShipRequest shipRequest)
    {
        List<Ship> ships = shipRepository.getShips(shipRequest);
        List<Ship> showShips = new ArrayList<>();

        int from = shipRequest.getPageNumber() * shipRequest.getPageSize();
        from = ships.size() < from ? ships.size() : from;
        int to = from + shipRequest.getPageSize();
        to = ships.size() < to ? ships.size() : to;

        for (int i = from; i < to; i++)
            showShips.add(ships.get(i));

        return showShips;
    }

    public int getCount(ShipRequest shipRequest)
    {
        return shipRepository.getShips(shipRequest).size();
    }

    public Ship createShip(Ship ship)
    {
        if (!Utils.canCreateShip(ship))
            return null;
        ship.setSpeed((double) Math.round(ship.getSpeed() * 100) / 100);
        Utils.countRating(ship);

        return shipRepository.createShip(ship);
    }

    public Ship getShip(String id)
    {
        return shipRepository.getShip(id);
    }

    public Ship updateShip(Ship ship)
    {
        return shipRepository.updateShip(ship);
    }

    public Ship deleteShip(String id)
    {
        return shipRepository.deleteShip(Long.parseLong(id));
    }
}
