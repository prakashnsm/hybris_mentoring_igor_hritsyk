package com.epam.training.core.dao.impl;

import com.epam.training.core.dao.OrganisationDao;
import com.epam.training.core.model.OrganisationModel;

import org.springframework.stereotype.Component;

import java.util.List;

import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;

@Component
public class DefaultOrganisationDao extends DefaultGenericDao<OrganisationModel> implements OrganisationDao {

	public DefaultOrganisationDao() {
		this("Organisation");
	}
	private DefaultOrganisationDao(String typecode) {
		super(typecode);
	}

	@Override
	public List<OrganisationModel> findAll() {
		return this.find();
	}
}
