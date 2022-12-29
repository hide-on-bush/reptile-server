package com.xsx.jsoup.common;

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.xsx.jsoup.common.annotation.DataMasking;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author:夏世雄
 * @Date: 2022/10/21/14:55
 * @Version: 1.0
 * @Discription:
 **/
@Slf4j
public class DataMaskingAnnotationIntrospector extends NopAnnotationIntrospector {

    @Override
    public Object findSerializer(Annotated am) {
        DataMasking annotation = am.getAnnotation(DataMasking.class);
        if (annotation != null) {
            return new DataMaskingSerializer(annotation.maskFunc().operation());
        }
        return null;
    }

}
