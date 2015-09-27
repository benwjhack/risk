package main;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.opengl.Texture;

import static org.lwjgl.opengl.GL11.*;

public class Draw {
	
	public static void DrawCirclea(float cx, float cy, float r, int num_segments) { 
		float theta = (float) (2 * 3.1415926 / (num_segments)); 
		float c = (float) Math.cos(theta);//precalculate the sine and cosine
		float s = (float) Math.sin(theta);
		float t;
		
		float x = r;//we start at angle = 0 
		float y = 0; 
	    
		GL11.glBegin(GL11.GL_LINE_LOOP); 
		for(int ii = 0; ii < num_segments; ii++) { 
			GL11.glVertex2f(x + cx, y + cy);//output vertex 
	        
			//apply the rotation matrix
			t = x;
			x = c * x - s * y;
			y = s * t + c * y;
		} 
		GL11.glEnd(); 
	}
	
	public static void DrawCircle(float cx, float cy, float r, int num_segments) { 
		float theta = (float) (2 * 3.1415926 / (num_segments)); 
		float tangetial_factor = (float) Math.tan(theta);//calculate the tangential factor 

		float radial_factor = (float) Math.cos(theta);//calculate the radial factor 
		
		float x = r;//we start at angle = 0 

		float y = 0; 
	    
		glBegin(GL_LINE_LOOP); 
		for(int ii = 0; ii < num_segments; ii++) { 
			glVertex2f(x + cx, y + cy);//output vertex 
	        
			//calculate the tangential vector 
			//remember, the radial vector is (x, y) 
			//to get the tangential vector we flip those coordinates and negate one of them 

			float tx = -y; 
			float ty = x; 
	        
			//add the tangential vector 

			x += tx * tangetial_factor; 
			y += ty * tangetial_factor; 
	        
			//correct using the radial factor 

			x *= radial_factor; 
			y *= radial_factor; 
		} 
		glEnd(); 
	}
	
	public static void DrawArc(float cx, float cy, float r, float start_angle, float arc_angle, int num_segments) { 
		float theta = arc_angle / (num_segments - 1);//theta is now calculated from the arc angle instead, the - 1 bit comes from the fact that the arc is open

		float tangetial_factor = (float) Math.tan(theta);

		float radial_factor = (float) Math.cos(theta);

		
		float x = (float) (r * Math.cos(start_angle));//we now start at the start angle
		float y = (float) (r * Math.sin(start_angle)); 
	    
		glBegin(GL_LINE_STRIP);//since the arc is not a closed curve, this is a strip now
		for(int ii = 0; ii < num_segments; ii++){ 
			glVertex2f(x + cx, y + cy);

			float tx = -y; 
			float ty = x; 

			x += tx * tangetial_factor; 
			y += ty * tangetial_factor; 

			x *= radial_factor; 
			y *= radial_factor; 
		} 
		glEnd(); 
	}
	
	public static void drawSquare(int a, int b, int width, int height){
		drawSquare(a, b, width, height, 0f, 0f, 0f);
	}
	
	public static void drawSquare(int a, int b, int width, int height,float q, float w, float e){
		GL11.glDisable(GL_TEXTURE_2D);
		GL11.glColor3f(q, w, e);
		
		GL11.glBegin(GL11.GL_LINE_STRIP);
		    GL11.glVertex2f(a,b);
		    GL11.glVertex2f(a+width,b);
		    GL11.glVertex2f(a+width,b+height);
		    GL11.glVertex2f(a,b+height);
		GL11.glEnd();
		glEnable(GL_TEXTURE_2D);
	}
	
	public static void drawFullSquare(int a, int b, int width, int height){
		GL11.glDisable(GL_TEXTURE_2D);
		GL11.glColor3f(0f, 0f, 0f);
		
		GL11.glBegin(GL11.GL_QUADS);
		    GL11.glVertex2f(a,b);
		    GL11.glVertex2f(a+width,b);
		    GL11.glVertex2f(a+width,b+height);
		    GL11.glVertex2f(a,b+height);
		GL11.glEnd();
		glEnable(GL_TEXTURE_2D);
	}
	
	public static void renderthis(Rectangle object, int a, int b, int c){
		glColor3f(a,b,c);
		glRectf(object.getX(), object.getY(), object.getX() + object.getWidth(), object.getY() + object.getHeight());
	}
	
	public static void renderthiso(Rectangle object, int a, int b, int c){
		glColor4f(a,b,c, 0.1f);
		
		glBegin(GL_QUADS);
		glVertex2i(object.getX(), object.getY()); 
		glVertex2i(object.getX() + object.getWidth(), object.getY()); 
		glVertex2i(object.getX() + object.getWidth(), object.getY() + object.getHeight()); 
		glVertex2i(object.getX(), object.getY() + object.getHeight()); 
		glEnd();
	}
	
	public static void renderthiso(Rectangle object, float a, float b, float c, float d){
		glDisable(GL_TEXTURE_2D);
		glColor4f(a,b,c, d);

		glBegin(GL_QUADS);
		glVertex2i(object.getX(), object.getY()); 
		glVertex2i(object.getX() + object.getWidth(), object.getY()); 
		glVertex2i(object.getX() + object.getWidth(), object.getY() + object.getHeight()); 
		glVertex2i(object.getX(), object.getY() + object.getHeight()); 
		glEnd();
		glEnable(GL_TEXTURE_2D);
	}
	
	public static void renderthistext(Rectangle object, Texture texture, int a, int b, int c, int d){
		renderthistex(object, texture, a, b, c, d, 1f);
	}
	
	public static void renderthistex(Rectangle object, Texture texture, int a, int b, int c, int d, float e){
		glColor4f(1f,1f,1f, e);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
		glBegin(GL_TRIANGLES);
	 
			glTexCoord2f(c, b);
			glVertex2i(object.getX() + object.getWidth(), object.getY());
			glTexCoord2f(a, b);
			glVertex2i(object.getX(), object.getY());
			glTexCoord2f(a, d);
			glVertex2i(object.getX(), object.getY() + object.getHeight());
			
			glTexCoord2f(a, d);
			glVertex2i(object.getX(), object.getHeight() + object.getY());
			glTexCoord2f(c, d);
			glVertex2i(object.getX() + object.getWidth(), object.getY() + object.getHeight());
			glTexCoord2f(c, b);
			glVertex2i(object.getX() + object.getWidth(), object.getY());
	 
		glEnd();

	}
	
	public static void renderthistex(Rectangle object, Texture texture){
		renderthistex(object,texture,0,0,1,1,1);
	}
	
	public static void renderthistex(Rectangle object, Texture texture, float d){
		renderthistex(object,texture,0,0,1,1,d);
	}
	
}
