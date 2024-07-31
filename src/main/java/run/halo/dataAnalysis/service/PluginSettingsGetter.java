package run.halo.dataAnalysis.service;

import reactor.core.publisher.Mono;
import run.halo.dataAnalysis.entity.PluginSettings;

/**
 * 插件配置获取接口
 * @author: dreamChaser
 * @date: 2024年07月31日 16:28
 */
public interface PluginSettingsGetter {

    /**
     * 获取插件的基本配置
     * @return
     */
    Mono<PluginSettings.BaseSettings> getBasicSettings();


}
