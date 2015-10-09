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
		if(Game.settings[5]==0){
			strategy = tactics[id];
		}else{
			strategy = 0;
		}
	}
	
	public Player(Player player, ArrayList<Country> countries2){
		this.id = player.id;
		this.name = player.name;
		this.draft = player.draft;
		this.strategy = player.strategy;
		this.countries = new ArrayList<Country>();
		for(int i = 0; i != player.countries.size(); i++){
			countries.add(countries2.get(player.countries.get(i).id));
		}
	}
	
	public Player clone(ArrayList<Country> countries2){
		return new Player(this, countries2);
	}
	
	public void update(){
		if(countries.size() == 0){
			Continent.advanceGo();
			return;
		}
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
		if(draft<3){
			draft = 3;
		}
		System.out.println(Country.names[id] + " has "+draft+" draft");
		if(player&&Game.settings[3]!=1){return;}
		switch(strategy){
		case 0:
			tactic0();
			break;
		case 1:
			tactic1();
			break;
		case 2:
			tactic2(Continent.overall, Game.mthis.players);
			break;
		default:
			System.out.println("Tactic not recognised!");
			break;
		}
		Continent.advanceGo();
	}
	
	public void tactic0(){
		float[] continents = new float[6];
		int chosen = -1;
		for(Country country: countries){
			continents[country.continent]++;
		}
		for(int i = 0; i != continents.length; i++){
			continents[i] /= Game.mthis.continents[i].countries.length;
		}
		float[] continents2 = Continent.sort(continents);
		int current = 0;
		while(true){
			for(int i = 0; i != 6; i++){
				if(continents2[current] == continents[i]){
					chosen = i;
					break;
				}
			}
			if(continents2[current]==1.0f){
				current++;
				if(current==Game.mthis.continents.length){
					Game.mthis.setMessage(Country.names[id]+" won!");
					Game.mthis.mtime = -1;
					break;
				}
			} else {
				break;
			}
		}
		System.out.println(name+" wants "+Game.mthis.continents[chosen].name);
		// Seeing if there's an enemy next to each country
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
					//System.out.println("Adding "+draft+" to "+country.name);
					country.army += draft;
					draft = 0;
				} 
				if(country.army > 2 && country.continent == chosen){
					for(int i: country.nextTo){
						if(Continent.overall.get(i).owner != id){
							trip = false;
							Continent.attack(country.id, i, country.army-1);
							//System.out.println("Attacking "+Continent.overall.get(i).name+" with "+country.name+" with "+(country.army-1));
						}
					}
				}
			}
			if(trip){
				break;
			}
		}
		if(draft!=0){
			for(Country country: Game.mthis.continents[chosen].countries){
				if(country.owner == id){
					country.army += draft;
					draft = 0;
					break;
				}
			}
		}
		if(draft != 0){
			int trip = -1, trip2 = -1;
			for(Country country: Game.mthis.continents[chosen].countries){
				for(int i: country.nextTo){
					if(Continent.overall.get(i).owner == id && Continent.overall.get(i).continent != chosen){
						trip = i;
						trip2 = country.id;
					}
				}
			}
			if(trip != -1){
				Continent.overall.get(trip).army+=draft;
				Continent.attack(trip, trip2, Continent.overall.get(trip).army-1);
			} else if(countries.size()!=0){
				countries.get(0).army+=draft;
			}
		}
	}
	
	public void tactic1(){
		int max = 10;
		@SuppressWarnings("unchecked")
		ArrayList<Integer[]>[] goes = new ArrayList[max];
		int[][] drafts = new int[max][];
		boolean[] good = new boolean[max];
		for(int count = 0; count != max; count++){
			int go = Game.go;
			ArrayList<Country> countries = new ArrayList<Country>();
			for(Country country: Continent.overall){
				countries.add(country.clone());
			}
			Player[] players = new Player[Game.mthis.players.length];
			for(int i = 0; i != Game.mthis.players.length; i++){
				players[i] = Game.mthis.players[i].clone(countries);
			}
			ArrayList<Integer[]> goe = new ArrayList<Integer[]>();
			int[] draft = new int[this.draft];
			for(int i = 0; i != this.draft; i++){
				int next = Game.random.nextInt(this.countries.size());
				draft[i] = next;
				countries.get(this.countries.get(next).id).army++;
			}
			drafts[count] = draft;
			int averageGoes = 20;
			while(true){
				if(Game.random.nextInt(averageGoes) == 0){
					break;
				}
				int next = Game.random.nextInt(players[go].countries.size());
				int next2 = Game.random.nextInt(players[go].countries.get(next).nextTo.length);
				if(players[go].countries.get(next).army < 2 || countries.get(players[go].countries.get(next).nextTo[next2]).owner == players[go].countries.get(next).owner){
					continue;
				}
				int troops = Game.random.nextInt(players[go].countries.get(next).army-1)+1;
				Continent.attack2(players[go].countries.get(next).id, players[go].countries.get(next).nextTo[next2], troops, countries, players);
				goe.add(new Integer[]{next, next2, troops});
			}
			goes[count] = goe;
			go++;
			while(true){
				go%=Country.players;
				boolean trip = true;
				int trip2 = countries.get(0).owner;
				for(Country country: countries){
					if(country.owner != trip2){
						trip = false;
						break;
					}
				}
				if(trip){
					goes[count] = goe;
					drafts[count] = draft;
					good[count] = countries.get(0).owner==id;
					break;
				}
				players[go].tactic2(countries, players);
				go++;
			}
		}
		for(int i = 0; i != max; i++){
			if(!good[i]){continue;}
			System.out.println("i "+i+" is a good one");
			for(int next: drafts[i]){
				this.countries.get(next).army++;
			}
			for(Integer[] go: goes[i]){
				if(go[0] > countries.size()-1){
					continue;
				}
				int next = countries.get(go[0]).id;
				int next2 = countries.get(go[0]).nextTo[go[1]];
				Continent.attack(next, next2, go[2]);
			}
			break;
		}
		System.out.println("END SIM");
	}
	
	public void tactic2(ArrayList<Country> overall, Player[] players){
		int averageGoes = 10;
		this.update2();
		outer: for(int i = 0; i != this.draft; i++){
			int next = -1;
			boolean loop = true;
			while(loop){
				if(this.countries.size() < 1){
					break outer;
				}
				next = Game.random.nextInt(this.countries.size());
				for(int next2: this.countries.get(next).nextTo){
					if(this.countries.get(next).owner != overall.get(next2).owner){
						loop = false;
					}
				}
			}
			overall.get(this.countries.get(next).id).army++;
		}
		while(true){
			if(Game.random.nextInt(averageGoes) == 0){
				break;
			}
			if(this.countries.size() < 1){
				break;
			}
			int next = Game.random.nextInt(this.countries.size());
			int next2 = Game.random.nextInt(this.countries.get(next).nextTo.length);
			
			if(this.countries.get(next).army < 2 || overall.get(this.countries.get(next).nextTo[next2]).owner == this.countries.get(next).owner){
				continue;
			}
			int troops = Game.random.nextInt(this.countries.get(next).army-1)+1;
			Continent.attack2(this.countries.get(next).id, this.countries.get(next).nextTo[next2], troops, overall, players);
		}
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
	
	public void update2(){
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
		if(draft<3){
			draft = 3;
		}
	}
	
}
