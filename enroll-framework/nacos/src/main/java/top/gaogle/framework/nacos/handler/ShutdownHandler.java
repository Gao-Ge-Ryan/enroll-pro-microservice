package top.gaogle.framework.nacos.handler;

import top.gaogle.framework.commons.i18n.I18nResult;

public interface ShutdownHandler{

    I18nResult<Object> shutdown();

}
