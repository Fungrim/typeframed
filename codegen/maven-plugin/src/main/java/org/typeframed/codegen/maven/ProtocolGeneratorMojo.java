package org.typeframed.codegen.maven;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.typeframed.codegen.Config;
import org.typeframed.codegen.JavaCodeGenerator;

@Mojo(name="generate")
public class ProtocolGeneratorMojo extends AbstractMojo {

	@Parameter(required=true)
	private File protocolFile;
	
	@Parameter(required=false, defaultValue="${project.build.directory}/generated-sources")
	private File outputDir;
	
	@Parameter(required=false, defaultValue="true")
	private boolean failOnDuplicates = true;
	
	@Parameter(required=false, defaultValue="false")
	private boolean failOnMissingId = false;
	
	@Parameter(required=false)
	private String javaPackage;
	
	@Parameter(required=false, defaultValue="type_id")
	private String typeIdName;
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		Config conf = new Config();
		conf.setFailOnDuplicates(failOnDuplicates);
		conf.setFailOnMissingId(failOnMissingId);
		conf.setIdOptionName(typeIdName);
		conf.setOutputDir(outputDir);
		conf.setProtoFiles(new File[] { protocolFile });
		getLog().info("Starting with configuration: " + conf);
		JavaCodeGenerator generator = new JavaCodeGenerator(conf);
		generator.setCodegenLogger(new MavenLogger(getLog()));
		try {
			generator.generate();
		} catch (IOException e) {
			throw new MojoFailureException("failed to generate sources", e);
		}
	}
}
