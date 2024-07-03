/*
 * This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 *  Copyright (c) 2024 Grégory Van den Borre
 *  More infos available: https://engine.yildiz-games.be
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 *  documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 *  the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 *  permit persons to whom the Software is furnished to do so, subject to the following conditions: The above copyright
 *  notice and this permission notice shall be included in all copies or substantial portions of the  Software.
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 *  OR COPYRIGHT  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 *  OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package be.yildizgames.module.compression.sevenzip;

import be.yildizgames.common.hashing.Algorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

/**
 * @author Grégory Van den Borre
 */
class ZipFileInfoRetrieverTest {

    @Test
    void test() {
        var retriever = new ZipFileInfoRetriever(Path.of("src/test/resources/archive.zip"));
        var result = retriever.getFileInfo(Algorithm.CRC32);
        Assertions.assertEquals("jpeg.jpg", result.getFirst().name());
        var hash = result.getFirst().hashes().getFirst().getBytes();
        Assertions.assertEquals(8, hash.length);
        Assertions.assertEquals(-82, hash[0]);
        Assertions.assertEquals(58, hash[1]);
        Assertions.assertEquals(-88, hash[2]);
        Assertions.assertEquals(-4, hash[3]);
        Assertions.assertEquals(0, hash[4]);
        Assertions.assertEquals(0, hash[5]);
        Assertions.assertEquals(0, hash[6]);
        Assertions.assertEquals(0, hash[7]);

    }

}
