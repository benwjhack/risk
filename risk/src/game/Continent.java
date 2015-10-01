package game;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class Continent {
	
	public static ArrayList<Country> overall = new ArrayList<Country>();
	public static float cwidth = 0.04f, cheight = 0.04f;
	
	public String name;
	public int bonus;
	public Country[] countries;
	
	public Continent(String name, int bonus, Country[] countries){
		this.name = name;
		this.bonus = bonus;
		this.countries = countries;
		for(Country country: countries){
			overall.add(country);
		}
	}
	
	public Continent(Continent continent){
		this.name = continent.name;
		this.bonus = continent.bonus;
		this.countries = new Country[continent.countries.length];
		for(int i = 0; i != countries.length; i++){
			countries[i] = continent.countries[i].clone();
		}
	}
	
	public Continent clone(){
		return new Continent(this);
	}
	
	public static void advanceGo(){
		Game.go++;
		Game.go%=Country.players;
		if(Game.go==Game.player){
			Game.mthis.players[Game.player].update();
		}
	}
	
	public static void attack(int country1, int country2, int troops){
		String message = "", origOwner = Country.names[overall.get(country2).owner];
		int stroops = troops;
		if(troops < 2){
			return;
		}
		boolean trp = true;
		for(int country: overall.get(country1).nextTo){
			if(country == country2){
				trp = false;
			}
		}
		if(trp){
			return;
		}
		while(troops > 0){
			int at = Game.mthis.players[overall.get(country1).owner].attackDice(country1), df = Game.mthis.players[overall.get(country2).owner].defendDice(country2);
			if(at == -1){
				overall.get(country1).army -= (stroops - troops);
				break;
			}
			int[] attackDice = new int[at];
			int[] defendDice = new int[df];
			for(int i = 0; i != at; i++){
				attackDice[i] = Game.random.nextInt(6);
			}
			for(int i = 0; i != df; i++){
				defendDice[i] = Game.random.nextInt(6);
			}
			attackDice = sort(attackDice);
			defendDice = sort(defendDice);
			int i = 0;
			boolean trip = false;
			while(i < at && i < df){
				if(attackDice[i] > defendDice[i]){
					overall.get(country1).army--;
					troops--;
					message += Country.names[overall.get(country1).owner] + " lost";
				} else {
					overall.get(country2).army--;
					if(overall.get(country2).army == 0){
						trip = true;
					}
					message += Country.names[overall.get(country1).owner] + " won";
				}
				i++;
			}
			if(trip){
				overall.get(country2).army = troops;
				Game.mthis.players[overall.get(country2).owner].countries.remove(overall.get(country2));
				overall.get(country2).owner = overall.get(country1).owner;
				Game.mthis.players[overall.get(country1).owner].countries.add(overall.get(country2));
				overall.get(country1).army -= troops;
				break;
			}
		}
		Game.mthis.setMessage(message + " vs " + origOwner + " at " + overall.get(country1).name);
	}
	

	
	public static void attack2(int country1, int country2, int troops, ArrayList<Country> overall, Player[] players){
		int stroops = troops;
		if(troops < 2){
			return;
		}
		boolean trp = true;
		for(int country: overall.get(country1).nextTo){
			if(country == country2){
				trp = false;
			}
		}
		if(trp){
			return;
		}
		while(troops > 0){
			int at = players[overall.get(country1).owner].attackDice(country1), df = players[overall.get(country2).owner].defendDice(country2);
			if(at == -1){
				overall.get(country1).army -= (stroops - troops);
				break;
			}
			int[] attackDice = new int[at];
			int[] defendDice = new int[df];
			for(int i = 0; i != at; i++){
				attackDice[i] = Game.random.nextInt(6);
			}
			for(int i = 0; i != df; i++){
				defendDice[i] = Game.random.nextInt(6);
			}
			attackDice = sort(attackDice);
			defendDice = sort(defendDice);
			int i = 0;
			boolean trip = false;
			while(i < at && i < df){
				if(attackDice[i] > defendDice[i]){
					overall.get(country1).army--;
					troops--;
				} else {
					overall.get(country2).army--;
					if(overall.get(country2).army == 0){
						trip = true;
					}
				}
				i++;
			}
			if(trip){
				overall.get(country2).army = troops;
				players[overall.get(country2).owner].countries.remove(overall.get(country2));
				overall.get(country2).owner = overall.get(country1).owner;
				System.out.println("Setting owner of "+overall.get(country2).name+" to "+overall.get(country2).owner);
				players[overall.get(country1).owner].countries.add(overall.get(country2));
				overall.get(country1).army -= troops;
				break;
			}
		}
	}
	
	public static int[] sort(int[] array){
		int[] answer = new int[array.length];
		int[] used = new int[array.length];
		int count = 0;
		for(int i = 0; i != array.length; i++){used[i] = -1;}
		while(count != array.length){
			int biggest = -1;
			for(int i = 0; i != array.length; i++){
				if(used[i]==-1 && (biggest == -1 || array[i] > array[biggest])){
					biggest = i;
				}
			}
			used[biggest] = 1;
			answer[count] = array[biggest];
			count++;
		}
		return answer;
	}
	
	public static boolean joined(Country country1, Country country2){
		ArrayBlockingQueue<Country> queue = new ArrayBlockingQueue<Country>(1024);
		ArrayList<Country> countries = new ArrayList<Country>();
		queue.add(country1);
		while(!queue.isEmpty()){
			Country country = queue.remove();
			countries.add(country);
			for(int i: country.nextTo){
				if(country2.id == i){
					return true;
				}
				if(!countries.contains(overall.get(i)) && overall.get(i).owner == country1.owner){
					queue.add(overall.get(i));
				}
			}
		}
		return false;
	}
	
}
