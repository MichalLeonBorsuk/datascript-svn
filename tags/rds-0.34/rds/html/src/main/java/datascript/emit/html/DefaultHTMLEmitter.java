/* BSD License
 *
 * Copyright (c) 2007, Harald Wellmann, Harman/Becker Automotive Systems
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

package datascript.emit.html;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import antlr.collections.AST;
import datascript.ast.DataScriptException;
import datascript.ast.Package;
import datascript.ast.TypeInterface;
import datascript.emit.DefaultEmitter;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

abstract public class DefaultHTMLEmitter extends DefaultEmitter
{
    protected static final String CONTENT_FOLDER = "content";

    protected static final String HTML_EXT = ".html";

    protected static Configuration cfg;

    protected List<CompoundEmitter> containers = new ArrayList<CompoundEmitter>();

    protected File directory;

    protected TypeInterface currentType;

    protected Package currentPackage;

    private String currentFolder = "/";

    public DefaultHTMLEmitter()
    {
        initConfig();
    }

    public DefaultHTMLEmitter(String outputPath)
    {
        if (outputPath != null && outputPath.length() > 0)
        {
            directory = new File(outputPath);
        }
        else
        {
            directory = new File("html");
        }

        initConfig();
    }

    private void initConfig()
    {
        if (cfg != null)
            return;

        cfg = new Configuration();
        cfg
                .setClassForTemplateLoading(DefaultHTMLEmitter.class,
                        "/freemarker/");
        cfg.setObjectWrapper(new DefaultObjectWrapper());
    }

    public void setCurrentFolder(String currentFolder)
    {
        this.currentFolder = currentFolder;
    }

    public String getCurrentFolder()
    {
        return currentFolder;
    }

    public String getPackageName()
    {
        return currentPackage.getPackageName();
    }

    public String getRootPackageName()
    {
        return Package.getRoot().getPackageName();
    }

    public List<CompoundEmitter> getContainers()
    {
        return containers;
    }

    @Override
    public void beginPackage(AST p)
    {
        currentPackage = Package.lookup(p);
    }

    public void emitStylesheet()
    {
        emit("html/webStyles.css.ftl", "webStyles.css");
    }

    public void emitFrameset()
    {
        emit("html/index.html.ftl", "index.html");
    }

    private void emit(String template, String outputName)
    {
        try
        {
            Template tpl = cfg.getTemplate(template);
            openOutputFile(directory, outputName);

            tpl.process(this, writer);
            writer.close();
        }
        catch (IOException exc)
        {
            throw new DataScriptException(exc);
        }
        catch (TemplateException exc)
        {
            throw new DataScriptException(exc);
        }
    }
}
