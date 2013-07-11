package net.larsan.protobuf.typeframe;

public class CurruptedChecksumException extends IllegalStateException {

	private static final long serialVersionUID = -8873832097630312343L;

	public CurruptedChecksumException() { }

	public CurruptedChecksumException(String s) {
		super(s);
	}
}
