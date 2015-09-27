package game;

import static org.lwjgl.opengl.GL11.glColor4f;

import java.awt.Font;
import java.io.*;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import main.Draw;
import main.IOHandle;
import main.Setup;

public class Init extends Thread{
	
	public void run(){
		init();
	}
	
	public void init(){
		
		/*try {
			String[] args = new String[]{"cmd.exe","/C","call","scripts/run.bat"};
			Process p = Runtime.getRuntime().exec(args);
			StreamGobbler a = new StreamGobbler(p.getInputStream(), "input");
			StreamGobbler b = new StreamGobbler(p.getErrorStream(), "error");
			a.start();
			b.start();
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		int[][] level = IOHandle.loadLevel("world.txt");
		Game.level = level;*/
		
		Setup.STATE = 2;
		Setup.drawText = "Setting up game...";
		
	}
	
	@SuppressWarnings("unchecked")
	public static void init2(){
		Game.FONTS = new UnicodeFont[4];
		Game.FONTS[0] = Setup.FONT;
		Game.FONTS[1] = Setup.FONT2;
		
		Font font = new Font(main.Main.FONT_TYPE, Font.BOLD, 30);
	    UnicodeFont FONT = new UnicodeFont(font);
	    FONT.addAsciiGlyphs();
	    FONT.addGlyphs(400, 600);
	    FONT.getEffects().add(new ColorEffect(java.awt.Color.white));
	    try {
	        FONT.loadGlyphs();
	    } catch (SlickException e) {
		    e.printStackTrace();
		    Display.destroy();
	    }
	    Game.FONTS[2] = FONT;
	    FONT = new UnicodeFont(font);
	    FONT.addAsciiGlyphs();
	    FONT.addGlyphs(400, 600);
	    FONT.getEffects().add(new ColorEffect(java.awt.Color.blue));
	    try {
	        FONT.loadGlyphs();
	    } catch (SlickException e) {
		    e.printStackTrace();
		    Display.destroy();
	    }
	    Game.FONTS[3] = FONT;
	    
	    Continent[] continents = new Continent[6];
	    Document doc = IOHandle.readXML("info/continents/continents.xml");
		NodeList nodes = doc.getDocumentElement().getChildNodes();
	    for(int i = 0; i != nodes.getLength(); i++){
	    	Node node = nodes.item(i);
	    	Country[] countries = new Country[node.getChildNodes().getLength()];
			for(int i2 = 0; i2 != node.getChildNodes().getLength(); i2++){
				Node cnode = node.getChildNodes().item(i2);
				String[] pos = cnode.getAttributes().getNamedItem("pos").getTextContent().split(" ");
				Country country = new Country(cnode.getAttributes().getNamedItem("name").getTextContent(), Float.parseFloat(pos[0]), Float.parseFloat(pos[1]));
				countries[i2] = country;
			}
			Continent continent = new Continent(node.getAttributes().getNamedItem("name").getTextContent(), Integer.parseInt(node.getAttributes().getNamedItem("bonus").getTextContent()), countries);
			continents[i] = continent;
		}
	    
	    Game.mthis.continents = continents;
	    
	    int sb = 1;
	    Game.mthis.buttons = new Button[sb];
		Game.mthis.buttons[0] = new Button(Setup.WIDTH-Game.FONTS[2].getWidth("Menu"), 0, "Menu", 1, true);
	    
		Setup.STATE = 3;
		Setup.drawText = "Done!";
	}
	
	public static void drawTop(){
		
		Draw.renderthiso(new Rectangle(Game.mthis.translate_x,Game.mthis.translate_y,Game.WIDTH,100), 1f, 1f, 1f, 0.5f);
		Draw.drawSquare(Game.mthis.translate_x, Game.mthis.translate_y, Game.WIDTH, 100);
		glColor4f(1f,1f,1f, 1f);
		
		/*Rectangle object = new Rectangle(Game.mthis.translate_x, Game.mthis.translate_y, Game.WIDTH, 100);
		
		glDisable(GL_TEXTURE_2D);

		glBegin(GL_QUADS);
		glVertex2i(object.getX(), object.getY()); 
		glVertex2i(object.getX() + object.getWidth(), object.getY()); 
		glVertex2i(object.getX() + object.getWidth(), object.getY() + object.getHeight()); 
		glVertex2i(object.getX(), object.getY() + object.getHeight()); 
		glEnd();
		
		glEnable(GL_TEXTURE_2D);*/
		
	}
	
}

class StreamGobbler extends Thread {
    InputStream is;
    String type;
    
    StreamGobbler(InputStream is, String type) {
        this.is = is;
        this.type = type;
    }
    
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null){
                System.out.println(type + ">" + line);
                if(line.equals("DONE")){
                	Setup.drawText = "Parsing level...";
                }
            }
        } catch (IOException ioe){
            ioe.printStackTrace();  
        }
    }
}