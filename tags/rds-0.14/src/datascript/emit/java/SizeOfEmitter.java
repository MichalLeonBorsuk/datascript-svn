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

import antlr.collections.AST;
import datascript.ast.DataScriptException;
import datascript.ast.EnumType;
import freemarker.template.Template;

public class SizeOfEmitter extends JavaDefaultEmitter
{
    private EnumType enumeration;

    public SizeOfEmitter(String outPathName, String defaultPackageName)
    {
        super(outPathName, defaultPackageName);
    }

    public void beginRoot(AST rootNode)
    {
        findAllPackageNames(rootNode, allPackageNames);
        setPackageName(rootNode.getFirstChild());
        openOutputFile(dir, "__SizeOf.java");
        try
        {
            Template tpl = cfg.getTemplate("java/SizeOf.ftl");
            tpl.process(this, writer);
        }
        catch (Exception e)
        {
            throw new DataScriptException(e);
        }
    }

    public void endRoot()
    {
        try
        {
            Template tpl = cfg.getTemplate("java/SequenceEnd.ftl");
            tpl.process(this, writer);
        }
        catch (Exception e)
        {
            throw new DataScriptException(e);
        }
        writer.close();
    }

    public void beginEnumeration(AST e)
    {
        enumeration = (EnumType) e;
        try
        {
            Template tpl = cfg.getTemplate("java/SizeOfEnumeration.ftl");
            tpl.process(this, writer);
        }
        catch (Exception ex)
        {
            throw new DataScriptException(ex);
        }
    }

    public EnumType getEnumerationType()
    {
        return enumeration;
    }

    /**** interface to freemarker FileHeader.inc template ****/

    public String getRootPackageName()
    {
        return datascript.ast.Package.getRoot().getPackageName();
    }

    public String getName()
    {
        return enumeration.getName();
    }
}
