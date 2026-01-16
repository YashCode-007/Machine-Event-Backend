package com.factory.machine_events.util;


import com.factory.machine_events.dto.EventIngestRequest;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PayloadHashUtil {

    private PayloadHashUtil() {

    }

    public static String generateHash(EventIngestRequest request) {

        String payload = request.getEventId() + "|" +
                request.getEventTime() + "|" +
                request.getMachineId() + "|" +
                request.getDurationMs() + "|" +
                request.getDefectCount();

        return sha256(payload);
    }

    private static String sha256(String input) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();

            for(byte b : hashBytes) {
                hexString.append(String.format("%02x",b));
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not found", e);
        }
    }
}
