package colors.difference;
import java.util.HashSet;
import java.util.List;

import colors.Vec2D;


public class MaximizeAvgDiff extends PixelDiff {
	
	public float avg(List<Integer> list) {
		if(list.isEmpty()) throw new IllegalArgumentException("Cannot calculate the average of an empty list");
		
		int avg = 0;
		
		for(int i=0; i < list.size(); i++) {
			avg += list.get(i);
		}
		
		return avg/(float)list.size();
	}

	@Override
	public Vec2D calcPos(int[] pixels, int width, int height, HashSet<Vec2D> available, int color) {
		Vec2D bestPos = null;
		float bestVal = -1;
		
		for(Vec2D pos : available) {
			List<Integer> list = colorDifferences(pixels, width, height, pos, color);
			float val = avg(list);
			if(val > bestVal) {
				bestPos = pos;
				bestVal = val;
			}
		}
		
		return bestPos;
	}

}
