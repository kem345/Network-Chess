package main;

@SuppressWarnings("serial")

public class InvalidSpaceException extends Exception{

	/** This exception means that something tried to access a space that is not on a regulation chess board **/
	public InvalidSpaceException() { super(); }
	/** This exception means that something tried to access a space that is not on a regulation chess board **/
	public InvalidSpaceException(String message) { super(message); }
}
