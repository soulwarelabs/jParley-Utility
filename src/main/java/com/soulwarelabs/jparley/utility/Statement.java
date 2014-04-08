/*
 * Project:  jParley-Utility
 * Outline:  jParley framework utility components
 *
 * File:     Statement.java
 * Folder:   /.../com/soulwarelabs/jparley/core
 * Revision: 1.06, 08 April 2014
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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Extended SQL callable statement.
 *
 * @see CallableStatement
 *
 * @since v1.0
 *
 * @author Ilya Gubarev
 * @version 08 April 2014
 */
public final class Statement {

    /**
     * Creates a new callable statement for an SQL function.
     *
     * @param connection an SQL database connection.
     * @param name full qualified name of an SQL subroutine.
     * @param parametersNumber total number of subroutine parameters.
     * @return extended callable statement.
     * @throws SQLException if error occurs while creating statement.
     *
     * @see Connection
     *
     * @since v1.0
     */
    public static Statement createFunction(Connection connection, String name,
            int parametersNumber) throws SQLException {
        StringBuilder parameters = createParameterString(parametersNumber - 1);
        String sql = String.format("{? = call %s(%s)}", name, parameters);
        return new Statement(connection, sql);
    }

    /**
     * Creates a new callable statement for an SQL procedure.
     *
     * @param connection an SQL database connection.
     * @param name full qualified name of an SQL subroutine.
     * @param parametersNumber total number of subroutine parameters.
     * @return extended callable statement.
     * @throws SQLException if error occurs while creating statement.
     *
     * @see Connection
     *
     * @since v1.0
     */
    public static Statement createProcedure(Connection connection, String name,
            int parametersNumber) throws SQLException {
        StringBuilder parameters = createParameterString(parametersNumber);
        String sql = String.format("{call %s(%s)}", name, parameters);
        return new Statement(connection, sql);
    }

    private static StringBuilder createParameterString(int number) {
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= number; i++) {
            result.append(i < number ? "?," : "?");
        }
        return result;
    }

    private final CallableStatement _base;
    private final String _sql;

    private Statement(Connection connection, String sql) throws SQLException {
        _base = connection.prepareCall(sql);
        _sql = sql;
    }

    /**
     * Gets an underlying SQL callable statement.
     *
     * @return the underlying callable statement.
     *
     * @see CallableStatement
     *
     * @since v1.0
     */
    public CallableStatement getBaseStatement() {
        return _base;
    }

    /**
     * Gets an sql string of the statement.
     *
     * @return sql string.
     *
     * @since v1.0
     */
    public String getSql() {
        return _sql;
    }

    /**
     * Executes the statement.
     *
     * @throws SQLException if error occurs while executing the statement.
     *
     * @since v1.0
     */
    public void execute() throws SQLException {
        _base.execute();
    }

    /**
     * Reads an output parameter value from the statement.
     *
     * @param key parameter key.
     * @return parameter value (optional).
     * @throws SQLException if error occurs while reading parameter value.
     *
     * @see ParameterKey
     *
     * @since v1.0
     */
    public Object readOutput(ParameterKey key) throws SQLException {
        if (key.isIndexBased()) {
            return _base.getObject(key.getIndex());
        } else {
            return _base.getObject(key.getName());
        }
    }

    /**
     * Registers an input parameter into the statement.
     *
     * @param key parameter key.
     * @param input parameter initial value (optional).
     * @param sqlType parameter SQL type code (optional).
     * @throws SQLException if error occurs while registering parameter.
     *
     * @see ParameterKey
     *
     * @since v1.0
     */
    public void setInput(ParameterKey key, Object input, Integer sqlType)
            throws SQLException {
        if (key.isIndexBased()) {
            if (sqlType == null) {
                _base.setObject(key.getIndex(), input);
            } else {
                _base.setObject(key.getIndex(), input, sqlType);
            }
        } else {
            if (sqlType == null) {
                _base.setObject(key.getName(), input);
            } else {
                _base.setObject(key.getName(), input, sqlType);
            }
        }
    }

    /**
     * Registers an output parameter into the statement.
     *
     * @param key parameter key.
     * @param sqlType parameter SQL type code (optional).
     * @param structName parameter structured type name (optional).
     * @throws SQLException if error occurs while registering parameter.
     *
     * @see ParameterKey
     *
     * @since v1.0
     */
    public void setOutput(ParameterKey key, int sqlType, String structName)
            throws SQLException {
        if (key.isIndexBased()) {
            if (structName == null) {
                _base.registerOutParameter(key.getIndex(), sqlType);
            } else {
                _base.registerOutParameter(key.getIndex(), sqlType, structName);
            }
        } else {
            if (structName == null) {
                _base.registerOutParameter(key.getName(), sqlType);
            } else {
                _base.registerOutParameter(key.getName(), sqlType, structName);
            }
        }
    }

    @Override
    public String toString() {
        return _sql;
    }
}
