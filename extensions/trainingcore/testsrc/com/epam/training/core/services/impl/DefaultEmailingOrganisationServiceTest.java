package com.epam.training.core.services.impl;

import com.epam.training.core.dao.OrganisationDao;
import com.epam.training.core.model.OrganisationModel;
import com.epam.training.core.services.EmailingOrganisationService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.email.EmailService;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.core.model.user.CustomerModel;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@UnitTest
public class DefaultEmailingOrganisationServiceTest {

	private static final String TEST_EMAIL="email@test.com";
	private static final String CUSTOMER_NAME="Ivan Ivanov";
	private static final String CUSTOMER_UID="testUid";
	private OrganisationModel organisationModel;
	private EmailAddressModel addressModel;

	@Mock
	private EmailService emailService;

	@Mock
	private OrganisationDao organisationDao;

	@InjectMocks
	private EmailingOrganisationService emailingOrganisationService =
			new DefaultEmailingOrganisationService(emailService, organisationDao);

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		organisationModel = getOrganisationModel();
		addressModel = getEmailAddressModel();
	}

	@Test
	public void requireInvokeEmailServicesMethodSendWhenSendsEmails() {
		when(organisationDao.findAll()).thenReturn(getOrganisationModelList());
		emailingOrganisationService.sendEmails();
		verify(emailService).send(any(EmailMessageModel.class));
	}

	private List<OrganisationModel> getOrganisationModelList() {
		return Collections.singletonList(organisationModel);
	}

	@Test
	public void requireInvokesEmailServiceMethodCreateEmailMessageWhenSendEmail()  {
		when(organisationDao.findAll()).thenReturn(getOrganisationModelList());
		emailingOrganisationService.sendEmails();
		verify(emailService).createEmailMessage(anyListOf(EmailAddressModel.class), anyListOf(EmailAddressModel.class),
				anyListOf(EmailAddressModel.class), any(EmailAddressModel.class),
								anyString(), anyString(), anyString(), anyListOf(EmailAttachmentModel.class));

	}

	@Test
	public void requireInvokesOrganisationDaoMethodFindAllWhenSendsEmail() throws Exception {
		emailingOrganisationService.sendEmails();
		verify(organisationDao).findAll();
	}

	@Test
	public void requireInvokesEmailServiceMethodGetOrCreateEmailAddressForEmailWhenSendEmail() {
		emailingOrganisationService.sendEmails();
		verify(emailService).getOrCreateEmailAddressForEmail(anyString(), anyString());
	}

	private List<CustomerModel> getCustomerModels() {
		return Collections.singletonList(getCustomerModel());
	}

	private EmailAddressModel getEmailAddressModel() {
		EmailAddressModel emailAddressModel=new EmailAddressModel();
		emailAddressModel.setEmailAddress(TEST_EMAIL);
		return emailAddressModel;
	}

	private CustomerModel getCustomerModel() {
		CustomerModel customerModel=new CustomerModel();
		customerModel.setName(CUSTOMER_NAME);
		customerModel.setUid(CUSTOMER_UID);
		return customerModel;
	}

	private OrganisationModel getOrganisationModel() {
		OrganisationModel organisationModel=new OrganisationModel();
		organisationModel.setEmail(TEST_EMAIL);
		organisationModel.setCustomers(getCustomerModels());
		return organisationModel;
	}

}
