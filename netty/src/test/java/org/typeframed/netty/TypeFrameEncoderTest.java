package org.typeframed.netty;

import static org.junit.Assert.assertEquals;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.MessageList;
import junit.framework.Assert;
import net.larsan.protobuf.typeframe.Messages;
import net.larsan.protobuf.typeframe.Messages.Person;

import org.junit.Test;
import org.typeframed.api.Int32HeaderParser;
import org.typeframed.api.MessageEnvelope;
import org.typeframed.api.NetworkFrame;
import org.typeframed.api.TypeDictionary;
import org.typeframed.api.digest.CRC32ChecksumProvider;
import org.typeframed.api.io.TypeFrameDecoder;
import org.typeframed.netty.MessageEnvelopeEncoder;

import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;

public class TypeFrameEncoderTest {

	@Test
	@SuppressWarnings("unchecked")
	public void testWriteFully() throws Exception {
		TypeDictionary dict = createTypeDictionary();
		Person person1 = createPerson(666, "kalle", "kalle@kalle.com");
		MessageEnvelopeEncoder<Integer> encoder = new MessageEnvelopeEncoder<Integer>(dict);
		encoder.setChecksum(new CRC32ChecksumProvider());
		encoder.setHeader(new Int32HeaderParser());
		ByteBuf bytes = Unpooled.buffer();
		encoder.encode(null, MessageEnvelope.wrap(1974, person1), bytes);
		bytes.resetReaderIndex();
		byte[] arr = bytes.copy().array();
		TypeFrameDecoder<Integer> decoder = new TypeFrameDecoder<Integer>(dict);
		decoder.setChecksum(new CRC32ChecksumProvider());
		decoder.setHeader(new Int32HeaderParser());
		MessageList<Object> list = MessageList.newInstance();
		decoder.decode(null, Unpooled.wrappedBuffer(arr), list); 
		assertEquals(1, list.size());
		NetworkFrame<Integer> frame = (NetworkFrame<Integer>) list.get(0);
		assertEquals(27, frame.getLength());
		assertEquals(666, frame.getType());
		assertEquals(new Integer(1974), frame.getHeader());
		assertEquals(person1, frame.getMessage());
	}
	
	private TypeDictionary createTypeDictionary() {
		return new TypeDictionary() {
			
			@Override
			public int getId(Message msg) {
				return 666;
			}
			
			@Override
			public Builder getBuilderForId(int id) {
				Assert.assertEquals(666, id);
				return Messages.Person.newBuilder();
			}
		};
	}

	private Person createPerson(int id, String name, String email) {
		return Messages.Person.newBuilder().setId(id).setName(name).setEmail(email).build();
	}
}
