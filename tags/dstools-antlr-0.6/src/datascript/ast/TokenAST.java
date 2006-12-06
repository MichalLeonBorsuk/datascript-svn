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

import antlr.BaseAST;
import antlr.CommonToken;
import antlr.Token;
import antlr.collections.AST;

/**
 * @author HWellmann
 *
 */
public class TokenAST extends BaseAST
{
    private Token token;

    public TokenAST() 
    {
    }

    public TokenAST(Token tok) 
    {
        token = tok;
    }

    public void initialize(int t, String txt)
    {
        token = new FileNameToken(t, txt);
    }

    public void initialize(AST t)
    {
        token = ((TokenAST)t).token;
    }

    public void initialize(Token t)
    {
        token = (FileNameToken) t;
    }
///////////////////////////////
    
    public int getLine()
    {
        return token.getLine();
    }
    
    public int getColumn()
    {
        return token.getColumn();
    }
    
    public String getFileName()
    {
        return token.getFilename();
    }

    /** Get the token text for this node */
    public String getText() {
        return token.getText();
    }

    /** Get the token type for this node */
    public int getType() {
        return token.getType();
    }

    /** Set the token text for this node */
    public void setText(String text) {
        token.setText(text);
    }

    /** Set the token type for this node */
    public void setType(int ttype) {
        token.setType(ttype);
    }
}

