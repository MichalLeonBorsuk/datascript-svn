/* BSD License
 *
 * Copyright (c) 2006, Henrik Wedekind, Harman/Becker Automotive Systems
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


package datascript.tools;


import datascript.antlr.DataScriptEmitter;
import datascript.antlr.util.TokenAST;



/**
 * Each extension must be named as rds*Extension.jar, i.e.
 * "rds_javaExtension.jar"
 * 
 * @author HWedekind
 * 
 */
public interface Extension
{
    /**
     * if the extension requires parameters they should be set via setParameter
     * 
     * @param params
     *            the parametervalues to set
     */
    public abstract void setParameter(Parameters params);


    /**
     * Enables implementions of Extension to generate their ouptut
     * 
     * @param emitter
     *            an already initialized datascript emitter
     * @param rootNode
     *            the already parsed input file, should be given to
     *            translateUnit
     */
    public abstract void generate(DataScriptEmitter emitter, TokenAST rootNode)
            throws Exception;


    /**
     * Parse all arguments from the commandline parameterlist that are necessary
     * for this extension.
     * 
     * @param rdsOptions
     */
    public void getOptions(org.apache.commons.cli.Options rdsOptions);
}
