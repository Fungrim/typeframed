# Typeframed #
Welcome to Typeframed, a protocol, API, plugins and code generators for polomorphic protobuf streaming. That's quite a mouthful, let's break it down. This project...

* ... introduces a wire protocol for determining type and message length of protobuf messages. It also adds an optional header and checksum per message.  
* ... have an API for handling protobuf messages in runtime, including utilities for writing them off/on streams and byte buffers.
* ... contains a build time code generator that will parse a protobuf file to determine message ID's and generate code to help with polymorphism.
* ... contains plugins for various build systems to run the above code generation.

Currently the code generation and API is Java only, but we're looking at Python, Go and JavaScript down the road.

## Wire Protocol ##
The wire protocol wraps each protobuf message in an envelope containing an optional header field and an equally optional checksum. 

```
-------------------------------------------------------------------
| type | hdr len [| header] | msg len | msg | chsum len [| chsum] |
-------------------------------------------------------------------
```

In order of appearance:

* *type* - Type identifier; varint.
* *header length* - Length of header; varint (0 == no header)
* *header* - Header data, custom encoded.
* *message length* - Message length; varint.
* *message* - Protobuf message
* *checksum length* - Length of checksum; varint (0 == no checksum)
* *checksum* - Checksum data, custom encoded

All length fields and the type field are 32 bit raw [varints](https://developers.google.com/protocol-buffers/docs/encoding). From this follows that a minimal Typeframed messages is 4 bytes excluding protobuf message length.  

## Header ##
The header field is optional and will be customized per installation. It could for example carry a correlation ID for request/response messaging, transaction ID for idempotent messaging etc. 

## Checksum ##
Likewise the checksum is optional. If included it should contain a fast checksum (such as the default CRC32) on the protobuf message in its binary form. 

# Embedding ID in .proto files #
Typeframed uses an option field in protobuf to embed ID information for each message. En example protofile for a *hello world* example might look like this:

```
package test;

// #1 - import descriptor
import "descriptor.proto";

// #2 - declare type_id option
extend google.protobuf.MessageOptions {
  optional int32 type_id = 50666;
}

message HelloRequest {
  // #3 - set ID for message
  option (type_id) = 100; 
  required string name = 1;
}

message HelloResponse {
  // #3 - set ID for message
  option (type_id) = 101; 
  required string text = 1;
}
``` 
The protofile above declares two messages with type ID 100 and 101. These type ID's are set as option fields - hence the import and separate declaration - which will be read by Typeframed to generate polymorphic helper code. 
