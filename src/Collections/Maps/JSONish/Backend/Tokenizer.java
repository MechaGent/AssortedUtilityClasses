package Collections.Maps.JSONish.Backend;

import Streams.BytesStreamer.BytesStreamer;

public interface Tokenizer
{
	public Token parseNextToken(BytesStreamer in);

	public Token peekAtNextToken(BytesStreamer in);
}
