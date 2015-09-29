package game;

import java.util.ArrayList;

public class Player {
	
	public static int[] tactics;
	
	public String name;
	public boolean player = false;
	public int id, draft = 0, strategy;
	public ArrayList<Country> countries = new ArrayList<Country>();
	
	public Player(int id){
		this.id = id;
		strategy = id;
		name = Country.names[id];
		strategy = tactics[id];
	}
	
	public void update(){
		draft = 0;
		for(Country country: countries){
			draft++;
			if(country.city){
				draft++;
			}
			if(country.capital){
				draft++;
			}
		}
		draft/=3;
		for(Continent continent: Game.mthis.continents){
			boolean trip = true;
			for(Country country: continent.countries){
				if(country.owner != id){
					trip = false;
					break;
				}
			}
			if(trip){
				draft += continent.bonus;
			}
		}
		System.out.println(Country.names[id] + " has "+draft+" draft");
		if(player){return;}
		switch(strategy){
		case 0:
			tactic0();
			break;
		default:
			System.out.println("Tactic not recognised!");
			break;
		}
		Continent.advanceGo();
	}
	
	public void tactic0(){
		int[] continents = new int[6];
		int chosen = -1;
		for(Country country: countries){
			continents[country.continent]++;
		}
		int[] continents2 = Continent.sort(continents);
		for(int i = 0; i != 6; i++){
			if(continents2[0] == continents[i]){
				chosen = i;
				break;
			}
		}
		System.out.println(name+" wants "+Game.mthis.continents[chosen].name);
		int[] country2i = new int[Continent.overall.size()];
		for(int i = 0; i != Continent.overall.size(); i++){
			int count = 0;
			if(Continent.overall.get(i).owner != id){
				continue;
			}
			for(int i2: Continent.overall.get(i).nextTo){
				if(Continent.overall.get(i2).owner != id){
					count++;
				}
			}
			country2i[i] = count;
		}
		while(true){
			boolean trip = true;
			@SuppressWarnings("unchecked")
			ArrayList<Country> countries2 = (ArrayList<Country>) countries.clone();
			for(Country country: countries2){
				if(draft != 0 && country.continent == chosen && country2i[country.id] != 0){
					System.out.println("Adding "+draft+" to "+country.name);
					country.army += draft;
					draft = 0;
				} 
				if(country.army > 2 && country.continent == chosen){
					for(int i: country.nextTo){
						if(Continent.overall.get(i).owner != id){
							trip = false;
							Continent.attack(country.id, i, country.army-1);
							System.out.println("Attacking "+Continent.overall.get(i).name+" with "+country.name+" with "+(country.army-1));
						}
					}
				}
			}
			if(trip){
				break;
			}
		}
	}
	
	public void tactic1(){
		
	}
	
	public void tactic2(){
		
	}
	
	public void tactic3(){
		
	}
	
	public int attackDice(int country){
		if(player){
			if(Continent.overall.get(country).army >= Game.settings[1]){
				return Game.settings[1];
			} else {
				if(Continent.overall.get(country).army >= 2){
					return 2;
				} else {
					return -1;
				}
			}
		}
		return 2; // Randomly chosen by a fair dice roll
	}
	
	public int defendDice(int country){
		if(player){
			if(Continent.overall.get(country).army >= Game.settings[2]){
				return Game.settings[2];
			} else {
				return 1;
			}
		}
		return 1; // Randomly chosen by a fair dice roll
	}
	
}
