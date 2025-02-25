package com.dotflix.application.castmember;

import com.dotflix.application.UseCase;
import com.dotflix.domain.castmember.CastMemberGateway;

import java.util.Objects;

public class DeleteCastMemberUseCase extends UseCase<String, String> {
    private final CastMemberGateway castMemberGateway;

    public DeleteCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public String execute(final String id) {
        this.castMemberGateway.deleteById(id);

        return "CastMember " + id + " deleted";
    }
}
