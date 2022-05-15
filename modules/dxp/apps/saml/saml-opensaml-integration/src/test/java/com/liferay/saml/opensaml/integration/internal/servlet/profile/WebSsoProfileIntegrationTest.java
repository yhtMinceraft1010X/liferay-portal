/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.saml.opensaml.integration.internal.servlet.profile;

import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.test.rule.LiferayUnitTestRule;
import com.liferay.saml.constants.SamlWebKeys;
import com.liferay.saml.opensaml.integration.internal.BaseSamlTestCase;
import com.liferay.saml.opensaml.integration.internal.bootstrap.SecurityConfigurationBootstrap;
import com.liferay.saml.opensaml.integration.internal.metadata.MetadataManagerImpl;
import com.liferay.saml.opensaml.integration.internal.util.OpenSamlUtil;
import com.liferay.saml.opensaml.integration.internal.util.SamlUtil;
import com.liferay.saml.persistence.model.SamlSpAuthRequest;
import com.liferay.saml.persistence.model.SamlSpIdpConnection;
import com.liferay.saml.persistence.model.impl.SamlSpAuthRequestImpl;
import com.liferay.saml.persistence.model.impl.SamlSpIdpConnectionImpl;
import com.liferay.saml.persistence.service.SamlSpAuthRequestLocalService;
import com.liferay.saml.persistence.service.SamlSpAuthRequestLocalServiceUtil;
import com.liferay.saml.persistence.service.SamlSpIdpConnectionLocalService;
import com.liferay.saml.persistence.service.SamlSpIdpConnectionLocalServiceUtil;
import com.liferay.saml.persistence.service.SamlSpMessageLocalService;
import com.liferay.saml.persistence.service.SamlSpMessageLocalServiceUtil;
import com.liferay.saml.persistence.service.SamlSpSessionLocalService;
import com.liferay.saml.persistence.service.SamlSpSessionLocalServiceUtil;
import com.liferay.saml.runtime.exception.AssertionException;
import com.liferay.saml.runtime.exception.AudienceException;
import com.liferay.saml.runtime.exception.DestinationException;
import com.liferay.saml.runtime.exception.ExpiredException;
import com.liferay.saml.runtime.exception.InResponseToException;
import com.liferay.saml.runtime.exception.IssuerException;
import com.liferay.saml.runtime.exception.SignatureException;
import com.liferay.saml.runtime.exception.SubjectException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.shibboleth.utilities.java.support.resolver.CriteriaSet;
import net.shibboleth.utilities.java.support.security.IdentifierGenerationStrategy;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Mockito;

import org.opensaml.core.criterion.EntityIdCriterion;
import org.opensaml.core.xml.XMLObject;
import org.opensaml.messaging.context.InOutOperationContext;
import org.opensaml.messaging.context.MessageContext;
import org.opensaml.messaging.handler.MessageHandlerException;
import org.opensaml.saml.common.messaging.context.SAMLBindingContext;
import org.opensaml.saml.common.messaging.context.SAMLMessageInfoContext;
import org.opensaml.saml.common.messaging.context.SAMLMetadataContext;
import org.opensaml.saml.common.messaging.context.SAMLPeerEntityContext;
import org.opensaml.saml.common.messaging.context.SAMLSelfEntityContext;
import org.opensaml.saml.common.messaging.context.SAMLSubjectNameIdentifierContext;
import org.opensaml.saml.common.xml.SAMLConstants;
import org.opensaml.saml.ext.saml2alg.SigningMethod;
import org.opensaml.saml.metadata.resolver.MetadataResolver;
import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.saml.saml2.core.AudienceRestriction;
import org.opensaml.saml.saml2.core.AuthnRequest;
import org.opensaml.saml.saml2.core.Conditions;
import org.opensaml.saml.saml2.core.Issuer;
import org.opensaml.saml.saml2.core.NameID;
import org.opensaml.saml.saml2.core.NameIDType;
import org.opensaml.saml.saml2.core.Response;
import org.opensaml.saml.saml2.core.Subject;
import org.opensaml.saml.saml2.core.SubjectConfirmation;
import org.opensaml.saml.saml2.core.SubjectConfirmationData;
import org.opensaml.saml.saml2.metadata.AssertionConsumerService;
import org.opensaml.saml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml.saml2.metadata.Extensions;
import org.opensaml.saml.saml2.metadata.IDPSSODescriptor;
import org.opensaml.saml.saml2.metadata.SPSSODescriptor;
import org.opensaml.security.credential.Credential;
import org.opensaml.xmlsec.signature.Signature;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author Mika Koivisto
 * @author Matthew Tambara
 * @author William Newbury
 */
public class WebSsoProfileIntegrationTest extends BaseSamlTestCase {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		_samlSpAuthRequestLocalService = getMockPortletService(
			SamlSpAuthRequestLocalServiceUtil.class,
			SamlSpAuthRequestLocalService.class);
		_samlSpSessionLocalService = getMockPortletService(
			SamlSpSessionLocalServiceUtil.class,
			SamlSpSessionLocalService.class);

		_webSsoProfileImpl.setIdentifierGenerationStrategyFactory(
			identifierGenerationStrategyFactory);
		_webSsoProfileImpl.setMetadataManager(metadataManagerImpl);
		_webSsoProfileImpl.setPortal(portal);
		_webSsoProfileImpl.setSamlBindings(samlBindings);

		_webSsoProfileImpl.setSamlProviderConfigurationHelper(
			samlProviderConfigurationHelper);
		_webSsoProfileImpl.setSamlSpAuthRequestLocalService(
			_samlSpAuthRequestLocalService);
		_webSsoProfileImpl.setSamlSpSessionLocalService(
			_samlSpSessionLocalService);
		_webSsoProfileImpl.setSamlSpMessageLocalService(
			getMockPortletService(
				SamlSpMessageLocalServiceUtil.class,
				SamlSpMessageLocalService.class));
		_webSsoProfileImpl.setSamlSpIdpConnectionLocalService(
			getMockPortletService(
				SamlSpIdpConnectionLocalServiceUtil.class,
				SamlSpIdpConnectionLocalService.class));
		_webSsoProfileImpl.activate(new HashMap<String, Object>());

		prepareServiceProvider(SP_ENTITY_ID);
	}

	@Test
	public void testAssertionSignatureAlgorithmIsNegotiated() throws Exception {
		Assertion assertion = OpenSamlUtil.buildAssertion();

		Credential credential = getCredential(IDP_ENTITY_ID);

		MetadataResolver metadataResolver =
			metadataManagerImpl.getMetadataResolver();

		EntityDescriptor entityDescriptor = metadataResolver.resolveSingle(
			new CriteriaSet(new EntityIdCriterion(SP_ENTITY_ID)));

		SPSSODescriptor spSSODescriptor = entityDescriptor.getSPSSODescriptor(
			SAMLConstants.SAML20P_NS);

		Extensions extensions = spSSODescriptor.getExtensions();

		List<XMLObject> unknownXMLObjects = extensions.getUnknownXMLObjects();

		Iterator<XMLObject> iterator = unknownXMLObjects.iterator();

		// Peer only allows SHA512

		while (iterator.hasNext()) {
			XMLObject xmlObject = iterator.next();

			if (xmlObject instanceof SigningMethod) {
				SigningMethod signingMethod = (SigningMethod)xmlObject;

				String algorithm = signingMethod.getAlgorithm();

				if (!algorithm.equals(
						"http://www.w3.org/2001/04/xmldsig-more#rsa-sha512")) {

					iterator.remove();
				}
			}
		}

		OpenSamlUtil.signObject(assertion, credential, spSSODescriptor);

		Signature signature = assertion.getSignature();

		Assert.assertEquals(
			"http://www.w3.org/2001/04/xmldsig-more#rsa-sha512",
			signature.getSignatureAlgorithm());

		// Blacklist SHA512 and check that it is not used even though it is the
		// only one signaled by the peer

		ReflectionTestUtil.invoke(
			new SecurityConfigurationBootstrap(), "activate",
			new Class<?>[] {Map.class},
			HashMapBuilder.<String, Object>put(
				"blacklisted.algorithms",
				new String[] {
					"http://www.w3.org/2001/04/xmldsig-more#rsa-sha512"
				}
			).build());

		assertion = OpenSamlUtil.buildAssertion();

		OpenSamlUtil.signObject(assertion, credential, spSSODescriptor);

		signature = assertion.getSignature();

		Assert.assertNotEquals(
			"http://www.w3.org/2001/04/xmldsig-more#rsa-sha512",
			signature.getSignatureAlgorithm());

		// Check that SHA512 was actually negotiated and that the default is
		// different

		assertion = OpenSamlUtil.buildAssertion();

		OpenSamlUtil.signObject(assertion, credential, null);

		signature = assertion.getSignature();

		Assert.assertEquals(
			"http://www.w3.org/2001/04/xmldsig-more#rsa-sha256",
			signature.getSignatureAlgorithm());
	}

	@Test
	public void testAssertionSignatureDefaultAlgorithmIsNotSHA1()
		throws Exception {

		Assertion assertion = OpenSamlUtil.buildAssertion();

		OpenSamlUtil.signObject(assertion, getCredential(IDP_ENTITY_ID), null);

		Signature signature = assertion.getSignature();

		Assert.assertNotEquals(
			"http://www.w3.org/2000/09/xmldsig#rsa-sha1",
			signature.getSignatureAlgorithm());
	}

	@Test
	public void testDecodeAuthnRequestIdpInitiatedSso() throws Exception {
		prepareIdentityProvider(IDP_ENTITY_ID);

		MockHttpServletRequest mockHttpServletRequest =
			getMockHttpServletRequest(SSO_URL);

		mockHttpServletRequest.setParameter("entityId", SP_ENTITY_ID);
		mockHttpServletRequest.setParameter("RelayState", RELAY_STATE);

		SamlSsoRequestContext samlSsoRequestContext =
			_webSsoProfileImpl.decodeAuthnRequest(
				mockHttpServletRequest, new MockHttpServletResponse());

		MessageContext<?> messageContext =
			samlSsoRequestContext.getSAMLMessageContext();

		SAMLSelfEntityContext samlSelfEntityContext =
			messageContext.getSubcontext(SAMLSelfEntityContext.class, false);

		Assert.assertEquals(IDP_ENTITY_ID, samlSelfEntityContext.getEntityId());

		SAMLMetadataContext samlMetadataContext =
			samlSelfEntityContext.getSubcontext(SAMLMetadataContext.class);

		Assert.assertNotNull(samlMetadataContext.getEntityDescriptor());
		Assert.assertNotNull(samlMetadataContext.getRoleDescriptor());
		Assert.assertTrue(
			samlMetadataContext.getRoleDescriptor() instanceof
				IDPSSODescriptor);

		SAMLPeerEntityContext samlPeerEntityContext =
			messageContext.getSubcontext(SAMLPeerEntityContext.class);

		Assert.assertEquals(SP_ENTITY_ID, samlPeerEntityContext.getEntityId());

		SAMLMetadataContext samlPeerMetadataContext =
			samlPeerEntityContext.getSubcontext(SAMLMetadataContext.class);

		Assert.assertNotNull(samlPeerMetadataContext.getEntityDescriptor());
		Assert.assertNotNull(samlPeerMetadataContext.getRoleDescriptor());
		Assert.assertTrue(
			samlPeerMetadataContext.getRoleDescriptor() instanceof
				SPSSODescriptor);

		SAMLBindingContext samlBindingContext = messageContext.getSubcontext(
			SAMLBindingContext.class);

		Assert.assertEquals(RELAY_STATE, samlBindingContext.getRelayState());

		Assert.assertTrue(samlSsoRequestContext.isNewSession());
	}

	@Test
	public void testDecodeAuthnRequestIdpInitiatedSsoAfterAuthentication()
		throws Exception {

		prepareIdentityProvider(IDP_ENTITY_ID);

		MockHttpServletRequest mockHttpServletRequest =
			getMockHttpServletRequest(SSO_URL + "?entityId=" + SP_ENTITY_ID);

		HttpSession mockHttpSession = mockHttpServletRequest.getSession();

		SamlSsoRequestContext samlSsoRequestContext = new SamlSsoRequestContext(
			SP_ENTITY_ID, RELAY_STATE, null, userLocalService);

		mockHttpSession.setAttribute(
			SamlWebKeys.SAML_SSO_REQUEST_CONTEXT, samlSsoRequestContext);

		samlSsoRequestContext = _webSsoProfileImpl.decodeAuthnRequest(
			mockHttpServletRequest, new MockHttpServletResponse());

		MessageContext<AuthnRequest> messageContext =
			(MessageContext<AuthnRequest>)
				samlSsoRequestContext.getSAMLMessageContext();

		SAMLSelfEntityContext samlSelfEntityContext =
			messageContext.getSubcontext(SAMLSelfEntityContext.class);

		Assert.assertEquals(IDP_ENTITY_ID, samlSelfEntityContext.getEntityId());

		SAMLMetadataContext samlMetadataContext =
			samlSelfEntityContext.getSubcontext(SAMLMetadataContext.class);

		Assert.assertNotNull(samlMetadataContext.getEntityDescriptor());
		Assert.assertNotNull(samlMetadataContext.getRoleDescriptor());
		Assert.assertTrue(
			samlMetadataContext.getRoleDescriptor() instanceof
				IDPSSODescriptor);

		SAMLPeerEntityContext samlPeerEntityContext =
			messageContext.getSubcontext(SAMLPeerEntityContext.class);

		Assert.assertEquals(SP_ENTITY_ID, samlPeerEntityContext.getEntityId());

		SAMLMetadataContext samlPeerMetadataContext =
			samlPeerEntityContext.getSubcontext(SAMLMetadataContext.class);

		Assert.assertNotNull(samlPeerMetadataContext.getEntityDescriptor());
		Assert.assertNotNull(samlPeerMetadataContext.getRoleDescriptor());
		Assert.assertTrue(
			samlPeerMetadataContext.getRoleDescriptor() instanceof
				SPSSODescriptor);

		SAMLBindingContext samlBindingContext = messageContext.getSubcontext(
			SAMLBindingContext.class);

		Assert.assertEquals(RELAY_STATE, samlBindingContext.getRelayState());

		Assert.assertTrue(samlSsoRequestContext.isNewSession());
	}

	@Test
	public void testDecodeAuthnRequestStageAuthenticated() throws Exception {
		SamlSpIdpConnectionLocalService samlSpIdpConnectionLocalService =
			getMockPortletService(
				SamlSpIdpConnectionLocalServiceUtil.class,
				SamlSpIdpConnectionLocalService.class);

		SamlSpIdpConnection samlSpIdpConnection = new SamlSpIdpConnectionImpl();

		samlSpIdpConnection.setSamlIdpEntityId(IDP_ENTITY_ID);

		Mockito.when(
			samlSpIdpConnectionLocalService.getSamlSpIdpConnection(
				Mockito.eq(COMPANY_ID), Mockito.eq(IDP_ENTITY_ID))
		).thenReturn(
			samlSpIdpConnection
		);

		_webSsoProfileImpl.setSamlSpIdpConnectionLocalService(
			samlSpIdpConnectionLocalService);

		MockHttpServletRequest mockHttpServletRequest =
			getMockHttpServletRequest(LOGIN_URL);

		mockHttpServletRequest.setAttribute(
			SamlWebKeys.SAML_SP_IDP_CONNECTION, samlSpIdpConnection);

		MockHttpServletResponse mockHttpServletResponse =
			new MockHttpServletResponse();

		_webSsoProfileImpl.doSendAuthnRequest(
			mockHttpServletRequest, mockHttpServletResponse, RELAY_STATE);

		String redirect = mockHttpServletResponse.getRedirectedUrl();

		prepareIdentityProvider(IDP_ENTITY_ID);

		mockHttpServletRequest = getMockHttpServletRequest(redirect);

		mockHttpServletResponse = new MockHttpServletResponse();

		SamlSsoRequestContext samlSsoRequestContext =
			_webSsoProfileImpl.decodeAuthnRequest(
				mockHttpServletRequest, mockHttpServletResponse);

		MessageContext<AuthnRequest> messageContext =
			(MessageContext<AuthnRequest>)
				samlSsoRequestContext.getSAMLMessageContext();

		InOutOperationContext<?, ?> inOutOperationContext =
			messageContext.getSubcontext(InOutOperationContext.class);

		MessageContext<?> inboundMessageContext =
			inOutOperationContext.getInboundMessageContext();

		SAMLMessageInfoContext samlMessageInfoContext =
			inboundMessageContext.getSubcontext(
				SAMLMessageInfoContext.class, false);

		Assert.assertNotNull(samlMessageInfoContext.getMessageId());

		String inboundSamlMessageId = samlMessageInfoContext.getMessageId();

		mockHttpServletRequest = getMockHttpServletRequest(
			SSO_URL + "?saml_message_id=" + inboundSamlMessageId);

		HttpSession mockHttpSession = mockHttpServletRequest.getSession();

		samlSsoRequestContext.setSAMLMessageContext(null);

		mockHttpSession.setAttribute(
			SamlWebKeys.SAML_SSO_REQUEST_CONTEXT, samlSsoRequestContext);

		Mockito.when(
			portal.getUserId(Mockito.any(MockHttpServletRequest.class))
		).thenReturn(
			1000L
		);

		samlSsoRequestContext = _webSsoProfileImpl.decodeAuthnRequest(
			mockHttpServletRequest, mockHttpServletResponse);

		messageContext =
			(MessageContext<AuthnRequest>)
				samlSsoRequestContext.getSAMLMessageContext();

		inOutOperationContext = messageContext.getSubcontext(
			InOutOperationContext.class);

		inboundMessageContext =
			inOutOperationContext.getInboundMessageContext();

		Assert.assertNotNull(inboundMessageContext.getMessage());

		SAMLMessageInfoContext messageInfoContext =
			inboundMessageContext.getSubcontext(SAMLMessageInfoContext.class);

		Assert.assertEquals(
			inboundSamlMessageId, messageInfoContext.getMessageId());

		SAMLBindingContext samlBindingContext = messageContext.getSubcontext(
			SAMLBindingContext.class);

		Assert.assertEquals(RELAY_STATE, samlBindingContext.getRelayState());

		Assert.assertNull(
			mockHttpSession.getAttribute(SamlWebKeys.SAML_SSO_REQUEST_CONTEXT));
		Assert.assertEquals(
			SamlSsoRequestContext.STAGE_AUTHENTICATED,
			samlSsoRequestContext.getStage());
		Assert.assertEquals(1000, samlSsoRequestContext.getUserId());
	}

	@Test
	public void testDecodeAuthnRequestStageInitial() throws Exception {
		SamlSpIdpConnectionLocalService samlSpIdpConnectionLocalService =
			getMockPortletService(
				SamlSpIdpConnectionLocalServiceUtil.class,
				SamlSpIdpConnectionLocalService.class);

		SamlSpIdpConnection samlSpIdpConnection = new SamlSpIdpConnectionImpl();

		samlSpIdpConnection.setSamlIdpEntityId(IDP_ENTITY_ID);

		Mockito.when(
			samlSpIdpConnectionLocalService.getSamlSpIdpConnection(
				Mockito.eq(COMPANY_ID), Mockito.eq(IDP_ENTITY_ID))
		).thenReturn(
			samlSpIdpConnection
		);

		_webSsoProfileImpl.setSamlSpIdpConnectionLocalService(
			samlSpIdpConnectionLocalService);

		MockHttpServletRequest mockHttpServletRequest =
			getMockHttpServletRequest(LOGIN_URL);

		mockHttpServletRequest.setAttribute(
			SamlWebKeys.SAML_SP_IDP_CONNECTION, samlSpIdpConnection);

		MockHttpServletResponse mockHttpServletResponse =
			new MockHttpServletResponse();

		_webSsoProfileImpl.doSendAuthnRequest(
			mockHttpServletRequest, mockHttpServletResponse, RELAY_STATE);

		String redirect = mockHttpServletResponse.getRedirectedUrl();

		Assert.assertNotNull(redirect);

		prepareIdentityProvider(IDP_ENTITY_ID);

		SamlSsoRequestContext samlSsoRequestContext =
			_webSsoProfileImpl.decodeAuthnRequest(
				getMockHttpServletRequest(redirect),
				new MockHttpServletResponse());

		MessageContext<AuthnRequest> messageContext =
			(MessageContext<AuthnRequest>)
				samlSsoRequestContext.getSAMLMessageContext();

		SAMLSelfEntityContext samlSelfEntityContext =
			messageContext.getSubcontext(SAMLSelfEntityContext.class);

		Assert.assertEquals(IDP_ENTITY_ID, samlSelfEntityContext.getEntityId());

		SAMLMetadataContext samlMetadataContext =
			samlSelfEntityContext.getSubcontext(SAMLMetadataContext.class);

		Assert.assertNotNull(samlMetadataContext.getEntityDescriptor());
		Assert.assertNotNull(samlMetadataContext.getRoleDescriptor());
		Assert.assertTrue(
			samlMetadataContext.getRoleDescriptor() instanceof
				IDPSSODescriptor);

		SAMLPeerEntityContext samlPeerEntityContext =
			messageContext.getSubcontext(SAMLPeerEntityContext.class);

		Assert.assertEquals(SP_ENTITY_ID, samlPeerEntityContext.getEntityId());

		SAMLMetadataContext samlPeerMetadataContext =
			samlPeerEntityContext.getSubcontext(SAMLMetadataContext.class);

		Assert.assertNotNull(samlPeerMetadataContext.getEntityDescriptor());
		Assert.assertNotNull(samlPeerMetadataContext.getRoleDescriptor());
		Assert.assertTrue(
			samlPeerMetadataContext.getRoleDescriptor() instanceof
				SPSSODescriptor);

		SAMLBindingContext samlBindingContext = messageContext.getSubcontext(
			SAMLBindingContext.class);

		Assert.assertEquals(RELAY_STATE, samlBindingContext.getRelayState());

		InOutOperationContext<?, ?> inOutOperationContext =
			messageContext.getSubcontext(InOutOperationContext.class);

		MessageContext<?> inboundMessageContext =
			inOutOperationContext.getInboundMessageContext();

		AuthnRequest authnRequest =
			(AuthnRequest)inboundMessageContext.getMessage();

		Assert.assertEquals(identifiers.get(0), authnRequest.getID());

		Assert.assertEquals(2, identifiers.size());
		Assert.assertFalse(authnRequest.isForceAuthn());
		Assert.assertTrue(samlSsoRequestContext.isNewSession());
	}

	@Test(expected = MessageHandlerException.class)
	public void testDecodeAuthnRequestVerifiesSignature() throws Exception {
		SamlSpIdpConnectionLocalService samlSpIdpConnectionLocalService =
			getMockPortletService(
				SamlSpIdpConnectionLocalServiceUtil.class,
				SamlSpIdpConnectionLocalService.class);

		SamlSpIdpConnection samlSpIdpConnection = new SamlSpIdpConnectionImpl();

		samlSpIdpConnection.setSamlIdpEntityId(IDP_ENTITY_ID);

		metadataManagerImpl = new MetadataManagerImpl();

		metadataManagerImpl.setCredentialResolver(credentialResolver);
		metadataManagerImpl.setLocalEntityManager(credentialResolver);
		metadataManagerImpl.setMetadataResolver(
			new MockMetadataResolver(false));
		metadataManagerImpl.setParserPool(parserPool);
		metadataManagerImpl.setPortal(portal);
		metadataManagerImpl.setSamlProviderConfigurationHelper(
			samlProviderConfigurationHelper);

		ReflectionTestUtil.invoke(
			metadataManagerImpl, "activate", new Class<?>[0]);

		_webSsoProfileImpl.setMetadataManager(metadataManagerImpl);

		Mockito.when(
			samlSpIdpConnectionLocalService.getSamlSpIdpConnection(
				Mockito.eq(COMPANY_ID), Mockito.eq(IDP_ENTITY_ID))
		).thenReturn(
			samlSpIdpConnection
		);

		_webSsoProfileImpl.setSamlSpIdpConnectionLocalService(
			samlSpIdpConnectionLocalService);

		MockHttpServletRequest mockHttpServletRequest =
			getMockHttpServletRequest(LOGIN_URL);

		mockHttpServletRequest.setAttribute(
			SamlWebKeys.SAML_SP_IDP_CONNECTION, samlSpIdpConnection);

		MockHttpServletResponse mockHttpServletResponse =
			new MockHttpServletResponse();

		_webSsoProfileImpl.doSendAuthnRequest(
			mockHttpServletRequest, mockHttpServletResponse, RELAY_STATE);

		String redirect = mockHttpServletResponse.getRedirectedUrl();

		prepareIdentityProvider(IDP_ENTITY_ID);

		mockHttpServletRequest = getMockHttpServletRequest(redirect);

		mockHttpServletResponse = new MockHttpServletResponse();

		Mockito.when(
			samlProviderConfiguration.authnRequestSignatureRequired()
		).thenReturn(
			true
		);

		_webSsoProfileImpl.decodeAuthnRequest(
			mockHttpServletRequest, mockHttpServletResponse);
	}

	@Test
	public void testForceAuthn() throws Exception {
		SamlSpIdpConnectionLocalService samlSpIdpConnectionLocalService =
			getMockPortletService(
				SamlSpIdpConnectionLocalServiceUtil.class,
				SamlSpIdpConnectionLocalService.class);

		SamlSpIdpConnection samlSpIdpConnection = new SamlSpIdpConnectionImpl();

		samlSpIdpConnection.setForceAuthn(true);

		samlSpIdpConnection.setSamlIdpEntityId(IDP_ENTITY_ID);

		Mockito.when(
			samlSpIdpConnectionLocalService.getSamlSpIdpConnection(
				Mockito.eq(COMPANY_ID), Mockito.eq(IDP_ENTITY_ID))
		).thenReturn(
			samlSpIdpConnection
		);

		_webSsoProfileImpl.setSamlSpIdpConnectionLocalService(
			samlSpIdpConnectionLocalService);

		MockHttpServletRequest mockHttpServletRequest =
			getMockHttpServletRequest(LOGIN_URL);

		mockHttpServletRequest.setAttribute(
			SamlWebKeys.SAML_SP_IDP_CONNECTION, samlSpIdpConnection);

		MockHttpServletResponse mockHttpServletResponse =
			new MockHttpServletResponse();

		_webSsoProfileImpl.doSendAuthnRequest(
			mockHttpServletRequest, mockHttpServletResponse, RELAY_STATE);

		String redirect = mockHttpServletResponse.getRedirectedUrl();

		Assert.assertNotNull(redirect);

		prepareIdentityProvider(IDP_ENTITY_ID);

		SamlSsoRequestContext samlSsoRequestContext =
			_webSsoProfileImpl.decodeAuthnRequest(
				getMockHttpServletRequest(redirect),
				new MockHttpServletResponse());

		MessageContext<AuthnRequest> messageContext =
			(MessageContext<AuthnRequest>)
				samlSsoRequestContext.getSAMLMessageContext();

		InOutOperationContext<AuthnRequest, ?> inOutOperationContext =
			messageContext.getSubcontext(InOutOperationContext.class);

		MessageContext<AuthnRequest> inboundMessageContext =
			inOutOperationContext.getInboundMessageContext();

		AuthnRequest authnRequest = inboundMessageContext.getMessage();

		Assert.assertTrue(authnRequest.isForceAuthn());
	}

	@Test(expected = SignatureException.class)
	public void testVerifyAssertionSignatureInvalidSignature()
		throws Exception {

		_testVerifyAssertionSignature(UNKNOWN_ENTITY_ID);
	}

	@Test
	public void testVerifyAssertionSignatureNoSignatureNotRequired()
		throws Exception {

		MessageContext<?> messageContext = _webSsoProfileImpl.getMessageContext(
			getMockHttpServletRequest(ACS_URL), new MockHttpServletResponse());

		SAMLSelfEntityContext samlSelfEntityContext =
			messageContext.getSubcontext(SAMLSelfEntityContext.class);

		SAMLMetadataContext samlMetadataContext =
			samlSelfEntityContext.getSubcontext(SAMLMetadataContext.class);

		EntityDescriptor entityDescriptor =
			samlMetadataContext.getEntityDescriptor();

		SPSSODescriptor spSSODescriptor = entityDescriptor.getSPSSODescriptor(
			SAMLConstants.SAML20P_NS);

		spSSODescriptor.setWantAssertionsSigned(false);

		samlMetadataContext.setRoleDescriptor(spSSODescriptor);

		SAMLPeerEntityContext samlPeerEntityContext =
			messageContext.getSubcontext(SAMLPeerEntityContext.class);

		samlPeerEntityContext.setEntityId(IDP_ENTITY_ID);

		_webSsoProfileImpl.verifyAssertionSignature(
			null, messageContext,
			metadataManagerImpl.getSignatureTrustEngine());
	}

	@Test(expected = SignatureException.class)
	public void testVerifyAssertionSignatureNoSignatureRequired()
		throws Exception {

		MessageContext<?> messageContext = _webSsoProfileImpl.getMessageContext(
			getMockHttpServletRequest(ACS_URL), new MockHttpServletResponse());

		SAMLSelfEntityContext samlSelfEntityContext =
			messageContext.getSubcontext(SAMLSelfEntityContext.class);

		SAMLMetadataContext samlSelfMetadataContext =
			samlSelfEntityContext.getSubcontext(SAMLMetadataContext.class);

		EntityDescriptor entityDescriptor =
			samlSelfMetadataContext.getEntityDescriptor();

		SPSSODescriptor spSSODescriptor = entityDescriptor.getSPSSODescriptor(
			SAMLConstants.SAML20P_NS);

		spSSODescriptor.setWantAssertionsSigned(true);

		samlSelfMetadataContext.setRoleDescriptor(spSSODescriptor);

		SAMLPeerEntityContext samlPeerEntityContext =
			messageContext.getSubcontext(SAMLPeerEntityContext.class);

		samlPeerEntityContext.setEntityId(IDP_ENTITY_ID);

		_webSsoProfileImpl.verifyAssertionSignature(
			null, messageContext,
			metadataManagerImpl.getSignatureTrustEngine());
	}

	@Test
	public void testVerifyAssertionSignatureValidSignature() throws Exception {
		_testVerifyAssertionSignature(IDP_ENTITY_ID);
	}

	@Test
	public void testVerifyAudienceRestrictionsAllow() throws Exception {
		List<AudienceRestriction> audienceRestrictions = new ArrayList<>();

		MessageContext<?> messageContext = _webSsoProfileImpl.getMessageContext(
			getMockHttpServletRequest(ACS_URL), new MockHttpServletResponse());

		SAMLSelfEntityContext samlSelfEntityContext =
			messageContext.getSubcontext(SAMLSelfEntityContext.class);

		AudienceRestriction audienceRestriction =
			_webSsoProfileImpl.getSuccessAudienceRestriction(
				samlSelfEntityContext.getEntityId());

		audienceRestrictions.add(audienceRestriction);

		_webSsoProfileImpl.verifyAudienceRestrictions(
			audienceRestrictions, messageContext);
	}

	@Test(expected = AudienceException.class)
	public void testVerifyAudienceRestrictionsDeny() throws Exception {
		List<AudienceRestriction> audienceRestrictions = new ArrayList<>();

		AudienceRestriction audienceRestriction =
			_webSsoProfileImpl.getSuccessAudienceRestriction(UNKNOWN_ENTITY_ID);

		audienceRestrictions.add(audienceRestriction);

		_webSsoProfileImpl.verifyAudienceRestrictions(
			audienceRestrictions,
			_webSsoProfileImpl.getMessageContext(
				getMockHttpServletRequest(ACS_URL),
				new MockHttpServletResponse()));
	}

	@Test(expected = AssertionException.class)
	public void testVerifyConditionNotOnBefore() throws Exception {
		prepareIdentityProvider(IDP_ENTITY_ID);

		MessageContext<?> idpMessageContext =
			_webSsoProfileImpl.getMessageContext(
				getMockHttpServletRequest(SSO_URL),
				new MockHttpServletResponse());

		SAMLPeerEntityContext samlPeerEntityContext =
			idpMessageContext.getSubcontext(SAMLPeerEntityContext.class);

		samlPeerEntityContext.setEntityId(SP_ENTITY_ID);

		SamlSsoRequestContext samlSsoRequestContext = new SamlSsoRequestContext(
			SP_ENTITY_ID, null, idpMessageContext, userLocalService);

		DateTime dateTime = new DateTime(DateTimeZone.UTC);

		Conditions conditions = _webSsoProfileImpl.getSuccessConditions(
			samlSsoRequestContext, dateTime.plusDays(1), null);

		prepareServiceProvider(SP_ENTITY_ID);

		MessageContext<?> spMessageContext =
			_webSsoProfileImpl.getMessageContext(
				getMockHttpServletRequest(ACS_URL),
				new MockHttpServletResponse());

		_webSsoProfileImpl.verifyConditions(spMessageContext, conditions);
	}

	@Test(expected = ExpiredException.class)
	public void testVerifyConditionNotOnOrAfter() throws Exception {
		prepareIdentityProvider(IDP_ENTITY_ID);

		MessageContext<?> idpMessageContext =
			_webSsoProfileImpl.getMessageContext(
				getMockHttpServletRequest(SSO_URL),
				new MockHttpServletResponse());

		SAMLPeerEntityContext samlPeerEntityContext =
			idpMessageContext.getSubcontext(SAMLPeerEntityContext.class);

		samlPeerEntityContext.setEntityId(SP_ENTITY_ID);

		SamlSsoRequestContext samlSsoRequestContext = new SamlSsoRequestContext(
			SP_ENTITY_ID, null, idpMessageContext, userLocalService);

		DateTime dateTime = new DateTime(DateTimeZone.UTC);

		Conditions conditions = _webSsoProfileImpl.getSuccessConditions(
			samlSsoRequestContext, null, dateTime.minusYears(1));

		prepareServiceProvider(SP_ENTITY_ID);

		MessageContext<?> spMessageContext =
			_webSsoProfileImpl.getMessageContext(
				getMockHttpServletRequest(ACS_URL),
				new MockHttpServletResponse());

		_webSsoProfileImpl.verifyConditions(spMessageContext, conditions);
	}

	@Test
	public void testVerifyConditionsNoDates() throws Exception {
		prepareIdentityProvider(IDP_ENTITY_ID);

		MessageContext<?> idpMessageContext =
			_webSsoProfileImpl.getMessageContext(
				getMockHttpServletRequest(SSO_URL),
				new MockHttpServletResponse());

		SAMLPeerEntityContext samlPeerEntityContext =
			idpMessageContext.getSubcontext(SAMLPeerEntityContext.class);

		samlPeerEntityContext.setEntityId(SP_ENTITY_ID);

		SamlSsoRequestContext samlSsoRequestContext = new SamlSsoRequestContext(
			SP_ENTITY_ID, null, idpMessageContext, userLocalService);

		Conditions conditions = _webSsoProfileImpl.getSuccessConditions(
			samlSsoRequestContext, null, null);

		prepareServiceProvider(SP_ENTITY_ID);

		_webSsoProfileImpl.verifyConditions(
			_webSsoProfileImpl.getMessageContext(
				getMockHttpServletRequest(ACS_URL),
				new MockHttpServletResponse()),
			conditions);
	}

	@Test
	public void testVerifyDestinationAllow() throws Exception {
		MessageContext<?> messageContext = _webSsoProfileImpl.getMessageContext(
			getMockHttpServletRequest(ACS_URL), new MockHttpServletResponse());

		SAMLBindingContext samlBindingContext = messageContext.getSubcontext(
			SAMLBindingContext.class);

		samlBindingContext.setBindingUri(SAMLConstants.SAML2_POST_BINDING_URI);

		_webSsoProfileImpl.verifyDestination(messageContext, ACS_URL);
	}

	@Test(expected = DestinationException.class)
	public void testVerifyDestinationDeny() throws Exception {
		MessageContext<?> messageContext = _webSsoProfileImpl.getMessageContext(
			getMockHttpServletRequest(ACS_URL), new MockHttpServletResponse());

		SAMLBindingContext samlBindingContext = messageContext.getSubcontext(
			SAMLBindingContext.class);

		samlBindingContext.setBindingUri(SAMLConstants.SAML2_POST_BINDING_URI);

		_webSsoProfileImpl.verifyDestination(
			messageContext, "http://www.fail.com/c/portal/saml/acs");
	}

	@Test
	public void testVerifyInResponseToInvalidResponse() throws Exception {
		Response response = OpenSamlUtil.buildResponse();

		response.setIssuer(OpenSamlUtil.buildIssuer(IDP_ENTITY_ID));

		_webSsoProfileImpl.verifyInResponseTo(response);
	}

	@Test(expected = InResponseToException.class)
	public void testVerifyInResponseToNoAuthnRequest() throws Exception {
		Response response = OpenSamlUtil.buildResponse();

		response.setInResponseTo("responseto");
		response.setIssuer(OpenSamlUtil.buildIssuer(IDP_ENTITY_ID));

		_webSsoProfileImpl.verifyInResponseTo(response);
	}

	@Test
	public void testVerifyInResponseToValidResponse() throws Exception {
		Response response = OpenSamlUtil.buildResponse();

		SamlSpAuthRequest samlSpAuthRequest = new SamlSpAuthRequestImpl();

		samlSpAuthRequest.setSamlIdpEntityId(IDP_ENTITY_ID);

		IdentifierGenerationStrategy identifierGenerationStrategy =
			identifierGenerationStrategyFactory.create(30);

		String samlSpAuthRequestKey =
			identifierGenerationStrategy.generateIdentifier();

		samlSpAuthRequest.setSamlSpAuthRequestKey(samlSpAuthRequestKey);

		Mockito.when(
			_samlSpAuthRequestLocalService.fetchSamlSpAuthRequest(
				Mockito.any(String.class), Mockito.any(String.class))
		).thenReturn(
			samlSpAuthRequest
		);

		response.setInResponseTo(samlSpAuthRequestKey);
		response.setIssuer(OpenSamlUtil.buildIssuer(IDP_ENTITY_ID));

		_webSsoProfileImpl.verifyInResponseTo(response);
	}

	@Test(expected = IssuerException.class)
	public void testVerifyIssuerInvalidFormat() throws Exception {
		MessageContext<?> messageContext = _webSsoProfileImpl.getMessageContext(
			getMockHttpServletRequest(ACS_URL), new MockHttpServletResponse());

		SAMLPeerEntityContext samlPeerEntityContext =
			messageContext.getSubcontext(SAMLPeerEntityContext.class);

		samlPeerEntityContext.setEntityId(IDP_ENTITY_ID);

		Issuer issuer = OpenSamlUtil.buildIssuer(IDP_ENTITY_ID);

		issuer.setFormat(NameIDType.UNSPECIFIED);

		_webSsoProfileImpl.verifyIssuer(messageContext, issuer);
	}

	@Test(expected = IssuerException.class)
	public void testVerifyIssuerInvalidIssuer() throws Exception {
		MessageContext<?> messageContext = _webSsoProfileImpl.getMessageContext(
			getMockHttpServletRequest(ACS_URL), new MockHttpServletResponse());

		SAMLPeerEntityContext samlPeerEntityContext =
			messageContext.getSubcontext(SAMLPeerEntityContext.class);

		samlPeerEntityContext.setEntityId(IDP_ENTITY_ID);

		_webSsoProfileImpl.verifyIssuer(
			messageContext, OpenSamlUtil.buildIssuer(UNKNOWN_ENTITY_ID));
	}

	@Test
	public void testVerifyIssuerValidIssuer() throws Exception {
		MessageContext<?> messageContext = _webSsoProfileImpl.getMessageContext(
			getMockHttpServletRequest(ACS_URL), new MockHttpServletResponse());

		SAMLPeerEntityContext samlPeerEntityContext =
			messageContext.getSubcontext(SAMLPeerEntityContext.class);

		samlPeerEntityContext.setEntityId(IDP_ENTITY_ID);

		_webSsoProfileImpl.verifyIssuer(
			messageContext, OpenSamlUtil.buildIssuer(IDP_ENTITY_ID));
	}

	@Test
	public void testVerifyReplayNoConditionsDate() throws Exception {
		prepareIdentityProvider(IDP_ENTITY_ID);

		MessageContext<?> idpMessageContext =
			_webSsoProfileImpl.getMessageContext(
				getMockHttpServletRequest(SSO_URL),
				new MockHttpServletResponse());

		SAMLPeerEntityContext samlPeerEntityContext =
			idpMessageContext.getSubcontext(SAMLPeerEntityContext.class);

		samlPeerEntityContext.setEntityId(SP_ENTITY_ID);

		SamlSsoRequestContext samlSsoRequestContext = new SamlSsoRequestContext(
			SP_ENTITY_ID, null, idpMessageContext, userLocalService);

		Conditions conditions = _webSsoProfileImpl.getSuccessConditions(
			samlSsoRequestContext, null, null);

		prepareServiceProvider(SP_ENTITY_ID);

		MessageContext<?> spMessageContext =
			_webSsoProfileImpl.getMessageContext(
				getMockHttpServletRequest(ACS_URL),
				new MockHttpServletResponse());

		Assertion assertion = OpenSamlUtil.buildAssertion();

		assertion.setConditions(conditions);

		assertion.setIssuer(OpenSamlUtil.buildIssuer(IDP_ENTITY_ID));

		String messageId = samlIdentifierGenerator.generateIdentifier();

		assertion.setID(messageId);

		SamlSpMessageLocalService samlSpMessageLocalService =
			getMockPortletService(
				SamlSpMessageLocalServiceUtil.class,
				SamlSpMessageLocalService.class);

		Mockito.when(
			samlSpMessageLocalService.fetchSamlSpMessage(
				Mockito.eq(IDP_ENTITY_ID), Mockito.eq(messageId))
		).thenReturn(
			null
		);

		_webSsoProfileImpl.verifyReplay(spMessageContext, assertion);
	}

	@Test(expected = ExpiredException.class)
	public void testVerifySubjectExpired() throws Exception {
		MessageContext<?> messageContext = _webSsoProfileImpl.getMessageContext(
			getMockHttpServletRequest(ACS_URL), new MockHttpServletResponse());

		SAMLBindingContext samlBindingContext = messageContext.getSubcontext(
			SAMLBindingContext.class);

		samlBindingContext.setBindingUri(SAMLConstants.SAML2_POST_BINDING_URI);

		SAMLPeerEntityContext samlPeerEntityContext =
			messageContext.getSubcontext(SAMLPeerEntityContext.class);

		samlPeerEntityContext.setEntityId(IDP_ENTITY_ID);

		NameID nameID = OpenSamlUtil.buildNameId(
			NameIDType.UNSPECIFIED, "test");

		DateTime issueDate = new DateTime(DateTimeZone.UTC);

		issueDate = issueDate.minusYears(1);

		_webSsoProfileImpl.verifySubject(
			messageContext, getSubject(messageContext, nameID, issueDate));
	}

	@Test(expected = SubjectException.class)
	public void testVerifySubjectNoBearerSubjectConfirmation()
		throws Exception {

		MessageContext<?> messageContext = _webSsoProfileImpl.getMessageContext(
			getMockHttpServletRequest(ACS_URL), new MockHttpServletResponse());

		SAMLBindingContext samlBindingContext = messageContext.getSubcontext(
			SAMLBindingContext.class);

		samlBindingContext.setBindingUri(SAMLConstants.SAML2_POST_BINDING_URI);

		SAMLPeerEntityContext samlPeerEntityContext =
			messageContext.getSubcontext(SAMLPeerEntityContext.class);

		samlPeerEntityContext.setEntityId(IDP_ENTITY_ID);

		NameID nameID = OpenSamlUtil.buildNameId(
			NameIDType.UNSPECIFIED, "test");

		Subject subject = getSubject(
			messageContext, nameID, new DateTime(DateTimeZone.UTC));

		List<SubjectConfirmation> subjectConfirmations =
			subject.getSubjectConfirmations();

		SubjectConfirmation subjectConfirmation = subjectConfirmations.get(0);

		subjectConfirmation.setMethod(
			SubjectConfirmation.METHOD_SENDER_VOUCHES);

		_webSsoProfileImpl.verifySubject(messageContext, subject);
	}

	@Test
	public void testVerifySubjectValidSubject() throws Exception {
		MessageContext<?> messageContext = _webSsoProfileImpl.getMessageContext(
			getMockHttpServletRequest(ACS_URL), new MockHttpServletResponse());

		SAMLBindingContext samlBindingContext = messageContext.getSubcontext(
			SAMLBindingContext.class);

		samlBindingContext.setBindingUri(SAMLConstants.SAML2_POST_BINDING_URI);

		SAMLPeerEntityContext samlPeerEntityContext =
			messageContext.getSubcontext(SAMLPeerEntityContext.class);

		samlPeerEntityContext.setEntityId(IDP_ENTITY_ID);

		NameID nameID = OpenSamlUtil.buildNameId(
			NameIDType.UNSPECIFIED, "test");

		Subject subject = getSubject(
			messageContext, nameID, new DateTime(DateTimeZone.UTC));

		_webSsoProfileImpl.verifySubject(messageContext, subject);

		SAMLSubjectNameIdentifierContext samlSubjectNameIdentifierContext =
			messageContext.getSubcontext(
				SAMLSubjectNameIdentifierContext.class);

		NameID resolvedNameID =
			samlSubjectNameIdentifierContext.getSAML2SubjectNameID();

		Assert.assertNotNull(resolvedNameID);
		Assert.assertEquals(nameID.getFormat(), resolvedNameID.getFormat());
		Assert.assertEquals(nameID.getValue(), resolvedNameID.getValue());
	}

	protected Subject getSubject(
			MessageContext<?> messageContext, NameID nameID, DateTime issueDate)
		throws Exception {

		SAMLPeerEntityContext samlPeerEntityContext =
			messageContext.getSubcontext(SAMLPeerEntityContext.class);

		SAMLBindingContext samlBindingContext = messageContext.getSubcontext(
			SAMLBindingContext.class);

		SamlSsoRequestContext samlSsoRequestContext = new SamlSsoRequestContext(
			samlPeerEntityContext.getEntityId(),
			samlBindingContext.getRelayState(), messageContext,
			userLocalService);

		SAMLSelfEntityContext samlSelfEntityContext =
			messageContext.getSubcontext(SAMLSelfEntityContext.class);

		SAMLMetadataContext samlSelfMetadataContext =
			samlSelfEntityContext.getSubcontext(SAMLMetadataContext.class);

		SPSSODescriptor spSSODescriptor =
			(SPSSODescriptor)samlSelfMetadataContext.getRoleDescriptor();

		AssertionConsumerService assertionConsumerService =
			SamlUtil.getAssertionConsumerServiceForBinding(
				spSSODescriptor, SAMLConstants.SAML2_POST_BINDING_URI);

		SubjectConfirmationData subjectConfirmationData =
			_webSsoProfileImpl.getSuccessSubjectConfirmationData(
				samlSsoRequestContext, assertionConsumerService, issueDate);

		return _webSsoProfileImpl.getSuccessSubject(
			samlSsoRequestContext, assertionConsumerService, nameID,
			subjectConfirmationData);
	}

	private void _testVerifyAssertionSignature(String entityId)
		throws Exception {

		Assertion assertion = OpenSamlUtil.buildAssertion();

		OpenSamlUtil.signObject(assertion, getCredential(entityId), null);

		MessageContext<?> messageContext = _webSsoProfileImpl.getMessageContext(
			getMockHttpServletRequest(ACS_URL), new MockHttpServletResponse());

		SAMLPeerEntityContext samlPeerEntityContext =
			messageContext.getSubcontext(SAMLPeerEntityContext.class);

		samlPeerEntityContext.setEntityId(IDP_ENTITY_ID);

		_webSsoProfileImpl.verifyAssertionSignature(
			assertion.getSignature(), messageContext,
			metadataManagerImpl.getSignatureTrustEngine());
	}

	private SamlSpAuthRequestLocalService _samlSpAuthRequestLocalService;
	private SamlSpSessionLocalService _samlSpSessionLocalService;
	private final WebSsoProfileImpl _webSsoProfileImpl =
		new WebSsoProfileImpl();

}