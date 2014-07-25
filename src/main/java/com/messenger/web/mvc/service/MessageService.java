package com.messenger.web.mvc.service;

import com.messenger.web.mvc.entity.Message;

/**
 * Created with IntelliJ IDEA.
 * User: Nastya Vilkova
 */
public interface MessageService {
    Message insertMessageToDb(String message, String userName);
    String searchMessages(String name, String dateFrom, String dateTo);
}
