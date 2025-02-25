package com.dotflix.application.castmember;

import com.dotflix.application.UseCase;
import com.dotflix.application.castmember.exceptions.CastMemberNotFoundException;
import com.dotflix.domain.castmember.CastMember;
import com.dotflix.domain.castmember.CastMemberGateway;
import java.util.Objects;

public class GetCastMemberByIdUseCase extends UseCase<String, CastMember> {

    private final CastMemberGateway castMemberGateway;

    public GetCastMemberByIdUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public CastMember execute(final String id) throws CastMemberNotFoundException {
        return this.castMemberGateway.findById(id).orElseThrow(() -> new CastMemberNotFoundException("CastMember with ID " + id + " was not found"));
    }
}