/**
 * Copyright 2015 Lars J. Nilsson <contact@larsan.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
	public void testCreateGo() throws Exception {
		Config config = new Config();
		config.setOutputDir(new File("build/generate-test"));
		config.setProtoFiles(new Source[] { new FileSource(new File("src/test/proto/echo.proto")) });
		config.setIdOptionName("type_id");
		config.setCodegenPackage("testpackage");
		// config.getProperties().put(Config.JAVA_PACKAGE_NAME, "testpackage");
		GoCodeGenerator generator = new GoCodeGenerator(config, new CodegenLogger() {
			
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

	@Test
	public void testCreateJava() throws Exception {
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
