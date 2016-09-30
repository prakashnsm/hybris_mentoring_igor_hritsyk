package com.epam.training.core.interceptors;

import com.epam.training.core.event.CustomerNumberEvent;
import com.epam.training.core.model.OrganisationModel;

import javax.annotation.Resource;

import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

public class CustomerNumberInterceptor implements ValidateInterceptor {

	private Integer eventBorder;

	private EventService eventService;

	@Resource
	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

	@Override
	public void onValidate(Object model, InterceptorContext interceptorContext) throws InterceptorException {
		if (model instanceof OrganisationModel) {
			final OrganisationModel organisation = (OrganisationModel) model;

			if (organisation.getCustomers().size() == eventBorder) {
				eventService.publishEvent(new CustomerNumberEvent(organisation.getName(), organisation.getCustomers()));
			}
		}

	}

	public void setEventBorder(int eventBorder) {
		this.eventBorder = eventBorder;
	}
}
