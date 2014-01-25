package skip;

public class Vehicle {
	private long id;
	private int maxLoad;
	private String brand, colour, regNumber;
	
	public Vehicle() {}
	
	public Vehicle(int maxLoad, String brand, String colour, String regNumber){
		this.maxLoad = maxLoad;
		this.brand = brand;
		this.colour = colour;
		this.regNumber = regNumber;
	}
	
	public long getId() {
		return id;
	}
	
	public int getMaxLoad() {
		return maxLoad;
	}
	
	public String getBrand(){
		return brand;
	}
	
	public String getColour() {
		return colour;
	}
	
	public String getRegNumber() {
		return regNumber;
	}
	
	@SuppressWarnings("unused")
	private void setId(long ID){
		this.id = ID;
	}
	
	public void setMaxLoad(int maxLoad){
		this.maxLoad = maxLoad;
	}
	
	public void setBrand(String brand){
		this.brand = brand;
	}

	public void setColour(String colour){
		this.colour = colour;
	}
	
	public void setRegNumber(String regNumber){
		this.regNumber = regNumber;
	}
}
