package org.typeframed.api.io;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.typeframed.api.util.Varints.readRawVarint32;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.typeframed.api.ChecksumProvider;
import org.typeframed.api.CurruptedChecksumException;
import org.typeframed.api.Envelope;
import org.typeframed.api.EnvelopeReader;
import org.typeframed.api.HeaderProvider;
import org.typeframed.api.TypeDictionary;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.Message.Builder;

public class StreamReader<H> implements EnvelopeReader<H> {

	private ChecksumProvider checksum;
	private HeaderProvider<H> header;
	private TypeDictionary dictionary;
	private InputStream target;
	
	public StreamReader(TypeDictionary dictionary, InputStream target) {
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
	public Envelope<H> read() throws IOException {
		Envelope<H> e = new Envelope<H>();
		/*
		 * 1: Mandatory type
		 */
		int type = readRawVarint32(target);
		Builder builder = dictionary.getBuilderForId(type);
		/*
		 * 2: Optional header, length + data
		 */
		int len = readRawVarint32(target);
		if(len > 0) {
			ensureHeaderProvider();
			byte[] tmp = new byte[len];
			expectedRead(tmp, len, "header");
			e.setHeader(header.fromBytes(tmp));
		}
		/*
		 * 3: Message, length + data
		 */
		len = readRawVarint32(target);
		byte[] rawMsg = new byte[len];
		expectedRead(rawMsg, len, "message");
		/*
		 * 4: Optional checksum, length + data
		 */
		len = readRawVarint32(target);
		if(len > 0) {
			ensureChecksumProvider();
			byte[] tmp = new byte[len];
			expectedRead(tmp, len, "checksum");
			byte[] current = checksum.calculate(rawMsg);
			if(!Arrays.equals(tmp, current)) {
				throw new CurruptedChecksumException(); // TODO message?
			}
		}
		/*
		 * Now parse the actual message...
		 */
		CodedInputStream cin = CodedInputStream.newInstance(rawMsg);
		do {
			builder.mergeFrom(cin);
		} while(cin.getBytesUntilLimit() > 0);
		cin.checkLastTagWas(0);
		e.setMessage(builder.build());
		return e;
	}
	

	// --- PRIVATE METHODS --- //
	
	private void expectedRead(byte[] tmp, int len, String component) throws IOException {
		int check = target.read(tmp);
		if(check < len) {
			throw new EOFException("Unexpected end of stream in " + component + "; Read " + check + " out of " + len + " bytes");
		}
	}
	
	private void ensureHeaderProvider() {
		if(header == null) {
			throw new IllegalStateException("Mismatch, incoming message contains header, but header provider is null");
		}
	}
	
	private void ensureChecksumProvider() {
		if(checksum == null) {
			throw new IllegalStateException("Mismatch, incoming message contains checksum, but checksum provider is null");
		}
	}
}
