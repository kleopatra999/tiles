/**
 *
 */
package org.apache.tiles.impl;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.BasicAttributeContext;
import org.apache.tiles.Definition;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.definition.NoSuchDefinitionException;
import org.apache.tiles.evaluator.AttributeEvaluator;
import org.apache.tiles.evaluator.AttributeEvaluatorFactory;
import org.apache.tiles.preparer.NoSuchPreparerException;
import org.apache.tiles.preparer.PreparerFactory;
import org.apache.tiles.preparer.ViewPreparer;
import org.apache.tiles.renderer.AttributeRenderer;
import org.apache.tiles.renderer.RendererFactory;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link BasicTilesContainer}.
 *
 * @version $Rev$ $Date$
 */
public class BasicTilesContainerUnitTest {

    /**
     * Name used to store attribute context stack.
     */
    private static final String ATTRIBUTE_CONTEXT_STACK =
        "org.apache.tiles.AttributeContext.STACK";

    private ApplicationContext applicationContext;

    private DefinitionsFactory definitionsFactory;

    private PreparerFactory preparerFactory;

    private RendererFactory rendererFactory;

    private AttributeEvaluatorFactory attributeEvaluatorFactory;

    private BasicTilesContainer container;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        applicationContext = createMock(ApplicationContext.class);
        definitionsFactory = createMock(DefinitionsFactory.class);
        preparerFactory = createMock(PreparerFactory.class);
        rendererFactory = createMock(RendererFactory.class);
        attributeEvaluatorFactory = createMock(AttributeEvaluatorFactory.class);
        container = new BasicTilesContainer();
        container.setApplicationContext(applicationContext);
        container.setAttributeEvaluatorFactory(attributeEvaluatorFactory);
        container.setDefinitionsFactory(definitionsFactory);
        container.setPreparerFactory(preparerFactory);
        container.setRendererFactory(rendererFactory);
    }

    /**
     * Test method for {@link org.apache.tiles.impl.BasicTilesContainer#startContext(org.apache.tiles.request.Request)}.
     */
    @Test
    public void testStartContext() {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Deque<AttributeContext> deque = createMock(Deque.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);

        expect(request.getContext("request")).andReturn(requestScope);
        expect(requestScope.get(ATTRIBUTE_CONTEXT_STACK)).andReturn(deque);
        expect(deque.isEmpty()).andReturn(false);
        expect(deque.peek()).andReturn(attributeContext);
        expect(attributeContext.getCascadedAttributeNames()).andReturn(null);
        deque.push(isA(BasicAttributeContext.class));

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext);
        assertTrue(container.startContext(request) instanceof BasicAttributeContext);
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext);
    }

    /**
     * Test method for {@link org.apache.tiles.impl.BasicTilesContainer#endContext(org.apache.tiles.request.Request)}.
     */
    @Test
    public void testEndContext() {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Deque<AttributeContext> deque = createMock(Deque.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);

        expect(request.getContext("request")).andReturn(requestScope);
        expect(requestScope.get(ATTRIBUTE_CONTEXT_STACK)).andReturn(deque);
        expect(deque.pop()).andReturn(attributeContext);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext);
        container.endContext(request);
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext);
    }

    /**
     * Test method for {@link org.apache.tiles.impl.BasicTilesContainer#renderContext(org.apache.tiles.request.Request)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testRenderContext() throws IOException {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Deque<AttributeContext> deque = createMock(Deque.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        ViewPreparer preparer = createMock(ViewPreparer.class);
        Attribute templateAttribute = createMock(Attribute.class);
        AttributeRenderer renderer = createMock(AttributeRenderer.class);

        expect(request.getContext("request")).andReturn(requestScope);
        expect(requestScope.get(ATTRIBUTE_CONTEXT_STACK)).andReturn(deque);
        expect(deque.isEmpty()).andReturn(false);
        expect(deque.peek()).andReturn(attributeContext);
        expect(attributeContext.getPreparer()).andReturn(null);
        expect(attributeContext.getTemplateAttribute()).andReturn(templateAttribute);
        expect(templateAttribute.getRenderer()).andReturn("renderer");
        expect(rendererFactory.getRenderer("renderer")).andReturn(renderer);
        renderer.render(templateAttribute, request);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext, preparer, templateAttribute, renderer);
        container.renderContext(request);
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext, preparer, templateAttribute, renderer);
    }

    /**
     * Test method for {@link org.apache.tiles.impl.BasicTilesContainer#getApplicationContext()}.
     */
    @Test
    public void testGetApplicationContext() {
        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory);
        assertEquals(applicationContext, container.getApplicationContext());
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory);
    }

    /**
     * Test method for {@link org.apache.tiles.impl.BasicTilesContainer#getAttributeContext(org.apache.tiles.request.Request)}.
     */
    @Test
    public void testGetAttributeContext() {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Deque<AttributeContext> deque = createMock(Deque.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);

        expect(request.getContext("request")).andReturn(requestScope);
        expect(requestScope.get(ATTRIBUTE_CONTEXT_STACK)).andReturn(deque);
        expect(deque.isEmpty()).andReturn(false);
        expect(deque.peek()).andReturn(attributeContext);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext);
        assertEquals(attributeContext, container.getAttributeContext(request));
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext);
    }

    /**
     * Test method for {@link org.apache.tiles.impl.BasicTilesContainer#getAttributeContext(org.apache.tiles.request.Request)}.
     */
    @Test
    public void testGetAttributeContextNew() {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Deque<AttributeContext> deque = createMock(Deque.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);

        expect(request.getContext("request")).andReturn(requestScope).times(2);
        expect(requestScope.get(ATTRIBUTE_CONTEXT_STACK)).andReturn(deque).times(2);
        expect(deque.isEmpty()).andReturn(true);
        deque.push(isA(BasicAttributeContext.class));

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext);
        assertTrue(container.getAttributeContext(request) instanceof BasicAttributeContext);
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext);
    }

    /**
     * Test method for {@link org.apache.tiles.impl.BasicTilesContainer#getDefinitionsFactory()}.
     */
    @Test
    public void testGetDefinitionsFactory() {
        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory);
        assertEquals(definitionsFactory, container.getDefinitionsFactory());
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory);
    }

    /**
     * Test method for {@link org.apache.tiles.impl.BasicTilesContainer#getPreparerFactory()}.
     */
    @Test
    public void testGetPreparerFactory() {
        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory);
        assertEquals(preparerFactory, container.getPreparerFactory());
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory);
    }

    /**
     * Test method for {@link org.apache.tiles.impl.BasicTilesContainer#prepare(java.lang.String, org.apache.tiles.request.Request)}.
     */
    @Test
    public void testPrepare() {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Deque<AttributeContext> deque = createMock(Deque.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        ViewPreparer preparer = createMock(ViewPreparer.class);

        expect(preparerFactory.getPreparer("preparer", request)).andReturn(preparer);
        expect(request.getContext("request")).andReturn(requestScope);
        expect(requestScope.get(ATTRIBUTE_CONTEXT_STACK)).andReturn(deque);
        expect(deque.isEmpty()).andReturn(false);
        expect(deque.peek()).andReturn(attributeContext);
        preparer.execute(request, attributeContext);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext, preparer);
        container.prepare("preparer", request);
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext, preparer);
    }

    /**
     * Test method for {@link org.apache.tiles.impl.BasicTilesContainer#prepare(java.lang.String, org.apache.tiles.request.Request)}.
     */
    @Test(expected=NoSuchPreparerException.class)
    public void testPrepareException() {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Deque<AttributeContext> deque = createMock(Deque.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);

        expect(preparerFactory.getPreparer("preparer", request)).andReturn(null);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext);
        try {
            container.prepare("preparer", request);
        } finally {
            verify(applicationContext, attributeEvaluatorFactory,
                    definitionsFactory, preparerFactory, rendererFactory,
                    request, requestScope, deque, attributeContext);
        }
    }

    /**
     * Test method for {@link org.apache.tiles.impl.BasicTilesContainer#render(java.lang.String, org.apache.tiles.request.Request)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testRenderStringRequest() throws IOException {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Deque<AttributeContext> deque = createMock(Deque.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        ViewPreparer preparer = createMock(ViewPreparer.class);
        AttributeRenderer renderer = createMock(AttributeRenderer.class);
        Definition definition = createMock(Definition.class);

        Attribute templateAttribute = Attribute.createTemplateAttribute("/my/template.jsp");

        expect(definitionsFactory.getDefinition("definition", request)).andReturn(definition);
        expect(request.getContext("request")).andReturn(requestScope).times(3);
        expect(requestScope.get(ATTRIBUTE_CONTEXT_STACK)).andReturn(deque).times(3);
        expect(deque.isEmpty()).andReturn(false);
        expect(deque.peek()).andReturn(attributeContext);
        expect(attributeContext.getPreparer()).andReturn(null);
        expect(attributeContext.getTemplateAttribute()).andReturn(templateAttribute);
        expect(attributeContext.getLocalAttributeNames()).andReturn(null);
        expect(attributeContext.getCascadedAttributeNames()).andReturn(null);
        expect(definition.getTemplateAttribute()).andReturn(templateAttribute);
        expect(rendererFactory.getRenderer("template")).andReturn(renderer);
        deque.push(isA(BasicAttributeContext.class));
        renderer.render(templateAttribute, request);
        expect(deque.pop()).andReturn(null);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext, preparer, renderer, definition);
        container.render("definition", request);
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext, preparer, renderer, definition);
    }

    /**
     * Test method for {@link org.apache.tiles.impl.BasicTilesContainer#render(java.lang.String, org.apache.tiles.request.Request)}.
     */
    @Test(expected=NoSuchDefinitionException.class)
    public void testRenderStringRequestException() {
        Request request = createMock(Request.class);

        expect(definitionsFactory.getDefinition("definition", request)).andReturn(null);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request);
        try {
            container.render("definition", request);
        } finally {
            verify(applicationContext, attributeEvaluatorFactory,
                    definitionsFactory, preparerFactory, rendererFactory);
        }
    }

    /**
     * Test method for {@link org.apache.tiles.impl.BasicTilesContainer#render(org.apache.tiles.Attribute, org.apache.tiles.request.Request)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testRenderAttributeRequest() throws IOException {
        Request request = createMock(Request.class);
        Attribute templateAttribute = createMock(Attribute.class);
        AttributeRenderer renderer = createMock(AttributeRenderer.class);

        expect(templateAttribute.getRenderer()).andReturn("renderer");
        expect(rendererFactory.getRenderer("renderer")).andReturn(renderer);
        renderer.render(templateAttribute, request);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                templateAttribute, renderer);
        container.render(templateAttribute, request);
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                templateAttribute, renderer);
    }

    /**
     * Test method for {@link org.apache.tiles.impl.BasicTilesContainer#render(org.apache.tiles.Attribute, org.apache.tiles.request.Request)}.
     * @throws IOException If something goes wrong.
     */
    @Test(expected=CannotRenderException.class)
    public void testRenderAttributeRequestException1() throws IOException {
        Request request = createMock(Request.class);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request);
        try {
            container.render((Attribute) null, request);
        } finally {
            verify(applicationContext, attributeEvaluatorFactory,
                    definitionsFactory, preparerFactory, rendererFactory,
                    request);
        }
    }

    /**
     * Test method for {@link org.apache.tiles.impl.BasicTilesContainer#render(org.apache.tiles.Attribute, org.apache.tiles.request.Request)}.
     * @throws IOException If something goes wrong.
     */
    @Test(expected=CannotRenderException.class)
    public void testRenderAttributeRequestException2() throws IOException {
        Request request = createMock(Request.class);
        Attribute templateAttribute = createMock(Attribute.class);

        expect(templateAttribute.getRenderer()).andReturn("renderer");
        expect(rendererFactory.getRenderer("renderer")).andReturn(null);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                templateAttribute);
        try {
            container.render(templateAttribute, request);
        } finally {
            verify(applicationContext, attributeEvaluatorFactory,
                    definitionsFactory, preparerFactory, rendererFactory,
                    request, templateAttribute);
        }
    }

    /**
     * Test method for {@link org.apache.tiles.impl.BasicTilesContainer#evaluate(org.apache.tiles.Attribute, org.apache.tiles.request.Request)}.
     */
    @Test
    public void testEvaluate() {
        Request request = createMock(Request.class);
        AttributeEvaluator evaluator = createMock(AttributeEvaluator.class);
        Attribute templateAttribute = createMock(Attribute.class);

        expect(attributeEvaluatorFactory.getAttributeEvaluator(templateAttribute)).andReturn(evaluator);
        expect(evaluator.evaluate(templateAttribute, request)).andReturn(new Integer(1));

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                templateAttribute, evaluator);
        assertEquals(new Integer(1), container.evaluate(templateAttribute, request));
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                templateAttribute, evaluator);
    }

    /**
     * Test method for {@link org.apache.tiles.impl.BasicTilesContainer#isValidDefinition(java.lang.String, org.apache.tiles.request.Request)}.
     */
    @Test
    public void testIsValidDefinition() {
        Request request = createMock(Request.class);
        Definition definition = createMock(Definition.class);

        expect(definitionsFactory.getDefinition("definition", request)).andReturn(definition);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request, definition);
        assertTrue(container.isValidDefinition("definition", request));
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request, definition);
    }

    /**
     * Test method for {@link org.apache.tiles.impl.BasicTilesContainer#isValidDefinition(java.lang.String, org.apache.tiles.request.Request)}.
     */
    @Test
    public void testIsValidDefinitionNull() {
        Request request = createMock(Request.class);

        expect(definitionsFactory.getDefinition("definition", request)).andReturn(null);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request);
        assertFalse(container.isValidDefinition("definition", request));
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request);
    }

    /**
     * Test method for {@link org.apache.tiles.impl.BasicTilesContainer#isValidDefinition(java.lang.String, org.apache.tiles.request.Request)}.
     */
    @Test
    public void testIsValidDefinitionException() {
        Request request = createMock(Request.class);

        expect(definitionsFactory.getDefinition("definition", request))
                .andThrow(new NoSuchDefinitionException());

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request);
        assertFalse(container.isValidDefinition("definition", request));
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request);
    }

    /**
     * Test method for {@link org.apache.tiles.impl.BasicTilesContainer#getDefinition(java.lang.String, org.apache.tiles.request.Request)}.
     */
    @Test
    public void testGetDefinition() {
        Request request = createMock(Request.class);
        Definition definition = createMock(Definition.class);

        expect(definitionsFactory.getDefinition("definition", request)).andReturn(definition);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request, definition);
        assertEquals(definition, container.getDefinition("definition", request));
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request, definition);
    }

    /**
     * Test method for {@link org.apache.tiles.impl.BasicTilesContainer#getContextStack(org.apache.tiles.request.Request)}.
     */
    @Test
    public void testGetContextStack() {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Deque<AttributeContext> deque = createMock(Deque.class);

        expect(request.getContext("request")).andReturn(requestScope);
        expect(requestScope.get(ATTRIBUTE_CONTEXT_STACK)).andReturn(deque);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque);
        assertEquals(deque, container.getContextStack(request));
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque);
    }

    /**
     * Test method for {@link org.apache.tiles.impl.BasicTilesContainer#getContextStack(org.apache.tiles.request.Request)}.
     */
    @Test
    public void testGetContextStackNew() {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);

        expect(request.getContext("request")).andReturn(requestScope);
        expect(requestScope.get(ATTRIBUTE_CONTEXT_STACK)).andReturn(null);
        expect(requestScope.put(eq(ATTRIBUTE_CONTEXT_STACK), isA(LinkedList.class))).andReturn(null);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope);
        assertTrue(container.getContextStack(request) instanceof LinkedList);
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope);
    }

    /**
     * Test method for {@link org.apache.tiles.impl.BasicTilesContainer#pushContext(org.apache.tiles.AttributeContext, org.apache.tiles.request.Request)}.
     */
    @Test
    public void testPushContext() {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Deque<AttributeContext> deque = createMock(Deque.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);

        expect(request.getContext("request")).andReturn(requestScope);
        expect(requestScope.get(ATTRIBUTE_CONTEXT_STACK)).andReturn(deque);
        deque.push(attributeContext);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext);
        container.pushContext(attributeContext, request);
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext);
    }

    /**
     * Test method for {@link org.apache.tiles.impl.BasicTilesContainer#popContext(org.apache.tiles.request.Request)}.
     */
    @Test
    public void testPopContext() {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Deque<AttributeContext> deque = createMock(Deque.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);

        expect(request.getContext("request")).andReturn(requestScope);
        expect(requestScope.get(ATTRIBUTE_CONTEXT_STACK)).andReturn(deque);
        expect(deque.pop()).andReturn(attributeContext);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext);
        assertEquals(attributeContext, container.popContext(request));
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext);
    }

    /**
     * Test method for {@link org.apache.tiles.impl.BasicTilesContainer#getContext(org.apache.tiles.request.Request)}.
     */
    @Test
    public void testGetContext() {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Deque<AttributeContext> deque = createMock(Deque.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);

        expect(request.getContext("request")).andReturn(requestScope);
        expect(requestScope.get(ATTRIBUTE_CONTEXT_STACK)).andReturn(deque);
        expect(deque.isEmpty()).andReturn(false);
        expect(deque.peek()).andReturn(attributeContext);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext);
        assertEquals(attributeContext, container.getContext(request));
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext);
    }

    /**
     * Test method for {@link org.apache.tiles.impl.BasicTilesContainer#getContext(org.apache.tiles.request.Request)}.
     */
    @Test
    public void testGetContextNull() {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Deque<AttributeContext> deque = createMock(Deque.class);

        expect(request.getContext("request")).andReturn(requestScope);
        expect(requestScope.get(ATTRIBUTE_CONTEXT_STACK)).andReturn(deque);
        expect(deque.isEmpty()).andReturn(true);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque);
        assertNull(container.getContext(request));
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque);
    }

    /**
     * Test method for {@link org.apache.tiles.impl.BasicTilesContainer#render(org.apache.tiles.request.Request, org.apache.tiles.Definition)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testRenderRequestDefinition() throws IOException {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Deque<AttributeContext> deque = createMock(Deque.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        ViewPreparer preparer = createMock(ViewPreparer.class);
        AttributeRenderer renderer = createMock(AttributeRenderer.class);
        Definition definition = createMock(Definition.class);

        Attribute templateAttribute = Attribute.createTemplateAttribute("/my/template.jsp");

        expect(request.getContext("request")).andReturn(requestScope).times(3);
        expect(requestScope.get(ATTRIBUTE_CONTEXT_STACK)).andReturn(deque).times(3);
        expect(deque.isEmpty()).andReturn(false);
        expect(deque.peek()).andReturn(attributeContext);
        expect(attributeContext.getPreparer()).andReturn(null);
        expect(attributeContext.getTemplateAttribute()).andReturn(templateAttribute);
        expect(attributeContext.getLocalAttributeNames()).andReturn(null);
        expect(attributeContext.getCascadedAttributeNames()).andReturn(null);
        expect(definition.getTemplateAttribute()).andReturn(templateAttribute);
        expect(rendererFactory.getRenderer("template")).andReturn(renderer);
        deque.push(isA(BasicAttributeContext.class));
        renderer.render(templateAttribute, request);
        expect(deque.pop()).andReturn(null);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext, preparer, renderer, definition);
        container.render(request, definition);
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext, preparer, renderer, definition);
    }

    /**
     * Test method for {@link org.apache.tiles.impl.BasicTilesContainer#render(org.apache.tiles.request.Request, org.apache.tiles.Definition)}.
     * @throws IOException If something goes wrong.
     */
    @Test(expected=CannotRenderException.class)
    public void testRenderRequestDefinitionException() throws IOException {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Deque<AttributeContext> deque = createMock(Deque.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        ViewPreparer preparer = createMock(ViewPreparer.class);
        AttributeRenderer renderer = createMock(AttributeRenderer.class);
        Definition definition = createMock(Definition.class);

        Attribute templateAttribute = Attribute.createTemplateAttribute("/my/template.jsp");

        expect(request.getContext("request")).andReturn(requestScope).times(3);
        expect(requestScope.get(ATTRIBUTE_CONTEXT_STACK)).andReturn(deque).times(3);
        expect(deque.isEmpty()).andReturn(false);
        expect(deque.peek()).andReturn(attributeContext);
        expect(attributeContext.getPreparer()).andReturn(null);
        expect(attributeContext.getTemplateAttribute()).andReturn(templateAttribute);
        expect(attributeContext.getLocalAttributeNames()).andReturn(null);
        expect(attributeContext.getCascadedAttributeNames()).andReturn(null);
        expect(definition.getTemplateAttribute()).andReturn(templateAttribute);
        expect(rendererFactory.getRenderer("template")).andReturn(renderer);
        deque.push(isA(BasicAttributeContext.class));
        renderer.render(templateAttribute, request);
        expectLastCall().andThrow(new IOException());
        expect(deque.pop()).andReturn(null);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext, preparer, renderer, definition);
        try {
            container.render(request, definition);
        } finally {
            verify(applicationContext, attributeEvaluatorFactory,
                    definitionsFactory, preparerFactory, rendererFactory,
                    request, requestScope, deque, attributeContext, preparer,
                    renderer, definition);
        }
    }

    /**
     * Test method for {@link org.apache.tiles.impl.BasicTilesContainer#render(org.apache.tiles.request.Request, org.apache.tiles.AttributeContext)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testRenderRequestAttributeContext() throws IOException {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Deque<AttributeContext> deque = createMock(Deque.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        ViewPreparer preparer = createMock(ViewPreparer.class);
        Attribute templateAttribute = createMock(Attribute.class);
        AttributeRenderer renderer = createMock(AttributeRenderer.class);

        expect(attributeContext.getPreparer()).andReturn(null);
        expect(attributeContext.getTemplateAttribute()).andReturn(templateAttribute);
        expect(templateAttribute.getRenderer()).andReturn("renderer");
        expect(rendererFactory.getRenderer("renderer")).andReturn(renderer);
        renderer.render(templateAttribute, request);

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext, preparer, templateAttribute, renderer);
        container.render(request, attributeContext);
        verify(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext, preparer, templateAttribute, renderer);
    }

    /**
     * Test method for {@link org.apache.tiles.impl.BasicTilesContainer#render(org.apache.tiles.request.Request, org.apache.tiles.AttributeContext)}.
     * @throws IOException If something goes wrong.
     */
    @Test(expected=CannotRenderException.class)
    public void testRenderRequestAttributeContextException() throws IOException {
        Request request = createMock(Request.class);
        Map<String, Object> requestScope = createMock(Map.class);
        Deque<AttributeContext> deque = createMock(Deque.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        Attribute templateAttribute = createMock(Attribute.class);
        AttributeRenderer renderer = createMock(AttributeRenderer.class);

        expect(attributeContext.getPreparer()).andReturn("preparer").times(2);
        expect(preparerFactory.getPreparer("preparer", request)).andReturn(null);
        expect(attributeContext.getTemplateAttribute()).andReturn(templateAttribute);
        expect(templateAttribute.getRenderer()).andReturn("renderer");
        expect(rendererFactory.getRenderer("renderer")).andReturn(renderer);
        renderer.render(templateAttribute, request);
        expectLastCall().andThrow(new IOException());

        replay(applicationContext, attributeEvaluatorFactory,
                definitionsFactory, preparerFactory, rendererFactory, request,
                requestScope, deque, attributeContext, templateAttribute, renderer);
        try {
            container.render(request, attributeContext);
        } finally {
            verify(applicationContext, attributeEvaluatorFactory,
                    definitionsFactory, preparerFactory, rendererFactory,
                    request, requestScope, deque, attributeContext,
                    templateAttribute, renderer);
        }
    }
}
