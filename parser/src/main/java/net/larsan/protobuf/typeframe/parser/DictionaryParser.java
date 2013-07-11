package net.larsan.protobuf.typeframe.parser;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import net.larsan.protobuf.typeframe.TypeDictionary;

import com.google.protobuf.Message;

public interface DictionaryParser {
	
	public TypeDictionary parseDictionary(File[] protoFiles) throws IOException, ClassNotFoundException;
	
	public Map<Integer, Class<? extends Message>> parse(File[] protoFiles) throws IOException, ClassNotFoundException;
	
}
