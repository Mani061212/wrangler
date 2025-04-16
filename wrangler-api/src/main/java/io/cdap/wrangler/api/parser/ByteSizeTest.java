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
 * Unit tests for the {@link ByteSize} class.
 * These tests verify the correctness of byte size parsing and conversion logic.
 */
public class ByteSizeTest {

    /**
     * Tests construction of ByteSize objects with valid input strings
     * and verifies that the value and type are set correctly.
     */
    @Test
    public void testValidByteSizeConstructor() {
      ByteSize size = new ByteSize("10B");
      assertEquals("10B", size.value());
      assertEquals(TokenType.BYTE_SIZE, size.type());
  
      size = new ByteSize("1.5KB");
      assertEquals("1.5KB", size.value());
      assertEquals(TokenType.BYTE_SIZE, size.type());
    }
  
    /**
     * Tests the conversion of various units (B, KB, MB, GB, TB, PB)
     * to their corresponding byte values.
     */
    @Test
    public void testBytesConversion() {
      assertEquals(10L, new ByteSize("10B").getBytes());
      assertEquals(100L, new ByteSize("100B").getBytes());
      assertEquals(1024L, new ByteSize("1KB").getBytes());
      assertEquals(1536L, new ByteSize("1.5KB").getBytes());
      assertEquals(1048576L, new ByteSize("1MB").getBytes());
      assertEquals(2097152L, new ByteSize("2MB").getBytes());
      assertEquals(1073741824L, new ByteSize("1GB").getBytes());
      assertEquals(2147483648L, new ByteSize("2GB").getBytes());
      assertEquals(1099511627776L, new ByteSize("1TB").getBytes());
      assertEquals(2199023255552L, new ByteSize("2TB").getBytes());
      assertEquals(1125899906842624L, new ByteSize("1PB").getBytes());
      assertEquals(2251799813685248L, new ByteSize("2PB").getBytes());
    }
  
    /**
     * Tests construction with an empty string which should throw NumberFormatException.
     */
    @Test(expected = NumberFormatException.class)
    public void testEmptyString() {
      new ByteSize("");
    }
  
    /**
     * Tests input with no number part. Should throw NumberFormatException.
     */
    @Test(expected = NumberFormatException.class)
    public void testMissingNumber() {
      new ByteSize("KB");
    }
  
    /**
     * Tests input with a number but missing unit part. Should throw IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMissingUnit() {
      new ByteSize("100").getBytes();
    }
  
    /**
     * Tests input with an unrecognized unit. Should throw IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidUnit() {
      new ByteSize("100XB").getBytes();
    }
  
    /**
     * Tests boundary and edge cases including zero values and precision handling.
     */
    @Test
    public void testEdgeCases() {
      assertEquals(0L, new ByteSize("0B").getBytes());
      assertEquals(0L, new ByteSize("0KB").getBytes());
      assertEquals(0L, new ByteSize("0MB").getBytes());
      assertEquals(1536L, new ByteSize("1.5KB").getBytes()); // decimal to bytes
      assertEquals(2560L, new ByteSize("2.5KB").getBytes()); // another decimal case
      assertEquals(1074790400L, new ByteSize("1.0009765625GB").getBytes()); // large decimal
    }
  
    /**
     * Tests the toJson method of ByteSize which should serialize the object correctly.
     */
    @Test
    public void testToJson() {
      ByteSize size = new ByteSize("1.5KB");
      assertNotNull(size.toJson());
      assertTrue(size.toJson().isJsonObject());
      assertEquals("BYTE_SIZE", size.toJson().getAsJsonObject().get("type").getAsString());
      assertEquals("1.5KB", size.toJson().getAsJsonObject().get("value").getAsString());
    }
  }
  