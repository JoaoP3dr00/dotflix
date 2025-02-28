package com.dotflix.infrastructure.castmember.persistence;

import com.dotflix.domain.Pagination;
import com.dotflix.domain.SearchQuery;
import com.dotflix.domain.castmember.CastMember;
import com.dotflix.domain.castmember.CastMemberGateway;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Component
public class CastMemberPostgresqlGateway implements CastMemberGateway {
    private final CastMemberRepository castMemberRepository;

    public CastMemberPostgresqlGateway(final CastMemberRepository castMemberRepository) {
        this.castMemberRepository = Objects.requireNonNull(castMemberRepository);
    }

    @Override
    public CastMember create(final CastMember aCastMember) {
        return this.castMemberRepository.save(CastMemberEntity.fromDomain(aCastMember)).toDomain();
    }

    @Override
    public void deleteById(final String id) {
        if (this.castMemberRepository.existsById(id)) {
            this.castMemberRepository.deleteById(id);
        }
    }

    @Override
    public Optional<CastMember> findById(final String id) {
        return this.castMemberRepository.findById(id).map(CastMemberEntity::toDomain);
    }

    @Override
    public CastMember update(final CastMember aCastMember) {
        return this.castMemberRepository.save(CastMemberEntity.fromDomain(aCastMember)).toDomain();
    }

    @Override
    public List<String> existsByIds(final Iterable<String> castMemberIDS) {
        final var ids = StreamSupport.stream(castMemberIDS.spliterator(), false).toList();

        return this.castMemberRepository.existsByIds(ids).stream().toList();
    }

    @Override
    public Pagination<CastMember> findAll(final SearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final Specification<CastMemberEntity> specifications = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map((final String str) -> {
                    final String converLike = "%" + str + "%";

                    final Specification<CastMemberEntity> nameLike = (root, query, cb) ->
                            cb.like(
                                cb.upper(
                                    root.get("name")
                                ),
                                converLike.toUpperCase()
                            );

                    return nameLike;
                })
                .orElse(null);

        final Page<CastMemberEntity> pageResult = this.castMemberRepository.findAll(Specification.where(specifications), page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CastMemberEntity::toDomain).toList()
        );
    }
}