package game;

import java.awt.Font;
import java.io.*;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import main.Draw;
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
		
		Setup.STATE = 3;
		Setup.drawText = "Done!";
	}
	
	public static void drawTop(){
		
		Draw.renderthiso(new Rectangle(Game.mthis.translate_x,Game.mthis.translate_y,Game.WIDTH,100), 0, 0, 0);
		
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