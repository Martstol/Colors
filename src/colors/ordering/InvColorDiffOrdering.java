package colors.ordering;

import java.util.Arrays;
import java.util.Comparator;

import colors.difference.PixelDiff;

public class InvColorDiffOrdering extends ColorOrdering {

	@Override
	public void order(int[] colors) {
		Integer[] list = new Integer[colors.length];
		for(int i=0; i<colors.length; i++) {
			list[i] = colors[i];
		}
		
		Arrays.sort(list, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return -PixelDiff.colorDifference(o1, o2);
			}
		});
		
		for(int i=0; i<colors.length; i++) {
			colors[i] = list[i];
		}
	}

}
