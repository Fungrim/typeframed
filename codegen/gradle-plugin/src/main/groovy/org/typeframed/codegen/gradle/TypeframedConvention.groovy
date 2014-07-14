package org.typeframed.codegen.gradle

import org.gradle.api.Project;

class TypeframedConvention {

	def boolean failOnDuplicates = true
	def boolean failOnMissingId = false
	
	def String codegenPackage = "";
	def String typeIdName = "type_id"
	
	def TypeframedConvention(Project project) { }
	
	def typeframed(Closure closure) {
		closure.delegate = this
		closure()
	}
}
	