package com.markergus.secondaryserver.controller;

import com.markergus.secondaryserver.dto.IncomingTokenRequestDto;
import com.markergus.secondaryserver.service.TokenGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API to generate a token for customer and linked device
 * Dummy, so in this case just the concat of userId and deviceId
 */
@RestController
@RequestMapping("api/v1/tokenService")
public class TokenGenerationController {

    Logger logger = LoggerFactory.getLogger(TokenGenerationController.class);

    private TokenGeneratorService tokenGeneratorService;

    @Autowired
    public TokenGenerationController(TokenGeneratorService tokenGeneratorService) {
        this.tokenGeneratorService = tokenGeneratorService;
    }

    @PostMapping(value = "/genToken")
    public String generateToken(@RequestBody IncomingTokenRequestDto incomingTokenRequestDto) {
        logger.info(incomingTokenRequestDto.toString());
        return tokenGeneratorService.generateToken(incomingTokenRequestDto);
    }
}
