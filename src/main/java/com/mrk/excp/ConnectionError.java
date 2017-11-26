package com.mrk.excp;

/**
 * Connection Error exception
 * @author Leo
 * @version 1.0
 *
 */
public class ConnectionError extends Exception {
	public ConnectionError(String message) {
        super(message);
    }
}
