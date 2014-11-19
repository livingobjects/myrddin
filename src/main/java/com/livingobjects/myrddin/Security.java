package com.livingobjects.myrddin;

import com.google.common.collect.ImmutableList;

public final class Security {

    public final String securityScheme;

    public final ImmutableList<String> roles;

    public Security(String securityScheme, ImmutableList<String> roles) {
        this.securityScheme = securityScheme;
        this.roles = roles;
    }
}
