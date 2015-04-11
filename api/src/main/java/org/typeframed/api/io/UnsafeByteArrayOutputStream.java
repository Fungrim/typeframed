/**
 * Copyright 2015 Lars J. Nilsson <contact@larsan.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.typeframed.api.io;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * This output stream acts as a normal byte array output stream, but
 * is not synchronized and also have a "asByteBuffer" method which returns
 * the underlying byte buffer, rendering the output stream unsafe for 
 * re-use. 
 * 
 * <p>The default size of the buffer is 64, and it grows with at least the
 * initial size using the same algorithm as the standard libs. 
 * 
 * <p>This output stream does not throw IO exceptions.
 */
public class UnsafeByteArrayOutputStream extends OutputStream {

	private byte buf[];
    private int count;
	private final int initialSize;
	
    public UnsafeByteArrayOutputStream() {
		this(64);
	}
    
	public UnsafeByteArrayOutputStream(int initialSize) {
		checkArgument(initialSize >= 0, "Initial size must be zero or more; Was: " + initialSize); 
		this.initialSize = initialSize;
		buf = new byte[initialSize];
		count = 0;
	}
	
	public InputStream reverse() {
		return new UnsafeByteArrayInputStream(buf, 0, count);
	}
	
    private void ensureCapacity(int minCapacity) {
        if (minCapacity - buf.length > 0) {
        	grow(minCapacity);
        }
    }
    
    @Override
    public void write(byte b[]) {
        write(b, 0, b.length);
    }
    
    private void grow(int minCapacity) {
        int oldCapacity = buf.length;
        int newCapacity = oldCapacity << 1;
        if (newCapacity - minCapacity < 0) {        	
        	newCapacity = minCapacity;
        }
        if (newCapacity < 0) {
            if (minCapacity < 0) { // overflow
                throw new OutOfMemoryError();
            }
            newCapacity = Integer.MAX_VALUE;
        }
        buf = Arrays.copyOf(buf, newCapacity);
    }
    
    public int size() {
        return count;
    }

	@Override
	public void write(int b) {
        ensureCapacity(count + 1);
        buf[count] = (byte) b;
        count += 1;
	}

    public void write(byte b[], int off, int len) {
        if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) - b.length > 0)) {
            throw new IndexOutOfBoundsException();
        }
        ensureCapacity(count + len);
        System.arraycopy(b, off, buf, count, len);
        count += len;
    }
    
    public void reset(boolean toInitialSize) {
        count = 0;
        if(toInitialSize) {
        	buf = new byte[initialSize];
        }
    }
    
    public void flush() { }

    public void close() { }
    
    /**
     * This method returns a copy of the underlying buffer. For a live view of
     * the buffer, use {@link #asByteBuffer()}.
     */
    public byte[] toByteArray() {
        return Arrays.copyOf(buf, count);
    }
    
    
    /**
     * This method returns a wrapped byte buffer based on the underlying
     * byte array. For a cloned byte array, use {@link #toByteArray()} instead.
     * 
     * @return A live reference to the buffer, never null
     */
    public ByteBuffer asByteBuffer() {
    	return ByteBuffer.wrap(buf, 0, count);
    }
}
