package itss.batch.ingestion.exceptions;

public class NoDataFoundException extends RuntimeException{
    public NoDataFoundException() {
        super("Not found job id");
    }
}
