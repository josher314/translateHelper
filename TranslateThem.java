
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 * Object used for updating the properties files with new definitions
 * 
 * @author Joshua.Roberts
 *
 */

public class TranslateThem {
	// basePathEdit refers to the base file path for the property files we want
	// to edit.

	private String basePathEdit;

	private String basePathSrc;

	public TranslateThem(String p1, String p2) {
		this.basePathEdit = p1;
		this.basePathSrc = p2;
	}

	public void translateEm(String path) throws IOException {
		// file to be edited: put in the beginning half of the directory path up
		// to the substring which contains the fileID.
		String path1 = this.basePathEdit + path;

		Parameters params1 = new Parameters();

		// set encoding for properties files. Note: if you want to encode the
		// files without updating the translations,
		// comment out everything related to the src, e.g., string 'path2' and
		// builderSrc, etc.

		FileBasedConfigurationBuilder.setDefaultEncoding(
			PropertiesConfiguration.class, "UTF-8");

		// FileBasedConfigurationBuilder<PropertiesConfiguration> object loads
		// the property file so we can edit the
		// .properties definitions
		FileBasedConfigurationBuilder<FileBasedConfiguration> builderEdit = new FileBasedConfigurationBuilder<FileBasedConfiguration>(
			PropertiesConfiguration.class).configure(
				params1.properties().setFileName(path1));

		// source file that provides the definitions
		String path2 = this.basePathSrc + path;
		Parameters params2 = new Parameters();

		// FileBasedConfigurationBuilder<PropertiesConfiguration> object loads
		// the property file so we can provide the definitions to the
		// other FileBasedConfigurationBuilder<PropertiesConfiguration> object
		// corresponding to the .properties file we want to edit.
		FileBasedConfigurationBuilder<FileBasedConfiguration> builderSrc = new FileBasedConfigurationBuilder<FileBasedConfiguration>(
			PropertiesConfiguration.class).configure(
				params2.properties().setFileName(path2));

		try {
			Configuration configEdit = builderEdit.getConfiguration();
			Configuration configSrc = builderSrc.getConfiguration();

			// Iterate through the keys within the src .properties file and
			// replace the edit .properties file definitions with the
			// src .properties file definitions .

			Iterator<String> srcKeys = configSrc.getKeys();
			while (srcKeys.hasNext()) {
				String key1 = srcKeys.next();
				if (configEdit.containsKey(key1)) {
					String replaceVal = configSrc.getString(key1);
					configEdit.setProperty(key1, replaceVal);
				}

			}

			// save and write back out to the .properties file
			builderEdit.save();

		}
		catch (ConfigurationException cex) {
			// loading of the configuration file failed
			System.out.println(cex.getMessage());
		}

	}

	/**
	 * @param dir
	 * @param filePathId
	 * @throws IOException
	 */
	/*
	 * replaceValues(dir, filePathId) recursively grabs files within parent
	 * directory 'dir' and passes their relative filepaths to 'translateEm().
	 * Use 'filePathId' as a unique identifier for the first common string
	 * between two file paths which can uniquely identify a directory of files.
	 * e.g for two directories with file paths
	 * "C:/username/Messages for Solpac/com/linoma/ga/core/admin/action.properties"
	 * and
	 * "C:/username/git_repos/gamft/admin-messages_build/msg-ui-admin/ja/com/linoma/ga/core/admin/action/properties"
	 * the common filePathId would be "core". since slicing anywhere before
	 * "core" might allow for other directories like "ui" within the directory
	 * sent to us while our gamft directory structure is different and doesn't
	 * have a "ui", depending on the directory structure,
	 * 
	 */
	public void replaceValues(String dir, String filePathId)
		throws IOException {
		File folderSrc = new File(dir);
		File[] listSrc = folderSrc.listFiles();
		for (File srcFile : listSrc) {
			if (srcFile.isDirectory()) {
				replaceValues(srcFile.getAbsolutePath(), filePathId);
			}
			else {
				String fullPath1 = srcFile.getAbsolutePath();
				String relativePath1 = fullPath1.substring(
					fullPath1.indexOf(filePathId));
				System.out.println("relativePath = " + relativePath1);
				translateEm(relativePath1);

			}
		}

	}

	public static void main(String[] args) throws IOException {
		TranslateThem ja_dpa1 = new TranslateThem(
			"C:\\Users\\joshua.roberts\\git_repos\\gamft\\admin-messages_build\\resources\\ja\\com\\linoma\\",
			"C:\\Users\\joshua.roberts\\Downloads\\Messages for Solpac\\Messages for Solpac\\com\\linoma\\");
		ja_dpa1.replaceValues(
			"C:\\Users\\joshua.roberts\\Downloads\\Messages for Solpac\\Messages for Solpac\\com\\linoma\\dpa",
			"dpa");

		TranslateThem ja_core = new TranslateThem(
			"\\C:\\Users\\joshua.roberts\\git_repos\\gamft\\admin-messages_build\\msg-core\\ja\\com\\linoma\\ga\\",
			"\\C:\\Users\\joshua.roberts\\Downloads\\Messages for Solpac\\Messages for Solpac\\com\\linoma\\ga\\");

		ja_core.replaceValues(
			"C:\\Users\\joshua.roberts\\Downloads\\Messages for Solpac\\Messages for Solpac\\com\\linoma\\ga\\core",
			"core");

		TranslateThem ja_ui = new TranslateThem(
			"C:\\Users\\joshua.roberts\\git_repos\\gamft\\admin-messages_build\\msg-ui-admin\\ja\\com\\linoma\\ga\\",
			"C:\\Users\\joshua.roberts\\Downloads\\Messages for Solpac\\Messages for Solpac\\com\\linoma\\ga\\");
		ja_ui.replaceValues(
			"C:\\Users\\joshua.roberts\\Downloads\\Messages for Solpac\\Messages for Solpac\\com\\linoma\\ga\\ui",
			"ui");
		TranslateThem ja_ = new TranslateThem(
			"C:\\Users\\joshua.roberts\\git_repos\\gamft\\admin-messages_build\\resources\\ja\\com\\linoma\\",
			"C:\\Users\\joshua.roberts\\Downloads\\Messages for Solpac\\Messages for Solpac\\com\\linoma\\");
		ja_.replaceValues(
			"C:\\Users\\joshua.roberts\\Downloads\\Messages for Solpac\\Messages for Solpac\\com\\linoma\\startup",
			"startup");

		TranslateThem ja_beansProj = new TranslateThem(
			"C:\\Users\\joshua.roberts\\git_repos\\linoma-projects\\beaninfo_build\\ja\\com\\linoma\\ga\\",
			"C:\\Users\\joshua.roberts\\Downloads\\beaninfo\\beaninfo\\com\\linoma\\ga\\");
		ja_beansProj.replaceValues(
			"C:\\Users\\joshua.roberts\\Downloads\\beaninfo\\beaninfo\\com\\linoma\\ga\\projects",
			"projects");
		TranslateThem ja_coreProj = new TranslateThem(
			"C:\\Users\\joshua.roberts\\git_repos\\linoma-projects\\resources_build\\ja\\com\\linoma\\ga\\core\\",
			"C:\\Users\\joshua.roberts\\Downloads\\beaninfo\\beaninfo\\com\\linoma\\ga\\core\\");
		ja_coreProj.replaceValues(
			"C:\\Users\\joshua.roberts\\Downloads\\beaninfo\\beaninfo\\com\\linoma\\ga\\core\\projects",
			"projects");
		TranslateThem ja_dpa = new TranslateThem(
			"C:\\Users\\joshua.roberts\\git_repos\\linoma-projects\\resources_build\\ja\\com\\linoma\\",
			"C:\\Users\\joshua.roberts\\Downloads\\beaninfo\\beaninfo\\com\\linoma\\");
		ja_dpa.replaceValues(
			"C:\\Users\\joshua.roberts\\Downloads\\beaninfo\\beaninfo\\com\\linoma\\dpa",
			"dpa");

		// TranslateThem messageBuild = new TranslateThem(
		// "C:\\Users\\joshua.roberts\\git_repos\\gamft\\messages_build\\ja\\com\\linoma\\ga\\",
		// "C:\\Users\\joshua.roberts\\git_repos\\gamft\\messages_build\\ja\\com\\linoma\\ga\\");
		// messageBuild.replaceValues(
		// "C:\\Users\\joshua.roberts\\git_repos\\gamft\\messages_build\\ja\\com\\linoma\\ga\\file",
		// "file");

		// TranslateThem messageBuild2 = new TranslateThem(
		// "C:\\Users\\joshua.roberts\\git_repos\\gamft\\messages_build\\ja\\com\\linoma\\ga\\",
		// "C:\\Users\\joshua.roberts\\git_repos\\gamft\\messages_build\\ja\\com\\linoma\\ga\\");
		// messageBuild2.replaceValues(
		// "C:\\Users\\joshua.roberts\\git_repos\\gamft\\messages_build\\ja\\com\\linoma\\ga\\godrive",
		// "godrive");

		// TranslateThem messageBuild3 = new TranslateThem(
		// "C:\\Users\\joshua.roberts\\git_repos\\gamft\\messages_build\\ja\\com\\linoma\\ga\\",
		// "C:\\Users\\joshua.roberts\\git_repos\\gamft\\messages_build\\ja\\com\\linoma\\ga\\");
		// messageBuild3.replaceValues(
		// "C:\\Users\\joshua.roberts\\git_repos\\gamft\\messages_build\\ja\\com\\linoma\\ga\\security",
		// "security");
		// TranslateThem messageBuild4 = new TranslateThem(
		// "C:\\Users\\joshua.roberts\\git_repos\\gamft\\messages_build\\ja\\com\\linoma\\ga\\",
		// "C:\\Users\\joshua.roberts\\git_repos\\gamft\\messages_build\\ja\\com\\linoma\\ga\\");
		// messageBuild4.replaceValues(
		// "C:\\Users\\joshua.roberts\\git_repos\\gamft\\messages_build\\ja\\com\\linoma\\ga\\webclient",
		// "webclient");

	}

}
