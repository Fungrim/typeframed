package net.larsan.protobuf.typeframe;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.typeframed.api.util.Varints;

import com.google.protobuf.CodedOutputStream;

public class ProtobufUtilTest {

	private final Random rand = new Random();
	
	@Test
	public void test() throws Exception {
		int[] source = new int[513];
		for (int i = 0; i < source.length; i++) {
			source[i] = rand.nextInt(50000);
		}
		ByteArrayOutputStream baout = new ByteArrayOutputStream();
		CodedOutputStream out = CodedOutputStream.newInstance(baout);
		for (int i = 0; i < source.length; i++) {
			out.writeRawVarint32(source[i]);
		}
		out.flush();
		byte[] arr = baout.toByteArray();
		ByteArrayInputStream in = new ByteArrayInputStream(arr);
		for (int i = 0; i < source.length; i++) {
			Assert.assertEquals(source[i], Varints.readRawVarint32(in));
		}
	}
}
