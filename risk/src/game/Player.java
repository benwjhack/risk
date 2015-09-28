package game;

import java.util.ArrayList;

public class Player {
	
	public boolean player = false;
	public int id, draft = 0;
	public ArrayList<Country> countries = new ArrayList<Country>();
	
	public Player(int id){
		this.id = id;
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
		if(player){return;}
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
