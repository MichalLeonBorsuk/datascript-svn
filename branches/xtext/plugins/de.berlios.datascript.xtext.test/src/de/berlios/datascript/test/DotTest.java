package de.berlios.datascript.test;

import java.io.IOException;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Injector;

import de.berlios.datascript.DataScriptStandaloneSetup;
import de.berlios.datascript.dataScript.Expression;
import de.berlios.datascript.dataScript.Field;
import de.berlios.datascript.dataScript.Model;
import de.berlios.datascript.dataScript.SequenceType;
import de.berlios.datascript.dataScript.Type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DotTest
{
    private Resource resource;
    private Model model;
    private org.eclipse.emf.common.util.Diagnostic diagnostic;
    
    @Before
    public void buildModel() throws IOException
    {
        Injector injector = new DataScriptStandaloneSetup().createInjectorAndDoEMFRegistration();
        XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
        resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
        resource = resourceSet.getResource(URI.createURI("classpath:/model/dot.ds"), true);
        model = (Model) resource.getContents().get(0);
        
        diagnostic = Diagnostician.INSTANCE.validate(model);
        System.out.println(diagnostic);
        for (Resource.Diagnostic error: resource.getErrors())
        {
            System.out.println(error);
        }
        assertEquals(1, resource.getErrors().size());
        String msg = resource.getErrors().get(0).getMessage();
        assertEquals("Couldn't resolve reference to Value c", msg);
        assertEquals(Diagnostic.OK, diagnostic.getSeverity());
    }
    
    @Test
    public void dot() throws IOException
    {
    }

}
