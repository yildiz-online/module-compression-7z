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
import be.yildizgames.common.hashing.FileHash;
import be.yildizgames.common.hashing.HashingFactory;
import be.yildizgames.module.compression.FileInfo;
import be.yildizgames.module.compression.FileInfoRetriever;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Grégory Van den Borre
 */
public class SevenZipFileInfoRetriever implements FileInfoRetriever {

    public SevenZipFileInfoRetriever() {
        super();
    }

    @Override
    public List<FileInfo> getFileInfo(Algorithm... algorithms) {
        if (algorithms == null || algorithms.length == 0) {
            return noCompute();
        }
        return computeHashes(algorithms);
    }

    private List<FileInfo> noCompute() {
       /* var result = new ArrayList<FileInfo>();
        try (var sevenZFile = new SevenZFile(this.path.toFile(), SevenZFileOptions.builder().withTryToRecoverBrokenArchives(true).build())) {
            for (var e : sevenZFile.getEntries()) {
                if (!e.isDirectory()) {
                    result.add(new FileInfo(e.getName(), List.of()));
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return result;*/
        return List.of();
    }

    private List<FileInfo> computeHashes(Algorithm... algorithms) {
        /*var result = new ArrayList<FileInfo>();
        Map<String, List<FileHash>> hashes = new HashMap<>();
        for (var a : algorithms) {
            try (var sevenZFile = new SevenZFile(this.path.toFile(), SevenZFileOptions.builder().withTryToRecoverBrokenArchives(true).build())) {
                for (var e : sevenZFile.getEntries()) {
                    if (!e.isDirectory()) {
                        var is = sevenZFile.getInputStream(e);
                        var hash = HashingFactory.get(a).compute(is, (int) e.getSize());
                        if (!hashes.containsKey(e.getName())) {
                            hashes.put(e.getName(), new ArrayList<>());
                        }
                        hashes.get(e.getName()).add(hash);
                    }
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        for(var entry : hashes.entrySet()) {
            result.add(new FileInfo(entry.getKey(), entry.getValue()));
        }
        return result;*/
        return List.of();
    }
}
