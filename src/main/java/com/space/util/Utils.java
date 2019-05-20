package com.space.util;

import com.space.model.Ship;
import com.space.repository.ShipRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.Date;

public class Utils
{
    private static final Logger log = Logger.getLogger(ShipRepository.class);
    private static final int CURRENT_YEAR = 3019;

    public static int getYear(Long year)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(year));

        return calendar.get(Calendar.YEAR);
    }

    public static boolean canCreateShip(Ship ship)
    {
        boolean isCan = true;
        if (StringUtils.isEmpty(ship.getName()) || StringUtils.isEmpty(ship.getPlanet()))
            isCan = false;
        else if (ship.getName().length() > 50 || ship.getPlanet().length() > 50)
            isCan = false;
        else if (ship.getSpeed() == null || (ship.getSpeed() < 0.01 || ship.getSpeed() > 0.99))
            isCan = false;
        else if (ship.getCrewSize() == null || (ship.getCrewSize() < 1 || ship.getCrewSize() > 9999))
            isCan = false;
        else if (ship.getProdDate() == null || (ship.getProdDate().getTime() < 0) || (Utils.getYear(ship.getProdDate().getTime()) < 2800 || Utils.getYear(ship.getProdDate().getTime()) > 3019))
            isCan = false;

        return isCan;
    }

    public static boolean canUpdateShip(Ship ship)
    {
        boolean isCan = true;
        if (ship.getName() != null)
        {
            int length = ship.getName().length();
            if (length == 0 || length > 50)
                isCan = false;
        }
        if (ship.getPlanet() != null)
        {
            int length = ship.getPlanet().length();
            if (length == 0 || length > 50)
                isCan = false;
        }
        if (ship.getSpeed() != null && (ship.getSpeed() < 0.01 || ship.getSpeed() > 0.99))
            isCan = false;
        else if (ship.getCrewSize() != null && (ship.getCrewSize() < 1 || ship.getCrewSize() > 9999))
            isCan = false;
        else if (ship.getProdDate() != null)
        {
            if (ship.getProdDate().getTime() < 0)
                isCan = false;
            else if (Utils.getYear(ship.getProdDate().getTime()) < 2800 || Utils.getYear(ship.getProdDate().getTime()) > 3019)
                isCan = false;
        }


        return isCan;
    }

    public static void countRating(Ship ship)
    {
        if (ship.getUsed() == null || ship.getSpeed() == null || ship.getProdDate() == null)
            return;
        double k = ship.getUsed() ? 0.5 : 1;
        double t = 80 * ship.getSpeed() * k;
        double t1 = CURRENT_YEAR - Utils.getYear(ship.getProdDate().getTime()) + 1;
        double t2 = t / t1;
        Double rating = (double) Math.round(t2 * 100) / 100;
        ship.setRating(rating);
        log.debug("Ship rating: " + rating);
    }
}
