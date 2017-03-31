package com.tg.tiny4j.web.contextlistener;

import com.tg.tiny4j.commons.constants.WebApplicationEnvironment;
import com.tg.tiny4j.web.reader.ConfigLoader;
import com.tg.tiny4j.web.reader.WebScanedClassReader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by twogoods on 16/11/2.
 */
public class SingleRestLoaderListener implements ServletContextListener {
    private static Logger log = LoggerFactory.getLogger(SingleRestLoaderListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //TODO 去掉Web.xml下的包配置,使用注解?
        log.info("contextInit...");
        try {
            String packages = ConfigLoader.loadConfig().getComponentscan();
            if (StringUtils.isEmpty(packages)) {
                packages = servletContextEvent.getServletContext().getInitParameter("component-scan");
            }
            log.debug("auto package: {}", packages);
            WebScanedClassReader webScanedClassReader = new WebScanedClassReader();
            webScanedClassReader.loadClass(packages);
            webScanedClassReader.initRequestMap();
            webScanedClassReader.instance4SingleMode();
            servletContextEvent.getServletContext().setAttribute(WebApplicationEnvironment.RUN_MODE, WebApplicationEnvironment.SINGLE_MODE);
            servletContextEvent.getServletContext().setAttribute(WebApplicationEnvironment.WEBREQUESTMAPPER, webScanedClassReader.getRequestMapper());
        } catch (Exception e) {
            log.error("start rest failed", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log.info("SingleRestLoaderListener destroyed...");
    }
}