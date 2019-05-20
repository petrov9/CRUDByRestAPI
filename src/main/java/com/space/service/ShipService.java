package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipRequest;
import com.space.repository.ShipRepository;
import com.space.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShipService
{
    @Autowired
    private ShipRepository shipRepository;
    private List<Ship> cachedShips;

    public List<Ship> getShips(ShipRequest shipRequest)
    {
        cachedShips = shipRepository.getShips(shipRequest);
        List<Ship> showShips = new ArrayList<>();

        int from = shipRequest.getPageNumber() * shipRequest.getPageSize();
        from = cachedShips.size() < from ? cachedShips.size() : from;
        int to = from + shipRequest.getPageSize();
        to = cachedShips.size() < to ? cachedShips.size() : to;

        for (int i = from; i < to; i++)
            showShips.add(cachedShips.get(i));

        return showShips;
    }

    public int getCount(ShipRequest shipRequest)
    {
        int count;

        if (cachedShips != null)
            count = cachedShips.size();
        else
            count = shipRepository.getShips(shipRequest).size();

        return count;
    }

    public Ship createShip(Ship ship)
    {
        if (!Utils.canCreateShip(ship))
            return null;
        ship.setSpeed((double) Math.round(ship.getSpeed() * 100) / 100);
        Utils.countRating(ship);

        Ship savedShip = shipRepository.createShip(ship);
        return savedShip;
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
