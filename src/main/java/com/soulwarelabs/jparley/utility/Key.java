/*
 * Project:  jParley-Utility
 * Outline:  jParley framework utility components
 *
 * File:     Key.java
 * Folder:   /.../com/soulwarelabs/jparley/utility
 * Revision: 1.01, 08 April 2014
 * Created:  07 April 2014
 * Author:   Ilya Gubarev
 *
 * Copyright (c) 2014 Soulware Labs, Ltd.
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

import com.soulwarelabs.jcommons.Box;

/**
 * Parameter key.
 *
 * @see Box
 *
 * @since v1.0
 *
 * @author Ilya Gubarev
 * @version 08 April 2014
 */
public class Key extends Box<Object> {

    /**
     * Creates a new instance of parameter key.
     *
     * @param value key value.
     *
     * @since v1.0
     */
    public Key(Object value) {
        super(value);
    }
}
