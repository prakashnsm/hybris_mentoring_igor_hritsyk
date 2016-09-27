package com.epam.training.core.services.impl;

import com.epam.training.core.dao.OrganisationDao;
import com.epam.training.core.model.OrganisationModel;
import com.epam.training.core.services.EmailingOrganisationService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hybris.platform.acceleratorservices.email.EmailService;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.core.model.user.CustomerModel;

@Service
public class DefaultEmailingOrganisationService implements EmailingOrganisationService {

	public static final String REPLY_TO_ADDRESS = "projectcommon7@gmail.com";

	private static final String TABLE_HEADER_TAGS = "<table><tr><th>id</th><th>Name</th></tr>";

	private static final String TABLE_FOOTER_TAGS = "</table><br><br>";

	public static final String SUBJECT = "hybris";

	private final EmailService emailService;

	private final OrganisationDao organisationDao;

	@Autowired
	public DefaultEmailingOrganisationService(EmailService emailService, OrganisationDao organisationDao) {
		this.emailService = emailService;
		this.organisationDao = organisationDao;
	}

	@Override
	public void sendEmails() {
		EmailAddressModel fromAddress = emailService.getOrCreateEmailAddressForEmail(REPLY_TO_ADDRESS, "hybris");
		Map<String, String> mailMap = createOrganisationEmailMap();
		for (String organisationMail : mailMap.keySet()) {
			sendEmails(fromAddress, organisationMail, mailMap.get(organisationMail));
		}
	}

	public Map<String, String> createOrganisationEmailMap() {
		Map<String, Collection<CustomerModel>> customerModelMap = createOrganisationCustomerMap();
		Map<String, String> mails = new HashMap<>();
		for (String organisation : customerModelMap.keySet()) {
			mails.put(organisation, createEmailBody(customerModelMap.get(organisation)));
		}
		return mails;
	}

	private void sendEmails(EmailAddressModel from, String to, String messageBody) {
		EmailAddressModel toAddress = emailService.getOrCreateEmailAddressForEmail(to, "Organisation");
		List<EmailAddressModel> toAddresses = Collections.singletonList(toAddress);
		EmailMessageModel message = emailService
				.createEmailMessage(toAddresses, null, null, from, REPLY_TO_ADDRESS, SUBJECT, messageBody, null);
		emailService.send(message);
	}

	private String createEmailBody(Collection<CustomerModel> customerModels) {
		StringBuilder messageBody = new StringBuilder(TABLE_HEADER_TAGS);
		for (CustomerModel customerModel : customerModels) {
			messageBody
					.append("<tr><td>" + customerModel.getUid() + "</td><td>" + customerModel.getName() + "</td></tr>");
		}
		messageBody.append(TABLE_FOOTER_TAGS);
		return messageBody.toString();
	}

	private Map<String, Collection<CustomerModel>> createOrganisationCustomerMap() {
		Map<String, Collection<CustomerModel>> result = new HashMap<>();
		List<OrganisationModel> organisations = organisationDao.findAll();
		for (OrganisationModel next : organisations) {
			Collection<CustomerModel> customers = next.getCustomers();
			if (CollectionUtils.isNotEmpty(customers)) {
				result.put(next.getEmail(), customers);
			}
		}
		return result;
	}

}
