package com.example.basicauth.enums;

import java.util.Set;

public enum Role {
    ADMIN(Set.of(Permissions.USER_CREATE, Permissions.USER_READ, Permissions.USER_DELETE)),
    USER(Set.of(Permissions.USER_READ));

    private final Set<Permissions> permissions;

    Role(Set<Permissions> permissions) {
        this.permissions = permissions;
    }

    public Set<Permissions> getPermissions() {
        return permissions;
    }
}
