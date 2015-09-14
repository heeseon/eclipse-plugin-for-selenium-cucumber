package selenium_cucumber_template.wizards;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import selenium_cucumber_template.Activator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;


public class ProjectUtil {
	
	
	public static final String SELENIUM_CUCUMBER_PATH = "assets"+File.separator+"Template";
	
	/**
	 * 프로젝트 생성 메소드
	 * 
	 * @param project
	 * @param locationURI
	 * @param monitor
	 * @throws CoreException
	 */
	public void createProject(IProject project, IProjectDescription desc, IProgressMonitor monitor) throws CoreException {
		if (monitor == null) {
			monitor= new NullProgressMonitor();
		}
		
		
		monitor.beginTask("Create Project", 10); 
		

		// create the project
		try {
			if (!project.exists()) {
				project.create(desc, monitor);
//				monitor= null;
			}
			if (!project.isOpen()) {
				project.open(monitor);
//				monitor= null;
			}
		} finally {
			
			System.out.println("project is open:"+project.isOpen());
			
			if (monitor != null) {
				monitor.done();
			}
		}
	}

	
	public void copyTemplateFile(IProject projectName, String projectPath){
		
		
//		String projectPath = projectName.getProject().getPPlatform.getLocation().toString() + File.separator + projectName;
		
		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		File templateFile = null;
		URL url= bundle.getEntry(SELENIUM_CUCUMBER_PATH); // bundleresource://547.fwk25421790:1/resources/index.html
		
		
		try {
			templateFile = new File(FileLocator.toFileURL(url).getPath());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			copyFolder(templateFile, new File(projectPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void copyFolder(File srcFolderPath, File destFolderPath)
			throws IOException {

		if (!srcFolderPath.isDirectory()) {
			
			//System.out.println("srcFolderPath.getName():"+srcFolderPath.getPath());
			//System.out.println("destFolderPath.getName():"+destFolderPath.getPath());
			
				// If it is a File the Just copy It to the new Folder
				InputStream in = new FileInputStream(srcFolderPath);
				
				
				//destFolderPath.get
				OutputStream out = new FileOutputStream(destFolderPath);
				
				byte[] buffer = new byte[1024];
				
				int length;
				
				while ((length = in.read(buffer)) > 0) {
					out.write(buffer, 0, length);
				}
				
				in.close();
				out.close();
				//System.out.println("File copied from " + srcFolderPath + " to "
				//		+ destFolderPath + " successfully");

		} else {

			
			if (!destFolderPath.exists()) {
				boolean result = destFolderPath.mkdir();
				System.out.println("Directory copied from " + srcFolderPath
						+ "  to " + destFolderPath + " successfully, " + destFolderPath.exists() + ", " + result );
			}

			
				String folder_contents[] = srcFolderPath.list();
				
				for (String file : folder_contents) {
					
					File srcFile = new File(srcFolderPath, file);
					File destFile = new File(destFolderPath, file);
					System.out.println("destFolderPath = " + destFolderPath.exists());
					
					copyFolder(srcFile, destFile);
				}
			}

	}
	
	
//	public void copyFiles (File srcFolder, IContainer destFolder) {
//	    for (File f: srcFolder.listFiles()) {
//	        if (f.isDirectory()) {
//	            IFolder newFolder = destFolder.getFolder(new Path(f.getName()));
//	            newFolder.create(true, true, null);
//	            copyFiles(f, newDest);
//	        } else {
//	            IFile newFile = project.getFile(new Path(f.getName())));
//	            newFile.create(new FileInputStream(f), true, null);
//	        }
//	    }
//	}
	
	
	
	public void createJSSettingFile(IProject project){
		String BROWSER_LIBRARY_PATH = "org.eclipse.wst.jsdt.launching.baseBrowserLibrary";
		String BROWSER_SUPER_TYPE_NAME = "Window";


		try {
			IFolder rscPath = project.getFolder(".settings");
			if(!rscPath.exists()) rscPath.create(true, true, new NullProgressMonitor());

			IPath fullPath = new Path(".settings");
			//.append(LibrarySuperType.SUPER_TYPE_NAME);

			setSharedProperty(project, fullPath.append("org.eclipse.wst.jsdt.ui.superType.name").toString(), BROWSER_SUPER_TYPE_NAME);
			setSharedProperty(project, fullPath.append("org.eclipse.wst.jsdt.ui.superType.container").toString(), BROWSER_LIBRARY_PATH);
		} catch (CoreException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}

	public void createJSDTScopeFile(IProject project){
		StringBuffer storeString = new StringBuffer();
		storeString.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		storeString.append("<classpath>\n");
//		storeString.append("\t<classpathentry excluding=\"davinci/.design/device/|davinci/.design/framework/|davinci/system/\" kind=\"src\" path=\"\"/>\n");
		storeString.append("\t<classpathentry kind=\"con\" path=\"org.eclipse.wst.jsdt.launching.JRE_CONTAINER\"/>\n");
		storeString.append("\t<classpathentry kind=\"con\" path=\"org.eclipse.wst.jsdt.launching.WebProject\">\n");
		storeString.append("\t\t<attributes>\n");
		storeString.append("\t\t\t<attribute name=\"hide\" value=\"true\"/>\n");
		storeString.append("\t\t</attributes>\n");
		storeString.append("\t</classpathentry>\n");
		storeString.append("\t<classpathentry kind=\"con\" path=\"org.eclipse.wst.jsdt.launching.baseBrowserLibrary\"/>\n");
		storeString.append("\t<classpathentry kind=\"output\" path=\"\"/>\n");
		storeString.append("</classpath>");

		try {
			IFolder rscPath = project.getFolder(".settings");
			if(!rscPath.exists()) rscPath.create(true, true, new NullProgressMonitor());

			IPath fullPath = new Path(".settings");
			//.append(LibrarySuperType.SUPER_TYPE_NAME);

			setSharedProperty(project, fullPath.append(".jsdtscope").toString(), storeString.toString());
		} catch (CoreException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}
	
	private void setSharedProperty(IProject project, String key, String value) throws CoreException{
		//IFile rscFile = this.project.getFile(key);
		IFile rscFile = project.getFile(key);
		byte[] bytes = null;
		try {
			bytes = value.getBytes("UTF-8"); // .classpath always encoded with UTF-8
		} catch (UnsupportedEncodingException e) {
			//Util.log(e, "Could not write .jsdtscope with UTF-8 encoding "); //$NON-NLS-1$
			// fallback to default
			bytes = value.getBytes();
		}
		InputStream inputStream = new ByteArrayInputStream(bytes);
		// update the resource content
		if (rscFile.exists()) {
			if (rscFile.isReadOnly()) {
				// provide opportunity to checkout read-only .classpath file (23984)
				ResourcesPlugin.getWorkspace().validateEdit(new IFile[]{rscFile}, null);
			}
			rscFile.setContents(inputStream, IResource.FORCE, null);
		} else {
			rscFile.create(inputStream, IResource.FORCE, null);
		}
	}

	
	
	
}
