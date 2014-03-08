package colors;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import colors.difference.*;
import colors.ordering.*;

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
		long s=System.currentTimeMillis();
		final BufferedImage img = createImage(new ColorDiffOrdering(), new MaximizeMinDiff(), WIDTH, HEIGHT, NUMCOLORS, STARTX, STARTY);
		long f=System.currentTimeMillis();
		System.out.println("Time taken: " + ((f-s)/1000.0) + " seconds");
		
		try {
			ImageIO.write(img, "png", new File("output.png"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		final JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Canvas c = new Canvas() {
			private static final long serialVersionUID = 1L;

			public void paint(Graphics g) {
				super.paint(g);
				g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
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
	
	public static BufferedImage createImage(ColorOrdering ordering, PixelDiff pDiff, int width, int height, int nColors, int startX, int startY) {
		assert width*height == nColors*nColors*nColors;
		int[] colors = new int[nColors*nColors*nColors];
		int[] pixels = new int[width*height];
		
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
		ordering.order(colors);
		assert colors != null;
		assert colors.length == nColors*nColors*nColors;
		
		HashSet<Vec2D> available = new HashSet<>();
		
		Vec2D start = new Vec2D(startX, startY);
		pixels[start.y*width + start.x] = colors[0];
		
		for(Vec2D n : start.getNeighbors(width, height)) {
			if(pixels[n.y*width + n.x] == 0x00000000) {
				available.add(n);
			}
		}
		
		int progressCounter = colors.length / 16;
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
			
			if((i+1) == progressCounter) {
				System.out.println("Status: " + (progressCounter / (float)colors.length));
				progressCounter += colors.length / 16;
			}
			
		}
		assert available.size() == 0;
		
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		img.setRGB(0, 0, width, height, pixels, 0, width);
		
		return img;
	}

}
