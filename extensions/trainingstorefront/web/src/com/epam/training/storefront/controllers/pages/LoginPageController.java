/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.epam.training.storefront.controllers.pages;

import com.epam.training.storefront.controllers.ControllerConstants;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractLoginPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.RegisterForm;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

/**
 * Login Controller. Handles login and register for the account flow.
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/login")
public class LoginPageController extends AbstractLoginPageController {

	public static final int MAX_BRUTEFORCE_ATTACK_COUNTER = 3;

	private HttpSessionRequestCache httpSessionRequestCache;

	private ModelService modelService;

	private UserService userService;

	@Override
	protected String getView() {
		return ControllerConstants.Views.Pages.Account.AccountLoginPage;
	}

	@Override
	protected String getSuccessRedirect(final HttpServletRequest request, final HttpServletResponse response) {
		if (httpSessionRequestCache.getRequest(request, response) != null) {
			return httpSessionRequestCache.getRequest(request, response).getRedirectUrl();
		}
		return "/my-account";
	}

	@Override
	protected AbstractPageModel getCmsPage() throws CMSItemNotFoundException {
		return getContentPageForLabelOrId("login");
	}

	@Resource(name = "httpSessionRequestCache")
	public void setHttpSessionRequestCache(final HttpSessionRequestCache accHttpSessionRequestCache) {
		this.httpSessionRequestCache = accHttpSessionRequestCache;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String doLogin(@RequestHeader(value = "referer", required = false) final String referer, @RequestParam(
			value = "error", defaultValue = "false") final boolean loginError, final Model model,
			final HttpServletRequest request, final HttpServletResponse response, final HttpSession session)
			throws CMSItemNotFoundException {
		CustomerModel customer = getCustomerFromSession(session);
		if (loginError) {
			incrementAttemptCounter(customer);
			setBanMessage(model, customer);
		} else {
			storeReferer(referer, request, response);
		}
		return getDefaultLoginPage(loginError, session, model);
	}

	private CustomerModel getCustomerFromSession(HttpSession session) {
		String failerName = lastNameFromSession(session);
		if (StringUtils.isNotEmpty(failerName)) {
			return (CustomerModel) userService.getUserForUID(failerName);
		}
		return null;
	}

	private String lastNameFromSession(HttpSession session) {
		return (String) session.getAttribute(SPRING_SECURITY_LAST_USERNAME);
	}

	private void incrementAttemptCounter(final CustomerModel customer) {
		if (customer != null) {
			customer.setAttemptCount(customer.getAttemptCount() + 1);
			modelService.save(customer);
		}
	}

	private void setBanMessage(final Model model, final CustomerModel customer) {
		if (customer != null && customer.getAttemptCount() >= MAX_BRUTEFORCE_ATTACK_COUNTER) {
			GlobalMessages.addErrorMessage(model, "login.error.account.banned");
		}
	}

	protected void storeReferer(final String referer, final HttpServletRequest request,
			final HttpServletResponse response) {
		if (StringUtils.isNotBlank(referer) && !StringUtils.endsWith(referer, "/login") && StringUtils
				.contains(referer, request.getServerName())) {
			httpSessionRequestCache.saveRequest(request, response);
		}
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String doRegister(@RequestHeader(value = "referer", required = false) final String referer,
			final RegisterForm form, final BindingResult bindingResult, final Model model,
			final HttpServletRequest request, final HttpServletResponse response,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException {
		getRegistrationValidator().validate(form, bindingResult);
		return processRegisterUserRequest(referer, form, bindingResult, model, request, response, redirectModel);
	}

	public ModelService getModelService() {
		return modelService;
	}

	@Resource(name = "modelService")
	public void setModelService(final ModelService modelService) {
		this.modelService = modelService;
	}

	public UserService getUserService() {
		return userService;
	}

	@Resource(name = "userService")
	public void setUserService(final UserService userService) {
		this.userService = userService;
	}
}
