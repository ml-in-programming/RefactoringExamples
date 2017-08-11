package name_pending;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.DefaultCaret;

@SuppressWarnings("serial")
public class ConsoleWindow extends JFrame{
	public JTextArea console = new JTextArea(200,600);
	private JScrollPane scroller = new JScrollPane(console);
	
	ConsoleWindow()
	{
		//add our text area to the scroll area
		console = new JTextArea(200,600);
		DefaultCaret caret = (DefaultCaret)console.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		console.setText(">>Text Area Created.");
		scroller = new JScrollPane(console);
		console.setLineWrap(true);
		
		//make the scroll area scroll
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		this.add(scroller);
	}

}
