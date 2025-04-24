package com.eli.ads.user.role;

public interface RoleService {
    Role findRoleByName(RoleEnum name);
    Role createRole(Role role);
}
