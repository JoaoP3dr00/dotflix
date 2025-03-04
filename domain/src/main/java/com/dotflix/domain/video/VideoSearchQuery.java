package com.dotflix.domain.video;

import java.util.Set;

public record VideoSearchQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction,
        Set<String> castMembers,
        Set<String> categories,
        Set<String> genres
) {
}
