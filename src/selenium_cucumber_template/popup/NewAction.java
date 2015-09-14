package selenium_cucumber_template.popup;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;


public class NewAction implements IObjectActionDelegate {

private Shell shell;
private CucumberSeleniumConsole console;
/**
* Constructor for Action1.
*/
public NewAction() {
	super();
	
	console = CucumberSeleniumConsole.getInstance();
}

/**
* @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
*/
public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	shell = targetPart.getSite().getShell();
}

public  String getProjectPath(ISelection selection){
	String path = null;
	if (selection instanceof TreeSelection) {
		TreeSelection ts = (TreeSelection) selection;
		TreePath[] tps = ts.getPaths();
		
		for (TreePath tp : tps) {
			int segsize = tp.getSegmentCount();

			for (int i = 0; i < segsize; ++i) {
			
				Object ooo = tp.getSegment(i);
				
				if (ooo instanceof File) {
					File ff = (File) ooo;
					String fullpath = ((IResource) ff).getLocation().toPortableString();
					String projectpath = ff.getPath();
					path = fullpath.substring(0, fullpath.length() - projectpath.length());
					break;
				}else if (ooo instanceof Project){
					Project pj = (Project)ooo;
					path = pj.getLocation().toPortableString();
				
					break;
				}
			}
		}
	}
	return path;
}

/**
* @see IActionDelegate#run(IAction)
*/
public void run(IAction action) {

	IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

	if(window == null) {
		MessageDialog.openInformation(
				shell,
				"Selenium-Cucumber",
				"Can't get window... please give this inform to domich.hwang@gmail.com");
		return;
	}

	final Shell shell = window.getShell();

	if(shell == null) {
		MessageDialog.openInformation(
				shell,
				"Selenium-Cucumber",
				"Can't get shell... please give this inform to domich.hwang@gmail.com");
		return;
	}

	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	if(page == null) {
		MessageDialog.openInformation(
				shell,
				"Selenium-Cucumber",
				"Can't get page... please give this inform to domich.hwang@gmail.com");
		return;
	}
	
	ISelection selection = page.getSelection();
	if(selection == null) {
		MessageDialog.openInformation(
				shell,
				"Selenium-Cucumber",
				"Can't get selection... please give this inform to domich.hwang@gmail.com");
		return;
	}
	if (selection instanceof IStructuredSelection) {
		IStructuredSelection ssel = (IStructuredSelection) selection;
		Object obj = ssel.getFirstElement();
		IFile file = (IFile) Platform.getAdapterManager().getAdapter(obj, IFile.class);

		if (file == null) {
			if (obj instanceof IAdaptable) {
				file = (IFile) ((IAdaptable) obj).getAdapter(IFile.class);
			}
		}

   
		if (file == null) {
   
 	   		MessageDialog.openInformation(
 	   				shell,
 	   				"Selenium Cucumber",
 	   				"Please select android apk file");
 	   		return;
   
		}
   
		String fileName = file.getName(); 
   
		int dotLoc = fileName.lastIndexOf('.');

		if (dotLoc != -1) {
			String ext = fileName.substring(dotLoc + 1);
			if (ext.equalsIgnoreCase("apk") == false) {
				MessageDialog.openInformation(
						shell,
						"Selenium Cucumber",
						"File extension must be \"apk\"");
				return;
			}
			else {
				String projectpath = getProjectPath(selection);
				
				String[] com = commandGenerate(projectpath, fileName);

				int result = doCmd(com, projectpath);
				System.out.println("doCmd result :"+ result);
			}
		}
	}
}


public int doCmd(String[] cmd, String projectpath) {

	int exitCode=9;
	
	Process p = null ;

	List<String> cmdt = new ArrayList<String>();
	
	for(int i = 0 ; i < cmd.length ; ++ i){
		cmdt.add(cmd[i]);
	}
	
	ProcessBuilder bld = new ProcessBuilder(cmdt);
	Map<String, String> env = bld.environment();
	String javaHome = System.getenv("PATH");
	
	env.put("PATH", "/usr/local/bin:" + javaHome + ":" + "/Users/hwangheeseon/Library/Android/sdk/tools:/Users/hwangheeseon/Library/Android/sdk/platform-tools");
	
	bld.directory(new File(projectpath));

	try {
		p = bld.start();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	try {
		p.waitFor();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	

	console.print("----------------------------------------------------------------------------------------------------");
	console.print("-----------------------------------Selenium Cucumber started----------------------------------------");
	console.print("----------------------------------------------------------------------------------------------------");
	console.print("\n");
   
	BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
	String line = null;
	try {
		while((line = br.readLine()) != null){
			console.print(line);
		}
	} catch (IOException e) {
		//// TODO Auto-generated catch block
		e.printStackTrace();
	}

	BufferedReader br1 = new BufferedReader(new InputStreamReader(p.getErrorStream()));
	String line1 = null;
	try {
		while((line1 = br1.readLine()) != null){
			console.print(line1);
		}
	} catch (IOException e) {
		//// TODO Auto-generated catch block
		e.printStackTrace();
	}
   
	return exitCode;
}

public void byRuntime(String[] command)
            throws IOException, InterruptedException {
    Runtime runtime = Runtime.getRuntime();
    Process process = runtime.exec(command);
    printStream(process);
}

public void byProcessBuilder(String[] command)
            throws IOException,InterruptedException {
    ProcessBuilder builder = new ProcessBuilder(command);
    Process process = builder.start();
    printStream(process);
}

private void printStream(Process process)
            throws IOException, InterruptedException {

    process.waitFor();
    try (InputStream psout = process.getInputStream()) {
        copy(psout, System.out);
    }
}

public void copy(InputStream input, OutputStream output) throws IOException {

    byte[] buffer = new byte[1024];
    int n = 0;
    while ((n = input.read(buffer)) != -1) {
        output.write(buffer, 0, n);
    }
}

public String[] commandGenerate(String projectpath, String appName){
	String[] cmd = new String[3];
	
	cmd[0] = "/bin/sh";
	cmd[1] = "-c";
	cmd[2] =  "cucumber PLATFORM=android APP_PATH=features"+File.separator + "apps" + File.separator  + appName;

	System.err.println("command[0] =           " + cmd[0]);
	System.err.println("command[1] =           " + cmd[1]);
	System.err.println("command[2] =           " + cmd[2]);

	return cmd;
}

/**
* @see IActionDelegate#selectionChanged(IAction, ISelection)
*/
public void selectionChanged(IAction action, ISelection selection) {
}

}
