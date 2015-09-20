package game;

import static org.lwjgl.input.Keyboard.KEY_DOWN;
import static org.lwjgl.input.Keyboard.KEY_LEFT;
import static org.lwjgl.input.Keyboard.KEY_RIGHT;
import static org.lwjgl.input.Keyboard.KEY_SPACE;
import static org.lwjgl.input.Keyboard.KEY_UP;
import static org.lwjgl.input.Keyboard.isKeyDown;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;
import main.Draw;
import main.Main;
import main.Setup;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.opengl.Texture;

public class Game {

	public static Game mthis;
	public static int WIDTH, HEIGHT, RWIDTH, RHEIGHT, iHEIGHT, iWIDTH, player;
	public static int twidth, theight;
	public static UnicodeFont FONT, FONT2;
	public static boolean run = true;
	public static int[][] level;
	public static UnicodeFont[] FONTS;
	
	public Texture[] images;
	
	public int mousex, mousey, translate_x = 0, translate_y = 0;
	
	public Game(int country){
		player = country;
	}
	
	public void init(){
		FONT = Main.FONT;
		FONT2 = Main.FONT2;
		
		WIDTH = Main.WIDTH;
		HEIGHT = Main.HEIGHT;
		
		RWIDTH = Main.RWIDTH;
		RHEIGHT = Main.RHEIGHT;
		
	}
	
	public boolean run(){
		mthis = this;
		init();
		
		while(run){
			
			// Retrieve the "true" coordinates of the mouse.
			mousex = (int) (Mouse.getX() * ((double)(WIDTH) / RWIDTH) - -translate_x);
			mousey = (int) (HEIGHT - Mouse.getY() * ((double)(HEIGHT) / RHEIGHT) - 1 - -translate_y);
			
			if(Display.isCloseRequested()){
				run = false;
				Setup.run = false;
				Main.run = false;
			}
			
			logic();
			draw();
			update();
			
			Display.sync(60);
			
		}
		
		Setup.RWIDTH = RWIDTH;
		Setup.RHEIGHT = RHEIGHT;
		
		return true;
	}
	
	// Top level logic
	public void logic(){
		
	}
	
	public void draw(){
		
		glClear(GL_COLOR_BUFFER_BIT);
		
		glPushMatrix();
		
		glTranslatef(-translate_x, -translate_y, 0);
		
		if (Display.wasResized()) {
            RWIDTH = Display.getWidth();
            RHEIGHT = Display.getHeight();
            GL11.glViewport(0, 0, RWIDTH, RHEIGHT);
            GL11.glLoadIdentity();
		}
		
		Init.drawTop();
		Draw.drawSquare(0, 0, 100, 100);
		
		glPopMatrix();
		Display.update();
		
	}
	
	// Back end logic
	public void update(){
		
		if(isKeyDown(KEY_LEFT) || mousex - translate_x < WIDTH / 10 && mousey - translate_y > 100){
			translate_x -= 3;
		} else if(isKeyDown(KEY_RIGHT) || mousex - translate_x > WIDTH - WIDTH / 10 && mousey - translate_y > 100){
			translate_x += 3;
		}
		if(isKeyDown(KEY_DOWN) || mousey - translate_y - 100 > (HEIGHT - 100) - (HEIGHT - 100) / 10){
			translate_y += 3;
		} else if(isKeyDown(KEY_UP) || mousey - translate_y - 100 < (HEIGHT - 100) / 10 && mousey - translate_y > 100 && mousey - translate_y < 200){
			translate_y -= 3;
		}
		if(isKeyDown(KEY_SPACE)){
			translate_x = 0; translate_y = 0;
		}
		/*if(translate_x < 0){
			translate_x = 0;
		} else if(translate_x + WIDTH > iWIDTH){
			translate_x = iWIDTH - WIDTH;
		}
		if(translate_y < 0){
			translate_y = 0;
		} else if(translate_y + HEIGHT > iHEIGHT){
			translate_y = iHEIGHT - HEIGHT;
		}*/
		
	}
	
}
