package com.space.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Ship")
public class Ship implements Serializable
{
    private static final long serialVersionUID = -1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50)
    private String name;
    @Column(length = 50)
    private String planet;
    @Column
    @Enumerated(EnumType.STRING)
    private ShipType shipType;
    @Column
    @Temporal(TemporalType.DATE)
    private Date prodDate;
    @Column
    private Boolean isUsed;
    @Column
    private Double speed;
    @Column
    private Integer crewSize;
    @Column
    private Double rating;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPlanet()
    {
        return planet;
    }

    public void setPlanet(String planet)
    {
        this.planet = planet;
    }

    public ShipType getShipType()
    {
        return shipType;
    }

    public void setShipType(ShipType shipType)
    {
        this.shipType = shipType;
    }

    public Date getProdDate()
    {
        return prodDate;
    }

    public void setProdDate(Date prodDate)
    {
        this.prodDate = prodDate;
    }

    public Boolean getUsed()
    {
        return isUsed;
    }

    public void setUsed(Boolean used)
    {
        isUsed = used;
    }

    public Double getSpeed()
    {
        return speed;
    }

    public void setSpeed(Double speed)
    {
        this.speed = speed;
    }

    public Integer getCrewSize()
    {
        return crewSize;
    }

    public void setCrewSize(Integer crewSize)
    {
        this.crewSize = crewSize;
    }

    public Double getRating()
    {
        return rating;
    }

    public void setRating(Double rating)
    {
        this.rating = rating;
    }

    @Override
    public String toString()
    {
        return "Ship{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", planet='" + planet + '\'' +
                ", shipType=" + shipType +
                ", prodDate=" + prodDate +
                ", isUsed=" + isUsed +
                ", speed=" + speed +
                ", crewSize=" + crewSize +
                ", rating=" + rating +
                '}';
    }
}
