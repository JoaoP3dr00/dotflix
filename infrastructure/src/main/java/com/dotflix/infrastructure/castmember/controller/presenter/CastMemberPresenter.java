package com.dotflix.infrastructure.castmember.controller.presenter;

import com.dotflix.domain.castmember.CastMember;
import com.dotflix.infrastructure.castmember.controller.dto.CastMemberListResponse;
import com.dotflix.infrastructure.castmember.controller.dto.CastMemberResponse;

public interface CastMemberPresenter {
    static CastMemberResponse present(final CastMember aMember) {
        return new CastMemberResponse(
                aMember.getId(),
                aMember.getName(),
                aMember.getType().name(),
                aMember.getCreatedAt().toString(),
                aMember.getUpdatedAt().toString()
        );
    }

    static CastMemberListResponse presentGetAll(final CastMember aMember) {
        return new CastMemberListResponse(
                aMember.getId(),
                aMember.getName(),
                aMember.getType().name(),
                aMember.getCreatedAt().toString()
        );
    }
}