package com.fitu.benefitu.domain.users.dto;

public record UsersInfoResponse(
        BaseInfoResponseDto baseInfo,
        DetailInfoResponse detailInfo
) {
}