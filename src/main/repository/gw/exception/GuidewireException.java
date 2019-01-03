package repository.gw.exception;

public class GuidewireException extends Exception {

	private static final long serialVersionUID = 1L;

	public GuidewireException(String url, String exceptionMessage) {
		super("ERROR -- Server: " + url + " -- " + exceptionMessage);
	}

}
