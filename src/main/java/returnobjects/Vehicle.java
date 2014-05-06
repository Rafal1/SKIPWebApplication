package returnobjects;

public class Vehicle {
	private long id;
	private int truckload;
	private String brand, colour, registrationNumber;
	
	public Vehicle() {}

    public Vehicle(int maxLoad, String brand, String colour, String regNumber){
        this.truckload = maxLoad;
        this.brand = brand;
        this.colour = colour;
        this.registrationNumber = regNumber;
    }

    public long getId() {
        return id;
    }

    public int getTruckload() {
        return truckload;
    }

    public String getBrand(){
        return brand;
    }

    public String getColour() {
        return colour;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    @SuppressWarnings("unused")
    private void setId(long ID){
        this.id = ID;
    }

    public void setTruckload(int maxLoad){
        this.truckload = maxLoad;
    }

    public void setBrand(String brand){
        this.brand = brand;
    }

    public void setColour(String colour){
        this.colour = colour;
    }

    public void setRegistrationNumber(String regNumber){
        this.registrationNumber = regNumber;
    }
}
