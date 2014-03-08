package colors;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Vec2D {
	
	public final int x, y;
	private int salt;
	
	private static final long seed = System.currentTimeMillis();
	
	public Vec2D(int x, int y) {
		this.x = x;
		this.y = y;
		salt = new Random(seed ^ ((long)x ^ y)).nextInt();
	}
	
	@Override
	public int hashCode() {
		return (x^(y<<16))^salt;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Vec2D) {
			Vec2D other = (Vec2D)obj;
			return other.x == this.x && other.y == this.y;
		} else {
			return false;
		}
	}
	
	public List<Vec2D> getNeighbors(int width, int height) {
		List<Vec2D> neighbors = new ArrayList<>(8);
		
		for(int dy = -1; dy <= 1; dy++) {
			int y = this.y+dy;
			
			if(y < 0 || y >= height) continue;
			
			for(int dx = -1; dx <= 1; dx++) {
				int x = this.x+dx;
				
				if(x < 0 || x >= width) continue;
				
				neighbors.add(new Vec2D(x, y));
			}
		}
		
		return neighbors;
	}
	
	@Override
	public String toString() {
		return "[" + x + ", " + y + "]";
	}

}
