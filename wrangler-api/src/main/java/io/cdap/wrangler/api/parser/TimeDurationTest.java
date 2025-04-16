/*
 * Copyright © 2017-2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

 package io.cdap.wrangler.api.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * Unit tests for the {@link TimeDuration} class.
 * These tests validate the correctness of time duration parsing, conversion to nanoseconds,
 * exception handling for invalid inputs, and JSON serialization.
 */
public class TimeDurationTest {

    /**
     * Tests that TimeDuration correctly stores the input value and assigns the appropriate token type
     * for valid duration strings.
     */
    @Test
    public void testValidTimeDurationConstructor() {
      TimeDuration duration = new TimeDuration("10ms");
      assertEquals("10ms", duration.value());
      assertEquals(TokenType.TIME_DURATION, duration.type());
  
      duration = new TimeDuration("1.5s");
      assertEquals("1.5s", duration.value());
      assertEquals(TokenType.TIME_DURATION, duration.type());
    }
  
    /**
     * Tests the conversion of various time units (ms, s, m, h, d) to nanoseconds.
     */
    @Test
    public void testNanosecondsConversion() {
      // Milliseconds
      assertEquals(10_000_000L, new TimeDuration("10ms").getNanoseconds());
      assertEquals(100_000_000L, new TimeDuration("100ms").getNanoseconds());
  
      // Seconds
      assertEquals(1_000_000_000L, new TimeDuration("1s").getNanoseconds());
      assertEquals(1_500_000_000L, new TimeDuration("1.5s").getNanoseconds());
  
      // Minutes
      assertEquals(60_000_000_000L, new TimeDuration("1m").getNanoseconds());
      assertEquals(120_000_000_000L, new TimeDuration("2m").getNanoseconds());
  
      // Hours
      assertEquals(3_600_000_000_000L, new TimeDuration("1h").getNanoseconds());
      assertEquals(7_200_000_000_000L, new TimeDuration("2h").getNanoseconds());
  
      // Days
      assertEquals(86_400_000_000_000L, new TimeDuration("1d").getNanoseconds());
      assertEquals(172_800_000_000_000L, new TimeDuration("2d").getNanoseconds());
    }
  
    /**
     * Tests that constructing a TimeDuration with an empty string throws NumberFormatException.
     */
    @Test(expected = NumberFormatException.class)
    public void testEmptyString() {
      new TimeDuration("");
    }
  
    /**
     * Tests that a duration string with no numeric part throws NumberFormatException.
     */
    @Test(expected = NumberFormatException.class)
    public void testMissingNumber() {
      new TimeDuration("ms");
    }
  
    /**
     * Tests that a string with only numeric part and no unit throws IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMissingUnit() {
      new TimeDuration("100").getNanoseconds();
    }
  
    /**
     * Tests that using an invalid or unsupported unit throws IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidUnit() {
      new TimeDuration("100xs").getNanoseconds();
    }
  
    /**
     * Tests handling of special and boundary cases like zero duration and decimal precision.
     */
    @Test
    public void testEdgeCases() {
      // Zero durations
      assertEquals(0L, new TimeDuration("0ms").getNanoseconds());
      assertEquals(0L, new TimeDuration("0s").getNanoseconds());
      assertEquals(0L, new TimeDuration("0m").getNanoseconds());
  
      // Decimal durations
      assertEquals(1_500_000_000L, new TimeDuration("1.5s").getNanoseconds());
      assertEquals(2_500_000_000L, new TimeDuration("2.5s").getNanoseconds());
  
      // Fractional milliseconds
      assertEquals(1_500_000L, new TimeDuration("1.5ms").getNanoseconds());
    }
  
    /**
     * Tests the JSON serialization of a TimeDuration instance to ensure the structure is correct.
     */
    @Test
    public void testToJson() {
      TimeDuration duration = new TimeDuration("1.5s");
      assertNotNull(duration.toJson());
      assertTrue(duration.toJson().isJsonObject());
      assertEquals("TIME_DURATION", duration.toJson().getAsJsonObject().get("type").getAsString());
      assertEquals("1.5s", duration.toJson().getAsJsonObject().get("value").getAsString());
    }
  }
  