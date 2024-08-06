/*
 This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 Copyright (c) 2020-2024 Grégory Van den Borre
 More infos available: https://engine.yildiz-games.be
 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to the following conditions: The above copyright
 notice and this permission notice shall be included in all copies or substantial portions of the  Software.
 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 OR COPYRIGHT  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package be.yildizgames.module.compression.sevenzip;

import be.yildizgames.module.compression.FileInfoRetriever;
import be.yildizgames.common.hashing.Algorithm;
import be.yildizgames.common.hashing.HashingFactory;
import be.yildizgames.module.compression.FileInfo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Retrieve info stored in a ZIP archive.
 * @author Grégory Van den Borre
 */
public class ZipFileInfoRetriever implements FileInfoRetriever {

    private final Path path;

    public ZipFileInfoRetriever(Path path) {
        super();
        this.path = path;
    }

    @Override
    public final List<FileInfo> getFileInfo(Algorithm... algorithms) {
        if (algorithms == null) {
            algorithms = new Algorithm[0];
        }
        var result = new ArrayList<FileInfo>();
        try (ZipFile zip = new ZipFile(path.toFile())) {
            for (Enumeration<? extends ZipEntry> e = zip.entries(); e.hasMoreElements(); ) {
                ZipEntry entry = e.nextElement();
                result.add(new FileInfo(entry.getName(), Arrays.stream(algorithms).map(
                        a -> {
                            try {
                                return HashingFactory.get(a).compute(zip.getInputStream(entry));
                            } catch (IOException ex) {
                                throw new IllegalStateException(ex);
                            }
                        }
                ).toList()));
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return result;
    }
}
