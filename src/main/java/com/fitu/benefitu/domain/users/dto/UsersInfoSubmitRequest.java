package com.fitu.benefitu.domain.users.dto;

public record UsersInfoSubmitRequest(
        BaseInfoDto baseInfo,
        DetailInfoRequest detailInfo
) {
}
