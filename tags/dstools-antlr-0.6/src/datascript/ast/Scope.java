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

import java.util.HashMap;
import java.util.Vector;

import antlr.collections.AST;
import datascript.tools.ToolContext;

public class Scope implements Context, LinkAction
{
    private HashMap symbolTable = new HashMap();

    private Context parentScope;
    private CompoundType owner;
    private Field currentField;

    private Vector<LinkAction> linkActions = new Vector();

    public Scope()
    {
        this(null);
    }

    public Scope(Context parentScope)
    {
        this.parentScope = parentScope;
        if (parentScope != null)
        {
            parentScope.postLinkAction(this);
        }
    }

    public CompoundType getOwner()
    {
        return owner;
    }
    
    public void setOwner(CompoundType owner)
    {
        this.owner = owner;
    }
    
    public Field getCurrentField()
    {
        return currentField;
    }
    
    public void setCurrentField(Field f)
    {
        currentField = f;
    }
    
    /**
     * get this symbol from this or a parent scope
     */
    public Object getSymbol(String name)
    {
        Object obj = symbolTable.get(name);
        if (obj == null && parentScope != null)
            return parentScope.getSymbol(name);
        return obj;
    }

    /**
     * Get the owner is whose scope this symbol is defined.
     */
    public CompoundType getDefiningType(String name)
    {
        Object obj = symbolTable.get(name);
        if (obj == null)
        {
            if (parentScope == null)
            {
                return null;
            }
            else
            {
                return ((Scope)parentScope).getDefiningType(name);
            }
        }
        else
        {
            return owner;
        }
    }

    /**
     * get this symbol from this scope
     */
    public Object getSymbolFromThis(String name)
    {
        Object obj = symbolTable.get(name);
        return obj;
    }

    /**
     * set this symbol, but throw SemanticException is symbol was already set in
     * this scope
     */
    public void setSymbol(AST node, Object symbol)
    {
        // Main.debug(this + ": defining " + name + " to be " +
        // symbol.getClass() + ":" + symbol);
        Object o = symbolTable.put(node.getText(), symbol);
        if (o != null)
            ToolContext.logError((TokenAST)node, "'" + node.getText()
                    + "' is already defined in this scope");
    }

    /**
     * set this symbol, discarding any previous setting (if any)
     */
    public void replaceSymbol(String name, Object symbol)
    {
        // Main.debug(this + ": redefining " + name + " to be " +
        // symbol.getClass() + ":" + symbol);
        symbolTable.put(name, symbol);
    }

    public Context getParentScope()
    {
        return parentScope;
    }

    public void postLinkAction(LinkAction act)
    {
        linkActions.addElement(act);
    }

    public void link(Context ctxt)
    {
        for (LinkAction l : linkActions)
        {
            l.link(this);
        }
    }
}
