package org.cws.web.software.engineer.task.security.authority;

public final class Authority {

	public static final String ADMIN = "hasRole('ADMIN')";
	public static final String USER_OR_ADMIN = "hasRole('USER') or hasRole('ADMIN')";
}
