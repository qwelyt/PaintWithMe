import java.awt.*;
import javax.swing.*;

public class PaintWithMe extends JFrame{
	private Canvas c;

	public static void main(String[] args){
		new PaintWithMe(args);
	}

	public PaintWithMe(String[] args){
		c = new Canvas(args);
		add(c);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setSize(600,400);
		setVisible(true);
	}
}
