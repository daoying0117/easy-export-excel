package com.daoying.system.exceptions;

/**
 * 请求异常
 * @author daoying
 */
public class BadRequestException extends RuntimeException{

  public BadRequestException(String message) {
    super(message);
  }
}
