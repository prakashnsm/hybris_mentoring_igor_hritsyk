package com.epam.training.core.event;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import de.hybris.platform.acceleratorservices.email.EmailService;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;

public class OrganisationEventListener extends AbstractEventListener<CustomerNumberEvent> {

	private static final Logger LOG = Logger.getLogger(OrganisationEventListener.class.getName());

	private String mailBody;

	private String replyToAddress;

	private String subject;

	private EmailService emailService;

	@Resource
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	@Override
	protected void onEvent(CustomerNumberEvent event) {
		LOG.info(event);
		sendMailToCustomers(event.getName(), event.getCustomers());
	}

	private void sendMailToCustomers(String name, Collection<CustomerModel> customers) {
		EmailAddressModel addressFrom =
				emailService.getOrCreateEmailAddressForEmail(replyToAddress, name);
		EmailMessageModel emailMessage = createEmailMessage(customers, addressFrom);
		emailService.send(emailMessage);
	}

	private EmailMessageModel createEmailMessage(Collection<CustomerModel> whom, EmailAddressModel addressFrom) {
		return emailService
				.createEmailMessage(customerAddresses(whom), null, null, addressFrom, replyToAddress, subject, mailBody,
						null);
	}

	private List<EmailAddressModel> customerAddresses(Collection<CustomerModel> customers) {
		List<EmailAddressModel> result = new ArrayList<>();
		for (CustomerModel customer : customers) {
			result.add(emailService.getOrCreateEmailAddressForEmail(customer.getOriginalUid(), customer.getName()));
		}

		return result;
	}

	public void setMailBody(String mailBody) {
		this.mailBody = mailBody;
	}

	public void setReplyToAddress(String replyToAddress) {
		this.replyToAddress = replyToAddress;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
}
