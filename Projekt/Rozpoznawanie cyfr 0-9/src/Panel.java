import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JPanel;


public class Panel extends JPanel implements MouseMotionListener, MouseListener {
	
	
	private Point staryPunkt;
	private ArrayList<Pair<Point, Point>> listaPunktow;
	private Border characterBorder;
	private DigitizeBorder digitizeBorder;
	

	public Panel() {
		listaPunktow = new ArrayList<Pair<Point,  Point> >();
		
		addMouseMotionListener(this);
		addMouseListener(this);
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(Color.WHITE);
		
		g2d.fillRect(0, 0, getWidth(), getHeight());
		
		if (digitizeBorder != null) {
			g2d.setColor(Color.LIGHT_GRAY);
			
			for (Pair<Point, Point> p : digitizeBorder)
				g2d.drawLine((int) p.getLeft().getX(), (int) p.getLeft().getY(), (int) p.getRight().getX(), (int) p.getRight().getY());
		}
			
		if (characterBorder != null) {
			g2d.setColor(Color.RED);
			
			g2d.drawLine(0, characterBorder.getTop(), getWidth(), characterBorder.getTop());
			g2d.drawLine(0, characterBorder.getBottom(), getWidth(), characterBorder.getBottom());
			g2d.drawLine(characterBorder.getLeft(), 0, characterBorder.getLeft(), getHeight());
			g2d.drawLine(characterBorder.getRight(), 0, characterBorder.getRight(), getHeight());
		}

	    //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g2d.setColor(Color.BLACK);
	    
	    for (Pair<Point, Point> p : listaPunktow)
	    	g2d.drawLine((int) p.getLeft().getX(), (int) p.getLeft().getY(), (int) p.getRight().getX(), (int) p.getRight().getY());
	}
	
	public void clear() {
		listaPunktow.clear();
		
		characterBorder = null;
		digitizeBorder = null;
		
		
		
		repaint();
	}
	
	public BufferedImage getImage() {
		BufferedImage img = new BufferedImage(getWidth() + Ustawienia.KOLUMNA, getHeight() + Ustawienia.WIERSZ, BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = img.createGraphics();
	    paint(g);
	    
	    return img;
	}

	private DigitizeBorder digitalizuj(BufferedImage img) {
		int width = characterBorder.getWidth() / Ustawienia.KOLUMNA;
		int height = characterBorder.getHeight() / Ustawienia.WIERSZ;
		
		DigitizeBorder digitizeBorder = new DigitizeBorder();
		
		for (int i = 0; i < Ustawienia.WIERSZ; i++) {
			Point l = new Point(characterBorder.getLeft(), characterBorder.getTop() + height * i);
			Point r = new Point(characterBorder.getRight(), characterBorder.getTop() + height * i);
			
			Pair<Point, Point> p = new Pair<Point, Point>(l, r);
			
			digitizeBorder.add(p);
		}
		
		for (int i = 0; i < Ustawienia.KOLUMNA; i++) {
			Point l = new Point(characterBorder.getLeft() + width * i, characterBorder.getTop());
			Point r = new Point(characterBorder.getLeft() + width * i, characterBorder.getBottom());
			
			Pair<Point, Point> p = new Pair<Point, Point>(l, r);
			
			digitizeBorder.add(p);
		}
		
		return digitizeBorder;
	}
	
	private Border findBorder(BufferedImage img) {
		int t = 0, b = 0, l = 0, r = 0;

		top:
		for (int i = 0; i < getHeight(); i++)
			for (int j = 0; j < getWidth(); j++)
				if (img.getRGB(j, i) == -16777216) {
					t = i;
					break top;
				}
	    
	    bottom:
	    for (int i = getHeight() - 1; i >= 0; i--)
			for (int j = getWidth() - 1; j >= 0; j--)
				if (img.getRGB(j, i) == -16777216) {
					b = i;
					break bottom;
				}
				
		left:
		for (int i = 0; i < getWidth(); i++)
			for (int j = 0; j < getHeight(); j++)
				if (img.getRGB(i, j) == -16777216) {
					l = i;
					break left;
				}
		
		right:
		for (int i = getWidth() - 1; i >= 0 ; i--)
			for (int j = getHeight() - 1; j >= 0 ; j--)
				if (img.getRGB(i, j) == -16777216) {
					r = i;
					break right;
				}
				
		return new Border(t, b, l, r);
	}
	

	public ArrayList<Integer> getInput() {
		boolean[][] character = getDigitizedCharacter();
		ArrayList<Integer> input = new ArrayList<Integer>();
		
		for (int i = 0; i < character.length; i++)
			for (int j = 0; j < character[0].length; j++)
				if (character[i][j])
					input.add(1);
				else
					input.add(0);
		
		return input;
	}
		
	public boolean[][] getDigitizedCharacter() {
		BufferedImage img = getImage();
		
		characterBorder = findBorder(img);
		
		boolean[][] r = getDigitizedCharacter(characterBorder, img);
		
		characterBorder = null;
		
		return r;
	}
	
	public boolean[][] getDigitizedCharacter(Border characterBorder, BufferedImage img) {
		int width = characterBorder.getWidth() / Ustawienia.KOLUMNA + 1;
		int height = characterBorder.getHeight() / Ustawienia.WIERSZ + 1;
		
		boolean[][] digit = new boolean[Ustawienia.WIERSZ][Ustawienia.KOLUMNA];
		
		for (int i = 0; i < Ustawienia.WIERSZ; i++)
			for (int j = 0; j < Ustawienia.KOLUMNA; j++)
				for (int k = characterBorder.getTop() + i * height; k < characterBorder.getTop() + i * height + height; k++)
					for (int l = characterBorder.getLeft() + j * width; l < characterBorder.getLeft() + j * width + width; l++) {
						if (img.getRGB(l, k) == -16777216) {
							digit[i][j] = true;
						}
						
						//digitizeBorder.add(new Pair<Point, Point>(new Point(l, k), new Point(l, k)));
					}
			
		return digit;
	}
	
	public void znajdzCyfre() {
		BufferedImage img = getImage();
		
		znajdzCyfre(img);
	}
	
	public void znajdzCyfre(BufferedImage img) {
		characterBorder = findBorder(img);
		digitizeBorder = digitalizuj(img);
		
		repaint();
	}

	
	
	private class Border {
		
		private int top;
		private int bottom;
		private int left;
		private int right;
		
		public Border(int t, int b, int l, int r) {
			top = t;
			bottom = b;
			left = l;
			right = r;
		}
		
		public int getTop() {
			return top;
		}
		public int getBottom() {
			return bottom;
		}
		public int getLeft() {
			return left;
		}
		public int getRight() {
			return right;
		}
		
		public int getWidth() {
			return right - left;
		}
		
		public int getHeight() {
			return bottom - top;
		}
		
	}
	
	private class DigitizeBorder implements Iterable<Pair<Point, Point>> {
		
		ArrayList<Pair<Point, Point>> list;
		
		public DigitizeBorder() {
			list = new ArrayList<Pair<Point,Point>>();
		}
		
		public void add(Pair<Point, Point> l) {
			list.add(l);
		}

		@Override
		public Iterator<Pair<Point, Point>> iterator() {
			return list.iterator();
		}
		
	}
	
	@Override
	public void mouseDragged(java.awt.event.MouseEvent e) {
		Pair<Point, Point> p = new Pair<Point, Point>(staryPunkt, e.getPoint());
		
		listaPunktow.add(p);
		
		staryPunkt = e.getPoint();
		
		repaint();
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		staryPunkt = e.getPoint();
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
	}
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}
	@Override
	public void mouseMoved(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
		
	}
	
}
