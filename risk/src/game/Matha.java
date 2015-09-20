package game;

public class Matha {
	
	public static double[] Direction(double x, double y, double x2, double y2){
		
		//Then, I try to get the angle.
		double angle = Math.atan2(y2 - y, x2 - x);
		double scaleX = (float)Math.cos(angle);
		double scaleY = (float)Math.sin(angle);
		
		double[] a = new double[2];
		a[0] = (scaleX);
		a[1] = (scaleY);
		
		return a;
	}
	
	public static double hypo(double opp, double adj){
		return Math.sqrt(Math.pow(opp,2) + Math.pow(adj, 2));
	}
	
}
