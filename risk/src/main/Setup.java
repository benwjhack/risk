package main;

import static org.lwjgl.input.Keyboard.*;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;
import game.Game;
import game.Init;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.awt.Font;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

@SuppressWarnings("unused")
public class Setup {
	
	public static Game game;
	public static int WIDTH = 1000;
	public static int HEIGHT = 1000;
	public static UnicodeFont FONT2;
	public static UnicodeFont FONT;
	public static int RWIDTH, RHEIGHT, twidth, theight, STATE = 0;
	public static boolean run = true;
	
	public Texture black;
	public Texture purple;
	public Texture[] images;
	
	public int chosen = -1;
	public static String drawText = "";
	public String[] list = {"North America", "South America", "Europe", "Africa", "Asia", "Oceania"};
	public boolean[] colours;
	public int[][] text;
	public String[] texts;
	public boolean[] colours2;
	public int[][] text2;
	public String[] texts2;
	
	public int mousex, mousey, translate_x, translate_y;
	
	public void init_game(int[] settings){
		texts2 = new String[]{"Back", "Next"};
		text2 = new int[][]{{0, HEIGHT - Main.FONT.getHeight("Back")}, {WIDTH - FONT.getWidth("Next"),  HEIGHT - Main.FONT.getHeight("Back")}};
        colours2 = new boolean[text2.length];
		
		theight = HEIGHT / 10;
		twidth = theight;
		//images = new Texture[15];
		
		try {
			black = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/black.png")));
			purple = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/purple.png")));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        
        setupStrings(new String[]{list[0], list[1], list[2], list[3], list[4], list[5]});
		
	}
	
	public void setupStrings(String[] newa){
		
		texts = newa;

		int a = 5-Main.unlocked;
		//System.out.println(a);
		switch(a){
		case 0:
			texts = (new String[]{texts[0], texts[1], texts[2], texts[3], texts[4], texts[5]});
			break;
		case 1:
			texts = (new String[]{texts[0] + " (Locked)", texts[1], texts[2], texts[3], texts[4], texts[5]});
			break;
		case 2:
			texts = (new String[]{texts[0] + " (Locked)", texts[1] + " (Locked)", texts[2], texts[3], texts[4], texts[5]});
			break;
		case 3:
			texts = (new String[]{texts[0] + " (Locked)", texts[1] + " (Locked)", texts[2] + " (Locked)", texts[3], texts[4], texts[5]});
			break;
		case 4:
			texts = (new String[]{texts[0] + " (Locked)", texts[1] + " (Locked)", texts[2] + " (Locked)", texts[3] + " (Locked)", texts[4], texts[5]});
			break;
		case 5:
			texts = (new String[]{texts[0] + " (Locked)", texts[1] + " (Locked)", texts[2] + " (Locked)", texts[3] + " (Locked)", texts[4] + " (Locked)", texts[5]});
			break;
		}
		if(chosen == -1){
			chosen = a;
		}
        text = new int[texts.length][];
        colours = new boolean[text.length];
		
		for(int i = 0; i != text.length; i++){
        	text[i] = new int[]{(int) (WIDTH / 2.3 - Main.FONT.getWidth(texts[i]) / 2), HEIGHT / (text.length + 1) * (i + 1) - Main.FONT.getHeight(texts[i])};
        }
	}
	
	public void init(boolean initi){
		int[] settings = IOHandle.getSettings();
		if(initi){
			System.out.println("THIS SHOULD NEVER HAPPEN 123");
		} else {
			RWIDTH = Main.RWIDTH;
			RHEIGHT = Main.RHEIGHT;
		}
		init_game(settings);
	}
	
	public void run(boolean initi) {
		
		FONT = Main.FONT;
		FONT2 = Main.FONT2;
		
		WIDTH = Main.WIDTH;
		HEIGHT = Main.HEIGHT;
		
		init(initi);
		
		while (run) {
			
			glClear(GL_COLOR_BUFFER_BIT);
			
			glPushMatrix();
			
			glTranslatef(translate_x, translate_y, 0);
			
			if (Display.wasResized()) {
	            RWIDTH = Display.getWidth();
	            RHEIGHT = Display.getHeight();

	            GL11.glViewport(0, 0, RWIDTH, RHEIGHT);
	            GL11.glLoadIdentity();
			}
			
			// Retrieve the "true" coordinates of the mouse.
			mousex = (int) (Mouse.getX() * ((float)(WIDTH) / RWIDTH) - translate_x);
			mousey = (int) (HEIGHT - Mouse.getY() * ((float)(HEIGHT) / RHEIGHT) - 1 - translate_y);
			Draw.renderthistex(new Rectangle(0,0,WIDTH,HEIGHT), purple);
			
			logic();
			if(!run){
				break;
			}
			render();
			update();
			
			if(Display.isCloseRequested()){
				run = false;
				Main.run = false;
				//Main.mthis.end();
			}

			glPopMatrix();
			Display.update();
			Display.sync(60);
		}
		
		Main.RWIDTH = RWIDTH;
		Main.RHEIGHT = RHEIGHT;
		
	}
	
	public void logic(){
		
		if(STATE == 3){
			(new Game(0)).run();
		}
		
		Mouse.poll();
		while(Mouse.next()){
			if(Mouse.getEventButtonState()){
				int eventKey = Mouse.getEventButton();
				switch(eventKey){
				case 0:
					outLoop: for(int i = 0; i != text.length; i++){
						if(mousex >= text[i][0] && mousex <= text[i][0] + Main.FONT.getWidth(texts[i]) && mousey >= text[i][1] && mousey <= text[i][1] + Main.FONT.getHeight(texts[i])){
							if(Main.unlocked < 5-i){
								break outLoop;
							}
							String[] strings = list.clone();
							strings[i] += "-";
							setupStrings(strings);
							chosen = i;
						}
					}
					for(int i = 0; i != text2.length; i++){
						if(mousex >= text2[i][0] && mousex <= text2[i][0] + Main.FONT.getWidth(texts2[i]) && mousey >= text2[i][1] && mousey <= text2[i][1] + Main.FONT.getHeight(texts2[i])){
							switch(i){
							case 0:
								run = false;
								break;
							case 1:
								STATE = 1;
								drawText = "Generating level...";
								(new Init()).start();
								//game = new Game(chosen);
								//run = game.run();
								break;
							}
						}
					}
					break;
				default:
					//System.out.println(eventKey);
					break;
				}
			}
		}
		
		Keyboard.poll();
		while(Keyboard.next()){
			if(Keyboard.getEventKeyState()){
				int eventKey = Keyboard.getEventKey();
				switch(eventKey){
				case KEY_ESCAPE:
					run = false;
					break;
				default:
					break;
				}
			}
		}

		for(int i = 0; i != text.length; i++){
			if(mousex >= text[i][0] && mousex <= text[i][0] + FONT.getWidth(texts[i]) && mousey >= text[i][1] && mousey <= text[i][1] + FONT.getHeight(texts[i])){
				colours[i] = true;
			} else {
				colours[i] = false;
			}
		}
		

		for(int i = 0; i != text2.length; i++){
			if(mousex >= text2[i][0] && mousex <= text2[i][0] + FONT.getWidth(texts2[i]) && mousey >= text2[i][1] && mousey <= text2[i][1] + FONT.getHeight(texts2[i])){
				colours2[i] = true;
			} else {
				colours2[i] = false;
			}
		}
	}
	
	public void render(){
		
		if(STATE != 0){
			FONT.drawString(WIDTH / 2 - FONT.getWidth(drawText) / 2, HEIGHT / 2 - FONT.getHeight("I") / 2, drawText);
			if(STATE == 2){
				Init.init2();
			}
			return;
		}
		
		for(int i = 0; i != text.length; i++){
			if(colours[i]){
				FONT2.drawString(text[i][0], text[i][1], texts[i]);
				continue;
			}
			FONT.drawString(text[i][0], text[i][1], texts[i]);
		}
		
		for(int i = 0; i != text2.length; i++){
			if(colours2[i]){
				FONT2.drawString(text2[i][0], text2[i][1], texts2[i]);
				continue;
			}
			FONT.drawString(text2[i][0], text2[i][1], texts2[i]);
		}
		
	}
	
	public void update(){
		
	}
	
	public void end(){
		
	}
	
}
