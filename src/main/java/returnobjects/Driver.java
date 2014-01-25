package skip;

import java.util.Date;

public class Driver {
	private long id;
	private String firstName, lastName, latestCoordinates, phoneNumber;
	private Date coordinatesUpdateDate;
	
	public Driver(){}
	
	public Driver(String firstName, String lastName){
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	@SuppressWarnings("unused")
	private void setId(long id){
		this.id = id;
	}
	
	public long getId(){
		return this.id;
	}
	
	public void setFirstName(String firstName){
		this.firstName = firstName;
	}
	
	public String getFirstName(){
		return this.firstName;
	}
	
	public void setLastName(String lastName){
		this.lastName = lastName;
	}
	
	public String getLastName(){
		return this.lastName;
	}
	
	public void setLatestCoordinates(String coordinates){
		this.latestCoordinates = coordinates;
	}
	
	public String getLatestCoordinates(){
		return this.latestCoordinates;
	}
	
	public void setPhoneNumber(String phoneNumber){
		this.phoneNumber = phoneNumber;
	}
	
	public String getPhoneNumber(){
		return this.phoneNumber;
	}
	
	public void setCoordinatesUpdateDate(Date date){
		this.coordinatesUpdateDate = date;
	}
	
	public Date getCoordinatesUpdateDate(){
		return this.coordinatesUpdateDate;
	}

}
