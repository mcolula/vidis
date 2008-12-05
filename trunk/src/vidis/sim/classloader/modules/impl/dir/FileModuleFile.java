package vidis.sim.classloader.modules.impl.dir;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import vidis.sim.classloader.modules.impl.AModuleFile;

/**
 * a file module file; a local file msim
 * @author Dominik
 *
 */
public class FileModuleFile extends AModuleFile {
	private File f;
	public FileModuleFile(File file) {
		f = file;
	}
	
	@Override
	public String getName() {
		return f.getName();
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new FileInputStream(f);
	}
	
	@Override
	public String toString() {
		return f.getName();
	}

}