package com.tg.tiny4j.integration.context;

import com.tg.tiny4j.integration.reader.WebAppBeanDefinitionReader;
import com.tg.tiny4j.core.ioc.beans.factory.AutoBeanFactory;
import com.tg.tiny4j.core.ioc.beans.reader.BeanDefinitionReader;
import com.tg.tiny4j.core.ioc.context.AbstractApplicationContext;
import com.tg.tiny4j.web.metadata.ControllerInfo;
import com.tg.tiny4j.web.reader.AbstractClassReader;

import java.util.Map;

/**
 * Created by twogoods on 16/11/2.
 */
public class WebApplicationContext extends AbstractApplicationContext {
    private AbstractClassReader reader;

    public WebApplicationContext(AbstractClassReader reader) throws Exception {
        this.reader=reader;
        refresh();
        setControllerInstance();
    }

    protected void registerBeans() throws Exception {
        BeanDefinitionReader beanDefinitionReader = new WebAppBeanDefinitionReader(reader);
        //读取bean信息
        beanDefinitionReader.loadResource();
        beanFactory = new AutoBeanFactory();
        beanFactory.addBeanDefinition(beanDefinitionReader.getRegisterBeans());
    }

    private void setControllerInstance() throws Exception {
        Map<String,ControllerInfo> controllers=reader.getApis();
        for(String key:controllers.keySet()){
            Object instance=this.getBean(key);
            controllers.get(key).setObject(instance);
        }
    }
}