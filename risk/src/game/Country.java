package game;

public class Country {
	
	public String name;
	public float[] pos;
	
	public Country(String name, float x, float y){
		this.name = name;
		this.pos = new float[]{x, y};
	}
	
}
