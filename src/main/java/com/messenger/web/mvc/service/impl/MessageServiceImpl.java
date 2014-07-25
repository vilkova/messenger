package com.messenger.web.mvc.service.impl;

import com.messenger.web.mvc.DBProperties;
import com.messenger.web.mvc.entity.Message;
import com.messenger.web.mvc.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Nastya Vilkova
 */
@Component
public class MessageServiceImpl implements MessageService {
    private static Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);
    private String failedSearchMessage = "No results found!";

    /**
     * Inserts user message to database
     * @return Message object
     */
    @Override
    public Message insertMessageToDb(String message, String userName) {

        Message ms = null;
        Connection con = getConnection();
        CallableStatement cs = null;
        try {
            ms = new Message();
            ms.setName(userName);
            ms.setMessage(message);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            ms.setDate(dateFormat.format(date));
            cs = con.prepareCall("{call addUserMessage(?, ?, ?)}");
            cs.setString(1, ms.getName());
            cs.setString(2, ms.getMessage());
            cs.setString(3, ms.getDate());
            cs.execute();
            logger.info("Added user name: " + userName + " and his message: " + message + " to DB");
            return ms;
        } catch (SQLException e) {
            logger.error("SQLException: " + e.getMessage());
        } finally {
            if (cs != null) {
                try {
                    cs.close();
                } catch (SQLException e) {
                    logger.error("SQLException: " + e.getMessage());
                }
            }
        }
        return ms;
    }

    private String generateHtmlFromResultSet(ResultSet rs) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("<table>");
            sb.append("<tr><td>User Name</td><td>User Message</td><td>Date</td></tr>");
            sb.append("<tr>");
            sb.append("<td>");
            sb.append(rs.getString("user_name"));
            sb.append("</td>");
            sb.append("<td>");
            sb.append(rs.getString("message_text"));
            sb.append("</td>");
            sb.append("<td>");
            sb.append(rs.getString("message_datetime"));
            sb.append("</td>");
            sb.append("</tr>");
            while (rs.next()) {
                sb.append("<tr>");
                sb.append("<td>");
                sb.append(rs.getString("user_name"));
                sb.append("</td>");
                sb.append("<td>");
                sb.append(rs.getString("message_text"));
                sb.append("</td>");
                sb.append("<td>");
                sb.append(rs.getString("message_datetime"));
                sb.append("</td>");
                sb.append("</tr>");
            }
            sb.append("</table>");
            return sb.toString();
        } catch (SQLException e) {
            logger.error("SQLException: " + e.getMessage());
        }
        return null;
    }

    /**
     * Searches messages by user name and message timestamp filters
     * @return Search results in HTML table format
     */
    @Override
    public String searchMessages(String name, String dateFrom, String dateTo) {
        String result = failedSearchMessage;
        if (dateFrom.trim().length() == 0) {
            dateFrom = null;
        }
        if (dateTo.trim().length() == 0) {
            dateTo = null;
        }
        if (name.trim().length() == 0) {
            name = null;
        }
        Connection con = getConnection();
        CallableStatement cs = null;
        try {
            cs = con.prepareCall("{call searchMessages(?, ?, ?)}");
            cs.setString(1, name);
            cs.setString(2, dateFrom);
            cs.setString(3, dateTo);
            cs.execute();
            ResultSet rs = cs.getResultSet();
            if (rs.first()) {
                result = generateHtmlFromResultSet(rs);
            }
        } catch (SQLException e) {
            logger.error("SQLException: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (cs != null) {
                try {
                    cs.close();
                } catch (SQLException e) {
                    logger.error("SQLException: " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    logger.error("SQLException: " + e.getMessage());
                }
            }
        }
        return result;
    }

    public String getFailedSearchMessage() {
        return failedSearchMessage;
    }

    private Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DBProperties.URL, DBProperties.USER_NAME, DBProperties.PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
