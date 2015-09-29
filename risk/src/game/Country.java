package game;

public class Country {
	
	public static float[][] colours;
	public static String[] names;
	public static int idCount = 0, players;
	
	public String name;
	public boolean city, capital, done = false;
	public float[] pos;
	public int[] rpos, nextTo;
	public int id, army, owner, origOwner, continent;
	
	public Country(String name, float x, float y, int continent){
		this.name = name;
		this.pos = new float[]{x, y};
		this.rpos = new int[]{(int) (x*Game.iWIDTH), (int) (y*Game.iHEIGHT)};
		this.id = idCount++;
		this.continent = continent;
	}
	
	public void position(){
		if(!done){
			System.out.println(name + " not done!");
		}
		this.rpos = new int[]{(int) (pos[0]*Game.iWIDTH), (int) (pos[1]*Game.iHEIGHT)};
	}
	
}
