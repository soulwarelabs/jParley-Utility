/*
 * Project:  jParley-Utility
 * Outline:  jParley framework utility components
 *
 * File:     ParameterKey.java
 * Folder:   /.../com/soulwarelabs/jparley/core
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

/**
 * SQL subroutine parameter key.
 *
 * @see Parameter
 * @see ParameterValue
 *
 * @since v1.0
 *
 * @author Ilya Gubarev
 * @version 08 April 2014
 */
public final class ParameterKey {

    private final Integer _index;
    private final String _name;

    private String _textCached;

    /**
     * Creates a new instance of parameter key.
     *
     * @param index a value of an index-based key.
     * @throws IllegalArgumentException if specified index is illegal.
     *
     * @since v1.0
     */
    public ParameterKey(int index) {
        _index = index;
        _name = null;
    }

    /**
     * Creates a new instance of parameter key.
     *
     * @param name a value of a name-based key.
     * @throws IllegalArgumentException if specified name is null.
     *
     * @since v1.0
     */
    public ParameterKey(String name) {
        _index = null;
        _name = name;
    }

    /**
     * Gets a value of the index-based key.
     *
     * @return key value.
     *
     * @since v1.0
     */
    public Integer getIndex() {
        return _index;
    }

    /**
     * Gets a value of the name-based key.
     *
     * @return key value.
     *
     * @since v1.0
     */
    public String getName() {
        return _name;
    }

    /**
     * Checks if the key is index-based.
     *
     * @return true if the key is index-based.
     *
     * @since v1.0
     */
    public boolean isIndexBased() {
        return _index != null;
    }

    /**
     * Checks if the key is name-based.
     *
     * @return true if the key is name-based.
     *
     * @since v1.0
     */
    public boolean isNameBased() {
        return _name != null;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        ParameterKey other = (ParameterKey) object;
        Integer index = other.getIndex();
        String name = other.getName();
        if ((_index != index) && (_index == null || !_index.equals(index))) {
            return false;
        }
        if ((_name == null) ? (name != null) : !_name.equals(name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + (_index != null ? _index.hashCode() : 0);
        hash = 71 * hash + (_name != null ? _name.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        if (_textCached == null) {
            if (_name == null) {
                _textCached = String.format("%d (index-based)", _index);
            } else {
                _textCached = String.format("%s (name-based)", _name);
            }
        }
        return _textCached;
    }
}
