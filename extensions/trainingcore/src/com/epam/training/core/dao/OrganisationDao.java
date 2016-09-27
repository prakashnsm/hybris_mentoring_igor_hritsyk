package com.epam.training.core.dao;

import com.epam.training.core.model.OrganisationModel;

import java.util.List;

public interface OrganisationDao {
	List<OrganisationModel> findAll();
}
