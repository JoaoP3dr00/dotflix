package com.dotflix.application.castmember;

import com.dotflix.application.UseCase;
import com.dotflix.application.castmember.dto.UpdateCastMemberDTO;
import com.dotflix.application.castmember.exceptions.CastMemberNotFoundException;
import com.dotflix.domain.castmember.CastMember;
import com.dotflix.domain.castmember.CastMemberGateway;
import com.dotflix.domain.castmember.CastMemberType;
import java.util.Objects;

public class UpdateCastMemberUseCase extends UseCase<UpdateCastMemberDTO, CastMember> {
    private final CastMemberGateway castMemberGateway;

    public UpdateCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public CastMember execute(final UpdateCastMemberDTO updateCastMemberDTO) throws Exception {
        final String id = updateCastMemberDTO.id();
        final String name = updateCastMemberDTO.name();
        final CastMemberType type = updateCastMemberDTO.type();

        final CastMember aMember = this.castMemberGateway.findById(id).orElseThrow(() -> new CastMemberNotFoundException("CastMember with ID " + id + " was not found"));

        aMember.update(name, type);

        return this.castMemberGateway.update(aMember);
    }
}