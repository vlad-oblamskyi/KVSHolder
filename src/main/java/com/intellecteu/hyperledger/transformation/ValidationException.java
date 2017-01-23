package com.intellecteu.hyperledger.transformation;

/**
 * Class to wrap any MT validation exception
 */
public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}
