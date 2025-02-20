package org.example.socialnetwork.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidGenerator {
    public UUID generateUuid() {
        return UUID.randomUUID();
    }
}
