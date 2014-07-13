package org.typeframed.api.io;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;

import org.typeframed.api.Envelope;
import org.typeframed.api.EnvelopeWriter;
import org.typeframed.api.HeaderProvider;
import org.typeframed.api.MessageTypeDictionary;
import org.typeframed.api.digest.ChecksumProvider;
import org.typeframed.api.util.NullDigest;

import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Message;

public class StreamWriter<H> implements EnvelopeWriter<H> {

	private ChecksumProvider checksum;
	private HeaderProvider<H> header;
	private MessageTypeDictionary dictionary;
	private OutputStream target;
	
	public StreamWriter(MessageTypeDictionary dictionary, OutputStream target) {
		checkNotNull(dictionary);
		checkNotNull(target);
		this.target = target;
		this.dictionary = dictionary;
	}
	
	public void setChecksumProvider(ChecksumProvider checksum) {
		this.checksum = checksum;
	}
	
	public void setHeaderProvider(HeaderProvider<H> header) {
		this.header = header;
	}
	
	@Override
	public void write(Envelope<H> env) throws IOException {
		target.write(toByteArray(env));
	}
	
	
	// --- PRIVATE METHODS --- //

	private byte[] toByteArray(Envelope<H> env) throws IOException {
		Message msg = env.getMessage();
		MessageDigest digest = checkDigest();
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		DigestOutputStream dout = new DigestOutputStream(bout, digest);
		CodedOutputStream cout = CodedOutputStream.newInstance(dout);
		// dout.on(false); // calculating only on message itself
		/*
		 * 1: Mandatory type
		 */
		int type = dictionary.getId(msg);
		cout.writeRawVarint32(type);
		/*
		 * 2: Optional header, length + data
		 */
		if(header != null) {
			byte[] tmp = header.toBytes(env.getHeader());
			cout.writeRawVarint32(tmp.length);
			cout.writeRawBytes(tmp);
		} else {
			cout.writeRawVarint32(0);
		}
		/*
		 * 3: Message, length + data
		 */
		int len = msg.getSerializedSize();
		cout.writeRawVarint32(len);
		cout.flush(); // !! (the coded output stream is caching... )
		dout.on(true); // start recording digest
		digest.reset();
		msg.writeTo(cout);
		cout.flush(); // !! (the coded output stream is caching... )
		dout.on(false); // end recording digest
		/*
		 * 4: Optional checksum, length + data
		 */
		byte[] checksum = digest.digest();
		if(checksum != null) {
			cout.writeRawVarint32(checksum.length);
			cout.writeRawBytes(checksum);
		} else {
			cout.writeRawVarint32(0);
		}
		cout.flush(); 
		return bout.toByteArray();
	}
	
	private MessageDigest checkDigest() {
		if(this.checksum != null) {
			return this.checksum.toDigest();
		} else {
			return NullDigest.INSTANCE;
		}
	}
}
