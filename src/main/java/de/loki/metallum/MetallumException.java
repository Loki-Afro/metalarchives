package de.loki.metallum;

import java.util.concurrent.ExecutionException;

public class MetallumException extends Exception {
	private static final long serialVersionUID = -2718275398369069425L;

	public MetallumException(final String message) {
		super(message);
	}

	public MetallumException(final ExecutionException e) {
		super(e);
	}
}
