/*
 * Project:  jParley-Utility
 * Outline:  jParley framework utility components
 *
 * File:     ParameterValue.java
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

import java.io.Serializable;

import com.soulwarelabs.jcommons.Box;

/**
 * Parameter value container.
 *
 * @see Box
 *
 * @since v1.0
 *
 * @author Ilya Gubarev
 * @version 08 April 2014
 */
public class Value implements Box, Serializable {

    private Object value;

    /**
     * Creates a new instance of value container.
     *
     * @since v1.0
     */
    public Value() {

    }

    /**
     * Creates a new instance of value container.
     *
     * @param value parameter value.
     *
     * @since v1.0
     */
    public Value(Object value) {
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("%s", value);
    }
}
