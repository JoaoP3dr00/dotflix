package com.dotflix.infrastructure.castmember.controller.dto;

import com.dotflix.domain.castmember.CastMemberType;

public record CreateCastMemberRequest(String name, CastMemberType type) {
}