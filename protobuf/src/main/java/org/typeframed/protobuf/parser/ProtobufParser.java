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
package org.typeframed.protobuf.parser;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.annotations.DontLabel;
import org.parboiled.annotations.MemoMismatches;
import org.parboiled.annotations.SuppressNode;
import org.parboiled.annotations.SuppressSubnodes;
import org.parboiled.support.Var;
import org.typeframed.protobuf.parser.node.EnumFieldNode;
import org.typeframed.protobuf.parser.node.EnumNode;
import org.typeframed.protobuf.parser.node.ExtendNode;
import org.typeframed.protobuf.parser.node.FieldsNode;
import org.typeframed.protobuf.parser.node.MessageBodyNode;
import org.typeframed.protobuf.parser.node.MessageNode;
import org.typeframed.protobuf.parser.node.OptionNode;
import org.typeframed.protobuf.parser.node.RootNode;
import org.typeframed.protobuf.parser.node.TypeNode;

/*
 * TODO:services, service options, service method options
 * 
 * Unclear: multiple options per field, byte string literal
 * 
 * Unsupported: groups
 */

@BuildParseTree
public class ProtobufParser extends BaseParser<Object> {

	final Rule RWING = TerminalOptionalSpacing("}");
	final Rule LWING = TerminalOptionalSpacing("{");
	final Rule RPARAN = TerminalOptionalSpacing(")");
	final Rule LPARAN = TerminalOptionalSpacing("(");
	final Rule SEMI = TerminalOptionalSpacing(";");
	final Rule EQUAL = TerminalOptionalSpacing("=");
	final Rule RBRACKET = TerminalOptionalSpacing("]");
	final Rule LBRACKET = TerminalOptionalSpacing("[");
	final Rule DOT = TerminalOptionalSpacing(".");

	final Rule OPTION = Keyword("option");
	final Rule IMPORT = Keyword("import");
	final Rule PUBLIC = Keyword("public");
	final Rule PACKAGE = Keyword("package");
	final Rule EXTEND = Keyword("extend");
	final Rule ENUM = Keyword("enum");
	final Rule MESSAGE = Keyword("message");
	final Rule REPEATED = Keyword("repeated");
	final Rule REQUIRED = Keyword("required");
	final Rule OPTIONAL = Keyword("optional");

	final Rule STRING = Keyword("string");
	final Rule BOOL = Keyword("bool");
	final Rule BYTES = Keyword("bytes");
	final Rule INT32 = Keyword("int32");
	final Rule INT64 = Keyword("int64");
	final Rule UINT32 = Keyword("uint32");
	final Rule UINT64 = Keyword("uint64");
	final Rule SINT32 = Keyword("sint32");
	final Rule SINT64 = Keyword("sint64");
	final Rule FIXED32 = Keyword("fixed32");
	final Rule FIXED64 = Keyword("fixed64");
	final Rule SFIXED32 = Keyword("sfixed32");
	final Rule SFIXED64 = Keyword("sfixed64");
	final Rule DOUBLE = Keyword("double");
	final Rule FLOAT = Keyword("float");
	
	final Rule TRUE = Keyword("false");
	final Rule FALSE = Keyword("true");

	public Rule File() {
		return Sequence(
					push(new RootNode()), 
					Optional(Spacing()),
					ZeroOrMore(
						Sequence(
							Optional(PackageDeclaration()),
							FirstOf(
								ImportDeclaration(),
								OptionDeclaration(),
								TypeDeclaration()
							)
						)
					),
					EOI
				);
	}
	
	Rule PackageDeclaration() {
        return Sequence(
        			PACKAGE, 
        			QualifiedIdentifier(), 
        			((RootNode) peek()).setRootPackage(match().trim()),
        			SEMI);
    }
	
	Rule ImportDeclaration() {
        return Sequence(
        			Sequence(IMPORT, Optional(PUBLIC)), 
        			StringLiteral(), 
        			SEMI);
    }
	
	Rule OptionDeclaration() {
        return Sequence(
        			OPTION, 
        			OptionBody(),
        			SEMI);
    }
	
	Rule OptionBody() {
		Var<OptionNode> node = new Var<OptionNode>();
		return Sequence(
					node.set(new OptionNode()),
					FirstOf(
						Identifier(),
						CustomOptionIdentifier()
					),
					node.get().setName(match().trim()),
					EQUAL,
					FirstOf(
						Sequence(
								DecimalLiteral(),
							node.get().setValue(match().trim())
						),
						Sequence(
							BooleanLiteral(),
							node.get().setValue(match().trim())
						),
						Sequence(
							StringLiteral(),
							node.get().setValue(match().trim())
						),
						Sequence(
							IntegerLiteral(),
							node.get().setValue(match().trim())
						),
						Sequence(
							Identifier(),
							node.get().setValue(match().trim())
						)
					),
					((OptionsHolder) peek()).addOption(node.get())
				);
	}

	Rule DecimalLiteral() {
		return Sequence(OneOrMore(Digit()), '.', ZeroOrMore(Digit()));
	}

	Rule TypeDeclaration() {
		return FirstOf(
					MessageDeclaration(),
					ExtendDeclaration(),
					EnumDeclaration(), 
					SEMI
				);
	}
	
	Rule EnumDeclaration() {
		Var<EnumNode> msg = new Var<EnumNode>();
		return Sequence(
	    			ENUM, 
	    			Identifier(),
	    			push(new EnumNode(match().trim())),
	    			EnumBody(),
	    			msg.set((EnumNode) pop()),
        			((EnumsHolder) peek()).addEnum(msg.get())
	    		);
	}
	
	Rule ExtendDeclaration() {
		Var<ExtendNode> msg = new Var<ExtendNode>();
        return Sequence(
        			EXTEND, 
        			QualifiedIdentifier(),
        			push(new ExtendNode(match().trim())),
        			MessageBody(),
        			msg.set((ExtendNode) pop()),
        			((FieldsNode) peek()).getNodes().add(msg.get())
        		);
    }

	Rule MessageDeclaration() {
		Var<MessageNode> msg = new Var<MessageNode>();
		return Sequence(
					MESSAGE, 
					Identifier(), 
					push(new MessageNode(match().trim())), 
					MessageBody(), 
					msg.set((MessageNode) pop()),
					((FieldsNode) peek()).getNodes().add(msg.get())
				);
	}
	
	Rule EnumBody() {
		return Sequence(LWING, ZeroOrMore(EnumBodyDeclaration()), RWING);
	}

	Rule MessageBody() {
		return Sequence(LWING, ZeroOrMore(MessageBodyDeclaration()), RWING);
	}
	
	Rule EnumBodyDeclaration() {
		return FirstOf(
				SEMI, 
				EnumFieldDeclaration(),
				OptionDeclaration()
			);
	}

	Rule MessageBodyDeclaration() {
		return FirstOf(
					SEMI, 
					MemberDecl(),
					OptionDeclaration(),
					EnumDeclaration()
				);
	}

	Rule MemberDecl() {
		Var<MessageBodyNode> body = new Var<MessageBodyNode>();
        return Sequence(
	        		push(((MessageNode) peek()).getBody()),
	        		FirstOf(
	        			FieldDeclaration(), 
	        			MessageDeclaration(),
	        			ExtendDeclaration()
	        		),
	        		body.set((MessageBodyNode) pop()) // why do I have to wrap this in a Var?
        		);
    }
	
	Rule EnumFieldDeclaration() {
		Var<EnumFieldNode> node = new Var<EnumFieldNode>();
		return Sequence(
    				Identifier(),
    				node.set(new EnumFieldNode(match().trim())),
    				EQUAL,
    				IntegerLiteral(),
    				node.get().setOrdinal(Integer.parseInt(match().trim())),
    				Optional(Spacing()),
    				push(node.get()),
	        		ZeroOrMore(FieldOption()),
	        		node.set((EnumFieldNode) pop()),
    				((EnumNode) peek()).getFields().add(node.get())
    			);
	}

	Rule FieldDeclaration() {
		Var<TypeNode> msg = new Var<TypeNode>();
		return Sequence(
	    			msg.set(new TypeNode()),
	        		Modifier(), 
	        		msg.get().setRule(FieldRule.valueOf(match().trim().toUpperCase())),
	        		Type(), 
	        		msg.get().setType(match().trim().toUpperCase()),
	        		Identifier(), 
	        		msg.get().setName(match().trim()),
	        		EQUAL, 
	        		IntegerLiteral(),
	        		msg.get().setTag(Integer.parseInt(match().trim())),
	        		push(msg.get()),
	        		Optional(Spacing()),
	        		ZeroOrMore(FieldOption()),
	        		msg.set((TypeNode) pop()), // ??
	        		((FieldsNode) peek()).getNodes().add(msg.get()),
	        		Optional(Spacing())
    		);
	}

	Rule FieldOption() {
		return Sequence(LBRACKET, OptionBody(), Optional(Spacing()), RBRACKET);
	}

	Rule Type() {
		return FirstOf(ScalarTypes(), MessageTypes());
	}

	Rule MessageTypes() {
		return Identifier();
	}

	Rule ScalarTypes() {
		return FirstOf(DOUBLE, FLOAT, STRING, BOOL, BYTES, INT32, INT64,
				SINT32, SINT64, UINT32, UINT64, FIXED32, FIXED64, SFIXED32,
				SFIXED64);
	}

	Rule Modifier() {
		return FirstOf(REPEATED, REQUIRED, OPTIONAL);
	}
	
	Rule QualifiedIdentifier() {
        return Sequence(Identifier(), ZeroOrMore(DOT, Identifier()));
    }
	
	@SuppressSubnodes
	@MemoMismatches
	Rule CustomOptionIdentifier() {
		return Sequence(LPARAN, QualifiedIdentifier(), RPARAN, Spacing());
	}

	@SuppressSubnodes
	@MemoMismatches
	Rule Identifier() {
		return Sequence(TestNot(Keyword()), Letter(), ZeroOrMore(LetterOrDigit()), Spacing());
	}

	@MemoMismatches
	Rule Letter() {
		return new CharMatcher("Letter", false);
	}

	@MemoMismatches
	Rule Keyword() {
		return Sequence(
				FirstOf("message", "enum", "package", "required",
						"repeated", "optional", "service", "rcp", "string",
						"bool", "bytes", "double", "float", "int32", "int64",
						"uint32", "uint64", "sint32", "sint64", "fixed32",
						"fixed64", "sfixed32", "sfixed64", "import", "option",
						"true", "false", "public"),
				TestNot(LetterOrDigit()));
	}

	@SuppressSubnodes
	Rule IntegerLiteral() {
		return FirstOf('0', Sequence(CharRange('1', '9'), ZeroOrMore(Digit())));
	}
	
	Rule Digit() {
        return CharRange('0', '9');
    }

	@SuppressSubnodes
	Rule BooleanLiteral() {
		return FirstOf(TRUE, FALSE);
	}
	
	@SuppressSubnodes
	Rule StringLiteral() {
		return Sequence(
					'"',
					ZeroOrMore(Sequence(TestNot(AnyOf("\r\n\"\\")), ANY)).suppressSubnodes(), 
					'"'
				);
	}
	
	@SuppressNode
	@DontLabel
	Rule Keyword(String keyword) {
		return Terminal(keyword, LetterOrDigit());
	}

	@MemoMismatches
	Rule LetterOrDigit() {
		return new CharMatcher("LetterOrDigit", true);
	}

	@DontLabel
	@SuppressNode
	Rule Terminal(String string) {
		return Sequence(string, Spacing()).label('\'' + string + '\'');
	}

	@DontLabel
	@SuppressNode
	Rule TerminalOptionalSpacing(String string) {
		return Sequence(string, Optional(Spacing()))
				.label('\'' + string + '\'');
	}

	@SuppressNode
	@DontLabel
	Rule Terminal(String string, Rule mustNotFollow) {
		return Sequence(string, TestNot(mustNotFollow), Spacing()).label(
				'\'' + string + '\'');
	}

	@SuppressNode
	Rule Spacing() {
		return ZeroOrMore(FirstOf(
					OneOrMore(AnyOf(" \t\r\n\f").label("Whitespace")),
					LineComment()
				));
	}
	
	@DontLabel
	Rule LineComment() {
		return Sequence(
	                "//",
	                ZeroOrMore(TestNot(AnyOf("\r\n")), ANY),
	                FirstOf("\r\n", '\r', '\n', EOI)
	        );
	}
}
