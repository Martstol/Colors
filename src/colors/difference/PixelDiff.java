package colors.difference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import colors.Vec2D;


public abstract class PixelDiff {
	
	public final static int colorDifference(int c1, int c2) {
		int r = ((c1 & 0x00FF0000) >> 16) - ((c2 & 0x00FF0000) >> 16); 
		int g = ((c1 & 0x0000FF00) >> 8) - ((c2 & 0x0000FF00) >> 8);
		int b = ((c1 & 0x000000FF) >> 0) - ((c2 & 0x000000FF) >> 0);
		return r*r + g*g + b*b;
	}
	
	public final static List<Integer> colorDifferences(int[] pixels, int width, int height, Vec2D p, int color) {
		List<Integer> diffs = new ArrayList<>(8);
		
		for(Vec2D np : p.getNeighbors(width, height)) {
			int nc = pixels[np.y*width + np.x];
			
			if(nc != 0x00000000) {
				diffs.add(colorDifference(nc, color));
			}
		}
		
		return diffs;
	}
	
	public abstract Vec2D calcPos(int[] pixels, int width, int height, HashSet<Vec2D> available, int color);

}
