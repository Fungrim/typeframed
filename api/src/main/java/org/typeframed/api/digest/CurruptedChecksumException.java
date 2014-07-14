package org.typeframed.api.digest;

/**
 * An illegal state exception thrown when a checksum is corrupted.
 *
 * @author Lars J. Nilsson
 */
public class CurruptedChecksumException extends IllegalStateException {

	private static final long serialVersionUID = -8873832097630312343L;

	public CurruptedChecksumException() { }

	public CurruptedChecksumException(String s) {
		super(s);
	}
}
