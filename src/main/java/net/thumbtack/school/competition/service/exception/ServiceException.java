package net.thumbtack.school.competition.service.exception;

public class ServiceException extends Throwable {

    private ServiceErrorCode serviceErrorCode;

    public ServiceException(ServiceErrorCode serviceErrorCode) {
        this.serviceErrorCode = serviceErrorCode;
    }

    public ServiceErrorCode getErrorCode() {
        return this.serviceErrorCode;
    }

}
