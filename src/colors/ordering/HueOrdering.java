package colors.ordering;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HueOrdering extends ColorOrdering {
	
	class ColorHue implements Comparable<ColorHue> {
		
		public float hue;
		public int color;
		
		public ColorHue(float hue, int color) {
			this.hue=hue;
			this.color=color;
		}

		@Override
		public int compareTo(ColorHue other) {
			if(this.hue < other.hue) {
				return 1;
			} else if (this.hue > other.hue) {
				return -1;
			} else {
				return 0;
			}
		}
		
	}

	@Override
	public void order(int[] colors) {
		
		List<ColorHue> list = new ArrayList<>(colors.length);
		for(int i=0; i<colors.length; i++) {
			int c = colors[i];
			int r = (c & 0x00FF0000) >> 16;
			int g = (c & 0x0000FF00) >> 8;
			int b = (c & 0x000000FF) >> 0;
			float[] hsb = Color.RGBtoHSB(r, g, b, null);
			list.add(new ColorHue(hsb[0], c));
		}
		
		assert colors.length == list.size();
		
		Collections.sort(list);
		
		for(int i=0; i<colors.length; i++) {
			colors[i] = list.get(i).color;
		}
	}

}
