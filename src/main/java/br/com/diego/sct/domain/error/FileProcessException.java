package br.com.diego.sct.domain.error;


public class FileProcessException extends RuntimeException {
    public FileProcessException(String message) {
        super(message);
    }
}
