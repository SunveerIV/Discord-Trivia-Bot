package com.sunveer.utility;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class HashTest {
    @Test
    void testUsingDateWithSha384() {
        LocalDateTime time = LocalDateTime.of(2025, 11, 21, 9, 1, 30, 1234);
        String input = time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String computedHash = Hash.sha384(input);
        String actualHash = "a1ea24a0df2b0999014a1aef707c172c492716684c2a107130910e36e243d4a9497f931552d3eae6999bb41e0120f7d8";
        assertEquals(actualHash, computedHash);
    }
}