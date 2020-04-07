// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.template;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum Resource {

    PROPERTIES_FILE,
    ARCHETYPES_FOLDER;

    private static final String ARCHETYPES_SUB_FOLDER = "archetypes";
    private static final String ARCHETYPES_PARENT_FOLDER = "resources";
    private static final String PROPERTIES_FILENAME = "vlingo-xoom-starter.properties";

    private static final Map<Resource, String> FOLDERS = new HashMap<>();

    public static void rootIn(final String rootPath) {
        if(rootPath == null) {
            throw new InvalidResourcesPathException();
        }
        PROPERTIES_FILE.path(Paths.get(rootPath, PROPERTIES_FILENAME).toString());
        ARCHETYPES_FOLDER.path(Paths.get(rootPath, ARCHETYPES_PARENT_FOLDER, ARCHETYPES_SUB_FOLDER).toString());
    }

    private void path(final String path) {
        if(!FOLDERS.containsKey(this)) {
            FOLDERS.put(this, path);
        }
    }

    public String path() {
        return FOLDERS.get(this);
    }

    public static void clear() {
        FOLDERS.clear();
    }

    private boolean hasPath() {
        return FOLDERS.containsKey(PROPERTIES_FILE);
    }

    public static boolean hasAllPaths() {
        return Arrays.asList(values()).stream().allMatch(Resource::hasPath);
    }

}
