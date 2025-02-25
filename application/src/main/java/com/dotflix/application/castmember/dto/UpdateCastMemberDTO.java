package com.dotflix.application.castmember.dto;

import com.dotflix.domain.castmember.CastMemberType;

public record UpdateCastMemberDTO(String id, String name, CastMemberType type) {
    public static UpdateCastMemberDTO with(final String anId, final String aName, final CastMemberType aType) {
        return new UpdateCastMemberDTO(anId, aName, aType);
    }
}