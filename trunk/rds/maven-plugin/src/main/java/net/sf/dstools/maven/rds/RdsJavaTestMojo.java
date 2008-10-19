package net.sf.dstools.maven.rds;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;

/**
 * Goal for generating Java sources from a DataScript model
 * 
 * @goal java-for-test
 * 
 * @phase generate-test-sources
 */
public class RdsJavaTestMojo extends AbstractRdsMojo 
{
    /**
	 * Specifies the root source directory for DataScript files.
	 * 
	 * @parameter expression="${basedir}/src/test/ds"
	 * @required
	 */
	private File sourceDirectory;

	/**
	 * Specifies the destination directory where Antlr should generate files. <br/>
	 * See <a
	 * href="http://www.antlr2.org/doc/options.html#Command%20Line%20Options"
	 * >Command Line Options</a>
	 * 
	 * @parameter expression="${project.build.directory}/generated-test-sources/rds"
	 * @required
	 */
	private File outputDirectory;

	protected File getSourceDirectory()
	{
		return sourceDirectory;
	}
	
	protected File getOutputDirectory()
	{
		return outputDirectory;
	}
	
	
	
	protected void registerSourceRoot()
	{
		project.addTestCompileSourceRoot(outputDirectory.toString());
	}
	
}
