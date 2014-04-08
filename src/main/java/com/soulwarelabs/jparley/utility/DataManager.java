/*
 * Project:  jParley-Utility
 * Outline:  jParley framework utility components
 *
 * File:     DataManager.java
 * Folder:   /.../com/soulwarelabs/jparley/core
 * Revision: 1.11, 08 April 2014
 * Created:  08 February 2014
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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.soulwarelabs.jparley.Converter;

/**
 * SQL data manager.
 *
 * @since v1.0
 *
 * @author Ilya Gubarev
 * @version 13 February 2014
 */
public class DataManager {

    private final Map<ParameterKey, Parameter> _mappings;

    /**
     * Creates a new instance of data manager.
     *
     * @since v1.0
     */
    public DataManager() {
        _mappings = new LinkedHashMap<ParameterKey, Parameter>();
    }

    /**
     * Gets a registered parameter by its key.
     *
     * @param key parameter key.
     * @return registered parameter (optional).
     *
     * @see Parameter
     * @see ParameterKey
     *
     * @since v1.0
     */
    public Parameter getParameter(ParameterKey key) {
        return _mappings.get(key);
    }

    /**
     * 
     *
     * @return 
     *
     * @since v1.0
     */
    public int getParametersCount() {
        return _mappings.size();
    }

    /**
     * Gets a text view of the inner state.
     *
     * @return printed state.
     *
     * @since v1.0
     */
    public StringBuilder getPrintedState() {
        int index = 1;
        StringBuilder result = new StringBuilder();
        for (ParameterKey key : _mappings.keySet()) {
            result.append(String.format("%s = %s", key, _mappings.get(key)));
            if (index < _mappings.size()) {
                result.append(", ");
            }
            index++;
        }
        return result;
    }

    /**
     * Registers an input parameter with specified key.
     *
     * @param key subroutine parameter key.
     * @param value subroutine parameter initial value (optional).
     * @param sqlType subroutine parameter SQL type code (optional).
     * @param encoder a converter for SQL data encoding (optional).
     *
     * @see Converter
     * @see ParameterKey
     *
     * @since v1.0
     */
    public void setInput(ParameterKey key, Object value, Integer sqlType, Converter encoder) {
        Parameter parameter = new Parameter();
        parameter.setInput(true);
        parameter.setValue(new Value(value));
        parameter.setSqlType(sqlType);
        parameter.setEncoder(encoder);
        merge(key, parameter);
    }

    /**
     * Registers an output parameter with specified key.
     *
     * @param key subroutine parameter key.
     * @param sqlType subroutine parameter SQL type code (optional).
     * @param structName a name of parameter structured type (optional).
     * @param decoder a converter for SQL data decoding (optional).
     * @return an output value container.
     *
     * @see Converter
     * @see ParameterKey
     * @see ParameterValue
     *
     * @since v1.0
     */
    public Value setOutput(ParameterKey key, Integer sqlType, String structName, Converter decoder) {
        Parameter parameter = new Parameter();
        parameter.setOutput(true);
        parameter.setValue(new Value());
        parameter.setSqlType(sqlType);
        parameter.setStructName(structName);
        parameter.setDecoder(decoder);
        return merge(key, parameter).getValue();
    }

    /**
     * Removes specified registered parameter.
     *
     * @param key parameter key.
     *
     * @see ParameterKey
     *
     * @since v1.0
     */
    public void remove(ParameterKey key) {
        _mappings.remove(key);
    }

    /**
     * Removes all registered parameters.
     *
     * @since v1.0
     */
    public void removeAll() {
        _mappings.clear();
    }

    /**
     * Set up all registered parameters to specified statement.
     *
     * @param connection an SQL database connection.
     * @param statement an SQL callable statement.
     * @throws SQLException if error occurs while setting up parameters.
     *
     * @see Connection
     * @see Statement
     *
     * @since v1.0
     */
    public void setupAll(Connection connection, Statement statement)
            throws SQLException {
        for (ParameterKey key : _mappings.keySet()) {
            Parameter parameter = _mappings.get(key);
            if (parameter.isInput()) {
                Object value = parameter.getValue().getValue();
                Converter encoder = parameter.getEncoder();
                if (encoder != null) {
                    value = encoder.process(connection, value);
                }
                statement.setInput(key, value, parameter.getSqlType());
            }
            if (parameter.isOutput()) {
                Integer sqlType = parameter.getSqlType();
                String structName = parameter.getStructName();
                statement.setOutput(key, sqlType, structName);
            }
        }
    }

    /**
     * Reads all registered output parameters from specified statement.
     *
     * @param connection an SQL database connection.
     * @param statement an SQL callable statement.
     * @throws SQLException if error occurs while setting up parameters.
     *
     * @see Connection
     * @see Statement
     *
     * @since v1.0
     */
    public void parseAll(Connection connection, Statement statement)
            throws SQLException {
        for (ParameterKey key : _mappings.keySet()) {
            Parameter parameter = _mappings.get(key);
            if (parameter.isOutput()) {
                Object output = statement.readOutput(key);
                Converter decoder = parameter.getDecoder();
                if (decoder != null) {
                    output = decoder.process(connection, output);
                }
                parameter.getValue().setValue(output);
            }
        }
    }

    @Override
    public String toString() {
        return "data manager";
    }

    private Parameter merge(ParameterKey key, Parameter parameter) {
        Parameter result = _mappings.get(key);
        if (result == null) {
            _mappings.put(key, parameter);
            result = parameter;
        } else {
            result.merge(parameter);
        }
        return result;
    }
}
