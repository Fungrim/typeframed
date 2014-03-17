package org.typeframed.protobuf.codegen.gradle

import org.gradle.api.Project;

class TypeframeConvention {

	def boolean failOnDuplicates = true
	def boolean failOnMissingId = false
	
	def String javaPackage = "";
	def String typeIdName = "type_id"
	
	def TypeframeConvention(Project project) { }
	
	def typeframe(Closure closure) {
		closure.delegate = this
		closure()
	}
}
	