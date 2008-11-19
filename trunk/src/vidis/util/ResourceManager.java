package vidis.util;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

/**
 * This class provides the Resources in a static way to all other classes
 * 
 * @author Christoph
 * 
 */
public class ResourceManager {
	private ResourceManager() {
	}

	// private static final long initialMemory;
	// static {
	// initialMemory = getMemoryUsed();
	// DebugJoe.watchObject(new MemoryWatcher());
	// }

	private static String pathSeperator;
	private static String rootPath;
	private static String shaderPath;
	private static String dataPath;
	/**
	 * local path for modules (msim storage)
	 */
	private static String modulesPath;
	/**
	 * here someone can put jar archives containing modules
	 */
	private static String dropInPath;
	/**
	 * static constructor getting the path seperator TODO: Warning this needs to
	 * be testet for other platforms works on Windows 2000,
	 * 
	 * NOTE BY DOMINIK: edited pathSeparator for all supported java platforms
	 * 
	 * This script is based on the fact that (hopefully) every vm has path to
	 * resources.jar in its sun.boot.class.path It simply takes the symbol
	 * before resources.jar as the path seperator
	 */
	static {
		pathSeperator = File.separator;
		rootPath = "." + pathSeperator;
		shaderPath = rootPath + "shaders" + pathSeperator;
		dataPath = rootPath + "data";
		modulesPath = dataPath + pathSeperator + "modules";
		dropInPath = dataPath + "dropIn";
		
		// modify class path in order to be able to load classes from the drop in folder directly
		System.setProperty("java.class.path", System.getProperty("java.class.path") + File.pathSeparatorChar + dropInPath);
	}

	/**
	 * This Function translates the shader name into a valid path
	 * 
	 * @param name
	 * @return valid shader path
	 */
	public static String getShaderPath(String name) {
		return shaderPath + name;
	}

	public static String getShaderCode(String name) {
		// FIXME
		return "";
	}

	public static final long getMemoryTotal() {
		return Runtime.getRuntime().totalMemory();
	}

	public static final long getMemoryFree() {
		return Runtime.getRuntime().freeMemory();
	}

	public static final long getMemoryMax() {
		return Runtime.getRuntime().maxMemory();
	}

	public static final long getMemoryTotalUsed() {
		return getMemoryTotal() - getMemoryFree();
	}

	public static ImageIcon getImageIcon(String icon) {
		return new ImageIcon(dataPath + pathSeperator + "resources" + pathSeperator + "images" + pathSeperator + icon);
	}
	
	public static List<File> getModules() {
		// check drop in folder for new modules and class files
		// System.getProperty("java.class.path");
		File dropInFolder = new File(ResourceManager.dropInPath);
		if(dropInFolder.exists()) {
			if(dropInFolder.isDirectory()) {
				// TODO detect all possible .jar archives
				// TODO load all possible .jar archives (if not loaded yet)
				// TODO detect all possible .msim files (within .jar archives)
			}
		}
		
		// check default folder for built in modules
		List<File> modules = new ArrayList<File>();
		File moduleFolder = new File(modulesPath);
		if(moduleFolder.exists()) {
			if(moduleFolder.isDirectory()) {
				if(! moduleFolder.getName().startsWith(".")) {
					for(File module : moduleFolder.listFiles()) {
						if(module.isDirectory()) {
							if(! module.getName().startsWith(".")) {
								modules.add(module);
							}
						}
					}
				}
			}
		}
		return modules;
	}
	
	public static List<File> getModuleFiles(File module) {
		List<File> moduleFiles = new ArrayList<File>();
		File moduleFolder = module;
		if(moduleFolder.exists()) {
			if(moduleFolder.isDirectory()) {
				if(! moduleFolder.getName().startsWith(".")) {
					for(File moduleFile : moduleFolder.listFiles()) {
						if(moduleFile.isFile() && moduleFile.canRead() && moduleFile.getName().toLowerCase().endsWith(".msim") ) {
							moduleFiles.add(moduleFile);
						}
					}
				}
			}
		}
		return moduleFiles;
	}

	public static File getModuleFile(String module, String moduleFile) {
		return new File(modulesPath + pathSeperator + module + pathSeperator + moduleFile);
	}
	
	// --------------------- fonts
	public static String FONT_ARIAL = "ARIAL.TTF";
	public static String FONT_ARIAL_BOLD = "ARIALBD.TTF";
	public static String FONT_ARIAL_BOLD_ITALIC = "ARIALBI.TTF";
	public static String FONT_ARIAL_ITALIC = "ARIALI.TTF";
	public static String FONT_COURIER = "COUR.TTF";
	public static String FONT_COURIER_BOLD = "COURBD.TTF";
	public static String FONT_COURIER_BOLD_ITALIC = "COURBI.TTF";
	public static String FONT_COURIER_ITALIC = "COURI.TTF";
	public static String FONT_TIMESNEWROMAN = "TIMES.TTF";
	public static String FONT_TIMESNEWROMAN_BOLD = "TIMESBD.TTF";
	public static String FONT_TIMESNEWROMAN_BOLD_ITALIC = "TIMESBI.TTF";
	public static String FONT_TIMESNEWROMAN_ITALIC = "TIMESI.TTF";
	public static String FONT_VERDANA = "verdana.TTF";
	public static String FONT_VERDANA_BOLD = "verdanab.TTF";
	public static String FONT_VERDANA_BOLD_ITALIC = "verdanaz.TTF";
	public static String FONT_VERDANA_ITALIC = "verdanai.TTF";
	
	private static Map<String, Map<Float, Font>> fontCache = new HashMap<String, Map<Float, Font>>();
	
	private static Font createFont(String filename) throws FontFormatException, IOException {
		Font font = Font.createFont(Font.TRUETYPE_FONT, new File(dataPath + pathSeperator + "resources" + pathSeperator + "fonts" + pathSeperator + filename));
		return font;
	}
	
	
	/**
	 * retrieves a font
	 * the font is created and cached by the resource manager
	 * @param filename
	 * @return
	 */
	public static Font getFont(String filename) throws FontFormatException, IOException {
		return getFont( filename, 130 );
		
	}
	
	/**
	 * retrieves a font
	 * the font is created and cached by the resource manager, then the size is applied
	 * the size is not cached
	 * @param filename
	 * @param size
	 * @return
	 */
	public static Font getFont(String filename, float size) throws FontFormatException, IOException {
		if( !fontCache.containsKey(filename) )
			fontCache.put(filename, new HashMap<Float, Font>());
		Font font = fontCache.get( filename ).get( size );
		if ( font == null ) {
			font = createFont( filename );
			font = font.deriveFont(size);
			fontCache.get(filename).put(size, font);
		}
		return font;
	}	

	// public static final long getMemoryInit() {
	// return initialMemory;
	// }

	// public static final long getMemoryUsed() {
	// return getMemoryTotalUsed() - getMemoryInit();
	// }

	// private static final String formatMemory(String name, double value) {
	// String[] unit = { "b", "kb", "mb", "gb" };
	// int i = 0;
	// while (i < unit.length && value > 1024d) {
	// i++;
	// value /= 1024d;
	// }
	// return String.format("%12s %5.3f%s", name, value, unit[i]);
	// }

	// private static class MemoryWatcher {
	// public String toString() {
	// return formatMemory("Total:", getMemoryTotal()) + "\n" +
	// formatMemory("Free:", getMemoryFree()) + "\n" + formatMemory("Max:",
	// getMemoryMax()) + "\n" + formatMemory("Init:", getMemoryInit()) + "\n" +
	// formatMemory("TotalUsed:", getMemoryTotalUsed()) + "\n" +
	// formatMemory("Used:", getMemoryUsed());
	// }
	// }
}
