package org.typeframed.netty;

import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.Assert.assertEquals;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.MessageList;
import junit.framework.Assert;
import net.larsan.protobuf.typeframe.Messages;
import net.larsan.protobuf.typeframe.Messages.Person;

import org.junit.Test;
import org.typeframed.api.Int32HeaderProvider;
import org.typeframed.api.MessageEnvelope;
import org.typeframed.api.TypeDictionary;
import org.typeframed.api.TypeFrame;
import org.typeframed.api.digest.CRC32ChecksumProvider;
import org.typeframed.netty.MessageEnvelopeEncoder;
import org.typeframed.netty.TypeFrameDecoder;

import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;

public class TypeFrameDecoderTest {

	@Test
	@SuppressWarnings("unchecked")
	public void testReadFully() throws Exception {
		byte[] bytes = createPersonBytes();
		TypeDictionary dict = createTypeDictionary();
		TypeFrameDecoder<Integer> decoder = new TypeFrameDecoder<Integer>(dict);
		decoder.setChecksum(new CRC32ChecksumProvider());
		decoder.setHeader(new Int32HeaderProvider());
		MessageList<Object> list = MessageList.newInstance();
		decoder.decode(null, wrappedBuffer(bytes, 0, 0), list);
		assertEquals(0, list.size());
		decoder.decode(null, wrappedBuffer(bytes, 0, 10), list);
		assertEquals(0, list.size());
		decoder.decode(null, wrappedBuffer(bytes, 0, 20), list); // full data frame is 18 + 10 bytes
		assertEquals(0, list.size());
		decoder.decode(null, wrappedBuffer(bytes), list); 
		assertEquals(1, list.size());
		TypeFrame<Integer> frame = (TypeFrame<Integer>) list.get(0);
		assertEquals(18, frame.getLength());
		assertEquals(666, frame.getType());
		assertEquals(new Integer(1974), frame.getHeader());
		Person person = createPerson(32, "kalle", "k@k.com");
		assertEquals(person, frame.getMessage());
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

	private byte[] createPersonBytes() throws Exception {
		Person person1 = createPerson(32, "kalle", "k@k.com");
		final TypeDictionary dict = createTypeDictionary();
		MessageEnvelopeEncoder<Integer> encoder = new MessageEnvelopeEncoder<Integer>(dict);
		encoder.setChecksum(new CRC32ChecksumProvider());
		encoder.setHeader(new Int32HeaderProvider());
		ByteBuf bytes = Unpooled.buffer();
		encoder.encode(null, MessageEnvelope.wrap(1974, person1), bytes);
		bytes.resetReaderIndex();
		byte[] arr = bytes.copy().array();
		return arr;
	}

	private Person createPerson(int id, String name, String email) {
		return Messages.Person.newBuilder().setId(id).setName(name).setEmail(email).build();
	}
}
