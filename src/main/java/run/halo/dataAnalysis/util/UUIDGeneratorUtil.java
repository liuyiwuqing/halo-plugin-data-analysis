package run.halo.dataAnalysis.util;

import org.springframework.http.server.reactive.ServerHttpRequest;
import java.util.UUID;

/**
 * 功能描述
 * UUID 生成工具
 * @author: dreamChaser
 * @date: 2024年07月31日 17:12
 */
public class UUIDGeneratorUtil {


    public static String generateUUIDFromRequest(ServerHttpRequest request) {
        // 获取请求头信息
        String headerInfo = String.valueOf(request.getHeaders().get("User-Agent"));
        if (headerInfo == null || headerInfo.isEmpty()) {
            headerInfo = "default";
        }
        // 使用请求头信息作为种子生成UUID
        UUID uuid = UUID.nameUUIDFromBytes(headerInfo.getBytes());
        return uuid.toString();
    }

}
