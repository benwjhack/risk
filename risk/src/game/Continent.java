package game;

public class Continent {
	
	public String name;
	public int bonus;
	public Country[] countries;
	
	public Continent(String name, int bonus, Country[] countries){
		this.name = name;
		this.bonus = bonus;
		this.countries = countries;
	}
	
}
