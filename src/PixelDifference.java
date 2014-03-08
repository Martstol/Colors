
public abstract class PixelDifference {
	
	public static int coldiff(int c1, int c2) {
		int r = ((c1 & 0x00FF0000) >> 16) - ((c2 & 0x00FF0000) >> 16); 
		int g = ((c1 & 0x0000FF00) >> 8) - ((c2 & 0x0000FF00) >> 8);
		int b = ((c1 & 0x000000FF) >> 0) - ((c2 & 0x000000FF) >> 0);
		return r*r + g*g + b*b;
	}
	
	public abstract int calcdiff(int[] pixels, Vec2D p, int c);

}
