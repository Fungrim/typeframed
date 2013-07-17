package net.larsan.protobuf.typeframe.codegen.gradle;

import static org.junit.Assert.*;

import net.larsan.protobuf.typeframe.codegen.Config
import net.larsan.protobuf.typeframe.codegen.JavaCodeGenerator
import org.junit.Test;

class TestJavaAccess {

	@Test
	public void test() {
		Config config = new Config()
		config.setFailOnDuplicates(true)
		new JavaCodeGenerator(config)
	}
}
