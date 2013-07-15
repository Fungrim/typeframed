package net.larsan.protobuf.typeframe.parser;

import static net.larsan.protobuf.typeframe.parser.Utilities.getOption;
import static net.larsan.protobuf.typeframe.parser.Utilities.getRootClassFromFile;
import static net.larsan.protobuf.typeframe.parser.Utilities.hasOption;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.larsan.protobuf.typeframe.NoSuchTypeException;
import net.larsan.protobuf.typeframe.TypeDictionary;
import net.larsan.protobuf.typeframe.UnknownMessageException;
import net.larsan.protobuf.typeframe.parser.ParserError.Location;
import net.larsan.protobuf.typeframe.parser.node.ExtendNode;
import net.larsan.protobuf.typeframe.parser.node.FieldNode;
import net.larsan.protobuf.typeframe.parser.node.MessageNode;
import net.larsan.protobuf.typeframe.parser.node.RootNode;

import org.apache.log4j.Logger;
import org.parboiled.Parboiled;
import org.parboiled.errors.ParseError;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;

import com.google.common.io.CharStreams;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;

public class StandardDictionaryParser implements DictionaryParser {
	
	private final Logger log = Logger.getLogger(getClass());
	private MessageInspector inspector;
	
	private ErrorHandler errorHandler = new StandardErrorHandler(log);
	
	public StandardDictionaryParser(MessageInspector inspector) {
		this.inspector = inspector;
	}
	
	@Override
	public TypeDictionary parseDictionary(Source[] protoFiles) throws ClassNotFoundException, IOException {
		final Map<Integer, Class<? extends Message>> map = this.parseClassMap(protoFiles);
		final Map<Class<? extends Message>, Integer> reverse = new HashMap<Class<? extends Message>, Integer>(map.size());
		return new TypeDictionary() {
			
			@Override
			public synchronized int getId(Message msg) throws UnknownMessageException {
				Class<? extends Message> cl = msg.getClass();
				Integer key = reverse.get(cl);
				if(key == null) {
					for (Entry<Integer, Class<? extends Message>> e : map.entrySet()) {
						if(e.getValue().equals(cl)) {
							reverse.put(e.getValue(), e.getKey());
							return e.getKey();
						}
					}
					throw new UnknownMessageException(cl);
				} else {
					return key;
				}
			}
			
			@Override
			public Builder getBuilderForId(int id) throws NoSuchTypeException {
				Class<? extends Message> cl = map.get(id);
				if(cl == null) {
					throw new NoSuchTypeException(id);
				} else {
					try {
						Method method = cl.getMethod("newBuilder");	
						return (Builder) method.invoke(null);
					} catch(Exception e) {
						throw new IllegalStateException("Failed to create new builder", e);
					}
				}
			}
		};
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Map<Integer, Class<? extends Message>> parseClassMap(Source[] protoFiles) throws IOException, ClassNotFoundException {
		Set<MessageDescriptor> set = parseMessageDescriptors(protoFiles);
		Map<Integer, Class<? extends Message>> map = new HashMap<Integer, Class<? extends Message>>();
		for (MessageDescriptor desc : set) {
			Class<? extends Message> cl = (Class<? extends Message>) getClass().getClassLoader().loadClass(desc.getJavaClassName());
			map.put(desc.getTypeId(), cl);
		}
		return map;
	}
	
	@Override
	public Set<MessageDescriptor> parseMessageDescriptors(Source[] protoFiles) throws IOException {
		Map<Integer, MessageDescriptor> map = new HashMap<Integer, MessageDescriptor>();
		for (Source f : protoFiles) {
			parse(f, map);
		}
		return new HashSet<MessageDescriptor>(map.values());
	}

	@SuppressWarnings("rawtypes")
	private void parse(Source f, Map<Integer, MessageDescriptor> map) throws IOException {
		ProtobufParser parser = Parboiled.createParser(ProtobufParser.class);
		ReportingParseRunner runner = new ReportingParseRunner(parser.File());
		String file = CharStreams.toString(f.getReader());
		ParsingResult result = runner.run(file);
		if(result.hasErrors()) {
			throw new ParserError(toStartLocations(result.parseErrors));
		} else {
			parse(f, (RootNode) result.valueStack.peek(), map);
		}
	}

	private void parse(Source file, RootNode root, Map<Integer, MessageDescriptor> map) {
		String javaPackage = findPackage(root);
		log.debug("Java package name: " + javaPackage);
		String parentClass = findRootClass(file, root, javaPackage);
		log.debug("Java root class name: " + parentClass);
		for (FieldNode node : root.getNodes()) {
			if(node instanceof MessageNode && !(node instanceof ExtendNode)) {
				parse(parentClass, (MessageNode) node, map, javaPackage);
			}
		}
	}

	private void parse(String parentClass, MessageNode node, Map<Integer, MessageDescriptor> map, String javaPackage) {
		log.debug("Parsing message '" + node.getName() + "'");
		int id = inspector.getId(node);
		if(id == -1) {
			errorHandler.typeMissingId(node.getName());
		} else {
			String cl = generateClassName(parentClass, javaPackage, node.getName());
			log.info("Java class name '" + cl + "' mapped to ID " + id);
			if(map.containsKey(id)) {
				errorHandler.duplicateId(id, map.get(id).getNodeName(), node.getName());
			}
			map.put(id, new MessageDescriptor(id, node.getName(), cl));
			for (FieldNode subnode : node.getBody().getNodes()) {
				if(subnode instanceof MessageNode && !(subnode instanceof ExtendNode)) {
					parse(cl, (MessageNode) subnode, map, javaPackage);
				}
			}
		}
	}
	
	private String findRootClass(Source file, RootNode root, String javaPackage) {
		String tmp = null; 
		if(hasOption(root, "java_outer_classname")) {
			tmp = getOption(root, "java_outer_classname", true);
		} else {
			tmp = getRootClassFromFile(file);
		}
		if(tmp == null) {
			return null;
		} else if(javaPackage != null) {
			return javaPackage + "." + tmp;
		} else {
			return tmp;
		}
 	}

	private String generateClassName(String parentClass, String javaPackage, String name) {
		if(parentClass != null) {
			return parentClass + "$" + name;
		} else if(javaPackage != null) {
			return javaPackage + "." + name;
		} else {
			return name;
		}
	}

	private String findPackage(RootNode root) {
		String tmp = null;
		if(root.getRootPackage() != null) {
			tmp = root.getRootPackage();
		}
		if(hasOption(root, "java_package")) {
			tmp = getOption(root, "java_package", true);
		}
		return tmp;
	}

	@SuppressWarnings("rawtypes")
	private Location[] toStartLocations(List parseErrors) {
		Location[] arr = new Location[parseErrors.size()];
		for (int i = 0; i < arr.length; i++) {
			ParseError er = (ParseError) parseErrors.get(i);
			arr[i] = new Location(er.getStartIndex());
		}
		return arr;
	}
}
