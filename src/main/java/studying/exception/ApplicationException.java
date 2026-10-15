package studying.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
    private final ApplicationErrorCode code;

    public ApplicationException(final ApplicationErrorCode code, final String message) {
        super(message);
        this.code = code;
    }

    public ApplicationException(final ApplicationErrorCode code, final String message, final Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
