package com.fitu.benefitu.domain.benefits.types;

import lombok.Getter;

@Getter
public enum ResidenceType {
    GANGWON("강원특별자치도", "51000"),
    GYEONGGI("경기도", "41000"),
    GYEONGNAM("경상남도", "48000"),
    GYEONGBUK("경상북도", "47000"),
    DAEGU("대구광역시", "27000"),
    DAEJEON("대전광역시", "30000"),
    BUSAN("부산광역시", "26000"),
    SEOUL("서울특별시", "11000"),
    SEJONG("세종특별자치시", "36110"),
    ULSAN("울산광역시", "31000"),
    INCHEON("인천광역시", "28000"),
    JEONNAM_GWANGJU("전남광주통합특별시", "12000"),
    JEONBUK("전북특별자치도", "52000"),
    JEJU("제주특별자치도", "50000"),
    CHUNGNAM("충청남도", "44000"),
    CHUNGBUK("충청북도", "43000");

    private final String residenceName;
    private final String residenceCode;

    ResidenceType(String residenceName, String residenceCode) {
        this.residenceName = residenceName;
        this.residenceCode = residenceCode;
    }

    public static ResidenceType getResidenceTypeByResidenceName(String residenceName) {
        if (residenceName == null) {return null;}
        for (ResidenceType residenceType : ResidenceType.values()) {
            if (residenceType.getResidenceName().equals(residenceName)) {
                return residenceType;
            }
        }
        return null;
    }

    public static boolean checkResidenceName(String residenceName){
        for(ResidenceType type : values()){
            if(type.getResidenceName().equals(residenceName)){
                return true;
            }
        }
        return false;
    }
}
