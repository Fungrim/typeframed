## Typeframed ##

Welcome to Typeframed, a protocol, API, plugins and code generators for polomorphic protobuf streaming. That's quite a mouthful, let's break it down. This project...

* ... introduces a wire protocol for determining type and message length of protobuf messages. It also adds an optional header and checksum per message.  
* ... have an API for handling protobuf messages in runtime, including utilities for writing them off/on streams and byte buffers.
* ... contains a build time code generator that will parse a protobuf file to determine message ID's and generate code to help with polymorphism.
* ... contains plugins for various build systems to run the above code generation.

Currently the code generation and API is Java only, but we're looking at Python, Go and JavaScript down the road.  