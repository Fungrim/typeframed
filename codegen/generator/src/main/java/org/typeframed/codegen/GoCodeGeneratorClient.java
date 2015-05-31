package org.typeframed.codegen;

import java.io.File;
import java.io.IOException;

import com.sampullara.cli.Args;
import com.sampullara.cli.Argument;

// TODO extra imports
// TODO multiple proto files
public class GoCodeGeneratorClient {

	@Argument(alias = "o", description = "Output root directory, required", required = true)
	private String outputDir;

	@Argument(alias = "p", description = "Proto file to parse, required", required = true)
	private String protoFile;

	@Argument(alias = "c", description = "Package for the generated code, required", required = true)
	private String codegenPackage;
	
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
		Config config = createConfig();
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

	private Config createConfig() {
		Config con = new Config();
		con.setCodegenPackage(codegenPackage);
		con.setFailOnDuplicates(true);
		con.setFailOnMissingId(false);
		con.setIdOptionName(idOptionName);
		con.setOutputDir(new File(outputDir));
		con.setProtoFiles(new File[] { new File(protoFile) });
		return con;
	}
}
