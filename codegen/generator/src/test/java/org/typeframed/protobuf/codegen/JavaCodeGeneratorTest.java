package org.typeframed.protobuf.codegen;

import java.io.File;

import org.junit.Test;
import org.typeframed.protobuf.codegen.Config;
import org.typeframed.protobuf.codegen.JavaCodeGenerator;
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
		// config.getProperties().put(Config.JAVA_PACKAGE_NAME, "testpackage");
		JavaCodeGenerator generator = new JavaCodeGenerator(config);
		generator.generate();
	}
}
