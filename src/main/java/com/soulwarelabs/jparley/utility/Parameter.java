/*
 * Project:  jParley-Utility
 * Outline:  jParley framework utility components
 *
 * File:     Parameter.java
 * Folder:   /.../com/soulwarelabs/jparley/utility
 * Revision: 1.10, 15 April 2014
 * Created:  09 February 2014
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
import com.soulwarelabs.jcommons.Optional;
import com.soulwarelabs.jparley.Converter;

/**
 * SQL subroutine parameter.
 *
 * @since v1.0
 *
 * @author Ilya Gubarev
 * @version 15 April 2014
 */
public class Parameter implements Serializable {

    private @Optional Converter decoder;
    private @Optional Converter encoder;
    private @Optional Box<Object> input;
    private @Optional Box<Object> output;
    private @Optional String struct;
    private @Optional Integer type;

    /**
     * Creates a new instance of SQL subroutine parameter.
     *
     * @since v1.0
     */
    public Parameter() {

    }

    /**
     * Gets an SQL output data decoder of the parameter.
     *
     * @return SQL output data decoder.
     *
     * @see Converter
     *
     * @since v1.0
     */
    public @Optional Converter getDecoder() {
        return decoder;
    }

    /**
     * Sets a new SQL output data decoder for the parameter.
     *
     * @param decoder SQL output data decoder.
     *
     * @see Converter
     *
     * @since v1.0
     */
    public void setDecoder(@Optional Converter decoder) {
        this.decoder = decoder;
    }

    /**
     * Gets an SQL input data encoder of the parameter.
     *
     * @return SQL input data encoder.
     *
     * @see Converter
     *
     * @since v1.0
     */
    public @Optional Converter getEncoder() {
        return encoder;
    }

    /**
     * Sets a new SQL input data encoder for the parameter.
     *
     * @param encoder SQL input data encoder.
     *
     * @see Converter
     *
     * @since v1.0
     */
    public void setEncoder(@Optional Converter encoder) {
        this.encoder = encoder;
    }

    /**
     * Gets a boxed input value of the parameter.
     *
     * @return boxed input value.
     *
     * @see Box
     *
     * @since v1.0
     */
    public @Optional Box<Object> getInput() {
        return input;
    }

    /**
     * Sets a new boxed input value for the parameter.
     *
     * @param input boxed input value.
     *
     * @see Box
     *
     * @since v1.0
     */
    public void setInput(@Optional Box<Object> input) {
        this.input = input;
    }

    /**
     * Gets a boxed output value of the parameter.
     *
     * @return boxed output value.
     *
     * @see Box
     *
     * @since v1.0
     */
    public @Optional Box<Object> getOutput() {
        return output;
    }

    /**
     * Sets a new boxed output value for the parameter.
     *
     * @param output boxed output value.
     *
     * @see Box
     *
     * @since v1.0
     */
    public void setOutput(@Optional Box<Object> output) {
        this.output = output;
    }

    /**
     * Gets an SQL structure name of the parameter.
     *
     * @return SQL structure name.
     *
     * @since v1.0
     */
    public @Optional String getStruct() {
        return struct;
    }

    /**
     * Sets a new SQL structure name for the parameter.
     *
     * @param struct SQL structure name.
     *
     * @since v1.0
     */
    public void setStruct(@Optional String struct) {
        this.struct = struct;
    }

    /**
     * Gets an SQL type code of the parameter.
     *
     * @return SQL type code.
     *
     * @since v1.0
     */
    public @Optional Integer getType() {
        return type;
    }

    /**
     * Sets a new SQL type code for the parameter.
     *
     * @param type SQL type code.
     *
     * @since v1.0
     */
    public void setType(@Optional Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("%s/%s (%s/%s)", input, output, type, struct);
    }
}
