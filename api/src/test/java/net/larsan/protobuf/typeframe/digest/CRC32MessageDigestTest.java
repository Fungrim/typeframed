package net.larsan.protobuf.typeframe.digest;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.zip.CRC32;

import org.junit.Assert;
import org.junit.Test;
import org.typeframed.api.digest.CRC32MessageDigest;

public class CRC32MessageDigestTest {

	@Test
	public void test() throws Exception {
		CRC32MessageDigest digest = new CRC32MessageDigest();
		byte[] raw = "kalle".getBytes("UTF-8");
		byte[] arr1 = digest.digest(raw);
		CRC32 crc32 = new CRC32();
		crc32.update(raw);
		long val1 = crc32.getValue();
		int val2 = new DataInputStream(new ByteArrayInputStream(arr1)).readInt();
		Assert.assertEquals((int) val1, val2);
	}
}
