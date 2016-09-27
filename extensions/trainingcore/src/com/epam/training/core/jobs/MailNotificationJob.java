package com.epam.training.core.jobs;

import com.epam.training.core.services.EmailingOrganisationService;

import org.springframework.beans.factory.annotation.Autowired;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

public class MailNotificationJob extends AbstractJobPerformable<CronJobModel> {

	private final EmailingOrganisationService emailingOrganisationService;

	@Autowired
	public MailNotificationJob(EmailingOrganisationService emailingOrganisationService) {
		this.emailingOrganisationService = emailingOrganisationService;
	}

	@Override
	public PerformResult perform(CronJobModel cronJobModel) {
		emailingOrganisationService.sendEmails();
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

}
