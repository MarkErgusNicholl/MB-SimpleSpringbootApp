package com.markergus.mainserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for admin functions such as refreshing cache, 'ok' api call etc.
 * Upgrades that could be made:
 *  1. Localhost calls only for certain APIs such as cache refreshing
 */
@Controller
@RequestMapping("admin")
public class AdminController {

    @GetMapping(value = "/ok")
    @ResponseBody
    public String ok() {
        return "ok";
    }
}
