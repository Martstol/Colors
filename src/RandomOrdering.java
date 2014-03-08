import java.util.Random;


public class RandomOrdering extends ColorOrdering {
	
	private Random r;
	
	public RandomOrdering(long seed) {
		r = new Random(seed);
	}

	@Override
	public void order(int[] colors) {
		for(int i=0; i < colors.length; i++) {
			int j = r.nextInt(colors.length - i) + i;
			int temp = colors[i];
			colors[i] = colors[j];
			colors[j] = temp;
		}
	}

}
