package com.messenger.web.mvc.controller;

import com.messenger.web.mvc.DBProperties;
import com.messenger.web.mvc.entity.Message;
import com.messenger.web.mvc.service.impl.MessageServiceImpl;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: Nastya Vilkova
 */
@RunWith(MockitoJUnitRunner.class)
public class FrontEndControllerTest {


    @InjectMocks
    private FrontEndController fc;

    @Mock
    private MessageServiceImpl messageService;

    Message expected;
    String userMessage = "test message!";
    String userName = "John Smith";

    @Before
    public void setUp() {
        expected = getMessage();
    }

    @Test
    public void testAddMessage() {
        when(messageService.insertMessageToDb("test message!", "John Smith")).thenReturn(expected);
        Message actual = fc.addMessage(userMessage, userName);

        Assert.assertEquals(actual.getMessage(), expected.getMessage());
        Assert.assertEquals(actual.getDate(), expected.getDate());
        Assert.assertEquals(actual.getName(), expected.getName());
    }

    @Test
    public void testSearchMessages() {
        String failedMessage = "No results found!";
        when(messageService.searchMessages(null, null, "1900-01-11 12:01:00")).thenReturn(failedMessage);
        String actual = fc.searchMessages(userMessage, userName, expected.getDate());
        Assert.assertFalse(failedMessage.equalsIgnoreCase(actual));
    }

    @After
    public void tearDown() {

        Connection connection = null;
        PreparedStatement ps = null;
        try {

            connection = DriverManager.getConnection(DBProperties.URL, DBProperties.USER_NAME, DBProperties.PASSWORD);
            ps = connection.prepareStatement("delete from tbl_user_messages " +
                    "where user_name = ? and message_text = ? and message_datetime = ?");
            ps.setString(1, expected.getName());
            ps.setString(2, expected.getMessage());
            ps.setString(3, expected.getDate());
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
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Creates Message object to verify adding and searching of user messages
     * @return the good example of Message, including user_name, message and datetime of creation
     */
    private Message getMessage() {
        Message m = new Message();
        m.setMessage("test message!");
        m.setName("John Smith");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        m.setDate(dateFormat.format(date));
        return m;
    }
}
