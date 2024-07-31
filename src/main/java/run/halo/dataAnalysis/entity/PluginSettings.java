package run.halo.dataAnalysis.entity;

import lombok.Data;

/**
 * 插件配置
 * @author: dreamChaser
 * @date: 2024年07月31日 16:06
 */
public class PluginSettings {

    @Data
    public static class BaseSettings {
        public static final String GROUP = "basicSettings";
        private String defaultToken;
        private boolean disableSslCertValidation;
        private boolean logFailedTracking;
    }

}
