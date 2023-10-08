package com.daoying.system.exceptions;

/**
 * 用户已存在异常
 * @author daoying
 */
public class UserAlreadyExistException extends RuntimeException{

      public UserAlreadyExistException(String message) {
        super(message);
      }
}
