package com.alfa.recommendationsignatureserver.dto;

public enum UseContextValues {
    base_operation_signature, // Перевод денег или подпись базового документа
    big_operation_signature, // Перевод крупных сумм денег или подпись важного документа
    sms_failure, // Ошибка при попытке подписать что-то через СМС
    change_signature_method // Переход во вкладку изменений способов подписания
}
