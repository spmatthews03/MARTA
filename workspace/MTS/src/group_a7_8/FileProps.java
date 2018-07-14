package group_a7_8;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FileProps {
	private static FileProps instance = null;
	private static String fileName="marta.props";
	private static String dirPath=".";
	Properties props = new Properties();
	public static String configPath;
	public FileProps() {
		load();
	}
	

	
	private void load() {
		
		String configFilePath = null;
		if(configPath!=null) {
			File file = new File(configPath); 
			if(file.exists() && file.isFile()) {
				configFilePath = configPath;
			}
		}
		if(configFilePath==null) {
			configFilePath = dirPath+"/"+fileName;
			File dir = new File(dirPath); 
			if(!dir.exists() || !dir.isDirectory()) {
				System.out.printf("Error with directory path %s\n",dirPath);
				return;
			};
			File file = new File(configFilePath); 
			if(!file.exists() || !file.isFile()) {
				System.out.printf("Property file %s does not exist\n",configFilePath);
				return;
			}
		}

		InputStream input = null;

		try {
			input = new FileInputStream(configFilePath);

			// load a properties file
			props.load(input);
		}
		catch(IOException ioe) {
			System.out.printf("Error loading properties from %s\n\tError: %s\n",fileName,ioe.getMessage());
		}
	}
	
	Properties getProps() {
		return props;
	}
	
	public static void SetConfigPath(String path) {configPath = path;}
	
	public static String get(String key) {
		if(!contains(key)) return null;
		return instance.getProps().getProperty(key);
	}
	
	public static boolean contains(String key) {
		if(instance==null) instance = new FileProps();
		return instance.getProps().containsKey(key);
	}
}
