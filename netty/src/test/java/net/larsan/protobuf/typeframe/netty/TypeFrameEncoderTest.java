package net.larsan.protobuf.typeframe.netty;

import static org.junit.Assert.assertEquals;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.MessageList;
import junit.framework.Assert;
import net.larsan.protobuf.typeframe.Int32HeaderProvider;
import net.larsan.protobuf.typeframe.MessageEnvelope;
import net.larsan.protobuf.typeframe.Messages;
import net.larsan.protobuf.typeframe.Messages.Person;
import net.larsan.protobuf.typeframe.TypeDictionary;
import net.larsan.protobuf.typeframe.TypeFrame;
import net.larsan.protobuf.typeframe.digest.CRC32ChecksumProvider;

import org.junit.Test;

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
		encoder.setHeader(new Int32HeaderProvider());
		ByteBuf bytes = Unpooled.buffer();
		encoder.encode(null, MessageEnvelope.wrap(1974, person1), bytes);
		bytes.resetReaderIndex();
		byte[] arr = bytes.copy().array();
		TypeFrameDecoder<Integer> decoder = new TypeFrameDecoder<Integer>(dict);
		decoder.setChecksum(new CRC32ChecksumProvider());
		decoder.setHeader(new Int32HeaderProvider());
		MessageList<Object> list = MessageList.newInstance();
		decoder.decode(null, Unpooled.wrappedBuffer(arr), list); 
		assertEquals(1, list.size());
		TypeFrame<Integer> frame = (TypeFrame<Integer>) list.get(0);
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
