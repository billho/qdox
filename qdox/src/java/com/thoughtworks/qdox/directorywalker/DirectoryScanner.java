package com.thoughtworks.qdox.directorywalker;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class DirectoryScanner {
    
    private File file;
    private Collection<Filter> filters = new HashSet<Filter>();

    public DirectoryScanner(File file) {
        this.file = file;
    }

    public List<File> scan() {
        final List<File> result = new LinkedList<File>();
        walk( new FileVisitor() {
            public void visitFile(File file) {
                result.add(file);
            }
        }, this.file);
        return result;
    }

    private void walk(FileVisitor visitor, File current) {
        if (current.isDirectory()) {
            File[] currentFiles = current.listFiles();
            for (int i = 0; i < currentFiles.length; i++) {
                walk(visitor, currentFiles[i]);
            }
        } else {
            for (Filter filter : this.filters) {
                if (!filter.filter(current)) {
                    return;
                }
            }
            visitor.visitFile(current);
        }
    }

    public void addFilter(Filter filter) {
        this.filters.add(filter);
    }

    public void scan(FileVisitor fileVisitor) {
        walk(fileVisitor, this.file);
    }
}
