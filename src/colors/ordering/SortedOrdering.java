package colors.ordering;
import java.util.Arrays;


public class SortedOrdering extends ColorOrdering {

	@Override
	public void order(int[] colors) {
		Arrays.sort(colors);
	}

}
