package com.fitu.benefitu.domain.users.dto;

public record UsersInfoSubmitResponse(
        BaseInfoDto baseInfo,
        DetailInfoResponse detailInfo
) {
}
