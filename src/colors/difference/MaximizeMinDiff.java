package colors.difference;

import java.util.HashSet;
import java.util.List;

import colors.Vec2D;


public class MaximizeMinDiff extends PixelDiff {
	
	public int min(List<Integer> list) {
		if(list.isEmpty()) throw new IllegalArgumentException("Cannot find the smallest element in an empty list");
		
		int min = list.get(0);
		
		for(int i=1; i < list.size(); i++) {
			if(list.get(i) < min) {
				min = list.get(i);
			}
		}
		
		return min;
	}

	@Override
	public Vec2D calcPos(int[] pixels, int width, int height, HashSet<Vec2D> available, int color) {
		Vec2D bestPos = null;
		int bestVal = -1;
		
		for(Vec2D pos : available) {
			List<Integer> diffs = colorDifferences(pixels, width, height, pos, color);
			int diff = min(diffs);
			if(diff > bestVal) {
				bestVal = diff;
				bestPos = pos;
			}
		}
		
		assert bestPos != null;
		
		return bestPos;
	}

}
