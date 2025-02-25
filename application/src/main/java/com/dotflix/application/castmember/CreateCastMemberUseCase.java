package com.dotflix.application.castmember;

import com.dotflix.application.UseCase;
import com.dotflix.application.castmember.dto.CreateCastMemberDTO;
import com.dotflix.domain.castmember.CastMember;
import com.dotflix.domain.castmember.CastMemberGateway;
import com.dotflix.domain.castmember.CastMemberType;

import java.util.Objects;

public class CreateCastMemberUseCase extends UseCase<CreateCastMemberDTO, CastMember> {
    private final CastMemberGateway castMemberGateway;

    public CreateCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public CastMember execute(final CreateCastMemberDTO createCastMemberDTO) throws Exception {
        final String name = createCastMemberDTO.name();
        final CastMemberType type = createCastMemberDTO.type();

        final CastMember castMember = CastMember.newMember(name, type);

        return this.castMemberGateway.create(castMember);
    }
}