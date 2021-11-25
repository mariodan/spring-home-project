package ro.home.project.domain.enums;

public enum ProcessStatus {
    PENDING(0),
    COMPLETED(1),
    REJECTED(2),
    SUCCESS(3),
    PARTIALLY_FAILED(4),
    FAILED(5),
    CANCELLED(6);

    private int code;

    ProcessStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
