import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/*
 * Optimized Java implementation of this: http://codegolf.stackexchange.com/questions/22144/images-with-all-colors/22326#22326
 */

public class Colors {
	
	// Algorithm settings
	public static final int NUMCOLORS = 32; // NUMCOLORS^3 has to be equal to WIDTH*HEIGHT
	public static final int WIDTH = 256;
	public static final int HEIGHT = 128;
	public static final int STARTX = 128;
	public static final int STARTY = 64;
	
	public static void main(String[] args) {
		final BufferedImage img = createImage(new MinimizeMinDiff(), WIDTH, HEIGHT, NUMCOLORS, STARTX, STARTY);
		
		try {
			ImageIO.write(img, "png", new File("output.png"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		final JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Canvas c = new Canvas() {
			public void paint(Graphics g) {
				super.paint(g);
				g.drawImage(img, 0, 0, frame.getWidth(), frame.getHeight(), null);
			}
		};
		Dimension d = new Dimension(WIDTH, HEIGHT);
		c.setPreferredSize(d);
		c.setMinimumSize(d);
		c.setMaximumSize(d);
		
		frame.add(c, BorderLayout.CENTER);
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public static BufferedImage createImage(PixelDiff pDiff, int width, int height, int nColors, int startX, int startY) {
		Integer[] colors = new Integer[nColors*nColors*nColors];
		int[] pixels = new int[width*height];
		assert pixels.length == colors.length;
		
		// Randomize the color order
		int q = 0;
		for(int i=0; i < nColors; i++) {
			for(int j=0; j < nColors; j++) {
				for(int k=0; k < nColors; k++) {
					int r = i*255/(nColors-1);
					int g = j*255/(nColors-1);
					int b = k*255/(nColors-1);
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
		
		HashSet<Vec2D> available = new HashSet<>();
		
		Vec2D start = new Vec2D(startX, startY);
		pixels[start.y*width + start.x] = colors[0];
		
		for(Vec2D n : start.getNeighbors(width, height)) {
			if(pixels[n.y*width + n.x] == 0x00000000) {
				available.add(n);
			}
		}
		
		for(int i = 1; i < colors.length; i++) {
			int color = colors[i];
			
			Vec2D best = pDiff.calcPos(pixels, width, height, available, color);
			assert best != null;
			
			// Put the pixel where it belong
			assert pixels[best.y*width + best.x] == 0x00000000;
			pixels[best.y*width + best.x] = color;
			
			// Adjust the available list
			available.remove(best);
			for(Vec2D n : best.getNeighbors(width, height)) {
				if(pixels[n.y*width + n.x] == 0x00000000) {
					available.add(n);
				}
			}
		}
		assert available.size() == 0;
		
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		img.setRGB(0, 0, width, height, pixels, 0, width);
		
		return img;
	}

}
