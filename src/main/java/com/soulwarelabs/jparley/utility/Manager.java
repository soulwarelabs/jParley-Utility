/*
 * Project:  jParley-Utility
 * Outline:  jParley framework utility components
 *
 * File:     Manager.java
 * Folder:   /.../com/soulwarelabs/jparley/utility
 * Revision: 1.12, 15 April 2014
 * Created:  09 February 2014
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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.soulwarelabs.jcommons.Box;
import com.soulwarelabs.jcommons.Optional;
import com.soulwarelabs.jparley.Converter;

/**
 * SQL data manager.
 *
 * @since v1.0
 *
 * @author Ilya Gubarev
 * @version 15 April 2014
 */
public class Manager {

    private Map<Object, Parameter> mappings;

    /**
     * Creates a new instance of SQL data manager.
     *
     * @since v1.0
     */
    public Manager() {
        mappings = new LinkedHashMap<Object, Parameter>();
    }

    /**
     * Gets registered parameter by specified key.
     *
     * @param key parameter key.
     * @return registered parameter.
     *
     * @see Parameter
     *
     * @since v1.0
     */
    public @Optional Parameter getParameter(Object key) {
        return mappings.get(key);
    }

    /**
     * Gets total number of registered parameters.
     *
     * @return number of parameters.
     *
     * @since v1.0
     */
    public int getParametersCount() {
        return mappings.size();
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
        for (Object key : mappings.keySet()) {
            result.append(String.format("%s = %s", key, mappings.get(key)));
            if (index < mappings.size()) {
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
     * @param value subroutine parameter initial value.
     * @param type subroutine parameter SQL type code.
     * @param encoder a converter for SQL data encoding.
     *
     * @see Box
     * @see Converter
     *
     * @since v1.0
     */
    public void in(Object key, @Optional Box<Object> value,
            @Optional Integer type, @Optional Converter encoder) {
        Parameter parameter = new Parameter();
        parameter.setInput(value);
        parameter.setType(type);
        parameter.setEncoder(encoder);
        merge(key, parameter);
    }

    /**
     * Registers an output parameter with specified key.
     *
     * @param key subroutine parameter key.
     * @param type subroutine parameter SQL type code.
     * @param struct a name of parameter structured type.
     * @param decoder a converter for SQL data decoding.
     * @return an output value container.
     *
     * @see Box
     * @see Converter
     *
     * @since v1.0
     */
    public Box<?> out(Object key, int type, @Optional String struct,
            @Optional Converter decoder) {
        Parameter parameter = new Parameter();
        parameter.setOutput(new Box<Object>());
        parameter.setType(type);
        parameter.setStruct(struct);
        parameter.setDecoder(decoder);
        return merge(key, parameter).getOutput();
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
        for (Object key : mappings.keySet()) {
            Parameter parameter = mappings.get(key);
            if (parameter.getOutput() != null) {
                Object output = statement.read(key);
                Converter decoder = parameter.getDecoder();
                if (decoder != null) {
                    output = decoder.process(connection, output);
                }
                parameter.getOutput().setValue(output);
            }
        }
    }

    /**
     * Removes specified registered parameter.
     *
     * @param key parameter key.
     *
     * @since v1.0
     */
    public void remove(Object key) {
        mappings.remove(key);
    }

    /**
     * Removes all registered parameters.
     *
     * @since v1.0
     */
    public void removeAll() {
        mappings.clear();
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
        for (Object key : mappings.keySet()) {
            Parameter parameter = mappings.get(key);
            if (parameter.getInput() != null) {
                Object value = parameter.getInput().getValue();
                Converter encoder = parameter.getEncoder();
                if (encoder != null) {
                    value = encoder.process(connection, value);
                }
                statement.input(key, value, parameter.getType());
            }
            if (parameter.getOutput() != null) {
                Integer sqlType = parameter.getType();
                String structName = parameter.getStruct();
                statement.output(key, sqlType, structName);
            }
        }
    }

    private Parameter merge(Object key, Parameter parameter) {
        Parameter result = mappings.get(key);
        if (result == null) {
            mappings.put(key, parameter);
            result = parameter;
        } else {
            result.setDecoder(parameter.getDecoder());
            result.setEncoder(parameter.getEncoder());
            result.setInput(parameter.getInput());
            result.setOutput(parameter.getOutput());
            result.setStruct(parameter.getStruct());
            result.setType(parameter.getType());
        }
        return result;
    }
}
