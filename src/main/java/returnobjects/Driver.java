package returnobjects;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

public class Driver {
	private long id;
	
	@Size(min=3, max=64)
	@Pattern(regexp="^[a-zA-Z-]*$")
	private String firstName, lastName;
	
	@Valid
	private Coordinates latestCoordinates;
	
	@Pattern(regexp="\\d{3,12}")
	private String phoneNumber, phoneNumber2;
	
//	@Email
	@Size(max=64)
	private String email;
	
	private Date coordinatesUpdateDate;
	
	public Driver(){}
	
	public Driver(String firstName, String lastName){
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public void setId(long id){
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
	
	public void setLatestCoordinates(Coordinates coordinates){
		this.latestCoordinates = coordinates;
	}
	
	public Coordinates getLatestCoordinates(){
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