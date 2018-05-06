package Display;

import java.awt.*;
import java.net.URL;
import javax.swing.*;

import Main.Handler;

public class DisplayScreen {

	private JFrame frame;
	private Canvas canvas;
    private Handler handler;

	private URL iconURL;
	
	private String title;
	private int width, height;
	
	public DisplayScreen(Handler handler, String title, int width, int height){
	    this.handler = handler;
		this.title = title;
		this.width = width;
		this.height = height;

		createDisplay();
	}
	
	private void createDisplay(){
		frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setBackground(Color.black);

		iconURL = getClass().getResource("/Sheets/icon.png");
		ImageIcon icon = new ImageIcon(iconURL);
		frame.setIconImage(icon.getImage());

		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.setMaximumSize(new Dimension(width, height));
		canvas.setMinimumSize(new Dimension(width, height));
		canvas.setFocusable(false);
		canvas.setBackground(Color.black);
		
		frame.add(canvas);
		frame.pack();
	}

    /*
     * Shows a window with button options "Play Again" and "Exit"
     * @param String - message to be printed out on the window
     */
    Object[] options = { "Exit", "Play Again" };
    public void showWindow(String message) {
        JOptionPane pane = new JOptionPane();
        pane.setMessage(message);
        pane.setOptions(options);
        
        JDialog dialog = pane.createDialog(null, "Game over");
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.setVisible(true);

        Object selectedValue = pane.getValue();
        
        if (selectedValue.equals(options[1])) {
            dialog.dispose();
        } else if (selectedValue.equals(options[0])) {
            System.exit(0);
        }
    }
    
	public Canvas getCanvas() {
		return canvas;
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
}
