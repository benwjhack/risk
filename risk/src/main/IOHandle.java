package main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;


public class IOHandle {
	
	public static Scanner getText(String path){
		Scanner file;
		try {
			file = new Scanner(new BufferedReader(new FileReader(path)));
			return file;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        return null;
	}

    public static String slurp(final InputStream is){
        final char[] buffer = new char[3019];
        final StringBuilder out = new StringBuilder();
        try {
            final Reader in = new InputStreamReader(is, "UTF-8");
            try {
                for (;;) {
                    int rsz = in.read(buffer, 0, buffer.length);
                    if (rsz < 0)
                        break;
                    out.append(buffer, 0, rsz);
                }
            }
            finally {
                in.close();
            }
        }
        catch (UnsupportedEncodingException ex) {
            System.out.println(ex.getMessage() + " " + ex.getMessage());
        }
        catch (IOException ex) {
        	System.out.println(ex.getMessage() + " " +ex.getMessage());
        }
        return out.toString();
    }
    
    public static int[] getSettings(){
    	String settings = null;
    	try {
			settings = slurp(new FileInputStream("res/.settings"));
		} catch (FileNotFoundException e) {
			System.out.println("Failure!");
		}
    	Scanner scan = new Scanner(settings);
    	int sn = 5;
    	int[] answers = new int[sn+3];
    	
    	for(int i = 0; i != sn; i++){
    		try{
    			answers[i] = scan.nextInt();
    		} catch(java.util.NoSuchElementException e){
    			answers[i] = 0;
    		}
    	}
    	
    	scan.close();
    	return answers;
    	
    }
    
    public static HashMap<String, String> getMultiSettings(){
    	HashMap<String, String> answers = new HashMap<String, String>();
    	String settings = null;
    	try {
			settings = slurp(new FileInputStream("res/multiplayer.settings"));
		} catch (FileNotFoundException e) {
			System.out.println("Failure!");
		}
    	if(settings.equals("")){
    		return answers;
    	}
    	String[] keys = new String[]{"host", "port"};
    	String[] pairs = settings.split(";");
    	for(int i = 0; i != pairs.length; i++){
    		String[] pair = pairs[i].toLowerCase().trim().split(":");
    		boolean trip = false;
    		for(String key: keys){
    			if(pair[0].equals(key)){trip=true;}
    		}
    		if(!trip){System.out.println("Unrecognized key - "+pair[0]+":"+pair[1]+"!");return null;}
    		answers.put(pair[0], pair[1]);
    	}
    	return answers;
    }
    
    public static Document readXML(String loc){
    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    	Document dom = null;

		try {

			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			//parse using builder to get DOM representation of the XML file
			dom = db.parse(loc);


		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		dom.normalize();
		
		explore(dom.getDocumentElement());
		
		//int oh = dom.getDocumentElement().getChildNodes().getLength();
		
		/*for(int i = 0; i != oh; i++){
			if(dom.getDocumentElement().getChildNodes().item(i) instanceof Text){
				dom.getDocumentElement().removeChild(dom.getDocumentElement().getChildNodes().item(i));
				oh--;i--;
			}
		}*/
		return dom;
    }
    
    public static void explore(Node node){
    	int oh = node.getChildNodes().getLength();
    	for(int i = 0; i != oh; i++){
    		if(node.getChildNodes().item(i) instanceof Text){
    			node.removeChild(node.getChildNodes().item(i));
    			i--;oh--;
    			//System.out.println("Removing "+node.getNodeName());
    			continue;
    		}
    		//System.out.println("Leaving "+node.getChildNodes().item(i).getNodeName());
    		if(node.getChildNodes().item(i).hasChildNodes()){
    			explore(node.getChildNodes().item(i));
    		}
    	}
    }
    
    /*public static Country[] getMCSettings(){
    	String settings = null;
    	try {
			settings = slurp(new FileInputStream("res/countries.settings"));
		} catch (FileNotFoundException e) {
			System.out.println("Failure!");
		}
    	return getMCSettings(settings);
    }
    
    public static Country[] getMCSettings(String settings){
    	Country[] countries = new Country[6];
    	boolean modded = false;
    	for(int i = 0; i != countries.length; i++){
    		countries[i] = new Country(i);
    	}
    	if(settings.equals("")){
    		return countries;
    	}
    	String[] keys = new String[]{"north america", "south america", "europe", "africa", "asia", "oceania"};
		String[] keys2 = new String[]{"money", "income", "popularity", "reserves", "strength", "soldiers"};
    	String[] pairs = settings.split(";");
    	for(int i = 0; i != pairs.length; i++){
    		String[] pair = pairs[i].toLowerCase().trim().split(":", 2);
    		int number = -1;
    		for(int i2 = 0; i2 != keys.length; i2++){
    			String key = keys[i2];
    			if(key.equals(pair[0])){
    				number = i2;
    				break;
    			}
    		}
    		if(number == -1){
    			System.out.println("Uh oh MCSettings error : "+pair[0]);
    			return null;
    		}
    		Country country = countries[number];
    		pair[1] = pair[1].replaceAll("[{}]", "");
    		String[] pairs2 = pair[1].split(",");
    		for(int a = 0; a != pairs2.length; a++){
    			String[] pair2 = pairs2[a].split(":");
    			if(pair2[0].equals(keys2[0])){
    				country.money = Integer.parseInt(pair2[1]);
    				modded = true;
    				continue;
    			}
    			if(pair2[0].equals(keys2[1])){
    				country.income = Integer.parseInt(pair2[1]);
    				country.bincome = country.income;
    				modded = true;
    				continue;
    			}
    			if(pair2[0].equals(keys2[2])){
    				country.popularity = Integer.parseInt(pair2[1]);
    				country.bpop = country.popularity;
    				modded = true;
    				continue;
    			}
    			if(pair2[0].equals(keys2[3])){
    				country.reserves = Integer.parseInt(pairs[1]);
    				modded = true;
    				continue;
    			}
    			if(pair2[0].equals(keys2[4])){
    				country.armyStrength = Integer.parseInt(pairs[1]);
    				modded = true;
    				continue;
    			}
    			if(pair2[0].equals(keys2[5])){
    				int size = Integer.parseInt(pairs[1]);
    				for(int count = 0; count != size; count++){
    					country.armyAdd(country.home[0], country.home[1]);
    				}
    				modded = true;
    				continue;
    			}
    			System.out.println("Unrecognised key : "+pair2[0]);
    			if(pair2[0].equals(keys2[])){
    				
    				continue
    			}
    		}
    	}
    	MultiplayerSetup.modded = modded;
    	return countries;
    }*/
    
    public static void writeSettings(int[] settings){
    	PrintWriter writer = null;
		try {
			writer = new PrintWriter("res/.settings", "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (UnsupportedEncodingException e){
			e.printStackTrace();
			return;
		}
		for(int i: settings){
			writer.print(i);
			writer.print(" ");
		}
		writer.flush();
		writer.close();
    }
    
    public static int[][] loadLevel(String loc){
    	String string = null;
    	try{
    		string = slurp(new FileInputStream("saves/"+loc));
    	} catch(IOException e){
    		System.out.println("Level failed to load!");
    	}
    	String[] strings = string.split("\n");
    	int[][] answer = new int[strings.length][];
    	for(int y = 0; y != strings.length; y++){
    		String[] strings2 = strings[y].replace("\r", "").split(" ");
    		for(int x = 0; x != strings2.length;x++){
    			if(y == 0){
    				answer[x] = new int[strings2.length];
    			}
    			if(strings2[x].equals("")){
    				continue;
    			}
    			answer[x][y] = Integer.parseInt(strings2[x]);
    		}
    	}
    	return answer;
    }
    
    /*
    public static ArrayList<String[]> getInfo(InputStream input){
        String slevel = slurp(input);
        Scanner scanner = new Scanner(slevel);

        String[] answer = slevel.split("\n");
        @SuppressWarnings("unused")
		List<String[]> meanIt = new ArrayList<String[]>();
        for(@SuppressWarnings("unused") String string: answer){

        }
        scanner.close();
        return null;

    }
	
	@SuppressWarnings("unused")
	public static Tile[][] getLevel(InputStream input){

        String slevel = slurp(input);
        Scanner scanner = new Scanner(slevel);

        int x = 0;
		int y = 0;
		String next;
        Tile[][] level;
        level = new Tile[150][10];
		while(scanner.hasNext()){
            next = scanner.next();
			if(next.equals("l")){
                //Log.d(Integer.toString(y),Integer.toString(y));
				y++;
				x = 0;
			} else {
				level[x][y] = ( new Tile(x, y, Integer.parseInt(next), Tiggles.twidth, Tiggles.theight));
				x++;
			}
		}

        loop: for(Tile[] tiles: level){

            lopp: for(Tile tile: tiles){

                if(tile.type == 1 || tile.type == 3){

                    tile.state = 1;
                    continue loop;

                }

            }

        }
		
		scanner.close();
		return level;
	}
	
	public static void writeLevel(Tile[][] world){
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("res/level", "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (UnsupportedEncodingException e){
			e.printStackTrace();
			return;
		}
		for(int y = 0; y != 10; y++){
			for(int x = 0; x != 150; x++){
				
				writer.print(world[x][y].type + " ");
				
			}
			writer.write("l\n");
		}
		writer.close();
		
		
	}
	
	public static int[] getLeveli(String path){
		
		int[] leveli = new int[3];
		
		Scanner file = getText(path);
		int next;
		
		for(int i = 0; i != 3; i++){
			next = file.nextInt();
			leveli[i] = next;
		}
		
		return leveli;
		
	}
	
	public static String[] getLevels(){
		
		File folder = new File("res/levels");
		File[] listOfFiles = folder.listFiles();
		String[] bob = new String[listOfFiles.length];
		int i = 0;

		for (File file : listOfFiles) {
		    bob[i] = file.getAbsolutePath();
		    i++;
		}
		
		return bob;
		
	}
	
	public static int getScore(){
		
		Scanner info = getText("res/info");
		if(info == null){
			return -1;
		} else {
			int score;
			try{
				String string = info.next();
				String sting = "";
				for(int i = 0; i != string.length(); i++){
					sting = sting + string.charAt(i);
				}
				score = Integer.parseInt(sting);
			} catch(java.util.NoSuchElementException e){
				PrintWriter outputStream;
				try {
					outputStream = new PrintWriter(new FileWriter(new File("res/info").getAbsolutePath(), true));
					outputStream.print(0);
					outputStream.close();
					System.out.println(99999);
				} catch (IOException e1) {
					return -1;
				}
				score = 0;
			}
			return score;
		}
		
	}
	
	public static int[] getScores(){
		
		int[] scores = new int[3];
		Scanner info = getText("res/info");
		if(info == null){
			return scores;
		} else {
			try{
				int i = 0;
				while(info.hasNextInt()){
					scores[i] = info.nextInt();
					i++;
				}
				return scores;
			} catch(java.util.NoSuchElementException e){
				PrintWriter outputStream;
				try {
					outputStream = new PrintWriter(new FileWriter(new File("res/info").getAbsolutePath(), true));
					outputStream.print(0);
					outputStream.close();
					System.out.println(99999);
				} catch (IOException e1) {
					return scores;
				}
			}
			return scores;
		}
		
	}
	
	public static void writeScore(int score){
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("res/info", "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
		writer.println(score);
		writer.close();
		
	}
	
	public static void writeScores(int[] scores){
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("res/info", "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
		for(int i: scores){
			writer.println(i);
		}
		writer.close();
		
	}*/
	
	/*public static void main(String[] args){
		
		/*for(Tilee tile : getLevel(64,48, "res/Text").values()){
			System.out.println(tile.x);
		}
		System.out.println(getLevel(64,48, "res/Text").size());
		System.out.println(18 * 48);
		int[] bob = new int[3];
		bob[0] = 1;
		bob[1] = 0;
		bob[2] = 2;
		//writeScores(bob);
		//for(int i: getScores()){
		//	System.out.println(i);
		//}
		
	}*/
	
}
