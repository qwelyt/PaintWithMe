import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Canvas extends JPanel{
	private ArrayList<HashMap<String, Integer>> points;
	private Link link;
	private JSpinner sSize;
	private SpinnerNumberModel model;
	private JColorChooser jcc;

	public Canvas(String[] args){
		model = new SpinnerNumberModel(2, 1, 100, 1);
		sSize = new JSpinner(model);
		jcc = new JColorChooser();
		jcc.setPreviewPanel(new JPanel());
		add(sSize);
		add(jcc);

		setBackground(Color.white);
		addMouseListener(new mouseListener());
		addMouseMotionListener(new mouseMotion());

		points = new ArrayList<HashMap<String, Integer>>();
		link = new Link(this, args);
	}

	public synchronized void addPoint(HashMap<String, Integer> p){
		points.add(p);
		repaint();

		// send the point to the other client
		StringBuffer sb = new StringBuffer();
		sb.append(p.get("x") +",");
		sb.append(p.get("y") +",");
		sb.append(p.get("size") +",");
		sb.append(p.get("red") +",");
		sb.append(p.get("green") +",");
		sb.append(p.get("blue") +",");
		sb.append(p.get("alpha"));
		link.send(sb.toString());
	}

	// This handles incomming points (sent from other user)
	public synchronized void addPoint(String s){
		String[] arr = s.split(",");
		HashMap<String, Integer> p = new HashMap<String, Integer>();
		p.put("x", Integer.parseInt(arr[0]));
		p.put("y", Integer.parseInt(arr[1]));
		p.put("size", Integer.parseInt(arr[2]));
		p.put("red", Integer.parseInt(arr[3]));
		p.put("green", Integer.parseInt(arr[4]));
		p.put("blue", Integer.parseInt(arr[5]));
		p.put("alpha", Integer.parseInt(arr[6]));

		points.add(p);
		repaint();
	}

	public synchronized void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(Color.black); // CHANGE LATER
		HashMap<String, Integer> p;

		Iterator<HashMap<String, Integer>> it = points.iterator();
		while(it.hasNext()){
			p = (HashMap<String, Integer>)it.next();
			g.setColor(new Color(p.get("red"), p.get("green"), p.get("blue"), p.get("alpha")));
			g.fillOval(p.get("x"), p.get("y"), p.get("size"), p.get("size"));
		}
	}

	// When the user left clicks, add a point
	class mouseListener extends MouseAdapter{
		public void mousePressed(MouseEvent me){
			HashMap<String, Integer> p = createPoint(me.getPoint().x, me.getPoint().y); // Create the dot
			addPoint(p); // Add it to the canvas.
		}
	}

	// This handles when the mouse is dragged while left mouse is held
	class mouseMotion extends MouseMotionAdapter{
		public void mouseDragged(MouseEvent me){
			HashMap<String, Integer> p = createPoint(me.getPoint().x, me.getPoint().y);
			addPoint(p);
		}
	}

	private HashMap<String, Integer> createPoint(int meX, int meY){
			int size = Integer.valueOf((Integer)sSize.getValue()); // Get size of the dot that is drawn.
			Color colour = jcc.getColor();
			
			// Make sure the mouse is center of the dot.
			int x = meX - size/2;
			int y = meY - size/2;

			HashMap<String, Integer> p = new HashMap<String, Integer>();

			// Add all values regarding the dot.
			p.put("x", x);
			p.put("y", y);
			p.put("size", size);
			p.put("red", colour.getRed());
			p.put("green", colour.getGreen());
			p.put("blue", colour.getBlue());
			p.put("alpha", colour.getAlpha());

			return p;
	}
}
