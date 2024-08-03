package run.halo.dataAnalysis.matomo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matomo.java.tracking.MatomoRequest;
import org.matomo.java.tracking.MatomoRequests;
import org.matomo.java.tracking.MatomoTracker;
import org.matomo.java.tracking.TrackerConfiguration;
import org.matomo.java.tracking.parameters.VisitorId;
import org.springframework.context.annotation.Bean;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import run.halo.app.infra.ExternalUrlSupplier;
import run.halo.app.plugin.ReactiveSettingFetcher;
import run.halo.app.security.AdditionalWebFilter;
import run.halo.dataAnalysis.entity.PluginSettings;
import run.halo.dataAnalysis.service.PluginSettingsGetter;
import run.halo.dataAnalysis.util.UUIDGeneratorUtil;
import java.net.URI;
import java.time.Duration;
import java.util.UUID;

/**
 * 在相应的请求中注入跟踪器
 * @author: dreamChaser
 * @date: 2024年07月31日 15:55
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MatomoTrackerWebFilter implements AdditionalWebFilter {

    private final ExternalUrlSupplier externalUrlSupplier;

    private final ReactiveSettingFetcher settingFetcher;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        return settingFetcher.fetch(PluginSettings.BaseSettings.GROUP, PluginSettings.BaseSettings.class).flatMap(baseSettings -> {

            ServerHttpRequest haloRequest = exchange.getRequest();

            TrackerConfiguration trackerConfiguration = initConfiguration(baseSettings);
            MatomoTracker matomoTracker = new MatomoTracker(trackerConfiguration);
            String uuidForVisitorId = UUIDGeneratorUtil.generateUUIDFromRequest(haloRequest);
            VisitorId visitorIdFromUUID = VisitorId.fromUUID(UUID.fromString(uuidForVisitorId));
            //创建单个跟踪请求数据类
            MatomoRequest singleMatomoRequest = MatomoRequests
                // 为内容展示创建 MatomoRequest 对象 name 参数后续要生成页面名称，文章名称 以及瞬间的ID等
                .contentImpression("name", null, haloRequest.getURI().toString())
                .eventCategory("request")
                .eventAction("api-operation")
                .visitorId(visitorIdFromUUID)
                .build();
            // 发送请求
            matomoTracker.sendBulkRequestAsync(singleMatomoRequest);

            // 放行其他过滤器
            chain.filter(exchange);
            return Mono.empty();
        });
    }

    /**
     * 初始化跟踪器配置
     * 默认站点 ID 为1
     * 默认链接超时为 3s
     * 默认数据包之间的最大不活动时间为5s
     * @return
     */
    public TrackerConfiguration initConfiguration(PluginSettings.BaseSettings basicSettings){

        return TrackerConfiguration.builder()
            .userAgent("org.matomo.java.tracking.Java11SenderProvider")
            .apiEndpoint(URI.create(externalUrlSupplier.getRaw().toString()))
            .connectTimeout(Duration.ofSeconds(3))
            .defaultAuthToken(basicSettings.getDefaultToken())
            .socketTimeout(Duration.ofSeconds(5))
            .disableSslCertValidation(basicSettings.isDisableSslCertValidation())
            .logFailedTracking(basicSettings.isLogFailedTracking())
            .defaultSiteId(1)
            .build();
    }

}
