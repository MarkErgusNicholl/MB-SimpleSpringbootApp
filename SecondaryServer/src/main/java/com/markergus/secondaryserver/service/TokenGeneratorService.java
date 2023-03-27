package com.markergus.secondaryserver.service;

import com.markergus.secondaryserver.dto.IncomingTokenRequestDto;
import org.springframework.stereotype.Service;

@Service
public class TokenGeneratorService {

    public String generateToken(IncomingTokenRequestDto incomingTokenRequestDto) {
        String key = incomingTokenRequestDto.getUserId() + incomingTokenRequestDto.getUserId();
        return key;
    }
}
