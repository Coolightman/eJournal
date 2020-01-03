package com.coolightman.app.service;


import com.coolightman.app.model.Admin;

public interface AdminService extends GenericService<Admin> {
    Admin findAdminByLogin(final String login);
}
