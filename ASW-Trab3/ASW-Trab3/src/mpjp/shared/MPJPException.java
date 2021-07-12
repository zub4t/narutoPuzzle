package mpjp.shared;

import java.io.Serializable;

public class MPJPException extends Exception implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MPJPException() {
	}

	public MPJPException(java.lang.String message, java.lang.Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MPJPException(java.lang.String message) {
		super(message);
	}

	public MPJPException(java.lang.Throwable cause) {
		super(cause);
	}
}
