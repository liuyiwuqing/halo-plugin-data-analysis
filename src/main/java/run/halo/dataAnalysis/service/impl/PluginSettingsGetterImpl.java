package run.halo.dataAnalysis.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import run.halo.app.plugin.ReactiveSettingFetcher;
import run.halo.dataAnalysis.entity.PluginSettings;
import run.halo.dataAnalysis.service.PluginSettingsGetter;

/**
 * 获取插件配置
 * @author: dreamChaser
 * @date: 2024年07月31日 16:29
 */
@Component
@RequiredArgsConstructor
public class PluginSettingsGetterImpl implements PluginSettingsGetter {

    private final ReactiveSettingFetcher settingFetcher;

    @Override
    public Mono<PluginSettings.BaseSettings> getBasicSettings() {
        return settingFetcher.fetch(PluginSettings.BaseSettings.GROUP, PluginSettings.BaseSettings.class)
            .defaultIfEmpty(new PluginSettings.BaseSettings());
    }

}
