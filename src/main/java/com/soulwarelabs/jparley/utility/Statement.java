/*
 * Project:  jParley-Utility
 * Outline:  jParley framework utility components
 *
 * File:     Statement.java
 * Folder:   /.../com/soulwarelabs/jparley/utility
 * Revision: 1.08, 16 April 2014
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
 * @version 16 April 2014
 */
public class Statement {

    /**
     * Creates a new callable statement for an SQL function.
     *
     * @param connection SQL database connection.
     * @param name full-qualified name of an SQL subroutine.
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
        return new Statement(connection.prepareCall(sql), sql);
    }

    /**
     * Creates a new callable statement for an SQL procedure.
     *
     * @param connection SQL database connection.
     * @param name full-qualified name of an SQL subroutine.
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
        return new Statement(connection.prepareCall(sql), sql);
    }

    private static StringBuilder createParameterString(int number) {
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= number; i++) {
            result.append(i < number ? "?," : "?");
        }
        return result;
    }

    private CallableStatement base;
    private String sql;

    private Statement(CallableStatement base, String sql) throws SQLException {
        this.base = base;
        this.sql = sql;
    }

    /**
     * Gets an underlying SQL callable statement.
     *
     * @return underlying callable statement.
     *
     * @see CallableStatement
     *
     * @since v1.0
     */
    public CallableStatement getBaseStatement() {
        return base;
    }

    /**
     * Gets an sql string of the statement.
     *
     * @return sql string.
     *
     * @since v1.0
     */
    public String getSql() {
        return sql;
    }

    /**
     * Executes the statement.
     *
     * @throws SQLException if error occurs while executing the statement.
     *
     * @since v1.0
     */
    public void execute() throws SQLException {
        base.execute();
    }

    /**
     * Registers an input parameter into the statement.
     *
     * @param key parameter key.
     * @param value parameter initial value (optional).
     * @param type parameter SQL type code (optional).
     * @throws SQLException if error occurs while registering parameter.
     *
     * @since v1.0
     */
    public void input(Object key, Object value, Integer type)
            throws SQLException {
        if (key instanceof Integer) {
            if (type == null) {
                base.setObject((Integer) key, value);
            } else {
                base.setObject((Integer) key, value, type);
            }
        } else {
            if (type == null) {
                base.setObject((String) key, value);
            } else {
                base.setObject((String) key, value, type);
            }
        }
    }

    /**
     * Registers an output parameter into the statement.
     *
     * @param key parameter key.
     * @param type parameter SQL type code.
     * @param struct parameter structured type name (optional).
     * @throws SQLException if error occurs while registering parameter.
     *
     * @since v1.0
     */
    public void output(Object key, int type, String struct)
            throws SQLException {
        if (key instanceof Integer) {
            if (struct == null) {
                base.registerOutParameter((Integer) key, type);
            } else {
                base.registerOutParameter((Integer) key, type, struct);
            }
        } else {
            if (struct == null) {
                base.registerOutParameter((String) key, type);
            } else {
                base.registerOutParameter((String) key, type, struct);
            }
        }
    }

    /**
     * Reads an output parameter value from the statement.
     *
     * @param key parameter key.
     * @return parameter value (optional).
     * @throws SQLException if error occurs while reading parameter value.
     *
     * @since v1.0
     */
    public Object read(Object key) throws SQLException {
        if (key instanceof Integer) {
            return base.getObject((Integer) key);
        } else {
            return base.getObject((String) key);
        }
    }
}
