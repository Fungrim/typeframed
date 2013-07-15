package net.larsan.protobuf.typeframe.codegen;

import com.sampullara.cli.Argument;

public class Bootstrap {

	@Argument(alias="o", description="Output directory to write dictionary to, MANDATORY", required=true)
	private String outputDir = null;

	@Argument(alias="i", description="Input directories for PROTO file, comman separated, MANDATORY", required=true, delimiter=",")
	private String[] inputDirs = null;
	
	@Argument(alias="di", description="'WARN' or 'FAIL' on duplicate ID's, defaults to 'FAIL'", required=false)
	private ErrorMode duplicateIds = ErrorMode.FAIL;
	
	@Argument(alias="mi", description="'WARN' or 'FAIL' on missing ID for message, defaults to 'WARN'", required=false)
	private ErrorMode missingId = ErrorMode.FAIL;

}
