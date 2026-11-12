package com.myproject.book_my_show.common.response;

import java.util.Map;

public record ApiError(
        String code,
        Map<String, String> fieldErrors
) {
}
