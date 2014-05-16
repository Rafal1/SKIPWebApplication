package returnobjects;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

public class Communique {

    private long id;

    private long driverId;

    private Date date;

    private Coordinates coordinates;

    private int state;

    Communique() {}

    public Communique(long id, long driverId, Date date, Coordinates coordinates, int state) {
        this.id = id;
        this.driverId = driverId;
        this.date = date;
        this.coordinates = coordinates;
        this.state = state;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Coordinates getcoordinates() {
        return coordinates;
    }

    public void setcoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

}
