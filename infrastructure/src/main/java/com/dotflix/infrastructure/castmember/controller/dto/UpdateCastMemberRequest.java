package com.dotflix.infrastructure.castmember.controller.dto;

import com.dotflix.domain.castmember.CastMemberType;

public record UpdateCastMemberRequest(String name, CastMemberType type) {
}