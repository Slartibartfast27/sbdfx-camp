/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.sbdfx.transport;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author cbu
 */
public class ResponseWithError<T> {

    private T result;
    private String errorMessage;
    private String errorStacktrace;

    public static <T> ResponseWithError ok(T result) {
        return new ResponseWithError(result, null);
    }

    public static ResponseWithError<Object> error(Throwable error) {
        return new ResponseWithError(null, error);
    }

    private ResponseWithError(T result, Throwable error) {
        this.result = result;
        if (error != null) {
            this.errorMessage = calcErrorMessage(error);
            this.errorStacktrace = calcErrorStacktrace(error);
        }
    }

    private static String calcErrorMessage(Throwable error) {
        // String result = "";
        final StringBuilder result = new StringBuilder();
        String separator = ""; //$NON-NLS-1$
        for (Throwable current = error; current != null; current = current.getCause()) {
            String message = current.getMessage();
            if (StringUtils.isBlank(message)) {
                message = current.getClass().getName();
            }
            // result = result + separator + message;
            result.append(separator).append(message);
            separator = "\n --> "; //$NON-NLS-1$
        }
        return result.toString();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorStacktrace() {
        return errorStacktrace;
    }

    public void setErrorStacktrace(String errorStacktrace) {
        this.errorStacktrace = errorStacktrace;
    }

    private static String calcErrorStacktrace(Throwable error) {
        return ExceptionUtils.getStackTrace(error);
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public boolean isSuccessful() {
        return errorStacktrace == null;
    }
}
