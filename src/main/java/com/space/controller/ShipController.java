package com.space.controller;

import com.space.model.ShipRequest;
import com.space.model.ShipType;
import com.space.model.Ship;
import com.space.service.ShipService;
import com.space.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class ShipController
{
    @Autowired
    private ShipService shipService;

    @RequestMapping(value = "/ships", method = RequestMethod.GET)
    public List<Ship> getShips(@RequestParam(required = false) String name,
                               @RequestParam(required = false) String planet,
                               @RequestParam(required = false) ShipType shipType,
                               @RequestParam(required = false) Long after,
                               @RequestParam(required = false) Long before,
                               @RequestParam(required = false) Boolean isUsed,
                               @RequestParam(required = false) Double minSpeed,
                               @RequestParam(required = false) Double maxSpeed,
                               @RequestParam(required = false) Integer minCrewSize,
                               @RequestParam(required = false) Integer maxCrewSize,
                               @RequestParam(required = false) Double minRating,
                               @RequestParam(required = false) Double maxRating,
                               @RequestParam(required = false) ShipOrder order,
                               @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                               @RequestParam(required = false, defaultValue = "3") Integer pageSize)
    {
        ShipRequest shipRequest = new ShipRequest(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize,
                maxCrewSize, minRating, maxRating, order, pageNumber, pageSize);

        return shipService.getShips(shipRequest);
    }

    @RequestMapping(value = "/ships/count", method = RequestMethod.GET)
    public int getShipsCount(@RequestParam(required = false) String name,
                             @RequestParam(required = false) String planet,
                             @RequestParam(required = false) ShipType shipType,
                             @RequestParam(required = false) Long after,
                             @RequestParam(required = false) Long before,
                             @RequestParam(required = false) Boolean isUsed,
                             @RequestParam(required = false) Double minSpeed,
                             @RequestParam(required = false) Double maxSpeed,
                             @RequestParam(required = false) Integer minCrewSize,
                             @RequestParam(required = false) Integer maxCrewSize,
                             @RequestParam(required = false) Double minRating,
                             @RequestParam(required = false) Double maxRating,
                             @RequestParam(required = false) ShipOrder order)
    {
        ShipRequest shipRequest = new ShipRequest(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize,
                maxCrewSize, minRating, maxRating, order);

        return shipService.getCount(shipRequest);
    }

    @RequestMapping(value = "/ships", method = RequestMethod.POST)
    public ResponseEntity<?> createShip(@RequestBody Ship ship)
    {
        if (ship.getUsed() == null)
            ship.setUsed(false);
        Ship savedShip = shipService.createShip(ship);
        if (savedShip == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(savedShip, HttpStatus.OK);
    }

    @RequestMapping(value = "/ships/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getShip(@PathVariable String id)
    {
        if (!isValidId(id))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Ship ship = shipService.getShip(id);
        if (ship == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(ship, HttpStatus.OK);
    }

    @RequestMapping(value = "/ships/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> updateShip(@PathVariable String id,
                                        @RequestBody Ship ship)
    {
        if (!isValidId(id) || !Utils.canUpdateShip(ship))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        ship.setId(Long.parseLong(id));
        Ship updatedShip = shipService.updateShip(ship);
        if (updatedShip == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(updatedShip, HttpStatus.OK);
    }

    @RequestMapping(value = "/ships/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteShip(@PathVariable String id)
    {
        if (!isValidId(id))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Ship deletedShip = shipService.deleteShip(id);
        if (deletedShip == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean isValidId(String id)
    {
        boolean isValidId = true;
        if (StringUtils.isNumeric(id))
        {
            double t = Double.parseDouble(id);
            if (t % 1 != 0 || t <= 0)
                isValidId = false;
        }
        else
            isValidId = false;

        return isValidId;
    }
}
