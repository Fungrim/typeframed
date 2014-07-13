package org.typeframed.protobuf.parser;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.typeframed.api.MessageTypeDictionary;

import com.google.protobuf.Message;

public interface DictionaryParser {
	
	public MessageTypeDictionary parseDictionary(Source[] protoFiles) throws IOException, ClassNotFoundException;
	
	public Map<Integer, Class<? extends Message>> parseClassMap(Source[] protoFiles) throws IOException, ClassNotFoundException;
	
	public Set<MessageDescriptor> parseMessageDescriptors(Source[] protoFiles) throws IOException;
	
}
