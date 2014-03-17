package org.typeframed.codegen.gradle;

import static org.junit.Assert.*;

import org.typeframed.codegen.Config
import org.typeframed.codegen.JavaCodeGenerator
import org.junit.Test;

class TestJavaAccess {

	@Test
	public void test() {
		Config config = new Config()
		config.setFailOnDuplicates(true)
		new JavaCodeGenerator(config)
	}
}
