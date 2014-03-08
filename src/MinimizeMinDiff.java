import java.util.HashSet;
import java.util.List;

public class MinimizeMinDiff extends PixelDiff {
	
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
		int bestVal = Integer.MAX_VALUE;
		
		for(Vec2D pos : available) {
			List<Integer> list = colorDifferences(pixels, width, height, pos, color);
			int val = min(list);
			if(val < bestVal) {
				bestPos = pos;
				bestVal = val;
			}
		}
		
		return bestPos;
	}

}
