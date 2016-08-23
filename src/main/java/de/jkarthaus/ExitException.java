package de.jkarthaus;

import org.springframework.boot.ExitCodeGenerator;

public class ExitException extends RuntimeException implements ExitCodeGenerator {

	@Override
	public int getExitCode() {
		return 10;
	}

}
