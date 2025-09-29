package com.postech.scheduling_service.utils;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
public class FeignSafeUtils {

    public static <T> T safeCall(Supplier<T> call, String description, Long id) {
        try {
            return call.get();
        } catch (FeignException.Unauthorized e) {
            log.error("{} id={} não autorizado no serviço (401).", description, id);
            return null;
        } catch (FeignException e) {
            log.error("Erro ao obter {} id={} no serviço: status={}, body={}",
                    description, id, e.status(), safeBody(e));
            return null;
        } catch (Exception e) {
            log.error("Falha inesperada ao obter {} id={} no serviço", description, id, e);
            return null;
        }
    }

    private static String safeBody(FeignException e) {
        try {
            return e.contentUTF8();
        } catch (Exception ex) {
            return "<no-body>";
        }
    }
}
