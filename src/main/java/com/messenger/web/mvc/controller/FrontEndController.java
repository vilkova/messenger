package com.messenger.web.mvc.controller;

import com.messenger.web.mvc.entity.Message;
import com.messenger.web.mvc.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nastya Vilkova
 */
@Controller
@RequestMapping("/")
public class FrontEndController {

    @Autowired
    MessageService messageService;

    @RequestMapping(value = "/save", method = {RequestMethod.POST}, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Message addMessage(@RequestParam(value = "message", required = true) String message,
                              @RequestParam(value = "name", required = true) String name) {
        return messageService.insertMessageToDb(message, name);

    }

    @RequestMapping(value = "/search", method = {RequestMethod.POST}, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String searchMessages(@RequestParam(value = "name", required = true) String name,
                                 @RequestParam(value = "from", required = true) String dateFrom,
                                 @RequestParam(value = "to", required = true) String dateTo) {
        return messageService.searchMessages(name, dateFrom, dateTo);
    }
}

