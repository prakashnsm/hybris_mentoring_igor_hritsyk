package com.epam.training.core.interceptors;

import com.epam.training.core.event.CustomerNumberEvent;
import com.epam.training.core.model.OrganisationModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@UnitTest
public class CustomerNumberInterceptorTest {

	public static final int EVENT_BORDER = 5;

	public static final int OUT_OF_EVENT_BORDER = 3;

	@Spy
	private OrganisationModel organisation = new OrganisationModel();

	@Mock
	private EventService eventService;

	@Mock
	private InterceptorContext interceptorContext;

	@InjectMocks
	private CustomerNumberInterceptor interceptor = new CustomerNumberInterceptor();

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		interceptor.setEventBorder(EVENT_BORDER);
	}

	@Test
	public void requireDoesNotPublishEventWhenOrganisationHasLessThenFiveCustomers() throws InterceptorException {
		doReturn(OUT_OF_EVENT_BORDER).when(organisation).getCustomersNumber();
		interceptor.onValidate(organisation, interceptorContext);
		verify(eventService, never()).publishEvent(any(CustomerNumberEvent.class));
	}

	@Test
	public void requirePublishEventWhenOrganisationHasFiveCustomers() throws InterceptorException {
		doReturn("Google").when(organisation).getName();
		doReturn(EVENT_BORDER).when(organisation).getCustomersNumber();
		interceptor.onValidate(organisation, interceptorContext);
		verify(eventService).publishEvent(any(CustomerNumberEvent.class));
	}

}
