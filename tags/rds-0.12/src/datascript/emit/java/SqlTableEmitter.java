/* BSD License
 *
 * Copyright (c) 2006, Harald Wellmann, Harman/Becker Automotive Systems
 * All rights reserved.
 * 
 * This software is derived from previous work
 * Copyright (c) 2003, Godmar Back.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 * 
 *     * Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials provided
 *       with the distribution.
 * 
 *     * Neither the name of Harman/Becker Automotive Systems or
 *       Godmar Back nor the names of their contributors may be used to
 *       endorse or promote products derived from this software without
 *       specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package datascript.emit.java;


import java.io.PrintStream;

import antlr.collections.AST;
import datascript.antlr.util.TokenAST;
import datascript.ast.CompoundType;
import datascript.ast.Parameter;
import datascript.ast.SqlTableType;
import datascript.jet.java.SqlTable;



/**
 * @author HWellmann
 * 
 */
public class SqlTableEmitter extends CompoundEmitter
{
    private SqlTableType tableType;
    private SqlTable tableTmpl;
    //private ParameterEmitter paramEmitter = new ParameterEmitter(this);


    public SqlTableEmitter(JavaEmitter j, SqlTableType table)
    {
        super(j);
        this.tableType = table;
        tableTmpl = new SqlTable();
    }


    public CompoundType getCompoundType()
    {
        return tableType;
    }


    public String getName()
    {
        return tableType.getName();
    }


    public SqlTableType getSqlTableType()
    {
        return tableType;
    }


    public void setOutputStream(PrintStream out)
    {
        super.setOutputStream(out);
        paramEmitter.setOutputStream(out);
    }


    public void emit(SqlTableType SqlTableType)
    {
        String result = tableTmpl.generate(this);
//        for (Parameter param : tableType.getParameters())
//        {
//            paramEmitter.emit(param);
//        }
        out.print(result);
    }


    public String getSqlConstraint()
    {
        TokenAST constraint = tableType.getSqlConstraint();
        if (constraint == null)
            return null;

        StringBuilder result = new StringBuilder();
        for (AST node = constraint.getFirstChild(); node != null; 
            node = node.getNextSibling())
        {
            String text = node.getText();
            result.append(text.substring(1, text.length() - 1));
        }
        return result.toString();
    }
}
