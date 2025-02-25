package com.dotflix.application.castmember.dto;

import com.dotflix.domain.castmember.CastMemberType;

public record CreateCastMemberDTO(String name, CastMemberType type) {
    public static CreateCastMemberDTO with(final String aName, final CastMemberType aType) {
        return new CreateCastMemberDTO(aName, aType);
    }
}