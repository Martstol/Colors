package colors.difference;

import java.util.HashSet;
import java.util.List;

import colors.Vec2D;


public class MaximizeMaxDiff extends PixelDiff {
	
	public int max(List<Integer> l) {
		if(l.isEmpty()) throw new IllegalArgumentException("Cannot find the smallest element in an empty list");
		
		int max = l.get(0);
		for(int i=1; i<l.size(); i++) {
			if(l.get(i) > max) {
				max = l.get(i);
			}
		}
		return max;
	}

	@Override
	public Vec2D calcPos(int[] pixels, int width, int height, HashSet<Vec2D> available, int color) {
		Vec2D bestPos = null;
		int bestVal = -1;
		
		for(Vec2D pos : available) {
			List<Integer> diffs = colorDifferences(pixels, width, height, pos, color);
			int diff = max(diffs);
			if(diff > bestVal) {
				bestVal = diff;
				bestPos = pos;
			}
		}
		
		assert bestPos != null;
		
		return bestPos;
	}

}
