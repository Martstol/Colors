public class Vec2D {
	
	public int x, y;
	
	public Vec2D(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public int hashCode() {
		return x^(y<<16);
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

}
