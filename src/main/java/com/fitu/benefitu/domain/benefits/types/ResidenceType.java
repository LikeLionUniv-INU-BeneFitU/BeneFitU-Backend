package com.fitu.benefitu.domain.benefits.types;

import lombok.Getter;

@Getter
public enum ResidenceType {
    GANGWON("강원특별자치도", "5100000000"),
    GYEONGGI("경기도", "4100000000"),
    GYEONGNAM("경상남도", "4800000000"),
    GYEONGBUK("경상북도", "4700000000"),
    DAEGU("대구광역시", "2700000000"),
    DAEJEON("대전광역시", "3000000000"),
    BUSAN("부산광역시", "2600000000"),
    SEOUL("서울특별시", "1100000000"),
    SEJONG("세종특별자치시", "3611000000"),
    ULSAN("울산광역시", "3100000000"),
    INCHEON("인천광역시", "2800000000"),
    JEONNAM_GWANGJU("전남광주통합특별시", "1200000000"),
    JEONBUK("전북특별자치도", "5200000000"),
    JEJU("제주특별자치도", "5000000000"),
    CHUNGNAM("충청남도", "4400000000"),
    CHUNGBUK("충청북도", "4300000000");

    private final String residenceName;
    private final String residenceCode;

    ResidenceType(String residenceName, String residenceCode) {
        this.residenceName = residenceName;
        this.residenceCode = residenceCode;
    }
}
