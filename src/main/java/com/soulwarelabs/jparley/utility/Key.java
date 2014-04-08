/*
 * Project:  jParley-Utility
 * Outline:  jParley framework utility components
 *
 * File:     ParameterKey.java
 * Folder:   /.../com/soulwarelabs/jparley/utility
 * Revision: 1.03, 08 April 2014
 * Created:  10 February 2014
 * Author:   Ilya Gubarev
 *
 * Copyright (c) 2013-2014 Soulware Labs, Ltd.
 * Contact information is available at http://www.soulwarelabs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.soulwarelabs.jparley.utility;

import java.io.Serializable;

/**
 * SQL subroutine parameter key.
 *
 * @since v1.0
 *
 * @author Ilya Gubarev
 * @version 08 April 2014
 */
public class Key implements Serializable {

    private Integer index;
    private String name;

    /**
     * Creates a new instance of parameter key.
     *
     * @param index a value of an index-based key.
     *
     * @since v1.0
     */
    public Key(int index) {
        this.index = index;
    }

    /**
     * Creates a new instance of parameter key.
     *
     * @param name a value of a name-based key.
     *
     * @since v1.0
     */
    public Key(String name) {
        this.name = name;
    }

    /**
     * Gets a value of the index-based key.
     *
     * @return key value.
     *
     * @since v1.0
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * Gets a value of the name-based key.
     *
     * @return key value.
     *
     * @since v1.0
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if the key is index-based.
     *
     * @return true if the key is index-based.
     *
     * @since v1.0
     */
    public boolean isIndexBased() {
        return index != null;
    }

    /**
     * Checks if the key is name-based.
     *
     * @return true if the key is name-based.
     *
     * @since v1.0
     */
    public boolean isNameBased() {
        return name != null;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        Key other = (Key) object;
        if ((index != other.index) &&
                (index == null || !index.equals(other.index))) {
            return false;
        }
        if ((name == null) ? (other.name != null) : !name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + (index != null ? index.hashCode() : 0);
        hash = 71 * hash + (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        if (name == null) {
            return String.format("{index: %d}", index);
        } else {
            return String.format("{name: %s}", name);
        }
    }
}
