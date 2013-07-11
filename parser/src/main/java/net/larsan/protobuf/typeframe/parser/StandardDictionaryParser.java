package net.larsan.protobuf.typeframe.parser;

import static net.larsan.protobuf.typeframe.parser.Utilities.getOption;
import static net.larsan.protobuf.typeframe.parser.Utilities.getRootClassFromFile;
import static net.larsan.protobuf.typeframe.parser.Utilities.hasOption;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.larsan.protobuf.typeframe.NoSuchTypeException;
import net.larsan.protobuf.typeframe.TypeDictionary;
import net.larsan.protobuf.typeframe.UnknownMessageException;
import net.larsan.protobuf.typeframe.codegen.IllegalIdException;
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
	
	public StandardDictionaryParser(MessageInspector inspector) {
		this.inspector = inspector;
	}
	
	@Override
	public TypeDictionary parseDictionary(File[] protoFiles) throws ClassNotFoundException, IOException {
		final Map<Integer, Class<? extends Message>> map = this.parse(protoFiles);
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
	public Map<Integer, Class<? extends Message>> parse(File[] protoFiles) throws IOException, ClassNotFoundException {
		Map<Integer, Class<? extends Message>> result = new HashMap<Integer, Class<? extends Message>>();
		for (File f : protoFiles) {
			parse(f, result);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	private void parse(File f, Map<Integer, Class<? extends Message>> map) throws IOException, ClassNotFoundException {
		ProtobufParser parser = Parboiled.createParser(ProtobufParser.class);
		ReportingParseRunner runner = new ReportingParseRunner(parser.File());
		String file = CharStreams.toString(new FileReader(f));
		ParsingResult result = runner.run(file);
		if(result.hasErrors()) {
			throw new ParserError(toStartLocations(result.parseErrors));
		} else {
			parse(f, (RootNode) result.valueStack.peek(), map);
		}
	}

	private void parse(File file, RootNode root, Map<Integer, Class<? extends Message>> map) throws ClassNotFoundException {
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void parse(String parentClass, MessageNode node, Map<Integer, Class<? extends Message>> map, String javaPackage) throws ClassNotFoundException {
		log.debug("Parsing message '" + node.getName() + "'");
		int id = inspector.getId(node);
		if(id == -1) {
			log.warn("Did not find any type ID in message '" + node.getName() + "'");
		} else {
			String cl = generateClassName(parentClass, javaPackage, node.getName());
			Class clazz = getClass().getClassLoader().loadClass(cl);
			// TODO figure out why this doesn't work.. ?
			/*if(Message.class.isAssignableFrom(clazz)) {
				throw new IllegalStateException("Class " + clazz.getName() + " is not a Message");
			}*/
			log.info("Java class name '" + cl + "' mapped to ID " + id);
			if(map.containsKey(id)) {
				throw new IllegalIdException("ID " + id + " already mapped to type: " + map.get(id).getName());
			} else {
				map.put(id, clazz);
			}
			for (FieldNode subnode : node.getBody().getNodes()) {
				if(subnode instanceof MessageNode && !(subnode instanceof ExtendNode)) {
					parse(cl, (MessageNode) subnode, map, javaPackage);
				}
			}
		}
	}
	
	private String findRootClass(File file, RootNode root, String javaPackage) {
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
