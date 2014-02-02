package returnobjects;

import java.util.Date;

public class Driver {
	private long id;

	private String firstName, lastName;
	
	private String latestCoordinates;
	
	private String phoneNumber, phoneNumber2;
	
	private String email;
	
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
	
	public void setPhoneNumber2(String phoneNumber2){
		this.phoneNumber2 = phoneNumber2;
	}
	
	public String getPhoneNumber2(){
		return this.phoneNumber2;
	}
	
	public void setEmail(String email){
		this.email = email;
	}
	
	public String getEmail(){
		return this.email;
	}
	
	public void setCoordinatesUpdateDate(Date date){
		this.coordinatesUpdateDate = date;
	}
	
	public Date getCoordinatesUpdateDate(){
		return this.coordinatesUpdateDate;
	}

}
