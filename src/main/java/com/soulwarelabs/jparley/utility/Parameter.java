/*
 * Project:  jParley-Utility
 * Outline:  jParley framework utility components
 *
 * File:     Parameter.java
 * Folder:   /.../com/soulwarelabs/jparley/core
 * Revision: 1.07, 08 April 2014
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

import com.soulwarelabs.jparley.Converter;

/**
 * SQL subroutine parameter.
 *
 * @see ParameterKey
 * @see ParameterValue
 *
 * @since v1.0
 *
 * @author Ilya Gubarev
 * @version 08 April 2014
 */
public final class Parameter {

    private Converter _decoder;
    private Converter _encoder;
    private boolean _input;
    private boolean _output;
    private Integer _sqlType;
    private String _structName;
    private Value _value;

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
     * @return output data decoder (optional).
     *
     * @see Converter
     *
     * @since v1.0
     */
    public Converter getDecoder() {
        return _decoder;
    }

    /**
     * Gets an SQL input data encoder of the parameter.
     *
     * @return input data encoder (optional).
     *
     * @see Converter
     *
     * @since v1.0
     */
    public Converter getEncoder() {
        return _encoder;
    }

    /**
     * Checks if the parameter is input.
     *
     * @return true if the parameter is input.
     *
     * @since v1.0
     */
    public boolean isInput() {
        return _input;
    }

    /**
     * Checks if the parameter is output.
     *
     * @return true if the parameter is output.
     *
     * @since v1.0
     */
    public boolean isOutput() {
        return _output;
    }

    /**
     * Gets an SQL type code of the parameter.
     *
     * @return an SQL type code (optional).
     *
     * @since v1.0
     */
    public Integer getSqlType() {
        return _sqlType;
    }

    /**
     * Gets a name of a structured type of the parameter.
     *
     * @return a structured type name (optional).
     *
     * @since v1.0
     */
    public String getStructName() {
        return _structName;
    }

    /**
     * Gets parameter value container.
     *
     * @return a value container.
     *
     * @see ParameterValue
     *
     * @since v1.0
     */
    public Value getValue() {
        return _value;
    }

    /**
     * Sets a new SQL output data decoder for the parameter.
     *
     * @param decoder output data decoder (optional).
     *
     * @see Converter
     *
     * @since v1.0
     */
    public void setDecoder(Converter decoder) {
        _decoder = decoder;
    }

    /**
     * Sets a new SQL input data encoder for the parameter.
     *
     * @param encoder input data encoder (optional).
     *
     * @see Converter
     *
     * @since v1.0
     */
    public void setEncoder(Converter encoder) {
        _encoder = encoder;
    }

    /**
     * Changes an input status of the parameter.
     *
     * @param input true if the parameter is input.
     *
     * @since v1.0
     */
    public void setInput(boolean input) {
        _input = input;
    }

    /**
     * Changes an output status of the parameter.
     *
     * @param output true if the parameter is output.
     *
     * @since v1.0
     */
    public void setOutput(boolean output) {
        _output = output;
    }

    /**
     * Sets a new SQL type code for the parameter.
     *
     * @param sqlType an SQL type code (optional).
     *
     * @since v1.0
     */
    public void setSqlType(Integer sqlType) {
        _sqlType = sqlType;
    }

    /**
     * Sets a new structured type name for the parameter.
     *
     * @param structName a structured type name (optional).
     *
     * @since v1.0
     */
    public void setStructName(String structName) {
        _structName = structName;
    }

    /**
     * Sets a new parameter value container.
     *
     * @param value a value container.
     *
     * @see ParameterValue
     *
     * @since v1.0
     */
    public void setValue(Value value) {
        _value = value;
    }

    /**
     * Merges the parameter with another one.
     *
     * @param another a parameter to merge with.
     *
     * @since v1.0
     */
    public void merge(Parameter another) {
        // TODO: add merging errors checking
        _decoder = another._decoder;
        _encoder = another._encoder;
        _input = _input || another._input;
        _output = _output || another._output;
        _sqlType = another._sqlType;
        _structName = another._structName;
        if (another._input) {
            _value.setValue(another.getValue().getValue());
        }
    }

    @Override
    public String toString() {
        return String.format("%s (%d/%s)", _value, _sqlType, _structName);
    }
}
