package br.com.diego.sct.domain.error;

public class FileLoadingException extends RuntimeException {
    public FileLoadingException(String message) {
        super(message);
    }
}
