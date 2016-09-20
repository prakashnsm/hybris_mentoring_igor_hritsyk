package com.epam.training.core.handlers;

import com.epam.training.core.model.OrganisationModel;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Collection;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;

@Component
public class CustomerNumberHandler implements DynamicAttributeHandler<Integer, OrganisationModel> {

	private static final String PASSED_ORGANIZATION_MODEL_IS_NULL = "Passed organization model is NULL";

	@Override
	public Integer get(OrganisationModel model) {
		if (model == null) {
			throw new IllegalArgumentException(PASSED_ORGANIZATION_MODEL_IS_NULL);
		}
		final Collection<CustomerModel> customers = model.getCustomers();
		if (CollectionUtils.isEmpty(customers)) {
			return BigInteger.ZERO.intValue();
		} else {
			return customers.size();
		}
	}

	@Override
	public void set(OrganisationModel model, Integer integer) {
		throw new UnsupportedOperationException();
	}

}
