package com.hpspells.core.util;

import java.io.File;
import java.io.FileFilter;

public final class FileExtensionFilter implements FileFilter {

    private final String extension;

    /**
     * Creates a new FileExtensionFilter.
     *
     * @param extension the extension to filter for
     */
    public FileExtensionFilter(final String extension) {
        this.extension = extension;
    }

    @Override
    public boolean accept(final File file) {
        return file.getName().endsWith(this.extension);
    }
}