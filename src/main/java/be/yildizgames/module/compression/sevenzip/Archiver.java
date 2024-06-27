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

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.nio.file.Path;
import java.util.List;

/**
 * This class is used to compress and uncompress files using the 7zip library.
 * It relies on native libraries to provide the compression and decompression functionality.
 *
 * @author Grégory Van den Borre
 */
public class Archiver {

    private final Path lib;

    private final Path sevenzipFile;

    /**
     * Construct a new Archiver.
     *
     * @param libDirectory  The directory containing the native libraries.
     * @param runtimeDirectory The directory containing the runtime libraries.
     * @param sevenZipFile The path to the 7zip native library.
     * @throws IllegalArgumentException If the native library or the runtime library cannot be loaded.
     */
    public Archiver(Path libDirectory, Path runtimeDirectory, Path sevenZipFile) {
        super();
        System.load(runtimeDirectory.resolve("libwinpthread-1.dll").toString());
        System.load(runtimeDirectory.resolve("libgcc_s_seh-1.dll").toString());
        System.load(runtimeDirectory.resolve("libstdc++-6.dll").toString());
        this.lib = libDirectory.resolve("libmodule_compression_7z.dll");
        this.sevenzipFile = sevenZipFile;
    }

    /**
     * Compress multiple files into an archive.
     *
     * @param source The list of files to compress.
     * @param archive The path to the archive file.
     * @throws IllegalArgumentException If the native library or the runtime library cannot be loaded.
     */
    public final void archive(List<Path> source, Path archive) {
        try (var session = Arena.ofConfined()) {
            var lk = SymbolLookup.libraryLookup(lib, session);
            lk.find("compressMultipleFiles").ifPresentOrElse(c -> {
                var compressMultipleFileFunction = Linker.nativeLinker().downcallHandle(c, FunctionDescriptor.of(
                        ValueLayout.JAVA_INT,
                        ValueLayout.ADDRESS,
                        ValueLayout.ADDRESS,
                        ValueLayout.JAVA_INT,
                        ValueLayout.ADDRESS));
                try {
                    var sourceFilePathNames = source
                            .stream()
                            .map(Path::toString)
                            .toArray(String[]::new);
                    var sourcePathsSegment = session.allocate(ValueLayout.ADDRESS, sourceFilePathNames.length);
                    for (int i = 0; i < sourceFilePathNames.length; i++) {
                        var nativeFilePathNAme = session.allocateFrom(sourceFilePathNames[i]);
                        sourcePathsSegment.setAtIndex(ValueLayout.ADDRESS, i, nativeFilePathNAme);
                    }
                    var result = (int) compressMultipleFileFunction.invokeExact(
                            session.allocateFrom(this.sevenzipFile.toString()),
                            sourcePathsSegment,
                            sourceFilePathNames.length,
                            session.allocateFrom(archive.toString()));
                    if(result > 0) {
                        logError(result, "Archive multiple file: " + source + " to " + archive);
                    }
                } catch (Throwable e) {
                    System.getLogger(Archiver.class.getName()).log(System.Logger.Level.ERROR, "", e);
                }
            }, () -> {
                throw new IllegalArgumentException("Function not found: compressMultipleFiles");
            });
        }
    }

    /**
     * Unarchive an archive into a directory.
     *
     * @param archive The path to the archive file.
     * @param destinationDirectory The directory where the files will be extracted.
     * @throws IllegalArgumentException If the native library or the runtime library cannot be loaded.
     */
    public void unarchive(Path archive, Path destinationDirectory) {
        try (var session = Arena.ofConfined()) {
            var lk = SymbolLookup.libraryLookup(lib, session);
            lk.find("decompress").ifPresentOrElse(d -> {
                var compressSingleFileFunction = Linker.nativeLinker().downcallHandle(d, FunctionDescriptor.of(
                        ValueLayout.JAVA_INT,
                        ValueLayout.ADDRESS,
                        ValueLayout.ADDRESS,
                        ValueLayout.ADDRESS));
                try {
                    var result = (int) compressSingleFileFunction.invokeExact(
                            session.allocateFrom(this.sevenzipFile.toString()),
                            session.allocateFrom(archive.toString()),
                            session.allocateFrom(destinationDirectory.toString()));
                    if(result > 0) {
                        logError(result, "Unarchive: " + archive + " to " + destinationDirectory);
                    }
                } catch (Throwable e) {
                    System.getLogger(Archiver.class.getName()).log(System.Logger.Level.ERROR, "", e);
                }
            }, () -> {
                throw new IllegalArgumentException("Function not found: decompress");
            });
        }
    }

    /**
     * Compress a single file into an archive.
     *
     * @param source The path to the file to compress.
     * @param archive The path to the archive file.
     * @throws IllegalArgumentException If the native library or the runtime library cannot be loaded.
     */
    public void archive(Path source, Path archive) {
        try (var session = Arena.ofConfined()) {
            var lk = SymbolLookup.libraryLookup(lib, session);
            lk.find("compressSingleFile").ifPresentOrElse(c -> {
                var compressSingleFileFunction = Linker.nativeLinker().downcallHandle(c, FunctionDescriptor.of(
                        ValueLayout.JAVA_INT,
                        ValueLayout.ADDRESS,
                        ValueLayout.ADDRESS,
                        ValueLayout.ADDRESS));
                try {
                    var result = (int) compressSingleFileFunction.invokeExact(
                            session.allocateFrom(this.sevenzipFile.toString()),
                            session.allocateFrom(source.toString()),
                            session.allocateFrom(archive.toString()));
                    if(result > 0) {
                        logError(result, "Archive single file: " + source + " to " + archive);
                    }
                } catch (Throwable e) {
                    System.getLogger(Archiver.class.getName()).log(System.Logger.Level.ERROR, "", e);
                }
            }, () -> {
                throw new IllegalArgumentException("Function not found: compressSingleFile");
            });
        }
    }

    private static void logError(int errorCode, String context) {
        switch (errorCode) {
            case 0:
                break;
            case 1:
                System.getLogger(Archiver.class.getName()).log(System.Logger.Level.ERROR, "Destination file already exists " + context);
                break;
            case 9:
                System.getLogger(Archiver.class.getName()).log(System.Logger.Level.ERROR, "Archive file does not exists " + context);
                break;
            default:
                System.getLogger(Archiver.class.getName()).log(System.Logger.Level.ERROR,"Unknown error code: " + errorCode + " : " + context);
        }
    }
}
