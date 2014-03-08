import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/*
 * Unoptimized Java implementation of this: http://codegolf.stackexchange.com/questions/22144/images-with-all-colors/22326#22326
 */

public class Colors {
	
	// Algorithm settings
	public static final boolean AVERAGE = false;
	public static final int NUMCOLORS = 32; // NUMCOLORS^3 has to be equal to WIDTH*HEIGHT
	public static final int WIDTH = 256;
	public static final int HEIGHT = 128;
	public static final int STARTX = 128;
	public static final int STARTY = 64;
	
	public static void main(String[] args) {
		final BufferedImage img = createImage();
		
		try {
			ImageIO.write(img, "png", new File("output.png"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		final JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		frame.add(new Canvas() {
			public void paint(Graphics g) {
				super.paint(g);
				g.drawImage(img, 0, 0, frame.getWidth(), frame.getHeight(), null);
			}
		}, BorderLayout.CENTER);
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public static BufferedImage createImage() {
		Integer[] colors = new Integer[NUMCOLORS*NUMCOLORS*NUMCOLORS];
		int[] pixels = new int[WIDTH*HEIGHT];
		assert pixels.length == colors.length;
		
		// Randomize the color order
		int q = 0;
		for(int i=0; i < NUMCOLORS; i++) {
			for(int j=0; j < NUMCOLORS; j++) {
				for(int k=0; k < NUMCOLORS; k++) {
					int r = i*255/(NUMCOLORS-1);
					int g = j*255/(NUMCOLORS-1);
					int b = k*255/(NUMCOLORS-1);
					colors[q++] = 0xFF000000 | r << 16 | g << 8 | b << 0;
				}
			}
		}
		final Random r = new Random(System.currentTimeMillis());
		for(int i=0; i < colors.length; i++) {
			int j = r.nextInt(colors.length - i) + i;
			int temp = colors[i];
			colors[i] = colors[j];
			colors[j] = temp;
		}
		/*Arrays.sort(colors, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return coldiff(o1, o2);
			}
		});*/
		
		// Constantly changing list of available coordinates (empty pixels which have non-empty neighbors)
		HashSet<Vec2D> available = new HashSet<>();
		
		// Loop through all the colors that we want to place
		int progressCounter = colors.length / 16;
		for(int i = 0; i < colors.length; i++) {
			int c = colors[i];
			
			Vec2D bestPos = null;
			if(available.isEmpty()) {
				// Use the starting posision
				bestPos = new Vec2D(STARTX, STARTY);
			} else {
				// Find the best place from the list of available coordinates
				// bestxy = available.AsParallel().OrderBy(xy => calcdiff(pixels, xy, colors[i])).First();
				int bestVal = Integer.MAX_VALUE;
				for(Vec2D pos : available) {
					int diff = calcdiff(pixels, pos, c);
					if(diff < bestVal) {
						bestVal = diff;
						bestPos = pos;
					}
				}
				assert bestVal != Integer.MAX_VALUE;
			}
			
			// Put the pixel where it belong
			assert pixels[bestPos.y*WIDTH + bestPos.x] == 0x00000000;
			pixels[bestPos.y*WIDTH + bestPos.x] = c;
			
			// Adjust the available list
			available.remove(bestPos);
			for(Vec2D nPos : bestPos.getNeighbors(WIDTH, HEIGHT)) {
				if(pixels[nPos.y*WIDTH + nPos.x] == 0x00000000) {
					available.add(nPos);
				}
			}
			
			if((i+1) == progressCounter) {
				System.out.println("Status: " + (progressCounter / (float)colors.length));
				progressCounter += colors.length / 16;
			}
			
		}
		assert available.size() == 0;
		
		BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		img.setRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);
		
		return img;
	}
	
	public static int average(List<Integer> l) {
		if(l.isEmpty()) throw new IllegalArgumentException("Cannot calculate the average of an empty list");
		
		int val=0;
		for(int i : l) {
			val += i;
		}
		return val / l.size();
	}
	
	public static int min(List<Integer> l) {
		if(l.isEmpty()) throw new IllegalArgumentException("Cannot find the minimum element in an empty list");
		
		int min = l.get(0);
		for(int i=1; i<l.size(); i++) {
			if(min < l.get(i)) {
				min = l.get(i);
			}
		}
		return min;
	}
	
	public static int calcdiff(int[] pixels, Vec2D p, int c) {
		List<Integer> diffs = new ArrayList<>(8);
		for(Vec2D np : p.getNeighbors(WIDTH, HEIGHT)) {
			int nc = pixels[np.y*WIDTH+np.x];
			if(nc != 0x00000000) {
				diffs.add(PixelDifference.coldiff(nc, c));
			}
		}
		
		if(AVERAGE) {
			return average(diffs);
		} else {
			return min(diffs);
		}
	}

}
