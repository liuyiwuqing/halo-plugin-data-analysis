package run.halo.dataAnalysis;

import org.springframework.stereotype.Component;
import run.halo.app.plugin.BasePlugin;
import run.halo.app.plugin.PluginContext;

/**
* 插件启动入口
* @author: dreamChaser
* @date: 2024/7/29 19:21
*/
@Component
public class DataAnalysisPlugin extends BasePlugin {

    public DataAnalysisPlugin(PluginContext pluginContext) {
        super(pluginContext);
    }

    @Override
    public void start() {
        System.out.println("插件启动成功！");
    }

    @Override
    public void stop() {
        System.out.println("插件停止！");
    }
}
