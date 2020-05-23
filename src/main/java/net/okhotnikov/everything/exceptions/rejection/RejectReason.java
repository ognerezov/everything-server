package net.okhotnikov.everything.exceptions.rejection;

/**
 * Created by Sergey Okhotnikov.
 */
public enum RejectReason {
    TokenIsNull,
    UserNotFound,
    TokenExpired,
    WrongAuthenticationFormat,
    ExceptionInProcess
}
