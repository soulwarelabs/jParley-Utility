/*
 * Project:  jParley-Utility
 * Outline:  jParley framework utility components
 *
 * File:     Manager.java
 * Folder:   /.../com/soulwarelabs/jparley/utility
 * Revision: 1.16, 16 June 2014
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

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.soulwarelabs.jcommons.Box;
import com.soulwarelabs.jparley.Converter;

/**
 * SQL data manager.
 *
 * @since v1.0.0
 *
 * @author Ilya Gubarev
 * @version 16 June 2014
 */
public class Manager implements Serializable {

    private Map<Object, Parameter> indexed;
    private Map<Object, Parameter> named;

    /**
     * Creates a new instance of manager.
     *
     * @since v1.0.0
     */
    public Manager() {
        indexed = new TreeMap<Object, Parameter>();
        named = new LinkedHashMap<Object, Parameter>();
    }

    /**
     * Gets all registered parameter keys.
     *
     * @return registered keys.
     *
     * @since v1.0.0
     */
    public Collection<Object> getKeys() {
        Set<Object> result = new LinkedHashSet<Object>();
        result.addAll(indexed.keySet());
        result.addAll(named.keySet());
        return result;
    }

    /**
     * Gets registered parameter by specified key.
     *
     * @param key parameter key.
     * @return registered parameter (optional).
     *
     * @see Parameter
     *
     * @since v1.0.0
     */
    public Parameter getParameter(Object key) {
        Parameter result = indexed.get(key);
        return result != null ? result : named.get(key);
    }

    /**
     * Gets total number of registered parameters.
     *
     * @return number of parameters.
     *
     * @since v1.0.0
     */
    public int getTotal() {
        return indexed.size() + named.size();
    }

    /**
     * Registers an input parameter with specified key.
     *
     * @param key subroutine parameter key.
     * @param value subroutine parameter initial value.
     * @param type subroutine parameter SQL type code (optional).
     * @param encoder a converter for SQL data encoding (optional).
     *
     * @see Box
     * @see Converter
     *
     * @since v1.0.0
     */
    public void in(Object key, Box<?> value, Integer type, Converter encoder) {
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
     * @param struct a name of parameter structured type (optional).
     * @param decoder a converter for SQL data decoding (optional).
     * @return an output value container.
     *
     * @see Box
     * @see Converter
     *
     * @since v1.0.0
     */
    public Box<Object> out(Object key, int type, String struct,
            Converter decoder) {
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
     * @since v1.0.0
     */
    public void parseAll(Connection connection, Statement statement)
            throws SQLException {
        Map<Object, Parameter> params = new LinkedHashMap<Object, Parameter>();
        params.putAll(indexed);
        params.putAll(named);
        for (Object key : params.keySet()) {
            Parameter parameter = params.get(key);
            if (parameter.getOutput() != null) {
                Object output = statement.read(key);
                Converter decoder = parameter.getDecoder();
                if (decoder != null) {
                    output = decoder.perform(connection, output);
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
     * @since v1.0.0
     */
    public void remove(Object key) {
        indexed.remove(key);
        named.remove(key);
    }

    /**
     * Removes all registered parameters.
     *
     * @since v1.0.0
     */
    public void removeAll() {
        indexed.clear();
        named.clear();
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
     * @since v1.0.0
     */
    public void setupAll(Connection connection, Statement statement)
            throws SQLException {
        Map<Object, Parameter> params = new LinkedHashMap<Object, Parameter>();
        params.putAll(indexed);
        params.putAll(named);
        for (Object key : params.keySet()) {
            Parameter parameter = params.get(key);
            if (parameter.getInput() != null) {
                Object value = parameter.getInput().getValue();
                Converter encoder = parameter.getEncoder();
                if (encoder != null) {
                    value = encoder.perform(connection, value);
                }
                statement.in(key, value, parameter.getType());
            }
            if (parameter.getOutput() != null) {
                Integer sqlType = parameter.getType();
                String structName = parameter.getStruct();
                statement.out(key, sqlType, structName);
            }
        }
    }

    private Parameter merge(Object key, Parameter parameter) {
        Parameter result = getParameter(key);
        if (result == null) {
            result = parameter;
            if (key instanceof Integer) {
                indexed.put(key, result);
            } else {
                named.put(key, result);
            }
        } else {
            result.setDecoder(parameter.getDecoder());
            result.setEncoder(parameter.getEncoder());
            result.setStruct(parameter.getStruct());
            result.setType(parameter.getType());
            if (parameter.getInput() != null) {
                result.setInput(parameter.getInput());
            }
            if (parameter.getOutput() != null) {
                result.setOutput(parameter.getOutput());
            }
        }
        return result;
    }
}
