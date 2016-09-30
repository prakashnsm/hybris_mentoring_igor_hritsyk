package com.epam.training.core.event;

import java.util.Collection;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

public class CustomerNumberEvent extends AbstractEvent {

	private final String name;
	private final Collection<CustomerModel> customers;

	public CustomerNumberEvent(String name, Collection<CustomerModel> customers) {
		super();
		this.name = name;
		this.customers = customers;
	}

	public String getName() {
		return name;
	}

	public Collection<CustomerModel> getCustomers() {
		return customers;
	}

	@Override
	public String toString() {
		return "Organisation " + name + " has " + customers.size()
				+ " customers now.";
	}
}
