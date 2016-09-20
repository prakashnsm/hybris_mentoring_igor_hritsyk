package com.epam.training.core.handlers;

import com.epam.training.core.model.OrganisationModel;

import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigInteger;
import java.util.Collections;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.user.CustomerModel;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@UnitTest
public class CustomerNumberHandlerTest {

	private static final int EXPECTED_NUMBER = 3;

	private CustomerNumberHandler customerNumberHandler = new CustomerNumberHandler();

	private OrganisationModel organisation = Mockito.mock(OrganisationModel.class);

	@Test
	public void requireReturnsExpectedAmountOfCustomers() {
		when(organisation.getCustomers()).thenReturn(Collections.nCopies(EXPECTED_NUMBER, new CustomerModel()));

		assertThat("Expected numder of customers does not match actual", customerNumberHandler.get(organisation), is(EXPECTED_NUMBER));
	}

	@Test
	public void requireReturnsZeroCapacityOfCustomersCollectionWhenOrganizationDoesNotContainThem() {
		when(organisation.getCustomers()).thenReturn(null);

		assertEquals("Empty customers collection is consireder as non empty", Integer.valueOf(BigInteger.ZERO.intValue()), customerNumberHandler.get(organisation));
	}

	@Test(expected = IllegalArgumentException.class)
	public void requireThrowsIllegalArgumentExceptionWhenNullIsPasswedAsParameter() {
		customerNumberHandler.get(null);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void requireThrowsUnsupportedOperationExceptionWhenTryingToCallSetterMethod() {
		customerNumberHandler.set(any(OrganisationModel.class), any(Integer.class));
	}

}
