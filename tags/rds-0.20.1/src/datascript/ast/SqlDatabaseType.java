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


package datascript.ast;


import antlr.Token;
import antlr.collections.AST;
import datascript.antlr.DataScriptParserTokenTypes;
import datascript.antlr.util.TokenAST;



/**
 * @author HWellmann
 * 
 */
@SuppressWarnings("serial")
public class SqlDatabaseType extends CompoundType
{
    @SuppressWarnings("unused")
    private TokenAST sqlConstraint;


    public void setSqlConstraint(TokenAST s)
    {
        sqlConstraint = s;
    }


    @Override
    public IntegerValue sizeof(Context ctxt)
    {
        throw new UnsupportedOperationException("sizeof not implemented");
    }


    public IntegerValue bitsizeof(Context ctxt)
    {
        throw new UnsupportedOperationException("sizeof not implemented");
    }


    @Override
    public boolean isMember(Context ctxt, Value val)
    {
        throw new UnsupportedOperationException("isMember not implemented");
    }


    public SqlPragmaType getPragma()
    {
        AST node = findFirstChildOfType(DataScriptParserTokenTypes.SQL_PRAGMA);
        return (SqlPragmaType) node;
    }


    public SqlMetadataType getMetadata()
    {
        AST node = findFirstChildOfType(DataScriptParserTokenTypes.SQL_METADATA);
        return (SqlMetadataType) node;
    }


    @Override
    public String getDocumentation()
    {
        String result = "";
        Token t = getHiddenBefore();
        if (t != null)
        {
            result = t.getText();
        }
        return result;
    }


    @Override
    public String toString()
    {
        return "SQL_DATABASE";
    }
}
