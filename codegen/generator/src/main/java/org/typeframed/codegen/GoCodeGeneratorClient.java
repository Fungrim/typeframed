package org.typeframed.codegen;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.typeframed.api.util.GlobFiles;

import com.sampullara.cli.Args;
import com.sampullara.cli.Argument;

// TODO extra imports
public class GoCodeGeneratorClient {

	@Argument(alias = "o", description = "Output root directory, defaults to current dir", required = false)
	private String outputDir = System.getProperty("user.dir");

	@Argument(alias = "d", description = "Input directory, default to current dir", required = false)
	private String inputDir = System.getProperty("user.dir");

	@Argument(alias = "f", description = "File pattern, default to '*.proto'", required = false)
	private String filePattern = "*.proto";
	
	@Argument(alias = "p", description = "Package for the generated code, defaults to directory name", required = false)
	private String codePackage;
	
	//@Argument(alias = "x", description = "Comma delimited list of extra imports for generated code, optional", required = false, delimiter=",")
	//private String[] extraImports = new String[0];	
	
	@Argument(alias = "i", description = "Name of ID option, defaults to 'type_id'", required = false)
	private String idOptionName = "type_id";
	
	@Argument(description = "Print debug information, true of false, defaults to false", required = false)
	private boolean debug = false;
	
	public static void main(String[] args) throws IOException {
		GoCodeGeneratorClient client = new GoCodeGeneratorClient();
		try {
			Args.parse(client, args);
			client.run();
		} catch(IllegalArgumentException e) {
			Args.usage(client);
		}
	}

	private void run() throws IOException {
		GoConfig config = createConfig();
		CodegenLogger logger = createLogger();
		GoCodeGenerator gen = new GoCodeGenerator(config, logger);
		gen.generate();
	}

	private CodegenLogger createLogger() {
		return new CodegenLogger() {
			
			@Override
			public void warn(String msg) {
				System.out.println("WARNING: " + msg);
			}
			
			@Override
			public void info(String msg) {
				System.out.println("INFO: " + msg);
			}
			
			@Override
			public void debug(String msg) {
				if(debug) {
					System.out.println("DEBUG: " + msg);
				}
			}
		};
	}

	private GoConfig createConfig() throws IOException {
		GoConfig con = new GoConfig();
		con.setFailOnDuplicates(true);
		con.setFailOnMissingId(false);
		con.setIdOptionName(idOptionName);
		con.setOutputDir(new File(outputDir));
		if(codePackage == null) {
			codePackage = con.getOutputDir().getName();
		}
		con.setCodegenPackage(codePackage);
		con.setProtoFiles(findFiles());
		return con;
	}

	private File[] findFiles() throws IOException {
		List<Path> list = GlobFiles.find(Paths.get(inputDir), filePattern);
		File[] files = new File[list.size()];
		for (int i = 0; i < files.length; i++) {
			files[i] = list.get(i).toFile();
		}
		return files;
	}
}
