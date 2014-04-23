package returnobjects;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


public class Coordinates {
	
	private long id;
	
	@Min(-180)
	@Max(180)
	private double longitude;
	
	@Min(-90)
	@Max(90)
	private double latitude;

	//Left for Hibernate 
	public Coordinates() {}
	
	public Coordinates(double longitude, double latitude) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	
	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	

}
