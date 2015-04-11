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

import java.io.InputStream;

/**
 * This input stream in unsyncronized and never copies
 * the original byte array. 
 */
public class UnsafeByteArrayInputStream extends InputStream {

	private byte buf[];
    private int pos;
    private int count;

    public UnsafeByteArrayInputStream(byte buf[], int offset, int length) {
    	this.count = Math.min(offset + length, buf.length);
        this.buf = buf;
        this.pos = offset;
    }
    
    public int read() {
        return (pos < count) ? (buf[pos++] & 0xff) : -1;
    }
    
    public int available() {
        return count - pos;
    }
}