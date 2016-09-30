package com.epam.training.core.event;

import com.epam.training.core.model.OrganisationModel;

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
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.core.model.user.CustomerModel;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@UnitTest
public class OrganisationEventListenerTest {
	private static final String TEST_EMAIL="email@test.com";
	private static final String CUSTOMER_NAME="Ivan Ivanov";
	private static final String CUSTOMER_UID="testUid";
	private OrganisationModel organisationModel;
	private EmailAddressModel addressModel;

	@Mock
	private EmailService emailService;

	@InjectMocks
	private OrganisationEventListener eventListener=new OrganisationEventListener();

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		addressModel = getEmailAddressModel();
		setUpEventListener();
	}


	@Test
	public void requireInvokesEmailServiceMethodSendWhenOnEvent() {
		eventListener.onEvent(new CustomerNumberEvent("organisation", getCustomerModels()));
		verify(emailService).send(any(EmailMessageModel.class));
	}



	private void setUpEventListener() {
		eventListener.setEmailService(emailService);
		eventListener.setMailBody("message");
		eventListener.setReplyToAddress("myAddress");
		eventListener.setSubject("subject");
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
