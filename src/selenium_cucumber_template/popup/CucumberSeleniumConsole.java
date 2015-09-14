package selenium_cucumber_template.popup;

import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

public class CucumberSeleniumConsole{

	private String consolename = "Console";
	private static CucumberSeleniumConsole sc = null;
	private MessageConsole mc = null;
	private MessageConsoleStream mcs = null;
	
	protected Color ERROR = new Color(null, 255, 0, 0);
	protected Color NOTICE = new Color(null, 0, 0, 255);
	protected Color INFO = new Color(null, 0, 0, 0);
	
	private CucumberSeleniumConsole()
	{
		mc = findConsole(consolename);
		mcs = mc.newMessageStream();
	}
	
	public static CucumberSeleniumConsole getInstance()
	{
		if (sc == null)
			sc = new CucumberSeleniumConsole();
		
		return sc;
	}
	
	public void print(String msg)
	{
		mcs.println(msg);
		
	}
	
	public void println(String msg)
	{
		if(msg != null)
			mcs.println(msg);
	}
	
	public void printNotice(String msg)
	{
		//mcs.setColor(NOTICE);
		mcs.println("[NOTICE] : "+msg);
	}

	
	public void printError(String msg)
	{
		//mcs.setColor(ERROR);
		println("[ERROR] : "+msg);
		//mcs.setColor(DEFAULT);
	}

	public void printInfo(String msg)
	{
		//mcs.setColor(INFO);
		println("[INFO] : "+msg);
		//mcs.setColor(DEFAULT);
	}

	private MessageConsole findConsole(String name) {
	      ConsolePlugin plugin = ConsolePlugin.getDefault();
	      IConsoleManager conMan = plugin.getConsoleManager();
	      IConsole[] existing = conMan.getConsoles();
	      for (int i = 0; i < existing.length; i++)
	         if (name.equals(existing[i].getName()))
	            return (MessageConsole) existing[i];
	      //no console found, so create a new one
	      MessageConsole myConsole = new MessageConsole(name, null);
	      conMan.addConsoles(new IConsole[]{myConsole});
	      return myConsole;
	   }
}
