package com.messenger.web.mvc.service.impl;

import com.messenger.web.mvc.DBProperties;
import com.messenger.web.mvc.entity.Message;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nastya Vilkova
 */
public class MessageServiceImplTest {

    private Message ms;
    private MessageServiceImpl messageService;

    @Before
    public void setUp() {
        messageService = new MessageServiceImpl();
        String userMessage = "Hello Worlds Message!";
        String userName = "Vasya";
        ms = messageService.insertMessageToDb(userMessage, userName);
    }

    @Test
    public void testAddMessageToDb() {
        Assert.assertNotNull(messageService.insertMessageToDb(null, null));
        Assert.assertNotNull(messageService.insertMessageToDb(ms.getMessage(), ms.getName()));
        Assert.assertNotNull(messageService.insertMessageToDb(ms.getMessage(), null));
        Assert.assertNotNull(messageService.insertMessageToDb(null, ms.getName()));
    }

    @Test
    public void searchMessagesTest() {
        String failedMessage = messageService.getFailedSearchMessage();
        Assert.assertFalse(failedMessage.equalsIgnoreCase(messageService.searchMessages("", "", "")));
        Assert.assertFalse(failedMessage.equalsIgnoreCase(messageService.searchMessages("", ms.getDate(), "")));
        Assert.assertFalse(failedMessage.equalsIgnoreCase(messageService.searchMessages("", "", ms.getDate())));
        Assert.assertFalse(failedMessage.equalsIgnoreCase(messageService.searchMessages(ms.getName(), "", "")));
        Assert.assertFalse(failedMessage.equalsIgnoreCase(messageService.searchMessages(ms.getName(), ms.getDate(), ms.getDate())));
        Assert.assertTrue(failedMessage.equalsIgnoreCase(messageService.searchMessages("Marusya", ms.getDate(), ms.getDate())));
    }

    @After
    public void tearDown() {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DriverManager.getConnection(DBProperties.URL, DBProperties.USER_NAME, DBProperties.PASSWORD);
            ps = connection.prepareStatement("delete from tbl_user_messages " +
                    "where user_name = ? and message_text = ? and message_datetime = ?");
            ps.setString(1, ms.getName());
            ps.setString(2, ms.getMessage());
            ps.setString(3, ms.getDate());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
