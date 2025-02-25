package com.dotflix.application.castmember;

import com.dotflix.application.UseCase;
import com.dotflix.domain.Pagination;
import com.dotflix.domain.SearchQuery;
import com.dotflix.domain.castmember.CastMember;
import com.dotflix.domain.castmember.CastMemberGateway;
import java.util.Objects;

public class GetAllCastMemberUseCase extends UseCase<SearchQuery, Pagination<CastMember>> {
    private final CastMemberGateway castMemberGateway;

    public GetAllCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public Pagination<CastMember> execute(final SearchQuery aQuery) {
        return this.castMemberGateway.findAll(aQuery);
    }
}