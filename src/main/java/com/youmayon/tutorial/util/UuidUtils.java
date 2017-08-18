package com.youmayon.tutorial.util;

import org.springframework.util.Assert;

import java.security.SecureRandom;

/**
 * Created by jawinton on 18/08/2017.
 * Generate long type uuid.
 * UUID: 43bit timestamp + 11bit machine id + 10bit sequence.
 */
public class UuidUtils {
    private final static long EPOCH = 1234567890000L;

    private final static long MACHINE_ID_BITS = 11L;

    private final static long SEQUENCE_BITS = 10L;

    private final static long MACHINE_ID_SHIFT = SEQUENCE_BITS;

    private final static long TIMESTAMP_SHIFT = SEQUENCE_BITS + MACHINE_ID_BITS;

    private final static long MAX_MACHINE_ID = -1L ^ (-1L << MACHINE_ID_BITS);

    private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BITS);

    private static long lastTimestamp = EPOCH;

    private static long sequence = 0L;

    public synchronized static long generateUuid(long machineId) {
        Assert.isTrue(machineId >= 0L && machineId <= MAX_MACHINE_ID, "Machine id error.");
        long currentTimestamp = System.currentTimeMillis();
        Assert.isTrue(currentTimestamp >= lastTimestamp, "Time error.");
        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                do {
                    currentTimestamp = System.currentTimeMillis();
                } while (currentTimestamp == lastTimestamp);
            }
        } else {
            sequence = new SecureRandom().nextInt(24);
        }
        long timestamp = currentTimestamp - EPOCH;
        lastTimestamp = currentTimestamp;
        timestamp = timestamp << TIMESTAMP_SHIFT;
        machineId = machineId << MACHINE_ID_SHIFT;
        return timestamp | machineId | sequence;
    }
}
