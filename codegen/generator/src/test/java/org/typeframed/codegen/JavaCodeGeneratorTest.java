package org.typeframed.codegen;

import java.io.File;

import org.junit.Test;
import org.typeframed.codegen.Config;
import org.typeframed.codegen.JavaCodeGenerator;
import org.typeframed.protobuf.parser.FileSource;
import org.typeframed.protobuf.parser.Source;

// TODO test compilation...
public class JavaCodeGeneratorTest {

	@Test
	public void testCreate() throws Exception {
		Config config = new Config();
		config.setOutputDir(new File("build/generate-test"));
		config.setProtoFiles(new Source[] { new FileSource(new File("src/test/proto/echo.proto")) });
		config.setIdOptionName("type_id");
		config.setCodegenPackage("testpackage");
		// config.getProperties().put(Config.JAVA_PACKAGE_NAME, "testpackage");
		JavaCodeGenerator generator = new JavaCodeGenerator(config, new CodegenLogger() {
			
			@Override
			public void warn(String msg) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void info(String msg) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void debug(String msg) {
				// TODO Auto-generated method stub
				
			}
		});
		generator.generate();
	}
}
