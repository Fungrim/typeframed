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

import org.junit.Before;
import org.typeframed.api.HeaderProvider;
import org.typeframed.api.Int32HeaderProvider;
import org.typeframed.api.MessageTypeDictionary;
import org.typeframed.api.NoSuchTypeException;
import org.typeframed.api.UnknownMessageException;
import org.typeframed.api.Msg.Tell;
import org.typeframed.api.digest.CRC32ChecksumProvider;
import org.typeframed.api.digest.ChecksumProvider;

import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;

public class BaseParseTest {
	
	protected MessageTypeDictionary types;
	protected  ChecksumProvider checksum;
	protected  HeaderProvider<Integer> header;
	
	@Before
	public void setup() throws Exception {
		header = new Int32HeaderProvider();
		checksum = new CRC32ChecksumProvider();
		types = new MessageTypeDictionary() {
			
			@Override
			public int getId(Message msg) throws UnknownMessageException {
				return 666;
			}
			
			@Override
			public Builder getBuilderForId(int id) throws NoSuchTypeException {
				if(id != 666) {
					throw new NoSuchTypeException(id);
				} else {
					return Tell.newBuilder();
				}
			}
		};
	}
}
