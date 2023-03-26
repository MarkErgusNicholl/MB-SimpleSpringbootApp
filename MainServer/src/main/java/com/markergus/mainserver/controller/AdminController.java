package com.markergus.mainserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for admin functions such as refreshing cache, 'ok' api call etc.
 * Upgrades that could be made:
 *  1. Localhost calls only for certain APIs such as cache refreshing
 */
@RestController
@RequestMapping("admin")
public class AdminController {

    @GetMapping(value = "/ok")
    public String ok() {
        return "ok";
    }
}
