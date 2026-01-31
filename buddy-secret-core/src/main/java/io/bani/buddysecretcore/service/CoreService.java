package io.bani.buddysecretcore.service;

import org.springframework.stereotype.Service;

@Service
public class CoreService {

    public String getCoreMessage() {
        return "Hello from Core Module!";
    }
}
