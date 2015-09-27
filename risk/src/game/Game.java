package game;

import static org.lwjgl.input.Keyboard.*;
import static org.lwjgl.input.Mouse.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;
import main.Draw;
import main.Main;
import main.Setup;
import main.Settings;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.Color;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.opengl.Texture;

public class Game {

	public static int[] settings;
	public static Game mthis;
	public static int WIDTH, HEIGHT, RWIDTH, RHEIGHT, iHEIGHT, iWIDTH, player;
	public static int twidth, theight;
	public static UnicodeFont FONT, FONT2;
	public static boolean run;
	public static UnicodeFont[] FONTS;
	
	public Texture[] images;
	
	public Button[] buttons;
	public Continent[] continents;
	public int mousex, mousey, translate_x = 0, translate_y = 0;
	
	public Game(int country, int number){
		mthis = this;
		player = country;
		run = true;
	}
	
	public void init(){
		
		images = Setup.mthis.images;
		
		FONT = Main.FONT;
		FONT2 = Main.FONT2;
		
		WIDTH = Main.WIDTH;
		HEIGHT = Main.HEIGHT;
		
		RWIDTH = Main.RWIDTH;
		RHEIGHT = Main.RHEIGHT;
		
		iWIDTH = WIDTH * 2;
		iHEIGHT = HEIGHT * 2;
		
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
			if(isKeyDown(KEY_ESCAPE)){
				run = false;
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
		
		for(Button button: buttons){
			if(isButtonDown(0)){
				if(mousex > button.x && mousex < button.x + button.width && mousey > button.y && mousey < button.y + button.height){
					(new Settings()).run(false);
				}
			}
		}
		
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

		Draw.renderthistex(new Rectangle(-iWIDTH,0,iWIDTH,iHEIGHT), images[0]);
		Draw.renderthistex(new Rectangle(0,0,iWIDTH,iHEIGHT), images[0]);
		Draw.renderthistex(new Rectangle(iWIDTH,0,iWIDTH,iHEIGHT), images[0]);
		
		for(Continent cont: continents){
			for(Country country: cont.countries){
				FONTS[2].drawString(country.pos[0] * iWIDTH, country.pos[1] * iHEIGHT, country.name, new Color(100, 100, 100));
			}
		}
		
		Init.drawTop();
		
		for(Button button: buttons){
			button.render();
		}
		
		glPopMatrix();
		Display.update();
		
	}
	
	// Back end logic
	public void update(){
		
		// MOUSE
		
		Mouse.poll();
		while(Mouse.next()){
			if(Mouse.getEventButtonState()){
				int eventKey = Mouse.getEventButton();
				switch(eventKey){
				case 0:
					break;
				}
			}
		}
		
		// MOUSE
		// SCREEN
		
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
		if(isKeyDown(KEY_Q)){
			iWIDTH*=1.01;
			iHEIGHT*=1.01;
		} else if(isKeyDown(KEY_A)){
			if(!(iWIDTH/1.01<WIDTH/1.2 || iHEIGHT/1.01<HEIGHT)){
				iWIDTH/=1.01;
				iHEIGHT/=1.01;
			}
		}
		if(translate_x < -iWIDTH*0.1){
			translate_x = (int) (-iWIDTH*0.1);
		} else if(translate_x + WIDTH > iWIDTH *1.1){
			translate_x = (int) (iWIDTH*1.1 - WIDTH);
		}
		if(translate_y < 0){
			translate_y = 0;
		} else if(translate_y + HEIGHT > iHEIGHT){
			translate_y = iHEIGHT - HEIGHT;
		}
		
		// SCREEN
		
		// BUTTON
		
		for(Button button: buttons){
			button.update();
		}
		
		// BUTTON
		
	}
	
}
