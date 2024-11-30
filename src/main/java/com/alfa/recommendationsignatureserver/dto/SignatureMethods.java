package com.alfa.recommendationsignatureserver.dto;

public enum SignatureMethods {
    NoRecommendedMethod, // Ничего не рекомендуем
    SMS, // Рекомендуем СМС
    PayControl, // Рекомендуем приложение
    QDSToken, // Рекомендуем КЭП на токене
    QDSMobile // Рекомендуем КЭП на приложении
}
