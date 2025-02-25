package com.dotflix.domain.castmember;

import com.dotflix.domain.Pagination;
import com.dotflix.domain.SearchQuery;

import java.util.List;
import java.util.Optional;

public interface CastMemberGateway {
    CastMember create(CastMember aCastMember);

    void deleteById(String anId);

    Optional<CastMember> findById(String anId);

    CastMember update(CastMember aCastMember);

    Pagination<CastMember> findAll(SearchQuery aQuery);

    List<String> existsByIds(Iterable<String> ids);
}