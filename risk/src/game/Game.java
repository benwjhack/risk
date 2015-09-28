package game;

import static org.lwjgl.input.Keyboard.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.util.Random;

import main.Draw;
import main.Main;
import main.Setup;
import main.Settings;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.Color;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.opengl.Texture;

public class Game {

	public static Random random = new Random();
	public static int[] settings;
	public static Game mthis;
	public static int WIDTH, HEIGHT, RWIDTH, RHEIGHT, iHEIGHT, iWIDTH, player;
	public static int twidth, theight, go, stage;
	public static UnicodeFont FONT, FONT2;
	public static boolean run;
	public static UnicodeFont[] FONTS;
	
	public Texture[] images;
	
	public int[] selected = new int[]{-1, 0};
	public Button[] buttons;
	public Player[] players;
	public Continent[] continents;
	public int mousex, mousey, translate_x = 0, translate_y = 0;
	
	public Game(int country, int number){
		mthis = this;
		player = country;
		run = true;
	}
	
	public void init(){
		
		FONT = Main.FONT;
		FONT2 = Main.FONT2;
		
		WIDTH = Main.WIDTH;
		HEIGHT = Main.HEIGHT;
		
		RWIDTH = Main.RWIDTH;
		RHEIGHT = Main.RHEIGHT;
		
		iWIDTH = WIDTH * 2;
		iHEIGHT = HEIGHT * 2;
		
		for(Country country: Continent.overall){
			country.position();
		}
		
		go = 0;
		stage = 0;
		players[player].update();
		
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
		
		Mouse.poll();
		while(Mouse.next()){
			if(player != go){
				break;
			}
			if(Mouse.getEventButtonState()){
				int eventKey = Mouse.getEventButton();
				switch(eventKey){
				case 0:
					for(Button button: buttons){
						if(mousex > button.x && mousex < button.x + button.width && mousey > button.y && mousey < button.y + button.height){
							switch(button.id){
							case 0:
								(new Settings()).run(false);
								break;
							case 1:
								if(go==player){
									if(stage == 2){
										Continent.advanceGo();
										selected[0] = -1;
									} else {
										stage++;
										selected[0] = -1;
									}
								}
								break;
							}
						}
					}
					switch(stage){
					case 0:
						for(Country country: Continent.overall){
							if(country.owner!=player){continue;}
							int width = FONTS[2].getWidth(country.name+": "+country.army);
							int height = FONTS[2].getHeight(country.name+": "+country.army);
							boolean trip = false;
							if(Math.abs(mousex - country.rpos[0]) < width / 2 && Math.abs(mousey - country.rpos[1]) < height / 2){
								trip = true;
							} else if(country.pos[0] < 0.1 && Math.abs(mousex - country.rpos[0] + iWIDTH) < width / 2 && Math.abs(mousey - country.rpos[1]) < height / 2){
								trip = true;
							} else if(country.pos[0] > 0.9 && Math.abs(mousex - country.rpos[0] - iWIDTH) < width / 2 && Math.abs(mousey - country.rpos[1]) < height / 2){
								trip = true;
							}
							if(trip){
								players[player].draft--;
								country.army++;
								if(players[player].draft==0){
									stage = 1;
								}
								break;
							}
						}
						break;
					case 1:
						boolean trp = true;
						for(Country country: Continent.overall){
							int width = FONTS[2].getWidth(country.name+": "+country.army);
							int height = FONTS[2].getHeight(country.name+": "+country.army);
							boolean trip = false;
							if(Math.abs(mousex - country.rpos[0]) < width / 2 && Math.abs(mousey - country.rpos[1]) < height / 2){
								trip = true;
							} else if(country.pos[0] < 0.1 && Math.abs(mousex - country.rpos[0] + iWIDTH) < width / 2 && Math.abs(mousey - country.rpos[1]) < height / 2){
								trip = true;
							} else if(country.pos[0] > 0.9 && Math.abs(mousex - country.rpos[0] - iWIDTH) < width / 2 && Math.abs(mousey - country.rpos[1]) < height / 2){
								trip = true;
							}
							if(trip){
								if(selected[0] == country.id){
									selected[1] %= (country.army-1);
									selected[1]++;
								} else {
									if(players[player].countries.contains(country)){
										selected[0] = country.id;
										selected[1] = 1;
									} else {
										Continent.attack(selected[0], country.id, selected[1]);
										selected[0] = -1;
									}
								}
								trp = false;
								break;
							}
						}
						if(trp){
							selected[0] = -1;
						}
						break;
					case 2:
						trp = true;
						for(Country country: Continent.overall){
							int width = FONTS[2].getWidth(country.name+": "+country.army);
							int height = FONTS[2].getHeight(country.name+": "+country.army);
							boolean trip = false;
							if(Math.abs(mousex - country.rpos[0]) < width / 2 && Math.abs(mousey - country.rpos[1]) < height / 2){
								trip = true;
							} else if(country.pos[0] < 0.1 && Math.abs(mousex - country.rpos[0] + iWIDTH) < width / 2 && Math.abs(mousey - country.rpos[1]) < height / 2){
								trip = true;
							} else if(country.pos[0] > 0.9 && Math.abs(mousex - country.rpos[0] - iWIDTH) < width / 2 && Math.abs(mousey - country.rpos[1]) < height / 2){
								trip = true;
							}
							if(trip){
								if(selected[0] == country.id){
									selected[1] %= (country.army-1);
									selected[1]++;
								} else {
									if(selected[0] == -1 && players[player].countries.contains(country)){
										selected[0] = country.id;
										selected[1] = 1;
									} else if(players[player].countries.contains(country) && Continent.joined(country, Continent.overall.get(selected[0]))){
										Continent.overall.get(selected[0]).army -= selected[1];
										country.army += selected[1];
										selected[0] = -1;
										stage++;
										Continent.advanceGo();
									}
								}
								trp = false;
								break;
							}
						}
						if(trp){
							selected[0] = -1;
						}
						break;
					}
					break;
				default:
					break;
				}
			}
		}
		
		Keyboard.poll();
		while(Keyboard.next()){
			if(Keyboard.getEventKeyState()){
				int eventKey = Keyboard.getEventKey();
				switch(eventKey){
				case KEY_W:
					settings[1]++;
					if(settings[1] == 4){
						settings[1] = 2;
					}
					break;
				case KEY_S:
					settings[2] %= 2;
					settings[2]++;
					break;
				default:
					break;
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
		
		boolean trip = false;
		for(Continent cont: continents){
			for(Country country: cont.countries){
				if(country.city){
					Rectangle rect = new Rectangle((int)((country.pos[0] - Continent.cwidth) * iWIDTH),(int) (((country.pos[1] - Continent.cheight / 2)*iHEIGHT)),(int)(Continent.cwidth *2* iWIDTH),(int)(Continent.cheight * iHEIGHT));
					Draw.renderthistex(rect, images[1]);
				}
				if(country.capital){
					Rectangle rect = new Rectangle((int)((country.pos[0] - Continent.cwidth / 2) * iWIDTH),(int) (((country.pos[1] - Continent.cheight / 2)*iHEIGHT)),(int)(Continent.cwidth * iWIDTH),(int)(Continent.cheight * iHEIGHT));
					float[] colour = Country.colours[country.origOwner];
					Draw.renderthistexS(rect, images[2], colour[0], colour[1], colour[2], 1f);
				}

				float[] colour = Country.colours[country.owner];
				String string = country.name+": "+country.army;
				int width = FONTS[2].getWidth(string);
				int height = FONTS[2].getHeight(string);
				if(!trip && Math.abs(mousex - country.rpos[0]) < width / 2 && Math.abs(mousey - country.rpos[1]) < height / 2){
					trip = true;
					FONTS[2].drawString(country.rpos[0] - width / 2, country.rpos[1] - height / 2, string, new Color(255, 255, 255));
				} else {
					FONTS[2].drawString(country.rpos[0] - width / 2, country.rpos[1] - height / 2, string, new Color(colour[0], colour[1], colour[2]));
				}
				if(country.pos[0] < 0.1){
					int x = country.rpos[0] + iWIDTH;
					int y = country.rpos[1];
					if(!trip && Math.abs(mousex - x) < width / 2 && Math.abs(mousey - y) < height / 2){
						trip = true;
						FONTS[2].drawString(x - width / 2, y - height / 2, string, new Color(255, 255, 255));
					} else {
						FONTS[2].drawString(x - width / 2, y - height / 2, string, new Color(colour[0], colour[1], colour[2]));
					}
				}
				if(country.pos[0] > 0.9){
					int x = country.rpos[0] - iWIDTH;
					int y = country.rpos[1];
					if(!trip && Math.abs(mousex - x) < width / 2 && Math.abs(mousey - y) < height / 2){
						trip = true;
						FONTS[2].drawString(x - width / 2, y - height / 2, string, new Color(255, 255, 255));
					} else {
						FONTS[2].drawString(x - width / 2, y - height / 2, string, new Color(colour[0], colour[1], colour[2]));
					}
				}
				/*for(int connect: country.nextTo){ I've no idea why this code didn't work.
					if(connect == 9){
						System.out.println("Well then "+country.rpos[0] +" "+ country.rpos[1]+" "+ Continent.overall.get(connect).rpos[0]+" "+ Continent.overall.get(connect).rpos[1]);
					}
					Draw.drawLine(country.rpos[0], country.rpos[1], Continent.overall.get(connect).rpos[0], Continent.overall.get(connect).rpos[1], new float[]{0f, 0f, 0f, 1f});
				}*/
			}
		}
		
		Init.drawTop();
		
		for(Button button: buttons){
			button.render();
		}
		
		switch(stage){
		case 0:
			FONTS[2].drawString(mousex, mousey, ""+players[player].draft);
			break;
		case 1:
		case 2:
			if(selected[0] != -1){
				FONTS[2].drawString(mousex, mousey, ""+selected[1]);
			}
			break;
		}
		
		glPopMatrix();
		Display.update();
		
	}
	
	// Back end logic
	public void update(){
		
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
			for(Country country: Continent.overall){
				country.position();
			}
		} else if(isKeyDown(KEY_A)){
			if(!(iWIDTH/1.01<WIDTH/1.2 || iHEIGHT/1.01<HEIGHT)){
				iWIDTH/=1.01;
				iHEIGHT/=1.01;
				for(Country country: Continent.overall){
					country.position();
				}
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
